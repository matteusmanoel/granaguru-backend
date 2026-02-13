package com.oficinapro.api.domain.ordemservico;

import org.springframework.data.jpa.repository.JpaRepository; // <--- Importante!
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
    // É esse 'extends JpaRepository' que cria o .save() e .findAll() automaticamente pra você
}