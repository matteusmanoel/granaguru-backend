package com.oficinapro.api.domain.ordemservico;

import java.math.BigDecimal;

public record DadosServicoOS(
        Long id,
        String descricao,
        BigDecimal valor,
        String nomeMecanico
) {

    // Construtor "Tradutor" de Entidade para DTO
    public DadosServicoOS(ItemServico item) {
        this(
                item.getId(),
                item.getDescricao(),
                item.getValor(),

                // 🛡️ Proteção: Se não tiver mecânico, escreve "Sem Mecânico"
                (item.getMecanicoResponsavel() != null)
                        ? item.getMecanicoResponsavel().getNome()
                        : "Sem Mecânico"
        );
    }
}