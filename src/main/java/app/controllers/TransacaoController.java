
package app.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import app.entities.Transacao;
import app.exceptions.TransacaoNotFoundException;
import app.services.TransacaoService;
import org.springframework.dao.DataIntegrityViolationException;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    /**
     * Retorna todas as transações cadastradas.
     */
    @GetMapping
    public List<Transacao> findAll() {
        return transacaoService.findAll();
    }

    /**
     * Busca uma transação pelo ID.
     * Retorna 404 se não for encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Transacao> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(transacaoService.findById(id));
        } catch (TransacaoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cria uma nova transação.
     * Retorna erro 400 caso algum dos IDs seja inválido.
     */
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Transacao transacao) {
        try {
            return ResponseEntity.ok(transacaoService.save(transacao));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Erro ao salvar: " + e.getMessage());
        }
    }

    /**
     * Atualiza uma transação existente pelo ID.
     * Retorna erro 404 caso o ID não exista.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Transacao> update(@PathVariable Long id, @RequestBody Transacao transacao) {
        try {
            transacaoService.findById(id);
            transacao.setId(id);
            return ResponseEntity.ok(transacaoService.save(transacao));
        } catch (TransacaoNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Exclui uma transação pelo ID.
     * Retorna erro 404 se não for encontrada.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            transacaoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (TransacaoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}