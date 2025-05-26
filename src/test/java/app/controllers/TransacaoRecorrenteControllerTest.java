package app.controllers;

import app.entities.TransacaoRecorrente;
import app.entities.Usuario;
import app.entities.Conta;
import app.entities.Categoria;
import app.enums.Periodicidade;
import app.enums.TipoTransacao;
import app.exceptions.TransacaoRecorrenteNotFoundException;
import app.services.TransacaoRecorrenteService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import app.config.TestSecurityConfig;
import app.exceptions.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;

@WebMvcTest(TransacaoRecorrenteController.class)
@Import({ TestSecurityConfig.class, GlobalExceptionHandler.class })
public class TransacaoRecorrenteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransacaoRecorrenteService service;
    @Autowired
    private ObjectMapper objectMapper;
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
    @DisplayName("GET /transacoes-recorrentes - deve retornar todas as transações recorrentes")
    void testFindAll() throws Exception {
        List<TransacaoRecorrente> lista = Arrays.asList(recorrente);
        Mockito.when(service.findAll()).thenReturn(lista);
        mockMvc.perform(get("/transacoes-recorrentes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("GET /transacoes-recorrentes/{id} - deve retornar transação recorrente por id")
    void testFindById() throws Exception {
        Mockito.when(service.findById(1L)).thenReturn(recorrente);
        mockMvc.perform(get("/transacoes-recorrentes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /transacoes-recorrentes/{id} - not found")
    void testFindByIdNotFound() throws Exception {
        Mockito.when(service.findById(999L)).thenThrow(new TransacaoRecorrenteNotFoundException(999L));
        mockMvc.perform(get("/transacoes-recorrentes/999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("POST /transacoes-recorrentes - deve criar transação recorrente")
    void testSave() throws Exception {
        Mockito.when(service.save(any(TransacaoRecorrente.class))).thenReturn(recorrente);
        mockMvc.perform(post("/transacoes-recorrentes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recorrente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /transacoes-recorrentes/{id} - deve atualizar transação recorrente")
    void testUpdate() throws Exception {
        Mockito.when(service.findById(1L)).thenReturn(recorrente);
        Mockito.when(service.save(any(TransacaoRecorrente.class))).thenReturn(recorrente);
        mockMvc.perform(put("/transacoes-recorrentes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recorrente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("DELETE /transacoes-recorrentes/{id} - deve excluir transação recorrente")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/transacoes-recorrentes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /transacoes-recorrentes/{id} - not found")
    void testDeleteNotFound() throws Exception {
        Mockito.doThrow(new TransacaoRecorrenteNotFoundException(999L)).when(service).deleteById(999L);
        mockMvc.perform(delete("/transacoes-recorrentes/999"))
                .andExpect(status().is4xxClientError());
    }
}