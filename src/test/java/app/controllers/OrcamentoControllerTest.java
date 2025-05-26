package app.controllers;

import app.entities.Orcamento;
import app.entities.Usuario;
import app.exceptions.OrcamentoNotFoundException;
import app.services.OrcamentoService;
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
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import app.config.TestSecurityConfig;
import app.exceptions.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;

@WebMvcTest(OrcamentoController.class)
@Import({ TestSecurityConfig.class, GlobalExceptionHandler.class })
public class OrcamentoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrcamentoService orcamentoService;
    @Autowired
    private ObjectMapper objectMapper;
    private Orcamento orcamento;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");
        orcamento = new Orcamento();
        orcamento.setId(1L);
        orcamento.setUsuario(usuario);
    }

    @Test
    @DisplayName("GET /orcamentos - deve retornar todos os orçamentos")
    void testFindAll() throws Exception {
        List<Orcamento> orcamentos = Arrays.asList(orcamento);
        Mockito.when(orcamentoService.findAll()).thenReturn(orcamentos);
        mockMvc.perform(get("/orcamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("GET /orcamentos/{id} - deve retornar orçamento por id")
    void testFindById() throws Exception {
        Mockito.when(orcamentoService.findById(1L)).thenReturn(orcamento);
        mockMvc.perform(get("/orcamentos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /orcamentos/{id} - not found")
    void testFindByIdNotFound() throws Exception {
        Mockito.when(orcamentoService.findById(999L)).thenThrow(new OrcamentoNotFoundException(999L));
        mockMvc.perform(get("/orcamentos/999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("GET /orcamentos/usuario/{usuarioId} - deve retornar orçamentos do usuário")
    void testFindByUsuarioId() throws Exception {
        List<Orcamento> orcamentos = Arrays.asList(orcamento);
        Mockito.when(orcamentoService.findByUsuarioId(1L)).thenReturn(orcamentos);
        mockMvc.perform(get("/orcamentos/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("POST /orcamentos - deve criar orçamento")
    void testSave() throws Exception {
        Mockito.when(orcamentoService.save(any(Orcamento.class))).thenReturn(orcamento);
        mockMvc.perform(post("/orcamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orcamento)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /orcamentos/{id} - deve atualizar orçamento")
    void testUpdate() throws Exception {
        Mockito.when(orcamentoService.findById(1L)).thenReturn(orcamento);
        Mockito.when(orcamentoService.save(any(Orcamento.class))).thenReturn(orcamento);
        mockMvc.perform(put("/orcamentos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orcamento)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("DELETE /orcamentos/{id} - deve excluir orçamento")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/orcamentos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /orcamentos/{id} - not found")
    void testDeleteNotFound() throws Exception {
        Mockito.doThrow(new OrcamentoNotFoundException(999L)).when(orcamentoService).deleteById(999L);
        mockMvc.perform(delete("/orcamentos/999"))
                .andExpect(status().is4xxClientError());
    }
}