package app.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import app.entities.Orcamento;
import app.entities.Usuario;
import app.entities.Categoria;
import app.exceptions.OrcamentoNotFoundException;
import app.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import app.repositories.OrcamentoRepository;

public class OrcamentoServiceTest {
    @Mock
    private OrcamentoRepository repo;
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private OrcamentoService service;
    private Orcamento orcamento;
    private Usuario usuario;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = Usuario.builder().id(1L).nome("Usuário Teste").build();
        categoria = Categoria.builder().id(1L).nomeCategoria("Categoria Teste").usuario(usuario).build();
        orcamento = Orcamento.builder().id(1L).usuario(usuario).categoria(categoria).periodo("MENSAL")
                .valorLimite(100.0).build();
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Cenário base para OrcamentoService")
    void testMetodoExemplo() {
        assertTrue(true);
    }

    @Test
    void testFindAll() {
        when(repo.findAll()).thenReturn(Arrays.asList(orcamento));
        List<Orcamento> result = service.findAll();
        assertEquals(1, result.size());
        verify(repo).findAll();
    }

    @Test
    void testFindByUsuarioId() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(repo.findByUsuarioId(1L)).thenReturn(Arrays.asList(orcamento));
        List<Orcamento> result = service.findByUsuarioId(1L);
        assertEquals(1, result.size());
        verify(usuarioRepository).existsById(1L);
        verify(repo).findByUsuarioId(1L);
    }

    @Test
    void testFindByUsuarioIdNotFound() {
        when(usuarioRepository.existsById(999L)).thenReturn(false);
        assertThrows(OrcamentoNotFoundException.class, () -> service.findByUsuarioId(999L));
        verify(usuarioRepository).existsById(999L);
    }

    @Test
    void testFindById() {
        when(repo.findById(1L)).thenReturn(Optional.of(orcamento));
        Orcamento result = service.findById(1L);
        assertNotNull(result);
        assertEquals(orcamento.getId(), result.getId());
        verify(repo).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(repo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(OrcamentoNotFoundException.class, () -> service.findById(999L));
        verify(repo).findById(999L);
    }

    @Test
    void testSave() {
        when(repo.save(orcamento)).thenReturn(orcamento);
        Orcamento result = service.save(orcamento);
        assertNotNull(result);
        assertEquals(orcamento.getId(), result.getId());
        verify(repo).save(orcamento);
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
        assertThrows(OrcamentoNotFoundException.class, () -> service.deleteById(999L));
        verify(repo).existsById(999L);
    }
}
