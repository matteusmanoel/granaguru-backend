package app.entities;

import java.time.LocalDate;


import com.fasterxml.jackson.annotation.JsonIgnore;

import app.enums.StatusMeta;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


  @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario_id", nullable = false)
	@NotNull(message = "O usuário da meta é obrigatório.")
	@JsonIgnore
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
