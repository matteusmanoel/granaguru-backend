package com.oficinapro.api.domain.ordemservico;

import java.math.BigDecimal;

public record DadosItemOS(
        Long id,
        String nomePeca,
        Integer qtd,
        BigDecimal valorUn,
        BigDecimal subtotal
) {
    // 👇 CONSTRUTOR PARA AS PEÇAS
    public DadosItemOS(ItemPeca item) {
        this(
                item.getId(),
                // Proteção se o produto for null
                (item.getProduto() != null) ? item.getProduto().getNome() : "Produto Excluído",
                item.getQuantidade(),
                item.getValorUnitario(),
                item.getSubtotal()
        );
    }
}