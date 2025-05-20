package app.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import app.entities.Usuario;
import app.enums.StatusUsuario;
import app.exceptions.UsuarioNotFoundException;
import app.services.UsuarioService;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Vik");
        usuario.setEmail("vik@email.com");
        usuario.setSenha("123");
        usuario.setStatus(StatusUsuario.ATIVO);
    }

    @Test
    @DisplayName("GET /usuarios – Listar todos")
    void testListAll() throws Exception {
        when(usuarioService.listAll()).thenReturn(List.of(usuario));
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Vik"));
    }

    @Test
    @DisplayName("GET /usuarios/{id} – Buscar por ID")
    void testFindById() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(usuario);
        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Vik"));
    }

    @Test
    @DisplayName("GET /usuarios/{id} – Buscar por ID não encontrado")
    void testFindByIdNotFound() throws Exception {
        when(usuarioService.findById(2L)).thenThrow(new UsuarioNotFoundException("Usuário não encontrado"));
        mockMvc.perform(get("/usuarios/2"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("GET /usuarios/buscar-por-nome?nome=X – Buscar por nome")
    void testFindByNome() throws Exception {
        when(usuarioService.findByNome("Vik")).thenReturn(List.of(usuario));
        mockMvc.perform(get("/usuarios/buscar-por-nome").param("nome", "Vik"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Vik"));
    }

    @Test
    @DisplayName("GET /usuarios/buscar-por-nome?nome=X – Buscar por nome não encontrado")
    void testFindByNomeNotFound() throws Exception {
        when(usuarioService.findByNome("Xuxu")).thenThrow(new UsuarioNotFoundException("Nenhum usuário encontrado"));
        mockMvc.perform(get("/usuarios/buscar-por-nome").param("nome", "Xuxu"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("GET /usuarios/buscar-por-email?email=X – Buscar por email")
    void testFindByEmail() throws Exception {
        when(usuarioService.findByEmail("vik@email.com")).thenReturn(usuario);
        mockMvc.perform(get("/usuarios/buscar-por-email").param("email", "vik@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Vik"));
    }

    @Test
    @DisplayName("GET /usuarios/buscar-por-email?email=X – Buscar por email não encontrado")
    void testFindByEmailNotFound() throws Exception {
        when(usuarioService.findByEmail("x@email.com"))
                .thenThrow(new UsuarioNotFoundException("Usuário não encontrado"));
        mockMvc.perform(get("/usuarios/buscar-por-email").param("email", "x@email.com"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("GET /usuarios/buscar-por-status?status=ATIVO – Buscar por status")
    void testFindByStatus() throws Exception {
        when(usuarioService.findByStatus(StatusUsuario.ATIVO)).thenReturn(List.of(usuario));
        mockMvc.perform(get("/usuarios/buscar-por-status").param("status", "ATIVO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ATIVO"));
    }

    @Test
    @DisplayName("GET /usuarios/buscar-por-status?status=INATIVO – Buscar por status não encontrado")
    void testFindByStatusNotFound() throws Exception {
        when(usuarioService.findByStatus(StatusUsuario.INATIVO))
                .thenThrow(new UsuarioNotFoundException("Nenhum usuário encontrado"));
        mockMvc.perform(get("/usuarios/buscar-por-status").param("status", "INATIVO"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("PUT /usuarios/{id} – Atualizar usuário")
    void testUpdate() throws Exception {
        Usuario atualizado = new Usuario();
        atualizado.setNome("Atualizado");
        atualizado.setEmail("novo@email.com");
        atualizado.setSenha("123");
        atualizado.setStatus(StatusUsuario.ATIVO);
        when(usuarioService.update(eq(1L), any(Usuario.class))).thenReturn(atualizado);
        mockMvc.perform(put("/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Atualizado"));
    }

    @Test
    @DisplayName("DELETE /usuarios/{id} – Remover usuário")
    void testDelete() throws Exception {
        doNothing().when(usuarioService).deleteById(1L);
        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());
    }
}