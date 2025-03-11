package app.entities;

import java.time.LocalDate;
import app.enums.StatusMeta;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

	@ManyToOne(fetch = FetchType.LAZY) // Permite ao Hibernate criar um proxy (objeto temporário) que carrega apenas o usuario_id.
										// Melhora perfomance do banco, evitando carregamento desnecessário de dados, pois não
										// precisa buscar todos os dados de usuario.
	@JoinColumn(name = "usuario_id", nullable = false)
	@JsonIgnore
	private Usuario usuario;

	@NotNull(message = "A descrição da meta é obrigatória")
	private String descricao;

	@NotNull(message = "O valor do objetivo é obrigatório")
	@Positive(message = "O valor do objetivo deve ser maior que zero")
	private Double valorObjetivo;

	@Builder.Default
	private Double valorAtual = 0.0;

	private LocalDate dataInicio;
	private LocalDate dataTermino;

	@Enumerated(EnumType.STRING)
	private StatusMeta status;
}
