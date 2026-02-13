package com.oficinapro.api.domain.empresa;

import jakarta.persistence.*;

@Table(name = "tb_empresas")
@Entity(name = "Empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 👇 ADICIONEI ESSES 3 CAMPOS QUE FALTAVAM
    private String nomeFantasia;
    private String cnpj;
    private String telefone;

    // --- CONSTRUTORES ---
    public Empresa() {}

    public Empresa(Long id, String nomeFantasia, String cnpj, String telefone) {
        this.id = id;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
        this.telefone = telefone;
    }

    // --- GETTERS E SETTERS (OBRIGATÓRIOS PRO JAVA NÃO RECLAMAR) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}