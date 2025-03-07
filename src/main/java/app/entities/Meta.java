package app.entities;

import java.time.LocalDateTime;

import app.enums.StatusMeta;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "metas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Meta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties({
        "nome",
        "email",
        "senha",
        "dataCriacao",
        "status",
        "metas",
        "contas",
        "categorias",
        "transacoes",
        "orcamentos",
        "notificacoes",
        "transacoesRecorrentes"
    })
    private Usuario usuario;

    private String descricao;
    private Double valorObjetivo;
    private Double valorAtual;
    private LocalDateTime dataInicio;
    private LocalDateTime dataTermino;

    @Enumerated(EnumType.STRING)
    private StatusMeta status;
}
