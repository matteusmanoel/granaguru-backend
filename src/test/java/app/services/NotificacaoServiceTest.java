package app.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import app.entities.Notificacao;
import app.entities.Usuario;
import app.enums.TipoNotificacao;
import app.exceptions.NotificacaoNotFoundException;
import app.repositories.UsuarioRepository;
import org.mockito.Mock;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import app.repositories.NotificacaoRepository;

public class NotificacaoServiceTest {
    @Mock
    private NotificacaoRepository repo;
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private NotificacaoService service;
    private Notificacao notificacao;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = Usuario.builder().id(1L).nome("Usuário Teste").email("teste@email.com").senha("123456")
                .role(app.enums.Role.USER).status(app.enums.StatusUsuario.ATIVO).build();
        notificacao = Notificacao.builder().id(1L).usuario(usuario).titulo("Titulo").mensagem("Mensagem")
                .tipo(TipoNotificacao.ALERTA_DESPESA).dataEnvio(LocalDateTime.now()).lida(false).build();
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Cenário base para NotificacaoService")
    void testMetodoExemplo() {
        assertTrue(true);
    }

    @Test
    void testFindAll() {
        when(repo.findAll()).thenReturn(Arrays.asList(notificacao));
        List<Notificacao> result = service.findAll();
        assertEquals(1, result.size());
        verify(repo).findAll();
    }

    @Test
    void testFindByUsuarioId() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(repo.findByUsuarioId(1L)).thenReturn(Arrays.asList(notificacao));
        List<Notificacao> result = service.findByUsuarioId(1L);
        assertEquals(1, result.size());
        verify(usuarioRepository).existsById(1L);
        verify(repo).findByUsuarioId(1L);
    }

    @Test
    void testFindByUsuarioIdNotFound() {
        when(usuarioRepository.existsById(999L)).thenReturn(false);
        assertThrows(NotificacaoNotFoundException.class, () -> service.findByUsuarioId(999L));
        verify(usuarioRepository).existsById(999L);
    }

    @Test
    void testFindById() {
        when(repo.findById(1L)).thenReturn(Optional.of(notificacao));
        Notificacao result = service.findById(1L);
        assertNotNull(result);
        assertEquals(notificacao.getId(), result.getId());
        verify(repo).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(repo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NotificacaoNotFoundException.class, () -> service.findById(999L));
        verify(repo).findById(999L);
    }

    @Test
    void testSave() {
        when(repo.save(notificacao)).thenReturn(notificacao);
        Notificacao result = service.save(notificacao);
        assertNotNull(result);
        assertEquals(notificacao.getId(), result.getId());
        verify(repo).save(notificacao);
    }

    @Test
    void testDeleteById() {
        when(repo.existsById(1L)).thenReturn(true);
        doNothing().when(repo).deleteById(1L);
        assertDoesNotThrow(() -> service.deleteById(1L));
        verify(repo).existsById(1L);
        verify(repo).deleteById(1L);
    }

    @Test
    void testDeleteByIdNotFound() {
        when(repo.existsById(999L)).thenReturn(false);
        assertThrows(NotificacaoNotFoundException.class, () -> service.deleteById(999L));
        verify(repo).existsById(999L);
    }
}
