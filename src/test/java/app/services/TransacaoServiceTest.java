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
import app.entities.Tag;
import app.entities.Transacao;
import app.entities.TransacaoRecorrente;
import app.entities.Usuario;
import app.enums.Periodicidade;
import app.enums.TipoTransacao;
import app.exceptions.TransacaoNotFoundException;
import app.repositories.CategoriaRepository;
import app.repositories.ContaRepository;
import app.repositories.TagRepository;
import app.repositories.TransacaoRecorrenteRepository;
import app.repositories.TransacaoRepository;
import app.repositories.UsuarioRepository;

import java.util.ArrayList;

public class TransacaoServiceTest {
    @Mock
    private TransacaoRepository transacaoRepository;
    @Mock
    private TransacaoRecorrenteService transacaoRecorrenteService;
    @Mock
    private TransacaoRecorrenteRepository transacaoRecorrenteRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ContaRepository contaRepository;
    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TransacaoService service;

    private Transacao transacao;
    private Usuario usuario;
    private Conta conta;
    private Categoria categoria;
    private Tag tag;

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

        tag = new Tag();
        tag.setId(1L);
        tag.setNome("Tag Teste");

        transacao = new Transacao();
        transacao.setId(1L);
        transacao.setDescricao("Transação Teste");
        transacao.setValor(100.0);
        transacao.setUsuario(usuario);
        transacao.setConta(conta);
        transacao.setCategoria(categoria);
        transacao.setTipo(TipoTransacao.SAIDA);
        transacao.setDataTransacao(LocalDateTime.now());
        transacao.setTag(new ArrayList<>(List.of(tag)));
    }

    @Test
    @DisplayName("Deve retornar todas as transações")
    void testFindAll() {
        when(transacaoRecorrenteRepository.findRecorrenciasParaProcessar()).thenReturn(new ArrayList<>());
        when(transacaoRepository.findAll()).thenReturn(Arrays.asList(transacao));
        List<Transacao> resultado = service.findAll();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(transacao.getDescricao(), resultado.get(0).getDescricao());
        verify(transacaoRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar transações com filtros")
    void testBuscarComFiltros() {
        when(transacaoRepository.findAll()).thenReturn(Arrays.asList(transacao));
        List<Transacao> resultado = service.buscarComFiltros("SAIDA", 1L, 1L, 1L);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(transacao.getDescricao(), resultado.get(0).getDescricao());
    }

    @Test
    @DisplayName("Deve encontrar transação por ID")
    void testFindById() {
        when(transacaoRepository.findById(1L)).thenReturn(Optional.of(transacao));
        Transacao resultado = service.findById(1L);
        assertNotNull(resultado);
        assertEquals(transacao.getId(), resultado.getId());
        verify(transacaoRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando transação não encontrada por ID")
    void testFindByIdNotFound() {
        when(transacaoRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(TransacaoNotFoundException.class, () -> service.findById(999L));
        verify(transacaoRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve buscar transações por conta")
    void testFindByContaId() {
        when(transacaoRepository.findByContaId(1L)).thenReturn(Arrays.asList(transacao));
        List<Transacao> resultado = service.findByContaId(1L);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(transacaoRepository).findByContaId(1L);
    }

    @Test
    @DisplayName("Deve buscar transações por categoria")
    void testFindByCategoriaId() {
        when(transacaoRepository.findByCategoriaId(1L)).thenReturn(Arrays.asList(transacao));
        List<Transacao> resultado = service.findByCategoriaId(1L);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(transacaoRepository).findByCategoriaId(1L);
    }

    @Test
    @DisplayName("Deve buscar transações por usuário e tipo")
    void testFindByUsuarioAndTipo() {
        when(transacaoRepository.findByUsuarioAndTipo(1L, TipoTransacao.SAIDA)).thenReturn(Arrays.asList(transacao));
        List<Transacao> resultado = service.findByUsuarioAndTipo(1L, TipoTransacao.SAIDA);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(transacaoRepository).findByUsuarioAndTipo(1L, TipoTransacao.SAIDA);
    }

    @Test
    @DisplayName("Deve buscar transações por periodicidade")
    void testFindByPeriodicidade() {
        when(transacaoRepository.findByPeriodicidade(Periodicidade.MENSAL)).thenReturn(Arrays.asList(transacao));
        List<Transacao> resultado = service.findByPeriodicidade(Periodicidade.MENSAL);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(transacaoRepository).findByPeriodicidade(Periodicidade.MENSAL);
    }

    @Test
    @DisplayName("Deve buscar transações por usuário e periodicidade")
    void testFindByUsuarioAndPeriodicidade() {
        when(transacaoRepository.findByUsuarioAndPeriodicidade(1L, Periodicidade.MENSAL))
                .thenReturn(Arrays.asList(transacao));
        List<Transacao> resultado = service.findByUsuarioAndPeriodicidade(1L, Periodicidade.MENSAL);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(transacaoRepository).findByUsuarioAndPeriodicidade(1L, Periodicidade.MENSAL);
    }

    @Test
    @DisplayName("Deve salvar transação com sucesso")
    void testSave() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);
        Transacao resultado = service.save(transacao);
        assertNotNull(resultado);
        assertEquals(transacao.getDescricao(), resultado.getDescricao());
        verify(usuarioRepository).findById(1L);
        verify(contaRepository).findById(1L);
        verify(categoriaRepository).findById(1L);
        verify(tagRepository).findById(1L);
        verify(transacaoRepository).save(transacao);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar transação com usuário inexistente")
    void testSaveUsuarioNotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DataIntegrityViolationException.class, () -> service.save(transacao));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar transação com conta inexistente")
    void testSaveContaNotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(contaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DataIntegrityViolationException.class, () -> service.save(transacao));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar transação com categoria inexistente")
    void testSaveCategoriaNotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DataIntegrityViolationException.class, () -> service.save(transacao));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar transação com tag inexistente")
    void testSaveTagNotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DataIntegrityViolationException.class, () -> service.save(transacao));
    }

    @Test
    @DisplayName("Deve excluir transação com sucesso")
    void testDeleteById() {
        when(transacaoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(transacaoRepository).deleteById(1L);
        assertDoesNotThrow(() -> service.deleteById(1L));
        verify(transacaoRepository).existsById(1L);
        verify(transacaoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar excluir transação inexistente")
    void testDeleteByIdNotFound() {
        when(transacaoRepository.existsById(999L)).thenReturn(false);
        assertThrows(TransacaoNotFoundException.class, () -> service.deleteById(999L));
        verify(transacaoRepository).existsById(999L);
        verify(transacaoRepository, never()).deleteById(any());
    }
}
