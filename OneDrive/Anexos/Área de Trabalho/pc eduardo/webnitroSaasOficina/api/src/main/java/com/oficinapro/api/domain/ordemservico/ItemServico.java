package com.oficinapro.api.domain.ordemservico;

import com.oficinapro.api.domain.mecanico.Mecanico;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Table(name = "tb_itens_servicos")
@Entity(name = "ItemServico")
public class ItemServico {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "os_id")
    private OrdemServico ordemServico;

    private String descricao;
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "mecanico_id")
    private Mecanico mecanicoResponsavel;

    // --- CONSTRUTORES ---
    public ItemServico() {}

    // --- GETTERS E SETTERS MANUAIS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public OrdemServico getOrdemServico() { return ordemServico; }
    public void setOrdemServico(OrdemServico ordemServico) { this.ordemServico = ordemServico; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public Mecanico getMecanicoResponsavel() { return mecanicoResponsavel; }
    public void setMecanicoResponsavel(Mecanico mecanicoResponsavel) { this.mecanicoResponsavel = mecanicoResponsavel; }
}