package app.controllers;

import static org.mockito.ArgumentMatchers.any;
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
import app.entities.TransacaoRecorrente;
import app.entities.Usuario;
import app.enums.Periodicidade;
import app.enums.TipoTransacao;
import app.exceptions.TransacaoRecorrenteNotFoundException;
import app.services.TransacaoRecorrenteService;

@WebMvcTest(TransacaoRecorrenteController.class)
public class TransacaoRecorrenteControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransacaoRecorrenteService service;

    private TransacaoRecorrente recorrente;
    private Usuario usuario;
    private Conta conta;
    private Categoria categoria;

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
    void testFindAll() throws Exception {
        List<TransacaoRecorrente> lista = Arrays.asList(recorrente);
        when(service.findAll()).thenReturn(lista);

        mockMvc.perform(get("/transacoes-recorrentes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descricao").value("Recorrente Teste"));
    }

    @Test
    @DisplayName("Deve encontrar transação recorrente por ID")
    void testFindById() throws Exception {
        when(service.findById(1L)).thenReturn(recorrente);

        mockMvc.perform(get("/transacoes-recorrentes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Recorrente Teste"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando transação recorrente não encontrada")
    void testFindByIdNotFound() throws Exception {
        when(service.findById(999L)).thenThrow(new TransacaoRecorrenteNotFoundException(999L));

        mockMvc.perform(get("/transacoes-recorrentes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve criar nova transação recorrente")
    void testSave() throws Exception {
        when(service.save(any(TransacaoRecorrente.class))).thenReturn(recorrente);

        mockMvc.perform(post("/transacoes-recorrentes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recorrente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Recorrente Teste"));
    }

    @Test
    @DisplayName("Deve atualizar transação recorrente existente")
    void testUpdate() throws Exception {
        when(service.findById(1L)).thenReturn(recorrente);
        when(service.save(any(TransacaoRecorrente.class))).thenReturn(recorrente);

        mockMvc.perform(put("/transacoes-recorrentes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recorrente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Recorrente Teste"));
    }

    @Test
    @DisplayName("Deve excluir transação recorrente")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/transacoes-recorrentes/1"))
                .andExpect(status().isNoContent());
    }
}
