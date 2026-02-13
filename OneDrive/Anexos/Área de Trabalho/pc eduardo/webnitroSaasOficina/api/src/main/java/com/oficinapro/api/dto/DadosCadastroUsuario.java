package com.oficinapro.api.dto;

public record DadosCadastroUsuario(
        String login,
        String senha,
        Long empresaId // Precisamos saber de qual oficina ele é!
) {
}