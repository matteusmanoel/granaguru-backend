package app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entities.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

}
