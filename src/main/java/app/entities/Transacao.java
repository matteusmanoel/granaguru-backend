package app.entities;

import java.time.LocalDateTime;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transacao {

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

	// Data da transação, padrão: Agora
	private LocalDateTime dataTransacao = LocalDateTime.now();

	@Enumerated(EnumType.STRING)
	@NotNull(message = "O tipo da transação (ENTRADA ou SAÍDA) é obrigatório.")
	private TipoTransacao tipo;

	@NotBlank(message = "A descrição da transação não pode estar vazia.")
	private String descricao;

	@NotNull(message = "O valor da transação é obrigatório.")
	@Positive(message = "O valor da transação deve ser maior que zero.")
	private Double valor;

	private String formaPagamento;

	private Integer parcelaAtual; // Será null para transações únicas.

	@ManyToOne
	@JoinColumn(name = "transacao_recorrente_id")
	private TransacaoRecorrente transacaoRecorrente;
}
