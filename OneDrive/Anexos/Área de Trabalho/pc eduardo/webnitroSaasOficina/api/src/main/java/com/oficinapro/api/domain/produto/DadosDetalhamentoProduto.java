package com.oficinapro.api.domain.produto;

import java.math.BigDecimal;

public record DadosDetalhamentoProduto(Long id, String nome, BigDecimal preco, Integer quantidadeEstoque) {
    public DadosDetalhamentoProduto(Produto produto) {
        // 👇 Adicionei o getQuantidadeEstoque() no final
        this(produto.getId(), produto.getNome(), produto.getPreco(), produto.getQuantidadeEstoque());
    }
}