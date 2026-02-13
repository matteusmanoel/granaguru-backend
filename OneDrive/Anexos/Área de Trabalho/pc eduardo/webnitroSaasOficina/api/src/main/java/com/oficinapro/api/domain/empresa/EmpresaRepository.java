package com.oficinapro.api.domain.empresa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    // Aqui o Spring já te dá métodos como save(), findAll(), delete() de graça
}