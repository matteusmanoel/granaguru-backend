package com.oficinapro.api.controller;

import com.oficinapro.api.domain.mecanico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest; // Para pegar o ID da oficina

@RestController
@RequestMapping("/mecanicos")
public class MecanicoController {

    @Autowired
    private MecanicoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody DadosCadastroMecanico dados, HttpServletRequest request) {
        Long idOficina = 1L;

        var mecanico = new Mecanico(dados.nome(), dados.comissaoPadrao());
        mecanico.setOficinaId(idOficina); // 🔒 Segurança

        repository.save(mecanico);
        return ResponseEntity.ok(mecanico);
    }

    @GetMapping
    public ResponseEntity listar() {
        Long idOficina = 1L; // Simulando ID da oficina logada
        return ResponseEntity.ok(repository.findAllByAtivoTrueAndOficinaId(idOficina));
    }
}