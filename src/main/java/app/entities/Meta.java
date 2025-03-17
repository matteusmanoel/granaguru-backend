package app.entities;

import java.time.LocalDate;
import app.enums.StatusMeta;
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

    // Troque para EAGER ou simplesmente omita fetch para usar o padrão ManyToOne
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
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
