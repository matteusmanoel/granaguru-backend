package com.oficinapro.api.domain.ordemservico;

import com.oficinapro.api.domain.cliente.Cliente;
import com.oficinapro.api.domain.empresa.Empresa; // 👈 1. Importe a Empresa
import com.oficinapro.api.domain.veiculo.Veiculo;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "tb_ordens_servico")
@Entity(name = "OrdemServico")
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 👇 A MUDANÇA: Usamos o objeto Empresa para ficar padrão
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    // --- VÍNCULOS ---
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;

    // --- DADOS DA O.S. ---
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private LocalDateTime dataPrevisaoEntrega;

    @Enumerated(EnumType.STRING)
    private StatusOS status;

    @Column(columnDefinition = "TEXT")
    private String defeitoRelatado;

    @Column(columnDefinition = "TEXT")
    private String laudoTecnico;

    @Column(columnDefinition = "TEXT")
    private String observacoesInternas;

    @Column(columnDefinition = "TEXT")
    private String checklistJson;

    // --- TOTAIS ---
    private BigDecimal totalPecas = BigDecimal.ZERO;
    private BigDecimal totalServicos = BigDecimal.ZERO;
    private BigDecimal totalGeral = BigDecimal.ZERO;

    // --- LISTAS ---
    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPeca> pecas = new ArrayList<>();

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemServico> servicos = new ArrayList<>();

    // --- CONSTRUTORES ---
    public OrdemServico() {
    }

    public OrdemServico(Cliente cliente, Veiculo veiculo) {
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.dataAbertura = LocalDateTime.now();
        this.status = StatusOS.ABERTA;
    }

    // --- MÉTODOS MÁGICOS ---

    @PrePersist
    public void prePersist() {
        if (this.dataAbertura == null) this.dataAbertura = LocalDateTime.now();
        if (this.status == null) this.status = StatusOS.ABERTA;
        recalcularTotais();
    }

    public void recalcularTotais() {
        this.totalPecas = pecas.stream()
                .map(item -> item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalServicos = servicos.stream()
                .map(item -> item.getValor() != null ? item.getValor() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalGeral = totalPecas.add(totalServicos);
    }

    public void adicionarPeca(ItemPeca item) {
        item.setOrdemServico(this);
        this.pecas.add(item);
        recalcularTotais();
    }

    public void adicionarServico(ItemServico item) {
        item.setOrdemServico(this);
        this.servicos.add(item);
        recalcularTotais();
    }

    // --- GETTERS E SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // 👇 Getter e Setter ATUALIZADOS para Empresa
    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Veiculo getVeiculo() { return veiculo; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }

    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }

    public LocalDateTime getDataFechamento() { return dataFechamento; }
    public void setDataFechamento(LocalDateTime dataFechamento) { this.dataFechamento = dataFechamento; }

    public LocalDateTime getDataPrevisaoEntrega() { return dataPrevisaoEntrega; }
    public void setDataPrevisaoEntrega(LocalDateTime dataPrevisaoEntrega) { this.dataPrevisaoEntrega = dataPrevisaoEntrega; }

    public StatusOS getStatus() { return status; }
    public void setStatus(StatusOS status) { this.status = status; }

    public String getDefeitoRelatado() { return defeitoRelatado; }
    public void setDefeitoRelatado(String defeitoRelatado) { this.defeitoRelatado = defeitoRelatado; }

    public String getLaudoTecnico() { return laudoTecnico; }
    public void setLaudoTecnico(String laudoTecnico) { this.laudoTecnico = laudoTecnico; }

    public String getObservacoesInternas() { return observacoesInternas; }
    public void setObservacoesInternas(String observacoesInternas) { this.observacoesInternas = observacoesInternas; }

    public String getChecklistJson() { return checklistJson; }
    public void setChecklistJson(String checklistJson) { this.checklistJson = checklistJson; }

    public BigDecimal getTotalPecas() { return totalPecas; }
    public void setTotalPecas(BigDecimal totalPecas) { this.totalPecas = totalPecas; }

    public BigDecimal getTotalServicos() { return totalServicos; }
    public void setTotalServicos(BigDecimal totalServicos) { this.totalServicos = totalServicos; }

    public BigDecimal getTotalGeral() { return totalGeral; }
    public void setTotalGeral(BigDecimal totalGeral) { this.totalGeral = totalGeral; }

    public List<ItemPeca> getPecas() { return pecas; }
    public void setPecas(List<ItemPeca> pecas) { this.pecas = pecas; }

    public List<ItemServico> getServicos() { return servicos; }
    public void setServicos(List<ItemServico> servicos) { this.servicos = servicos; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdemServico that = (OrdemServico) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}