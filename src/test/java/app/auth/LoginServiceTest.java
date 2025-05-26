package app.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import app.config.JwtServiceGenerator;
import app.entities.Usuario;
import app.repositories.UsuarioRepository;

public class LoginServiceTest {
	@Mock
	private UsuarioRepository usuarioRepository;
	@Mock
	private JwtServiceGenerator jwtService;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private Authentication authentication;

	@InjectMocks
	private LoginService loginService;

	private Login login;
	private Usuario usuario;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		login = new Login();
		login.setUsername("user@email.com");
		login.setPassword("senha123");
		usuario = new Usuario();
		usuario.setId(1L);
		usuario.setEmail("user@email.com");
		usuario.setSenha("senha123");
	}

	@Test
	@DisplayName("Deve autenticar e gerar token com sucesso")
	void testLogarComSucesso() {
		when(usuarioRepository.findByEmail(login.getUsername())).thenReturn(Optional.of(usuario));
		when(jwtService.generateToken(usuario)).thenReturn("token-jwt");
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);

		String token = loginService.logar(login);
		assertEquals("token-jwt", token);
		verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(jwtService).generateToken(usuario);
	}

	@Test
	@DisplayName("Deve lançar exceção ao autenticar com credenciais inválidas")
	void testLogarComCredenciaisInvalidas() {
		doThrow(new BadCredentialsException("Credenciais inválidas")).when(authenticationManager)
				.authenticate(any(UsernamePasswordAuthenticationToken.class));
		assertThrows(BadCredentialsException.class, () -> loginService.logar(login));
	}

	@Test
	@DisplayName("Deve lançar exceção se usuário não encontrado")
	void testLogarUsuarioNaoEncontrado() {
		when(usuarioRepository.findByEmail(login.getUsername())).thenReturn(Optional.empty());
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);
		assertThrows(NoSuchElementException.class, () -> loginService.logar(login));
	}
}
