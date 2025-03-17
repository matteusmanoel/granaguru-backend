package app.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
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
@JsonIgnoreProperties({ "transacoesRecorrentes" })
public class Conta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	@NotNull(message = "O usuário é obrigatório para a conta.")
	private Usuario usuario;

	@NotBlank(message = "O nome da conta não pode estar em branco.")
	@Size(min = 3, max = 50, message = "O nome da conta deve ter entre 3 e 50 caracteres.")
	private String nomeConta;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "O tipo da conta é obrigatório.")
	private TipoConta tipoConta;

	@NotNull(message = "O saldo inicial não pode ser nulo.")
	@PositiveOrZero(message = "O saldo inicial não pode ser negativo.")
	private Double saldoInicial;

	@Column(name = "data_criacao", nullable = false, updatable = false)
	@PastOrPresent(message = "A data de criação da conta não pode estar no futuro.")
	private LocalDateTime dataCriacao = LocalDateTime.now();

	@Enumerated(EnumType.STRING)
	@NotNull(message = "O status da conta é obrigatório.")
	private StatusConta status;
}
