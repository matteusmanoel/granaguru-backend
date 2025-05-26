package app.controllers;

import app.entities.Usuario;
import app.enums.StatusUsuario;
import app.enums.Role;
import app.exceptions.UsuarioNotFoundException;
import app.services.UsuarioService;
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

@WebMvcTest(UsuarioController.class)
@Import({ TestSecurityConfig.class, GlobalExceptionHandler.class })
public class UsuarioControllerTest {
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
        usuario.setSenha("123456");
        usuario.setStatus(StatusUsuario.ATIVO);
        usuario.setRole(Role.USER);
    }

    @Test
    @DisplayName("GET /usuarios - deve retornar todos os usuários")
    void testListAll() throws Exception {
        Mockito.when(usuarioService.listAll()).thenReturn(List.of(usuario));
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Vik"));
    }

    @Test
    @DisplayName("GET /usuarios/{id} - deve retornar usuário por id")
    void testFindById() throws Exception {
        Mockito.when(usuarioService.findById(1L)).thenReturn(usuario);
        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Vik"));
    }

    @Test
    @DisplayName("GET /usuarios/{id} - not found")
    void testFindByIdNotFound() throws Exception {
        Mockito.when(usuarioService.findById(999L)).thenThrow(new UsuarioNotFoundException("Usuário não encontrado"));
        mockMvc.perform(get("/usuarios/999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("POST /usuarios - deve criar usuário")
    void testSave() throws Exception {
        Mockito.when(usuarioService.save(any(Usuario.class))).thenReturn(usuario);
        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Vik"));
    }

    @Test
    @DisplayName("PUT /usuarios/{id} - deve atualizar usuário")
    void testUpdate() throws Exception {
        Mockito.when(usuarioService.update(Mockito.eq(1L), any(Usuario.class))).thenReturn(usuario);
        mockMvc.perform(put("/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Vik"));
    }

    @Test
    @DisplayName("DELETE /usuarios/{id} - deve excluir usuário")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /usuarios/{id} - not found")
    void testDeleteNotFound() throws Exception {
        Mockito.doThrow(new UsuarioNotFoundException("Usuário não encontrado")).when(usuarioService).deleteById(999L);
        mockMvc.perform(delete("/usuarios/999"))
                .andExpect(status().is4xxClientError());
    }
}