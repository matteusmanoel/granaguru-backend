package com.oficinapro.api.controller;

import com.oficinapro.api.domain.cliente.*;
import com.oficinapro.api.domain.usuario.Usuario; // 👈 Importante: Importar sua classe Usuario
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // 👈 Importante: Importar o Spring Security
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository repository;

    @PostMapping
    @Transactional
    // 👇 A MÁGICA 1: Recebe o usuário logado (@AuthenticationPrincipal)
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroCliente dados, @AuthenticationPrincipal Usuario usuarioLogado) {
        var cliente = new Cliente(dados);

        // 👇 Vincula o cliente à empresa do usuário que está logado (SaaS)
        cliente.setEmpresa(usuarioLogado.getEmpresa());

        repository.save(cliente);
        return ResponseEntity.ok(new DadosDetalhamentoCliente(cliente));
    }

    @GetMapping
    // 👇 A MÁGICA 2: Também recebe o usuário no listar
    public ResponseEntity<List<DadosListagemCliente>> listar(@AuthenticationPrincipal Usuario usuarioLogado) {
        // 👇 Filtra a lista: Só retorna clientes que têm o MESMO ID de empresa do usuário
        var lista = repository.findAll().stream()
                .filter(c -> c.getEmpresa() != null && c.getEmpresa().getId().equals(usuarioLogado.getEmpresa().getId()))
                .map(DadosListagemCliente::new)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosCadastroCliente dados) {
        var cliente = repository.getReferenceById(dados.id());
        cliente.setNome(dados.nome());
        cliente.setTelefone(dados.telefone());
        cliente.setEmail(dados.email());
        cliente.setDocumento(dados.documento());
        return ResponseEntity.ok(dados);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}