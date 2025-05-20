package app.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
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
import app.entities.Conta;
import app.entities.TransacaoRecorrente;
import app.entities.Usuario;
import app.enums.Periodicidade;
import app.enums.TipoTransacao;
import app.exceptions.TransacaoRecorrenteNotFoundException;
import app.repositories.CategoriaRepository;
import app.repositories.ContaRepository;
import app.repositories.TransacaoRecorrenteRepository;
import app.repositories.TransacaoRepository;
import app.repositories.UsuarioRepository;

public class TransacaoRecorrenteServiceTest {
    @Mock
    private TransacaoRecorrenteRepository transacaoRecorrenteRepository;
    @Mock
    private TransacaoRepository transacaoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ContaRepository contaRepository;
    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private TransacaoRecorrenteService service;

    private TransacaoRecorrente recorrente;
    private Usuario usuario;
    private Conta conta;
    private Categoria categoria;

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

        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNomeCategoria("Categoria Teste");
        categoria.setUsuario(usuario);

        recorrente = new TransacaoRecorrente();
        recorrente.setId(1L);
        recorrente.setDescricao("Recorrente Teste");
        recorrente.setUsuario(usuario);
        recorrente.setConta(conta);
        recorrente.setCategoria(categoria);
        recorrente.setTipo(TipoTransacao.SAIDA);
        recorrente.setValor(100.0);
        recorrente.setPeriodicidade(Periodicidade.MENSAL);
        recorrente.setDataInicial(LocalDateTime.now());
        recorrente.setProximaExecucao(LocalDateTime.now().plusMonths(1));
    }

    @Test
    @DisplayName("Deve retornar todas as transações recorrentes")
    void testFindAll() {
        List<TransacaoRecorrente> lista = Arrays.asList(recorrente);
        when(transacaoRecorrenteRepository.findAll()).thenReturn(lista);
        List<TransacaoRecorrente> resultado = service.findAll();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(recorrente.getDescricao(), resultado.get(0).getDescricao());
        verify(transacaoRecorrenteRepository).findAll();
    }

    @Test
    @DisplayName("Deve encontrar transação recorrente por ID")
    void testFindById() {
        when(transacaoRecorrenteRepository.findById(1L)).thenReturn(Optional.of(recorrente));
        TransacaoRecorrente resultado = service.findById(1L);
        assertNotNull(resultado);
        assertEquals(recorrente.getId(), resultado.getId());
        verify(transacaoRecorrenteRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando transação recorrente não encontrada por ID")
    void testFindByIdNotFound() {
        when(transacaoRecorrenteRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(TransacaoRecorrenteNotFoundException.class, () -> service.findById(999L));
        verify(transacaoRecorrenteRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve salvar transação recorrente com sucesso")
    void testSave() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(transacaoRecorrenteRepository.save(any(TransacaoRecorrente.class))).thenReturn(recorrente);
        TransacaoRecorrente resultado = service.save(recorrente);
        assertNotNull(resultado);
        assertEquals(recorrente.getDescricao(), resultado.getDescricao());
        verify(usuarioRepository).findById(1L);
        verify(contaRepository).findById(1L);
        verify(categoriaRepository).findById(1L);
        verify(transacaoRecorrenteRepository).save(recorrente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar transação recorrente com usuário inexistente")
    void testSaveUsuarioNotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DataIntegrityViolationException.class, () -> service.save(recorrente));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar transação recorrente com conta inexistente")
    void testSaveContaNotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(contaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DataIntegrityViolationException.class, () -> service.save(recorrente));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar transação recorrente com categoria inexistente")
    void testSaveCategoriaNotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DataIntegrityViolationException.class, () -> service.save(recorrente));
    }

    @Test
    @DisplayName("Deve excluir transação recorrente com sucesso")
    void testDeleteById() {
        when(transacaoRecorrenteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(transacaoRecorrenteRepository).deleteById(1L);
        assertDoesNotThrow(() -> service.deleteById(1L));
        verify(transacaoRecorrenteRepository).existsById(1L);
        verify(transacaoRecorrenteRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar excluir transação recorrente inexistente")
    void testDeleteByIdNotFound() {
        when(transacaoRecorrenteRepository.existsById(999L)).thenReturn(false);
        assertThrows(TransacaoRecorrenteNotFoundException.class, () -> service.deleteById(999L));
        verify(transacaoRecorrenteRepository).existsById(999L);
        verify(transacaoRecorrenteRepository, never()).deleteById(any());
    }
}
