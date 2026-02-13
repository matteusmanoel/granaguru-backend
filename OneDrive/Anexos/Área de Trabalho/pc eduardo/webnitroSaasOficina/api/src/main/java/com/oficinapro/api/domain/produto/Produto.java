package com.oficinapro.api.domain.produto;

import com.oficinapro.api.domain.empresa.Empresa;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Table(name = "tb_produtos")
@Entity(name = "Produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private BigDecimal preco;

    // 👇 1. CAMPO NOVO: QUANTIDADE EM ESTOQUE
    private Integer quantidadeEstoque;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    public Produto() {}

    public Produto(String nome, BigDecimal preco, Integer quantidadeEstoque) {
        this.nome = nome;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    // --- GETTERS E SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    // 👇 2. MÉTODOS NOVOS PARA O ESTOQUE FUNCIONAR
    public Integer getQuantidadeEstoque() {
        // Se estiver nulo no banco, retorna 0 pra não dar erro
        return quantidadeEstoque == null ? 0 : quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }
    // ----------------------------------------------------

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public BigDecimal getValorVenda() {
        return this.preco;
    }
}