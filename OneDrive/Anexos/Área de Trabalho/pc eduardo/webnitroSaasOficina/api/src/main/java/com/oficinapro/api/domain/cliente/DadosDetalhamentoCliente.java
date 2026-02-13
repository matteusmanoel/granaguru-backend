package com.oficinapro.api.domain.cliente;

public record DadosDetalhamentoCliente(Long id, String nome, String email, String telefone, String documento) {

    public DadosDetalhamentoCliente(Cliente cliente) {
        this(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getTelefone(), cliente.getDocumento());
    }
}