package app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.entities.Notificacao;
import app.enums.TipoNotificacao;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

	// 🔹 Métodos automáticos
	List<Notificacao> findByUsuarioId(Long usuarioId);

	List<Notificacao> findByLida(boolean lida);

	// 🔹 Método JPQL
	@Query("SELECT n FROM Notificacao n WHERE n.usuario.id = :usuarioId AND n.tipo = :tipo")
	List<Notificacao> findByUsuarioAndTipo(@Param("usuarioId") Long usuarioId, @Param("tipo") TipoNotificacao tipo);
}
