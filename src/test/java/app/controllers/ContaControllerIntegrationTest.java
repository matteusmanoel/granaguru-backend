package app.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import app.entities.Conta;
import app.entities.Usuario;
import app.exceptions.ContaNotFoundException;
import app.services.ContaService;

@WebMvcTest(ContaController.class)
public class ContaControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContaService service;

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
    @DisplayName("Deve retornar todas as contas")
    void testFindAll() throws Exception {
        List<Conta> contas = Arrays.asList(conta);
        when(service.findAll()).thenReturn(contas);

        mockMvc.perform(get("/contas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nomeConta").value("Conta Teste"));
    }

    @Test
    @DisplayName("Deve encontrar conta por ID")
    void testFindById() throws Exception {
        when(service.findById(1L)).thenReturn(conta);

        mockMvc.perform(get("/contas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeConta").value("Conta Teste"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando conta não encontrada")
    void testFindByIdNotFound() throws Exception {
        when(service.findById(999L)).thenThrow(new ContaNotFoundException(999L));

        mockMvc.perform(get("/contas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve encontrar contas por ID do usuário")
    void testFindByUsuarioId() throws Exception {
        List<Conta> contas = Arrays.asList(conta);
        when(service.findByUsuarioId(1L)).thenReturn(contas);

        mockMvc.perform(get("/contas/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nomeConta").value("Conta Teste"));
    }

    @Test
    @DisplayName("Deve criar nova conta")
    void testSave() throws Exception {
        when(service.save(any(Conta.class))).thenReturn(conta);

        mockMvc.perform(post("/contas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeConta").value("Conta Teste"));
    }

    @Test
    @DisplayName("Deve atualizar conta existente")
    void testUpdate() throws Exception {
        when(service.findById(1L)).thenReturn(conta);
        when(service.save(any(Conta.class))).thenReturn(conta);

        mockMvc.perform(put("/contas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeConta").value("Conta Teste"));
    }

    @Test
    @DisplayName("Deve excluir conta")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/contas/1"))
                .andExpect(status().isNoContent());
    }
}
