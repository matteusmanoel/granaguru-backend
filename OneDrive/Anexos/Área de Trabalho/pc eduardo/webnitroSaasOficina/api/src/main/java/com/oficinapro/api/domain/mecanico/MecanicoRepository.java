package com.oficinapro.api.domain.mecanico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MecanicoRepository extends JpaRepository<Mecanico, Long> {
    // Busca só os ativos e da minha oficina (Segurança!)
    List<Mecanico> findAllByAtivoTrueAndOficinaId(Long oficinaId);
}