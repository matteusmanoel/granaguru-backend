package app.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
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

import app.entities.Meta;
import app.entities.Usuario;
import app.enums.StatusMeta;
import app.exceptions.MetaNotFoundException;
import app.repositories.MetaRepository;
import app.repositories.UsuarioRepository;

public class MetaServiceTest {
    @Mock
    private MetaRepository metaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private MetaService service;

    private Meta meta;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");

        meta = new Meta();
        meta.setId(1L);
        meta.setDescricao("Meta Teste");
        meta.setValorObjetivo(1000.0);
        meta.setValorAtual(100.0);
        meta.setDataInicio(LocalDate.now());
        meta.setDataTermino(LocalDate.now().plusMonths(1));
        meta.setStatus(StatusMeta.EM_ANDAMENTO);
        meta.setUsuario(usuario);
    }

    @Test
    @DisplayName("Deve retornar todas as metas")
    void testListAll() {
        List<Meta> metas = Arrays.asList(meta);
        when(metaRepository.findAll()).thenReturn(metas);
        List<Meta> resultado = service.listAll();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(meta.getDescricao(), resultado.get(0).getDescricao());
        verify(metaRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar metas em andamento por usuário")
    void testBuscarMetasEmAndamento() {
        List<Meta> metas = Arrays.asList(meta);
        when(metaRepository.findMetasEmAndamentoPorUsuario(1L)).thenReturn(metas);
        List<Meta> resultado = service.buscarMetasEmAndamento(1L);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(meta.getDescricao(), resultado.get(0).getDescricao());
        verify(metaRepository).findMetasEmAndamentoPorUsuario(1L);
    }

    @Test
    @DisplayName("Deve encontrar meta por ID")
    void testFindById() {
        when(metaRepository.findById(1L)).thenReturn(Optional.of(meta));
        Meta resultado = service.findById(1L);
        assertNotNull(resultado);
        assertEquals(meta.getId(), resultado.getId());
        assertEquals(meta.getDescricao(), resultado.getDescricao());
        verify(metaRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando meta não encontrada por ID")
    void testFindByIdNotFound() {
        when(metaRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(MetaNotFoundException.class, () -> service.findById(999L));
        verify(metaRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve salvar meta com sucesso")
    void testSave() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(metaRepository.existsByDescricaoAndUsuario(meta.getDescricao(), usuario)).thenReturn(false);
        when(metaRepository.save(any(Meta.class))).thenReturn(meta);
        Meta resultado = service.save(meta);
        assertNotNull(resultado);
        assertEquals(meta.getDescricao(), resultado.getDescricao());
        verify(usuarioRepository).findById(1L);
        verify(metaRepository).existsByDescricaoAndUsuario(meta.getDescricao(), usuario);
        verify(metaRepository).save(meta);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar meta com usuário inexistente")
    void testSaveUsuarioNotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DataIntegrityViolationException.class, () -> service.save(meta));
        verify(usuarioRepository).findById(1L);
        verify(metaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar meta duplicada para o mesmo usuário")
    void testSaveMetaDuplicada() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(metaRepository.existsByDescricaoAndUsuario(meta.getDescricao(), usuario)).thenReturn(true);
        assertThrows(DataIntegrityViolationException.class, () -> service.save(meta));
        verify(metaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve atualizar meta com sucesso")
    void testUpdate() {
        Meta metaAtualizada = new Meta();
        metaAtualizada.setDescricao("Nova Descrição");
        metaAtualizada.setValorObjetivo(2000.0);
        metaAtualizada.setValorAtual(500.0);
        metaAtualizada.setDataInicio(LocalDate.now());
        metaAtualizada.setDataTermino(LocalDate.now().plusMonths(2));
        metaAtualizada.setStatus(StatusMeta.CONCLUIDA);
        metaAtualizada.setUsuario(usuario);

        when(metaRepository.findById(1L)).thenReturn(Optional.of(meta));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(metaRepository.save(any(Meta.class))).thenReturn(metaAtualizada);

        Meta resultado = service.update(1L, metaAtualizada);
        assertNotNull(resultado);
        assertEquals("Nova Descrição", resultado.getDescricao());
        assertEquals(2000.0, resultado.getValorObjetivo());
        assertEquals(500.0, resultado.getValorAtual());
        assertEquals(StatusMeta.CONCLUIDA, resultado.getStatus());
        verify(metaRepository).findById(1L);
        verify(usuarioRepository).findById(1L);
        verify(metaRepository).save(any(Meta.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar meta com usuário inexistente")
    void testUpdateUsuarioNotFound() {
        Meta metaAtualizada = new Meta();
        metaAtualizada.setUsuario(usuario);
        when(metaRepository.findById(1L)).thenReturn(Optional.of(meta));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DataIntegrityViolationException.class, () -> service.update(1L, metaAtualizada));
    }

    @Test
    @DisplayName("Deve excluir meta com sucesso")
    void testDeleteById() {
        when(metaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(metaRepository).deleteById(1L);
        assertDoesNotThrow(() -> service.deleteById(1L));
        verify(metaRepository).existsById(1L);
        verify(metaRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar excluir meta inexistente")
    void testDeleteByIdNotFound() {
        when(metaRepository.existsById(999L)).thenReturn(false);
        assertThrows(MetaNotFoundException.class, () -> service.deleteById(999L));
        verify(metaRepository).existsById(999L);
        verify(metaRepository, never()).deleteById(any());
    }
}
