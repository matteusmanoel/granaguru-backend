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

import app.entities.Conta;
import app.entities.Usuario;
import app.exceptions.ContaNotFoundException;
import app.exceptions.UsuarioNotFoundException;
import app.repositories.ContaRepository;
import app.repositories.UsuarioRepository;

public class ContaServiceTest {
    @Mock
    private ContaRepository contaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ContaService service;

    private Conta conta;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");

        conta = new Conta();
        conta.setId(1L);
        conta.setNomeConta("Conta Teste");
        conta.setUsuario(usuario);
    }

    @Test
    @DisplayName("Deve retornar todas as contas")
    void testFindAll() {
        List<Conta> contas = Arrays.asList(conta);
        when(contaRepository.findAll()).thenReturn(contas);

        List<Conta> resultado = service.findAll();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(conta.getNomeConta(), resultado.get(0).getNomeConta());
        verify(contaRepository).findAll();
    }

    @Test
    @DisplayName("Deve encontrar conta por ID")
    void testFindById() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        Conta resultado = service.findById(1L);
        assertNotNull(resultado);
        assertEquals(conta.getId(), resultado.getId());
        assertEquals(conta.getNomeConta(), resultado.getNomeConta());
        verify(contaRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando conta não encontrada por ID")
    void testFindByIdNotFound() {
        when(contaRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ContaNotFoundException.class, () -> service.findById(999L));
        verify(contaRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve encontrar contas por ID do usuário")
    void testFindByUsuarioId() {
        List<Conta> contas = Arrays.asList(conta);
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(contaRepository.findByUsuarioId(1L)).thenReturn(contas);

        List<Conta> resultado = service.findByUsuarioId(1L);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(conta.getNomeConta(), resultado.get(0).getNomeConta());
        verify(usuarioRepository).existsById(1L);
        verify(contaRepository).findByUsuarioId(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado ao buscar contas")
    void testFindByUsuarioIdNotFound() {
        when(usuarioRepository.existsById(999L)).thenReturn(false);
        assertThrows(ContaNotFoundException.class, () -> service.findByUsuarioId(999L));
        verify(usuarioRepository).existsById(999L);
    }

    @Test
    @DisplayName("Deve salvar conta com sucesso")
    void testSave() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Conta resultado = service.save(conta);
        assertNotNull(resultado);
        assertEquals(conta.getId(), resultado.getId());
        assertEquals(conta.getNomeConta(), resultado.getNomeConta());
        verify(usuarioRepository).findById(1L);
        verify(contaRepository).save(conta);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar conta sem usuário")
    void testSaveWithoutUsuario() {
        conta.setUsuario(null);
        assertThrows(DataIntegrityViolationException.class, () -> service.save(conta));
        verify(usuarioRepository, never()).findById(any());
        verify(contaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve excluir conta com sucesso")
    void testDeleteById() {
        when(contaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(contaRepository).deleteById(1L);
        assertDoesNotThrow(() -> service.deleteById(1L));
        verify(contaRepository).existsById(1L);
        verify(contaRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar excluir conta inexistente")
    void testDeleteByIdNotFound() {
        when(contaRepository.existsById(999L)).thenReturn(false);
        assertThrows(ContaNotFoundException.class, () -> service.deleteById(999L));
        verify(contaRepository).existsById(999L);
        verify(contaRepository, never()).deleteById(any());
    }
}
