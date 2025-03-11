package app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.entities.TransacaoRecorrente;
import app.enums.Periodicidade;

@Repository
public interface TransacaoRecorrenteRepository extends JpaRepository<TransacaoRecorrente, Long> {

	// 🔹 Métodos automáticos
	List<TransacaoRecorrente> findByContaId(Long contaId);

	List<TransacaoRecorrente> findByPeriodicidade(Periodicidade periodicidade);

	// 🔹 Método JPQL
	@Query("SELECT t FROM TransacaoRecorrente t WHERE t.usuario.id = :usuarioId AND t.periodicidade = :periodicidade")
	List<TransacaoRecorrente> findByUsuarioAndPeriodicidade(@Param("usuarioId") Long usuarioId,
			@Param("periodicidade") Periodicidade periodicidade);
}
