package app.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import app.entities.Usuario;
import app.enums.StatusUsuario;
import app.exceptions.UsuarioNotFoundException;
import app.repositories.UsuarioRepository;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Vik");
        usuario.setEmail("vik@email.com");
        usuario.setSenha("123");
        usuario.setStatus(StatusUsuario.ATIVO);
    }

    @Test
    @DisplayName("Deve listar todos os usuários")
    void testListAll() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));
        List<Usuario> result = usuarioService.listAll();
        assertEquals(1, result.size());
        assertEquals("Vik", result.get(0).getNome());
    }

    @Test
    @DisplayName("Deve buscar por ID com sucesso")
    void testFindByIdSuccess() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        Usuario result = usuarioService.findById(1L);
        assertEquals("Vik", result.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar por ID inexistente")
    void testFindByIdNotFound() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(UsuarioNotFoundException.class, () -> usuarioService.findById(2L));
    }

    @Test
    @DisplayName("Deve buscar por email com sucesso")
    void testFindByEmailSuccess() {
        when(usuarioRepository.findByEmailIgnoreCase("vik@email.com")).thenReturn(usuario);
        Usuario result = usuarioService.findByEmail("vik@email.com");
        assertEquals("Vik", result.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar por email inexistente")
    void testFindByEmailNotFound() {
        when(usuarioRepository.findByEmailIgnoreCase("x@email.com")).thenReturn(null);
        assertThrows(UsuarioNotFoundException.class, () -> usuarioService.findByEmail("x@email.com"));
    }

    @Test
    @DisplayName("Deve buscar por nome com sucesso")
    void testFindByNomeSuccess() {
        when(usuarioRepository.findByNomeContainingIgnoreCase("Vik")).thenReturn(Arrays.asList(usuario));
        List<Usuario> result = usuarioService.findByNome("Vik");
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar por nome inexistente")
    void testFindByNomeNotFound() {
        when(usuarioRepository.findByNomeContainingIgnoreCase("Xuxu")).thenReturn(List.of());
        assertThrows(UsuarioNotFoundException.class, () -> usuarioService.findByNome("Xuxu"));
    }

    @Test
    @DisplayName("Deve buscar por status com sucesso")
    void testFindByStatusSuccess() {
        when(usuarioRepository.findByStatus(StatusUsuario.ATIVO)).thenReturn(Arrays.asList(usuario));
        List<Usuario> result = usuarioService.findByStatus(StatusUsuario.ATIVO);
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar por status inexistente")
    void testFindByStatusNotFound() {
        when(usuarioRepository.findByStatus(StatusUsuario.INATIVO)).thenReturn(List.of());
        assertThrows(UsuarioNotFoundException.class, () -> usuarioService.findByStatus(StatusUsuario.INATIVO));
    }

    @Test
    @DisplayName("Deve salvar usuário novo com sucesso")
    void testSaveSuccess() {
        when(usuarioRepository.findByEmailIgnoreCase(usuario.getEmail())).thenReturn(null);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        Usuario saved = usuarioService.save(usuario);
        assertEquals(StatusUsuario.ATIVO, saved.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar com e-mail duplicado")
    void testSaveDuplicateEmail() {
        Usuario outro = new Usuario();
        outro.setId(2L);
        outro.setEmail("vik@email.com");
        when(usuarioRepository.findByEmailIgnoreCase(usuario.getEmail())).thenReturn(outro);
        assertThrows(DataIntegrityViolationException.class, () -> usuarioService.save(usuario));
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void testUpdateSuccess() {
        Usuario updated = new Usuario();
        updated.setNome("Vitória");
        updated.setEmail("nova@email.com");
        updated.setSenha("123");
        updated.setStatus(StatusUsuario.INATIVO);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(updated);

        Usuario result = usuarioService.update(1L, updated);
        assertEquals("Vitória", result.getNome());
        assertEquals(StatusUsuario.INATIVO, result.getStatus());
    }

    @Test
    @DisplayName("Deve deletar usuário por ID com sucesso")
    void testDeleteById() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(new Usuario()));
        doNothing().when(usuarioRepository).deleteById(1L);
        assertDoesNotThrow(() -> usuarioService.deleteById(1L));
        verify(usuarioRepository).deleteById(1L);
    }
}
