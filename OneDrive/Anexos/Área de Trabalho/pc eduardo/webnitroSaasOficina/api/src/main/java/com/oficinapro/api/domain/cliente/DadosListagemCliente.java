package com.oficinapro.api.domain.cliente;

public record DadosListagemCliente(
        Long id,
        String nome,
        String email,
        String telefone,
        String documento
) {

    // Construtor
    public DadosListagemCliente(Cliente cliente) {
        this(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getDocumento()
        );
    }
}