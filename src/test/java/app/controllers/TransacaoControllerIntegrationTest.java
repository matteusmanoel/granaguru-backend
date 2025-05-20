package app.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.entities.Categoria;
import app.entities.Conta;
import app.entities.Tag;
import app.entities.Transacao;
import app.entities.Usuario;
import app.enums.Periodicidade;
import app.enums.TipoTransacao;
import app.exceptions.TransacaoNotFoundException;
import app.services.TransacaoService;

import java.util.ArrayList;

@WebMvcTest(TransacaoController.class)
public class TransacaoControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransacaoService service;

    private Transacao transacao;
    private Usuario usuario;
    private Conta conta;
    private Categoria categoria;
    private Tag tag;

    @BeforeEach
    void setUp() {
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
    void testFindAll() throws Exception {
        List<Transacao> transacoes = Arrays.asList(transacao);
        when(service.findAll()).thenReturn(transacoes);

        mockMvc.perform(get("/transacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descricao").value("Transação Teste"));
    }

    @Test
    @DisplayName("Deve buscar transações com filtros")
    void testBuscarComFiltros() throws Exception {
        List<Transacao> transacoes = Arrays.asList(transacao);
        when(service.buscarComFiltros(eq("SAIDA"), eq(1L), eq(1L), eq(1L))).thenReturn(transacoes);

        mockMvc.perform(get("/transacoes/filtro?tipo=SAIDA&categoriaId=1&contaId=1&usuarioId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descricao").value("Transação Teste"));
    }

    @Test
    @DisplayName("Deve encontrar transação por ID")
    void testFindById() throws Exception {
        when(service.findById(1L)).thenReturn(transacao);

        mockMvc.perform(get("/transacoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Transação Teste"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando transação não encontrada")
    void testFindByIdNotFound() throws Exception {
        when(service.findById(999L)).thenThrow(new TransacaoNotFoundException(999L));

        mockMvc.perform(get("/transacoes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve buscar transações por conta")
    void testFindByContaId() throws Exception {
        List<Transacao> transacoes = Arrays.asList(transacao);
        when(service.findByContaId(1L)).thenReturn(transacoes);

        mockMvc.perform(get("/transacoes/conta/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descricao").value("Transação Teste"));
    }

    @Test
    @DisplayName("Deve buscar transações por categoria")
    void testFindByCategoriaId() throws Exception {
        List<Transacao> transacoes = Arrays.asList(transacao);
        when(service.findByCategoriaId(1L)).thenReturn(transacoes);

        mockMvc.perform(get("/transacoes/categoria/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descricao").value("Transação Teste"));
    }

    @Test
    @DisplayName("Deve buscar transações por usuário e tipo")
    void testFindByUsuarioAndTipo() throws Exception {
        List<Transacao> transacoes = Arrays.asList(transacao);
        when(service.findByUsuarioAndTipo(1L, TipoTransacao.SAIDA)).thenReturn(transacoes);

        mockMvc.perform(get("/transacoes/usuario/1/tipo/SAIDA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descricao").value("Transação Teste"));
    }

    @Test
    @DisplayName("Deve buscar transações por periodicidade")
    void testFindByPeriodicidade() throws Exception {
        List<Transacao> transacoes = Arrays.asList(transacao);
        when(service.findByPeriodicidade(Periodicidade.MENSAL)).thenReturn(transacoes);

        mockMvc.perform(get("/transacoes/periodicidade/MENSAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descricao").value("Transação Teste"));
    }

    @Test
    @DisplayName("Deve buscar transações por usuário e periodicidade")
    void testFindByUsuarioAndPeriodicidade() throws Exception {
        List<Transacao> transacoes = Arrays.asList(transacao);
        when(service.findByUsuarioAndPeriodicidade(1L, Periodicidade.MENSAL)).thenReturn(transacoes);

        mockMvc.perform(get("/transacoes/usuario/1/periodicidade/MENSAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descricao").value("Transação Teste"));
    }

    @Test
    @DisplayName("Deve criar nova transação")
    void testSave() throws Exception {
        when(service.save(any(Transacao.class))).thenReturn(transacao);

        mockMvc.perform(post("/transacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Transação Teste"));
    }

    @Test
    @DisplayName("Deve atualizar transação existente")
    void testUpdate() throws Exception {
        when(service.findById(1L)).thenReturn(transacao);
        when(service.save(any(Transacao.class))).thenReturn(transacao);

        mockMvc.perform(put("/transacoes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Transação Teste"));
    }

    @Test
    @DisplayName("Deve excluir transação")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/transacoes/1"))
                .andExpect(status().isNoContent());
    }
}
