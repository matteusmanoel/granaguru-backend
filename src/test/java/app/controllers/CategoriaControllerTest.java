package app.controllers;

import app.entities.Categoria;
import app.entities.Usuario;
import app.enums.TipoCategoria;
import app.exceptions.CategoriaNotFoundException;
import app.services.CategoriaService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import app.config.TestSecurityConfig;
import app.exceptions.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;

@WebMvcTest(CategoriaController.class)
@Import({ TestSecurityConfig.class, GlobalExceptionHandler.class })
public class CategoriaControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoriaService categoriaService;
    @Autowired
    private ObjectMapper objectMapper;
    private Categoria categoria;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNomeCategoria("Categoria Teste");
        categoria.setUsuario(usuario);
        categoria.setTipo(TipoCategoria.DESPESA);
    }

    @Test
    @DisplayName("GET /categorias - deve retornar todas as categorias")
    void testFindAll() throws Exception {
        List<Categoria> categorias = Arrays.asList(categoria);
        Mockito.when(categoriaService.findAll()).thenReturn(categorias);
        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("GET /categorias/{id} - deve retornar categoria por id")
    void testFindById() throws Exception {
        Mockito.when(categoriaService.findById(1L)).thenReturn(categoria);
        mockMvc.perform(get("/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /categorias/{id} - not found")
    void testFindByIdNotFound() throws Exception {
        Mockito.when(categoriaService.findById(999L)).thenThrow(new CategoriaNotFoundException(999L));
        mockMvc.perform(get("/categorias/999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("GET /categorias/usuario/{usuarioId} - deve retornar categorias do usuário")
    void testFindByUsuarioId() throws Exception {
        List<Categoria> categorias = Arrays.asList(categoria);
        Mockito.when(categoriaService.findByUsuarioId(1L)).thenReturn(categorias);
        mockMvc.perform(get("/categorias/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("POST /categorias - deve criar categoria")
    void testSave() throws Exception {
        Mockito.when(categoriaService.save(any(Categoria.class))).thenReturn(categoria);
        mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /categorias/{id} - deve atualizar categoria")
    void testUpdate() throws Exception {
        Mockito.when(categoriaService.findById(1L)).thenReturn(categoria);
        Mockito.when(categoriaService.save(any(Categoria.class))).thenReturn(categoria);
        mockMvc.perform(put("/categorias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("DELETE /categorias/{id} - deve excluir categoria")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/categorias/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /categorias/{id} - not found")
    void testDeleteNotFound() throws Exception {
        Mockito.doThrow(new CategoriaNotFoundException(999L)).when(categoriaService).deleteById(999L);
        mockMvc.perform(delete("/categorias/999"))
                .andExpect(status().is4xxClientError());
    }
}