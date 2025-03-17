package app.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.entities.Transacao;
import app.entities.TransacaoRecorrente;
import app.enums.Periodicidade;
import app.enums.TipoTransacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

	// 🔹 Busca transações por conta específica
	List<Transacao> findByContaId(Long contaId);

	// 🔹 Busca transações por categoria específica
	List<Transacao> findByCategoriaId(Long categoriaId);

	// 🔹 Busca transações de um usuário filtradas por tipo (Entrada ou Saída)
	@Query("SELECT t FROM Transacao t WHERE t.usuario.id = :usuarioId AND t.tipo = :tipo")
	List<Transacao> findByUsuarioAndTipo(@Param("usuarioId") Long usuarioId, @Param("tipo") TipoTransacao tipo);

	// 🔍 Busca transações associadas a uma transação recorrente com base na
	// periodicidade
	@Query("SELECT t FROM Transacao t JOIN t.transacaoRecorrente tr WHERE tr.periodicidade = :periodicidade")
	List<Transacao> findByPeriodicidade(@Param("periodicidade") Periodicidade periodicidade);

	// 🔍 Busca transações de um usuário filtrando pela periodicidade da recorrência
	@Query("SELECT t FROM Transacao t JOIN t.transacaoRecorrente tr WHERE t.usuario.id = :usuarioId AND tr.periodicidade = :periodicidade")
	List<Transacao> findByUsuarioAndPeriodicidade(@Param("usuarioId") Long usuarioId,
			@Param("periodicidade") Periodicidade periodicidade);

	// 🔍 Busca todas as transações associadas a uma transação recorrente específica
	@Query("SELECT t FROM Transacao t WHERE t.transacaoRecorrente.id = :recorrenteId")
	List<Transacao> findByTransacaoRecorrenteId(@Param("recorrenteId") Long recorrenteId);

	@Query("SELECT COUNT(t) FROM Transacao t WHERE t.transacaoRecorrente.id = :recorrenteId")
	int countByTransacaoRecorrenteId(@Param("recorrenteId") Long recorrenteId);

	// 🔍 Verifica se já existe uma transação gerada para a próxima execução da
	// transação recorrente
	boolean existsByTransacaoRecorrenteAndDataTransacao(TransacaoRecorrente transacaoRecorrente,
			LocalDateTime dataTransacao);
}
