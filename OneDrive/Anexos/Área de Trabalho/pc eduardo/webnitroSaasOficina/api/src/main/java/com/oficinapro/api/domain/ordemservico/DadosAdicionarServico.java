package com.oficinapro.api.domain.ordemservico;
import java.math.BigDecimal;

public record DadosAdicionarServico(
        String descricao,
        BigDecimal valor,
        Long mecanicoId // Quem fez o serviço?
) {}