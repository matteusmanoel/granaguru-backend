package app.entities;

import java.time.LocalDateTime;

import app.enums.StatusConta;
import app.enums.TipoConta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotBlank
    private String nomeConta;

    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;

    @Column(nullable = false) // pra nao ficar nulo no banco 
    private Double saldoInicial;
    
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private StatusConta status;
}