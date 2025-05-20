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

import app.entities.Categoria;
import app.entities.Usuario;
import app.enums.TipoCategoria;
import app.exceptions.CategoriaNotFoundException;
import app.exceptions.UsuarioNotFoundException;
import app.repositories.CategoriaRepository;
import app.repositories.UsuarioRepository;

public class CategoriaServiceTest {
    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CategoriaService service;

    private Categoria categoria;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");

        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNomeCategoria("Categoria Teste");
        categoria.setUsuario(usuario);
        categoria.setTipo(TipoCategoria.DESPESA);
    }

    @Test
    @DisplayName("Deve retornar todas as categorias")
    void testFindAll() {
        List<Categoria> categorias = Arrays.asList(categoria);
        when(categoriaRepository.findAll()).thenReturn(categorias);

        List<Categoria> resultado = service.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(categoria.getNomeCategoria(), resultado.get(0).getNomeCategoria());
        verify(categoriaRepository).findAll();
    }

    @Test
    @DisplayName("Deve encontrar categoria por ID")
    void testFindById() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Categoria resultado = service.findById(1L);

        assertNotNull(resultado);
        assertEquals(categoria.getId(), resultado.getId());
        assertEquals(categoria.getNomeCategoria(), resultado.getNomeCategoria());
        verify(categoriaRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando categoria não encontrada por ID")
    void testFindByIdNotFound() {
        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(CategoriaNotFoundException.class, () -> service.findById(999L));
        verify(categoriaRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve encontrar categorias por ID do usuário")
    void testFindByUsuarioId() {
        List<Categoria> categorias = Arrays.asList(categoria);
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(categoriaRepository.findByUsuarioId(1L)).thenReturn(categorias);

        List<Categoria> resultado = service.findByUsuarioId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(categoria.getNomeCategoria(), resultado.get(0).getNomeCategoria());
        verify(usuarioRepository).existsById(1L);
        verify(categoriaRepository).findByUsuarioId(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado ao buscar categorias")
    void testFindByUsuarioIdNotFound() {
        when(usuarioRepository.existsById(999L)).thenReturn(false);

        assertThrows(CategoriaNotFoundException.class, () -> service.findByUsuarioId(999L));
        verify(usuarioRepository).existsById(999L);
    }

    @Test
    @DisplayName("Deve salvar categoria com sucesso")
    void testSave() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        Categoria resultado = service.save(categoria);

        assertNotNull(resultado);
        assertEquals(categoria.getId(), resultado.getId());
        assertEquals(categoria.getNomeCategoria(), resultado.getNomeCategoria());
        verify(usuarioRepository).findById(1L);
        verify(categoriaRepository).save(categoria);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar categoria sem usuário")
    void testSaveWithoutUsuario() {
        categoria.setUsuario(null);

        assertThrows(DataIntegrityViolationException.class, () -> service.save(categoria));
        verify(usuarioRepository, never()).findById(any());
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve excluir categoria com sucesso")
    void testDeleteById() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(categoriaRepository).deleteById(1L);

        assertDoesNotThrow(() -> service.deleteById(1L));
        verify(categoriaRepository).existsById(1L);
        verify(categoriaRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar excluir categoria inexistente")
    void testDeleteByIdNotFound() {
        when(categoriaRepository.existsById(999L)).thenReturn(false);

        assertThrows(CategoriaNotFoundException.class, () -> service.deleteById(999L));
        verify(categoriaRepository).existsById(999L);
        verify(categoriaRepository, never()).deleteById(any());
    }
}
