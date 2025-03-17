package app.entities;

import java.time.LocalDateTime;

import app.enums.Periodicidade;
import app.enums.TipoTransacao;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transacoes_recorrentes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransacaoRecorrente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	@NotNull(message = "O usuário é obrigatório.")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "conta_id", nullable = false)
	@NotNull(message = "A conta é obrigatória.")
	private Conta conta;

	@ManyToOne
	@JoinColumn(name = "categoria_id", nullable = false)
	@NotNull(message = "A categoria é obrigatória.")
	private Categoria categoria;

	@NotNull(message = "O valor da transação recorrente é obrigatório.")
	@Positive(message = "O valor da transação recorrente deve ser maior que zero.")
	private Double valor;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "O tipo da transação recorrente (ENTRADA ou SAÍDA) é obrigatório.")
	private TipoTransacao tipo;

	private String descricao;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "A periodicidade da transação recorrente é obrigatória.")
	private Periodicidade periodicidade;

	private LocalDateTime dataInicial;
	private LocalDateTime dataFinal;

	@Min(value = 1, message = "O total de parcelas deve ser pelo menos 1.")
	private Integer totalParcelas; // Deve ser null para despesas fixas.

	private LocalDateTime proximaExecucao;

	private boolean despesaFixa; // Se true, será gerado automaticamente sem limite de parcelas.
}
