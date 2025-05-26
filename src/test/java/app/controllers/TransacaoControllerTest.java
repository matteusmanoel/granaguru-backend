package app.controllers;

import app.entities.Transacao;
import app.entities.Usuario;
import app.entities.Conta;
import app.entities.Categoria;
import app.entities.Tag;
import app.enums.TipoTransacao;
import app.enums.Periodicidade;
import app.exceptions.TransacaoNotFoundException;
import app.services.TransacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import app.config.TestSecurityConfig;
import app.exceptions.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;

@WebMvcTest(TransacaoController.class)
@Import({ TestSecurityConfig.class, GlobalExceptionHandler.class })
public class TransacaoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransacaoService transacaoService;
    @Autowired
    private ObjectMapper objectMapper;
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
    @DisplayName("GET /transacoes - deve retornar todas as transações")
    void testFindAll() throws Exception {
        List<Transacao> transacoes = Arrays.asList(transacao);
        Mockito.when(transacaoService.findAll()).thenReturn(transacoes);
        mockMvc.perform(get("/transacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("GET /transacoes/{id} - deve retornar transação por id")
    void testFindById() throws Exception {
        Mockito.when(transacaoService.findById(1L)).thenReturn(transacao);
        mockMvc.perform(get("/transacoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /transacoes/{id} - not found")
    void testFindByIdNotFound() throws Exception {
        Mockito.when(transacaoService.findById(999L)).thenThrow(new TransacaoNotFoundException(999L));
        mockMvc.perform(get("/transacoes/999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("POST /transacoes - deve criar transação")
    void testSave() throws Exception {
        Mockito.when(transacaoService.save(any(Transacao.class))).thenReturn(transacao);
        mockMvc.perform(post("/transacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /transacoes/{id} - deve atualizar transação")
    void testUpdate() throws Exception {
        Mockito.when(transacaoService.findById(1L)).thenReturn(transacao);
        Mockito.when(transacaoService.save(any(Transacao.class))).thenReturn(transacao);
        mockMvc.perform(put("/transacoes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("DELETE /transacoes/{id} - deve excluir transação")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/transacoes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /transacoes/{id} - not found")
    void testDeleteNotFound() throws Exception {
        Mockito.doThrow(new TransacaoNotFoundException(999L)).when(transacaoService).deleteById(999L);
        mockMvc.perform(delete("/transacoes/999"))
                .andExpect(status().is4xxClientError());
    }
}