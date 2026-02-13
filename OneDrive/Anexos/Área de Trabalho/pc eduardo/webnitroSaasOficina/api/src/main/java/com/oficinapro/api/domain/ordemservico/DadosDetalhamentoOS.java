package com.oficinapro.api.domain.ordemservico;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record DadosDetalhamentoOS(
        Long id,
        Long clienteId,
        String nomeCliente,
        String modeloVeiculo,
        String placaVeiculo,
        String defeitoRelatado,
        StatusOS status,
        BigDecimal totalPecas,
        BigDecimal totalServicos,
        BigDecimal totalGeral,
        String dataAbertura,
        List<DadosItemOS> pecas,
        List<DadosServicoOS> servicos
) {

    public DadosDetalhamentoOS(OrdemServico os) {
        this(
                os.getId(),

                // 🛡️ SEGURANÇA 1: Se não tiver cliente, não quebra
                os.getCliente() != null ? os.getCliente().getId() : null,
                os.getCliente() != null ? os.getCliente().getNome() : "Cliente não identificado",

                // 🛡️ SEGURANÇA 2: Se não tiver veículo, não quebra
                os.getVeiculo() != null ? os.getVeiculo().getModelo() : "Veículo desconhecido",
                os.getVeiculo() != null ? os.getVeiculo().getPlaca() : "S/ PLACA",

                os.getDefeitoRelatado(),
                os.getStatus(),

                // 🛡️ SEGURANÇA 3: Se os valores forem nulos, usa ZERO
                os.getTotalPecas() != null ? os.getTotalPecas() : BigDecimal.ZERO,
                os.getTotalServicos() != null ? os.getTotalServicos() : BigDecimal.ZERO,
                os.getTotalGeral() != null ? os.getTotalGeral() : BigDecimal.ZERO,

                os.getDataAbertura() != null ? os.getDataAbertura().toString() : null,

                // 🛡️ SEGURANÇA 4: Se as listas forem nulas, cria listas vazias
                os.getPecas() != null
                        ? os.getPecas().stream().map(DadosItemOS::new).collect(Collectors.toList())
                        : new ArrayList<>(),

                os.getServicos() != null
                        ? os.getServicos().stream().map(DadosServicoOS::new).collect(Collectors.toList())
                        : new ArrayList<>()
        );
    }
}