package com.oficinapro.api.domain.ordemservico;

import jakarta.validation.constraints.NotNull;

public record DadosAberturaOS(
        @NotNull
        Long veiculoId, // O ID do carro que vai receber a OS

        String defeitoRelatado // "Barulho na roda", "Troca de óleo", etc.
) {
}