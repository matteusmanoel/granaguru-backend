package app.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orcamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orcamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	@NotNull(message = "O usuário do orçamento é obrigatório.")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "categoria_id")
	@NotNull(message = "A categoria do orçamento é obrigatória.")
	private Categoria categoria;

	@NotBlank(message = "O período do orçamento não pode estar em branco.")
	@Pattern(regexp = "^(DIARIO|SEMANAL|MENSAL|ANUAL)$", message = "O período do orçamento deve ser DIARIO, SEMANAL, MENSAL ou ANUAL.")
	private String periodo;

	@NotNull(message = "O valor limite do orçamento é obrigatório.")
	@Positive(message = "O valor limite do orçamento deve ser maior que zero.")
	private Double valorLimite;

	@Column(name = "data_criacao", nullable = false, updatable = false)
	@PastOrPresent(message = "A data de criação do orçamento não pode estar no futuro.")
	private LocalDateTime dataCriacao = LocalDateTime.now();
}
