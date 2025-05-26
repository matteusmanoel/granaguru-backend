package app.controllers;

import app.entities.Tag;
import app.exceptions.TagNotFoundException;
import app.services.TagService;
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

@WebMvcTest(TagController.class)
@Import({ TestSecurityConfig.class, GlobalExceptionHandler.class })
public class TagControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TagService tagService;
    @Autowired
    private ObjectMapper objectMapper;
    private Tag tag;

    @BeforeEach
    void setUp() {
        tag = new Tag();
        tag.setId(1L);
        tag.setNome("Tag Teste");
    }

    @Test
    @DisplayName("GET /tags - deve retornar todas as tags")
    void testFindAll() throws Exception {
        List<Tag> tags = Arrays.asList(tag);
        Mockito.when(tagService.findAll()).thenReturn(tags);
        mockMvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("GET /tags/{id} - deve retornar tag por id")
    void testFindById() throws Exception {
        Mockito.when(tagService.findById(1L)).thenReturn(tag);
        mockMvc.perform(get("/tags/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /tags/{id} - not found")
    void testFindByIdNotFound() throws Exception {
        Mockito.when(tagService.findById(999L)).thenThrow(new TagNotFoundException("Tag não encontrada"));
        mockMvc.perform(get("/tags/999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("POST /tags - deve criar tag")
    void testCreate() throws Exception {
        Mockito.when(tagService.save(any(Tag.class))).thenReturn(tag);
        mockMvc.perform(post("/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /tags/{id} - deve atualizar tag")
    void testUpdate() throws Exception {
        Mockito.when(tagService.save(any(Tag.class))).thenReturn(tag);
        mockMvc.perform(put("/tags/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("DELETE /tags/{id} - deve excluir tag")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/tags/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /tags/{id} - not found")
    void testDeleteNotFound() throws Exception {
        Mockito.doThrow(new TagNotFoundException("Tag não encontrada")).when(tagService).deleteById(999L);
        mockMvc.perform(delete("/tags/999"))
                .andExpect(status().is4xxClientError());
    }
}