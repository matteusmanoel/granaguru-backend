package com.oficinapro.api.controller;

import com.oficinapro.api.domain.cliente.ClienteRepository;
import com.oficinapro.api.domain.veiculo.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
//@CrossOrigin(origins = "*") // Importante pro Angular não bloquear
public class VeiculoController {

    @Autowired
    private VeiculoRepository repository;

    @Autowired
    private ClienteRepository clienteRepository;

    // 1. CADASTRAR VEÍCULO
    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroVeiculo dados) {
        var dono = clienteRepository.findById(dados.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

        var veiculo = new Veiculo(dados, dono);
        repository.save(veiculo);

        return ResponseEntity.ok().build();
    }

    // 2. LISTAR VEÍCULOS DE UM CLIENTE
    @GetMapping("/{clienteId}")
    public List<Veiculo> listarPorCliente(@PathVariable Long clienteId) {
        return repository.findByClienteId(clienteId);
    }

    // 3. LISTAR TODOS
    @GetMapping
    public List<Veiculo> listarTodos() {
        return repository.findAll();
    }

    // 👇 4. ATUALIZAR (MÉTODO NOVO)
    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosCadastroVeiculo dados) {
        var veiculo = repository.getReferenceById(dados.id());

        veiculo.setMarca(dados.marca());
        veiculo.setModelo(dados.modelo());
        veiculo.setPlaca(dados.placa());
        veiculo.setCor(dados.cor());
        veiculo.setAno(dados.ano());

        // 👇 A MUDANÇA: Retornamos o DTO, que não tem loop infinito
        return ResponseEntity.ok(dados);
    }

    // 👇 5. EXCLUIR (MÉTODO NOVO)
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        // Verifica se existe para não dar erro 500 feio
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}