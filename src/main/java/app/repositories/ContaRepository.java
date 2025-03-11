package app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.entities.Conta;
import app.enums.TipoConta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

	// 🔹 Métodos automáticos
	List<Conta> findByUsuarioId(Long usuarioId);

	List<Conta> findByTipoConta(TipoConta tipoConta);

	// 🔹 Método JPQL
	@Query("SELECT c FROM Conta c WHERE c.usuario.id = :usuarioId AND c.status = 'ATIVA'")
	List<Conta> findAtivasPorUsuario(@Param("usuarioId") Long usuarioId);
}
