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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.entities.Categoria;
import app.entities.Usuario;
import app.enums.TipoCategoria;
import app.exceptions.CategoriaNotFoundException;
import app.services.CategoriaService;

@WebMvcTest(CategoriaController.class)
public class CategoriaControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoriaService service;

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
    @DisplayName("Deve retornar todas as categorias")
    void testFindAll() throws Exception {
        List<Categoria> categorias = Arrays.asList(categoria);
        when(service.findAll()).thenReturn(categorias);

        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nomeCategoria").value("Categoria Teste"));
    }

    @Test
    @DisplayName("Deve encontrar categoria por ID")
    void testFindById() throws Exception {
        when(service.findById(1L)).thenReturn(categoria);

        mockMvc.perform(get("/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeCategoria").value("Categoria Teste"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando categoria não encontrada")
    void testFindByIdNotFound() throws Exception {
        when(service.findById(999L)).thenThrow(new CategoriaNotFoundException(999L));

        mockMvc.perform(get("/categorias/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve encontrar categorias por ID do usuário")
    void testFindByUsuarioId() throws Exception {
        List<Categoria> categorias = Arrays.asList(categoria);
        when(service.findByUsuarioId(1L)).thenReturn(categorias);

        mockMvc.perform(get("/categorias/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nomeCategoria").value("Categoria Teste"));
    }

    @Test
    @DisplayName("Deve criar nova categoria")
    void testSave() throws Exception {
        when(service.save(any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeCategoria").value("Categoria Teste"));
    }

    @Test
    @DisplayName("Deve atualizar categoria existente")
    void testUpdate() throws Exception {
        when(service.findById(1L)).thenReturn(categoria);
        when(service.save(any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(put("/categorias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeCategoria").value("Categoria Teste"));
    }

    @Test
    @DisplayName("Deve excluir categoria")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/categorias/1"))
                .andExpect(status().isNoContent());
    }
}
