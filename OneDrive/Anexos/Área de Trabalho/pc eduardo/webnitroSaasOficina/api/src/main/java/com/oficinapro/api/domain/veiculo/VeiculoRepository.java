package com.oficinapro.api.domain.veiculo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    // Busca todos os carros de um cliente específico
    List<Veiculo> findByClienteId(Long clienteId);
}