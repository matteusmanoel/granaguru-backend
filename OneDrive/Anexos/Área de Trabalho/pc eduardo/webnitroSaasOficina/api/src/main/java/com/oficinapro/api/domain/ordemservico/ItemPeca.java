package com.oficinapro.api.domain.ordemservico;

import com.oficinapro.api.domain.produto.Produto;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Table(name = "tb_itens_pecas")
@Entity(name = "ItemPeca")
public class ItemPeca {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "os_id")
    private OrdemServico ordemServico;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private Integer quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal subtotal;

    // --- CONSTRUTORES ---
    public ItemPeca() {}

    public ItemPeca(OrdemServico os, Produto produto, Integer quantidade) {
        this.ordemServico = os;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitario = produto.getValorVenda(); // Certifique-se que Produto tem esse método
        this.subtotal = this.valorUnitario.multiply(new BigDecimal(quantidade));
    }

    // --- GETTERS E SETTERS MANUAIS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public OrdemServico getOrdemServico() { return ordemServico; }
    public void setOrdemServico(OrdemServico ordemServico) { this.ordemServico = ordemServico; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public BigDecimal getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}