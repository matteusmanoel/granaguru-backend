package app.entities;

import java.time.LocalDateTime;

import app.enums.TipoNotificacao;
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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notificacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	@NotNull(message = "O usuário da notificação é obrigatório.")
	private Usuario usuario;

	@NotBlank(message = "O título da notificação não pode estar em branco.")
	@Size(max = 100, message = "O título da notificação deve ter no máximo 100 caracteres.")
	private String titulo;

	@NotBlank(message = "A mensagem da notificação não pode estar em branco.")
	@Size(max = 500, message = "A mensagem da notificação deve ter no máximo 500 caracteres.")
	private String mensagem;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "O tipo da notificação é obrigatório.")
	private TipoNotificacao tipo;

	@NotNull(message = "A data de envio da notificação é obrigatória.")
	@PastOrPresent(message = "A data de envio da notificação não pode estar no futuro.")
	private LocalDateTime dataEnvio;

	private boolean lida;
}
