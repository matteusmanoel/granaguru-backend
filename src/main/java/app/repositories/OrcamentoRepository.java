package app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.entities.Orcamento;

@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

	// 🔹 Métodos automáticos
	List<Orcamento> findByUsuarioId(Long usuarioId);

	List<Orcamento> findByCategoriaId(Long categoriaId);

	// 🔹 Método JPQL
	@Query("SELECT o FROM Orcamento o WHERE o.usuario.id = :usuarioId AND o.valorLimite > :valor")
	List<Orcamento> findByUsuarioAndLimiteMaiorQue(@Param("usuarioId") Long usuarioId, @Param("valor") Double valor);
}
