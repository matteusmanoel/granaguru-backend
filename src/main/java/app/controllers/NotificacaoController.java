package app.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.entities.Notificacao;
import app.services.NotificacaoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<Notificacao>> buscarNotificacoes(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacaoService.listarNotificacoesDoUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<Notificacao> criarNotificacao(@RequestBody Notificacao notificacao) {
        return ResponseEntity.ok(notificacaoService.criarNotificacao(notificacao));
    }
}
