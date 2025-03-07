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

    // Remove cascade. EAGER para carregar o usuario
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    // Ignora todos os campos do usuario, exceto 'id'
    @JsonIgnoreProperties({
        "nome",
        "email",
        "senha",
        "dataCriacao",
        "status",
        "metas", // evita loop
        // se tiver "contas", "categorias", etc. também ignore
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
