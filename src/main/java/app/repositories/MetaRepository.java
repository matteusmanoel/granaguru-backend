package app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.entities.Meta;
import app.entities.Usuario;
import app.enums.StatusMeta;

@Repository
public interface MetaRepository extends JpaRepository<Meta, Long> {

	// 🔹 Métodos automáticos
	List<Meta> findByUsuarioId(Long usuarioId);

	List<Meta> findByStatus(StatusMeta status);

	// 🔹 Método JPQL para buscar metas em andamento
	@Query("SELECT m FROM Meta m WHERE m.usuario.id = :usuarioId AND m.status = 'EM_ANDAMENTO'")
	List<Meta> findMetasEmAndamentoPorUsuario(@Param("usuarioId") Long usuarioId);

	// 🔹 Verifica se já existe uma meta com a mesma descrição para um determinado
	// usuário
	boolean existsByDescricaoAndUsuario(String descricao, Usuario usuario);
}
