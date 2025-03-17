package app.entities;

import app.enums.TipoCategoria;
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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false) // 🔹 Garante que um usuário sempre esteja associado
	@NotNull(message = "O usuário é obrigatório para a categoria.")
	private Usuario usuario;

	@NotBlank(message = "O nome da categoria não pode estar em branco.")
	@Size(min = 3, max = 50, message = "O nome da categoria deve ter entre 3 e 50 caracteres.")
	private String nomeCategoria;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "O tipo da categoria é obrigatório.")
	private TipoCategoria tipo;

	@Size(max = 255, message = "O ícone não pode ter mais de 255 caracteres.")
	private String icone;
}
