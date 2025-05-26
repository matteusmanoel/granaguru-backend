package app.controllers;

import app.entities.Conta;
import app.entities.Usuario;
import app.exceptions.ContaNotFoundException;
import app.services.ContaService;
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

@WebMvcTest(ContaController.class)
@Import({ TestSecurityConfig.class, GlobalExceptionHandler.class })
public class ContaControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ContaService contaService;
    @Autowired
    private ObjectMapper objectMapper;
    private Conta conta;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");
        conta = new Conta();
        conta.setId(1L);
        conta.setNomeConta("Conta Teste");
        conta.setUsuario(usuario);
    }

    @Test
    @DisplayName("GET /contas - deve retornar todas as contas")
    void testFindAll() throws Exception {
        List<Conta> contas = Arrays.asList(conta);
        Mockito.when(contaService.findAll()).thenReturn(contas);
        mockMvc.perform(get("/contas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("GET /contas/{id} - deve retornar conta por id")
    void testFindById() throws Exception {
        Mockito.when(contaService.findById(1L)).thenReturn(conta);
        mockMvc.perform(get("/contas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /contas/{id} - not found")
    void testFindByIdNotFound() throws Exception {
        Mockito.when(contaService.findById(999L)).thenThrow(new ContaNotFoundException(999L));
        mockMvc.perform(get("/contas/999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("GET /contas/usuario/{usuarioId} - deve retornar contas do usuário")
    void testFindByUsuarioId() throws Exception {
        List<Conta> contas = Arrays.asList(conta);
        Mockito.when(contaService.findByUsuarioId(1L)).thenReturn(contas);
        mockMvc.perform(get("/contas/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("POST /contas - deve criar conta")
    void testSave() throws Exception {
        Mockito.when(contaService.save(any(Conta.class))).thenReturn(conta);
        mockMvc.perform(post("/contas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /contas/{id} - deve atualizar conta")
    void testUpdate() throws Exception {
        Mockito.when(contaService.findById(1L)).thenReturn(conta);
        Mockito.when(contaService.save(any(Conta.class))).thenReturn(conta);
        mockMvc.perform(put("/contas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("DELETE /contas/{id} - deve excluir conta")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/contas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /contas/{id} - not found")
    void testDeleteNotFound() throws Exception {
        Mockito.doThrow(new ContaNotFoundException(999L)).when(contaService).deleteById(999L);
        mockMvc.perform(delete("/contas/999"))
                .andExpect(status().is4xxClientError());
    }
}