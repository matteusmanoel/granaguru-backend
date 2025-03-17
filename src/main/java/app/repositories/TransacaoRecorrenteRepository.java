package app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.entities.TransacaoRecorrente;

@Repository
public interface TransacaoRecorrenteRepository extends JpaRepository<TransacaoRecorrente, Long> {

	// 🔍 Busca transações recorrentes pendentes de execução
	@Query("SELECT tr FROM TransacaoRecorrente tr WHERE tr.proximaExecucao <= CURRENT_TIMESTAMP")
	List<TransacaoRecorrente> findRecorrenciasParaProcessar();

}
