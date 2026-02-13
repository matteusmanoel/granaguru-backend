package com.oficinapro.api.domain.mecanico;

import jakarta.persistence.*;
import java.util.Objects;

@Table(name = "tb_mecanicos")
@Entity(name = "Mecanico")
public class Mecanico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 👇 A SEGURANÇA: O Mecânico pertence a UMA oficina específica
    @Column(nullable = false)
    private Long oficinaId;

    private String nome;

    // Quanto ele ganha de comissão padrão? (Ex: 30.0 para 30%)
    private Double comissaoPadrao;

    private Boolean ativo = true; // Já nasce ativo

    // --- CONSTRUTORES ---
    public Mecanico() {}

    public Mecanico(String nome, Double comissaoPadrao) {
        this.nome = nome;
        this.comissaoPadrao = comissaoPadrao;
        this.ativo = true;
    }

    // --- GETTERS E SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // 👇 Getter e Setter da Segurança
    public Long getOficinaId() {
        return oficinaId;
    }

    public void setOficinaId(Long oficinaId) {
        this.oficinaId = oficinaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getComissaoPadrao() {
        return comissaoPadrao;
    }

    public void setComissaoPadrao(Double comissaoPadrao) {
        this.comissaoPadrao = comissaoPadrao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    // --- EQUALS E HASHCODE ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mecanico mecanico = (Mecanico) o;
        return Objects.equals(id, mecanico.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}