package com.oficinapro.api.domain.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // O comando mágico que filtra por oficina
    List<Cliente> findByEmpresaId(Long id);

    boolean existsByDocumentoAndEmpresaId(String documento, Long empresaId);
}