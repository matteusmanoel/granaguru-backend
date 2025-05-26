package app.controllers;

import app.entities.Meta;
import app.entities.Usuario;
import app.enums.StatusMeta;
import app.exceptions.MetaNotFoundException;
import app.services.MetaService;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import app.config.TestSecurityConfig;
import app.exceptions.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;

@WebMvcTest(MetaController.class)
@Import({ TestSecurityConfig.class, GlobalExceptionHandler.class })
public class MetaControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MetaService metaService;
    @Autowired
    private ObjectMapper objectMapper;
    private Meta meta;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");
        meta = new Meta();
        meta.setId(1L);
        meta.setDescricao("Meta Teste");
        meta.setValorObjetivo(1000.0);
        meta.setValorAtual(100.0);
        meta.setDataInicio(LocalDate.now());
        meta.setDataTermino(LocalDate.now().plusMonths(1));
        meta.setStatus(StatusMeta.EM_ANDAMENTO);
        meta.setUsuario(usuario);
    }

    @Test
    @DisplayName("GET /metas - deve retornar todas as metas")
    void testListAll() throws Exception {
        List<Meta> metas = Arrays.asList(meta);
        Mockito.when(metaService.listAll()).thenReturn(metas);
        mockMvc.perform(get("/metas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("GET /metas/usuario/{usuarioId}/em-andamento - deve retornar metas em andamento")
    void testBuscarMetasEmAndamento() throws Exception {
        List<Meta> metas = Arrays.asList(meta);
        Mockito.when(metaService.buscarMetasEmAndamento(1L)).thenReturn(metas);
        mockMvc.perform(get("/metas/usuario/1/em-andamento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("GET /metas/{id} - deve retornar meta por id")
    void testFindById() throws Exception {
        Mockito.when(metaService.findById(1L)).thenReturn(meta);
        mockMvc.perform(get("/metas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /metas/{id} - not found")
    void testFindByIdNotFound() throws Exception {
        Mockito.when(metaService.findById(999L)).thenThrow(new MetaNotFoundException(999L));
        mockMvc.perform(get("/metas/999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("POST /metas - deve criar meta")
    void testSave() throws Exception {
        Mockito.when(metaService.save(any(Meta.class))).thenReturn(meta);
        mockMvc.perform(post("/metas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /metas/{id} - deve atualizar meta")
    void testUpdate() throws Exception {
        Mockito.when(metaService.update(Mockito.eq(1L), any(Meta.class))).thenReturn(meta);
        mockMvc.perform(put("/metas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("DELETE /metas/{id} - deve excluir meta")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/metas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /metas/{id} - not found")
    void testDeleteNotFound() throws Exception {
        Mockito.doThrow(new MetaNotFoundException(999L)).when(metaService).deleteById(999L);
        mockMvc.perform(delete("/metas/999"))
                .andExpect(status().is4xxClientError());
    }
}