package com.oficinapro.api.domain.ordemservico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemOrdemServicoRepository extends JpaRepository<ItemPeca, Long> {

    // O Spring cria o SQL sozinho só pelo nome do método!
    // "Busque itens PELA OrdemServico_Id"
    List<ItemPeca> findByOrdemServico_Id(Long id);
}