package app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.entities.Usuario;
import app.enums.StatusUsuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	// 🔹 Métodos automáticos
	List<Usuario> findByNomeContainingIgnoreCase(String nome);

	Usuario findByEmailIgnoreCase(String email);

	// 🔹 Método JPQL
	@Query("SELECT u FROM Usuario u WHERE u.status = :status")
	List<Usuario> findByStatus(@Param("status") StatusUsuario status);
}
