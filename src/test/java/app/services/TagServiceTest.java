package app.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import app.entities.Tag;
import app.exceptions.TagNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import app.repositories.TagRepository;

public class TagServiceTest {
    @Mock
    private TagRepository repo;

    @InjectMocks
    private TagService service;

    private Tag tag;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tag = Tag.builder().id(1L).nome("Tag Teste").build();
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Cenário base para TagService")
    void testMetodoExemplo() {
        assertTrue(true);
    }

    @Test
    void testFindAll() {
        when(repo.findAll()).thenReturn(Arrays.asList(tag));
        List<Tag> result = service.findAll();
        assertEquals(1, result.size());
        verify(repo).findAll();
    }

    @Test
    void testFindById() {
        when(repo.findById(1L)).thenReturn(Optional.of(tag));
        Tag result = service.findById(1L);
        assertNotNull(result);
        assertEquals(tag.getId(), result.getId());
        verify(repo).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(repo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> service.findById(999L));
        verify(repo).findById(999L);
    }

    @Test
    void testSave() {
        when(repo.findByNome(tag.getNome())).thenReturn(Optional.empty());
        when(repo.save(tag)).thenReturn(tag);
        Tag result = service.save(tag);
        assertNotNull(result);
        assertEquals(tag.getId(), result.getId());
        verify(repo).findByNome(tag.getNome());
        verify(repo).save(tag);
    }

    @Test
    void testSaveNomeDuplicadoCriacao() {
        Tag existente = Tag.builder().id(2L).nome("Tag Teste").build();
        when(repo.findByNome(tag.getNome())).thenReturn(Optional.of(existente));
        tag.setId(null);
        assertThrows(DataIntegrityViolationException.class, () -> service.save(tag));
    }

    @Test
    void testSaveNomeDuplicadoEdicao() {
        Tag existente = Tag.builder().id(2L).nome("Tag Teste").build();
        when(repo.findByNome(tag.getNome())).thenReturn(Optional.of(existente));
        tag.setId(1L);
        assertThrows(DataIntegrityViolationException.class, () -> service.save(tag));
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
        assertThrows(TagNotFoundException.class, () -> service.deleteById(999L));
        verify(repo).existsById(999L);
    }
}
