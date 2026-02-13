package com.oficinapro.api.controller;

import com.oficinapro.api.domain.produto.*;
import com.oficinapro.api.infra.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private SecurityUtils securityUtils; // ✅ Segurança ativada

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoProduto>> listar() {
        // 1. O sistema descobre sozinho quem é a empresa do usuário logado
        Long idMinhaEmpresa = securityUtils.getEmpresaLogada().getId();

        var lista = repository.findByEmpresaId(idMinhaEmpresa)
                .stream()
                .map(DadosDetalhamentoProduto::new)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody DadosCadastroProduto dados) {
        // 2. Vincula o produto à empresa do usuário logado automaticamente
        var empresaLogada = securityUtils.getEmpresaLogada();

        var produto = new Produto(dados.nome(), dados.preco(), dados.quantidadeEstoque());
        produto.setEmpresa(empresaLogada);

        repository.save(produto);

        return ResponseEntity.ok(new DadosDetalhamentoProduto(produto));
    }
}