package com.oficinapro.api.domain.veiculo;

import com.oficinapro.api.domain.cliente.Cliente;
import com.oficinapro.api.domain.empresa.Empresa; // 👈 1. Importe a Empresa
import jakarta.persistence.*;

@Table(name = "tb_veiculos")
@Entity(name = "Veiculo")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private String placa;
    private Integer ano;
    private String cor;

    // RELACIONAMENTO: Muitos carros para Um Cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // 👇 2. RELACIONAMENTO: Muitos carros para Uma Empresa (SaaS)
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    // Construtor vazio (obrigatório pro JPA)
    public Veiculo() {}

    // Construtor para criar a partir do DTO
    public Veiculo(DadosCadastroVeiculo dados, Cliente cliente) {
        this.marca = dados.marca();
        this.modelo = dados.modelo();
        this.placa = dados.placa();
        this.ano = dados.ano();
        this.cor = dados.cor();
        this.cliente = cliente;
    }

    // --- GETTERS E SETTERS MANUAIS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    // 👇 3. Getter e Setter da Empresa (Pra funcionar no Controller)
    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
}