package app.entities;

import java.time.LocalDateTime;
import java.util.List;

import app.enums.Periodicidade;
import app.enums.TipoTransacao;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "conta_id", nullable = false)
	private Conta conta;

	@ManyToOne
	@JoinColumn(name = "categoria_id", nullable = false)
	private Categoria categoria;

	private Double valor;

	@Enumerated(EnumType.STRING)
	private TipoTransacao tipo;

	private String descricao;

	@Enumerated(EnumType.STRING)
	private Periodicidade periodicidade;

	private LocalDateTime dataInicial;
	private LocalDateTime dataFinal;
	private Integer totalParcelas;

	@OneToMany(mappedBy = "transacaoRecorrente", cascade = CascadeType.ALL)
	private List<Transacao> transacoes;
}
