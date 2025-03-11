package app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.entities.Categoria;
import app.enums.TipoCategoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	// 🔹 Métodos automáticos
	List<Categoria> findByUsuarioId(Long usuarioId);

	List<Categoria> findByTipo(TipoCategoria tipo);

	// 🔹 Método JPQL
	@Query("SELECT c FROM Categoria c WHERE c.usuario.id = :usuarioId AND c.tipo = :tipo")
	List<Categoria> findByUsuarioAndTipo(@Param("usuarioId") Long usuarioId, @Param("tipo") TipoCategoria tipo);
}
