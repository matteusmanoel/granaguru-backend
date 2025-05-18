package app.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import app.enums.Role;
import app.enums.StatusUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O nome do usuário não pode estar em branco.")
	@Size(min = 3, max = 100, message = "O nome do usuário deve ter entre 3 e 100 caracteres.")
	private String nome;

	@NotBlank(message = "O e-mail do usuário não pode estar em branco.")
	@Email(message = "O e-mail informado não é válido.")
	@Column(unique = true)
	@Size(max = 255, message = "O e-mail do usuário deve ter no máximo 255 caracteres.")
	private String email;

	@NotBlank(message = "A senha do usuário não pode estar em branco.")
	@Size(min = 6, message = "A senha do usuário deve ter pelo menos 6 caracteres.")
	private String senha;
	
	@Enumerated(EnumType.STRING)
	@Column(
	    name = "role",
	    columnDefinition = "ENUM('ADMIN','USER')"
	)
	@NotNull(message = "O papel (role) do usuário é obrigatório.")
	private Role role;


	@Builder.Default
	@Column(name = "data_criacao", nullable = false, updatable = false)
	@PastOrPresent(message = "A data de criação do usuário não pode estar no futuro.")
	private LocalDateTime dataCriacao = LocalDateTime.now();

	@Enumerated(EnumType.STRING)
	@NotNull(message = "O status do usuário é obrigatório.")
	private StatusUsuario status;

	@OneToMany(mappedBy = "usuario")
	@JsonIgnoreProperties({ "usuario" }) // Evita referência cíclica ao serializar metas
	private List<Meta> metas;


	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    List<GrantedAuthority> authorities = new ArrayList<>();
	    authorities.add(new SimpleGrantedAuthority(this.role.name())); // usa o nome do enum
	    return authorities;
	}


	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return senha;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}
}
