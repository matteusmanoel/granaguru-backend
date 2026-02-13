package com.oficinapro.api.domain.ordemservico;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DadosAdicionarItem(
        @NotNull
        Long produtoId,

        @NotNull @Positive
        Integer quantidade
) {}