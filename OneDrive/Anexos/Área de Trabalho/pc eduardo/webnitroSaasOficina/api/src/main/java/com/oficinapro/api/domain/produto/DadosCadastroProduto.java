package com.oficinapro.api.domain.produto;

import java.math.BigDecimal;

public record DadosCadastroProduto(
        String nome,
        BigDecimal preco,
        Integer quantidadeEstoque // 👈 Novo!
) {}