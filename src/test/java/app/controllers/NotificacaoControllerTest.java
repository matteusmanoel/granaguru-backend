package app.controllers;

import app.entities.Notificacao;
import app.entities.Usuario;
import app.exceptions.NotificacaoNotFoundException;
import app.services.NotificacaoService;
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

@WebMvcTest(NotificacaoController.class)
@Import({ TestSecurityConfig.class, GlobalExceptionHandler.class })
public class NotificacaoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NotificacaoService notificacaoService;
    @Autowired
    private ObjectMapper objectMapper;
    private Notificacao notificacao;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");
        notificacao = new Notificacao();
        notificacao.setId(1L);
        notificacao.setMensagem("Teste");
        notificacao.setUsuario(usuario);
    }

    @Test
    @DisplayName("GET /notificacoes - deve retornar todas as notificações")
    void testFindAll() throws Exception {
        List<Notificacao> notificacoes = Arrays.asList(notificacao);
        Mockito.when(notificacaoService.findAll()).thenReturn(notificacoes);
        mockMvc.perform(get("/notificacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("GET /notificacoes/{id} - deve retornar notificação por id")
    void testFindById() throws Exception {
        Mockito.when(notificacaoService.findById(1L)).thenReturn(notificacao);
        mockMvc.perform(get("/notificacoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /notificacoes/{id} - not found")
    void testFindByIdNotFound() throws Exception {
        Mockito.when(notificacaoService.findById(999L)).thenThrow(new NotificacaoNotFoundException(999L));
        mockMvc.perform(get("/notificacoes/999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("GET /notificacoes/usuario/{usuarioId} - deve retornar notificações do usuário")
    void testFindByUsuarioId() throws Exception {
        List<Notificacao> notificacoes = Arrays.asList(notificacao);
        Mockito.when(notificacaoService.findByUsuarioId(1L)).thenReturn(notificacoes);
        mockMvc.perform(get("/notificacoes/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("POST /notificacoes - deve criar notificação")
    void testSave() throws Exception {
        Mockito.when(notificacaoService.save(any(Notificacao.class))).thenReturn(notificacao);
        mockMvc.perform(post("/notificacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /notificacoes/{id} - deve atualizar notificação")
    void testUpdate() throws Exception {
        Mockito.when(notificacaoService.findById(1L)).thenReturn(notificacao);
        Mockito.when(notificacaoService.save(any(Notificacao.class))).thenReturn(notificacao);
        mockMvc.perform(put("/notificacoes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("DELETE /notificacoes/{id} - deve excluir notificação")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/notificacoes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /notificacoes/{id} - not found")
    void testDeleteNotFound() throws Exception {
        Mockito.doThrow(new NotificacaoNotFoundException(999L)).when(notificacaoService).deleteById(999L);
        mockMvc.perform(delete("/notificacoes/999"))
                .andExpect(status().is4xxClientError());
    }
}