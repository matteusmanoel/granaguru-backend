package app.entities;

import java.time.LocalDateTime;
import java.util.List;

import app.enums.StatusUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //Diz ao Hibernate que ignore as propriedades de proxy, evitando erros de serialização (Objeto Java -> JSON)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    private String nome;

    @NotBlank
    @Email
    @Column(unique = true)
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(min = 6)
    private String senha;

    @Builder.Default
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private StatusUsuario status;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Meta> metas;

}
