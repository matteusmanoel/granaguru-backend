package app.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.entities.Conta;
import app.services.ContaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService contaService;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<Conta>> buscarContas(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(contaService.listarContasDoUsuario(usuarioId));
    }

    @PostMapping("/save")
    public ResponseEntity<Conta> criarConta(@RequestBody Conta conta) {
        return ResponseEntity.ok(contaService.criarConta(conta));
    }
}
