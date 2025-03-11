package app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.entities.Transacao;
import app.enums.TipoTransacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

	// 🔹 Métodos automáticos
	List<Transacao> findByContaId(Long contaId);

	List<Transacao> findByCategoriaId(Long categoriaId);

	// 🔹 Método JPQL
	@Query("SELECT t FROM Transacao t WHERE t.usuario.id = :usuarioId AND t.tipo = :tipo")
	List<Transacao> findByUsuarioAndTipo(@Param("usuarioId") Long usuarioId, @Param("tipo") TipoTransacao tipo);
}
