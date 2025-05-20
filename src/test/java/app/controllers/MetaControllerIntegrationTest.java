package app.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
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

import app.entities.Meta;
import app.entities.Usuario;
import app.enums.StatusMeta;
import app.exceptions.MetaNotFoundException;
import app.services.MetaService;

@WebMvcTest(MetaController.class)
public class MetaControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MetaService service;

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
    @DisplayName("Deve retornar todas as metas")
    void testListAll() throws Exception {
        List<Meta> metas = Arrays.asList(meta);
        when(service.listAll()).thenReturn(metas);

        mockMvc.perform(get("/metas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descricao").value("Meta Teste"));
    }

    @Test
    @DisplayName("Deve buscar metas em andamento por usuário")
    void testBuscarMetasEmAndamento() throws Exception {
        List<Meta> metas = Arrays.asList(meta);
        when(service.buscarMetasEmAndamento(1L)).thenReturn(metas);

        mockMvc.perform(get("/metas/usuario/1/em-andamento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descricao").value("Meta Teste"));
    }

    @Test
    @DisplayName("Deve encontrar meta por ID")
    void testFindById() throws Exception {
        when(service.findById(1L)).thenReturn(meta);

        mockMvc.perform(get("/metas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Meta Teste"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando meta não encontrada")
    void testFindByIdNotFound() throws Exception {
        when(service.findById(999L)).thenThrow(new MetaNotFoundException(999L));

        mockMvc.perform(get("/metas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve criar nova meta")
    void testSave() throws Exception {
        when(service.save(any(Meta.class))).thenReturn(meta);

        mockMvc.perform(post("/metas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Meta Teste"));
    }

    @Test
    @DisplayName("Deve atualizar meta existente")
    void testUpdate() throws Exception {
        Meta metaAtualizada = new Meta();
        metaAtualizada.setId(1L);
        metaAtualizada.setDescricao("Nova Descrição");
        metaAtualizada.setValorObjetivo(2000.0);
        metaAtualizada.setValorAtual(500.0);
        metaAtualizada.setDataInicio(LocalDate.now());
        metaAtualizada.setDataTermino(LocalDate.now().plusMonths(2));
        metaAtualizada.setStatus(StatusMeta.CONCLUIDA);
        metaAtualizada.setUsuario(usuario);

        when(service.update(eq(1L), any(Meta.class))).thenReturn(metaAtualizada);

        mockMvc.perform(put("/metas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(metaAtualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Nova Descrição"))
                .andExpect(jsonPath("$.valorObjetivo").value(2000.0))
                .andExpect(jsonPath("$.valorAtual").value(500.0))
                .andExpect(jsonPath("$.status").value("CONCLUIDA"));
    }

    @Test
    @DisplayName("Deve excluir meta")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/metas/1"))
                .andExpect(status().isNoContent());
    }
}
