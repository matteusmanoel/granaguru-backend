package com.oficinapro.api.domain.veiculo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroVeiculo(

        @NotBlank
        String marca,

        @NotBlank
        String modelo,

        @NotBlank
        String placa,

        Integer ano,

        String cor,

        long id,
        @NotNull
        Long clienteId // <--- Importante: Precisamos saber quem é o dono!
) {
}