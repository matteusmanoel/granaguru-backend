package com.oficinapro.api.controller;

import com.oficinapro.api.domain.ordemservico.ItemPeca;
import com.oficinapro.api.domain.ordemservico.ItemOrdemServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itens-os")
public class ItemOrdemServicoController {

    @Autowired
    private ItemOrdemServicoRepository repository;

    @PostMapping
    public ItemPeca adicionarItem(@RequestBody ItemPeca item) {
        // Futuramente, aqui a gente abate do estoque automaticamente!
        return repository.save(item);
    }

    @GetMapping
    public List<ItemPeca> listar() {
        return repository.findAll();
    }
}