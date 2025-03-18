package app.entities;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import app.enums.StatusMeta;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "metas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	@NotNull(message = "O usuário da meta é obrigatório.")
	@JsonIgnoreProperties({ "metas" }) // Evita referência cíclica ao serializar
	private Usuario usuario;

	@NotBlank(message = "A descrição da meta não pode estar em branco.")
	@Size(min = 3, max = 100, message = "A descrição da meta deve ter entre 3 e 100 caracteres.")
	private String descricao;

	@NotNull(message = "O valor do objetivo é obrigatório.")
	@Positive(message = "O valor do objetivo deve ser maior que zero.")
	private Double valorObjetivo;

	@NotNull(message = "O valor atual não pode ser nulo.")
	@PositiveOrZero(message = "O valor atual não pode ser negativo.")
	@Builder.Default
	private Double valorAtual = 0.0;

	@PastOrPresent(message = "A data de início não pode estar no futuro.")
	private LocalDate dataInicio;

	@FutureOrPresent(message = "A data de término deve estar no futuro ou no presente.")
	private LocalDate dataTermino;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "O status da meta é obrigatório.")
	private StatusMeta status;
}
