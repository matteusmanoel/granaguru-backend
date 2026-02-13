package com.oficinapro.api.domain.cliente;

import com.oficinapro.api.domain.empresa.Empresa;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Table(name = "tb_clientes")
@Entity(name = "Cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String telefone;
    private String documento;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    // Construtor para criar a partir do DTO
    public Cliente(DadosCadastroCliente dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.documento = dados.documento();
    }

    // --- MÉTODOS MANUAIS (Para garantir que o Controller não reclame) ---

    // 1. Getter e Setter da EMPRESA
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    // 2. Getter e Setter do ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // 3. Getter do Documento (usado na validação)
    public String getDocumento() {
        return documento;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }
}
