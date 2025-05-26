package app.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.config.JwtAuthenticationFilter;
import app.config.JwtServiceGenerator;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false) // Desabilita filtros de segurança para o teste
public class LoginControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LoginService loginService;

	// Mocks necessários para a segurança
	@MockBean
	private JwtServiceGenerator jwtService;

	@MockBean
	private JwtAuthenticationFilter jwtAuthFilter;

	@Autowired
	private ObjectMapper objectMapper;

	private Login login;

	@BeforeEach
	void setUp() {
		login = new Login();
		login.setUsername("user@email.com");
		login.setPassword("senha123");
	}

	@Test
	@DisplayName("POST /api/login – Login bem-sucedido retorna token")
	void testLoginSuccess() throws Exception {
		when(loginService.logar(any(Login.class))).thenReturn("token-jwt");
		mockMvc.perform(post("/api/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(login)))
				.andExpect(status().isOk())
				.andExpect(content().string("token-jwt"));
	}

	@Test
	@DisplayName("POST /api/login – Falha de autenticação retorna 400")
	void testLoginFailure() throws Exception {
		when(loginService.logar(any(Login.class))).thenThrow(new RuntimeException("Credenciais inválidas"));
		mockMvc.perform(post("/api/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(login)))
				.andExpect(status().isBadRequest());
	}
}
