package app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.entities.TransacaoRecorrente;
import app.services.TransacaoRecorrenteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/transacoes-recorrentes")
@CrossOrigin(origins = "http://localhost:4200")
public class TransacaoRecorrenteController {

	@Autowired
	private TransacaoRecorrenteService transacaoRecorrenteService;

	/**
	 * Retorna todas as transações recorrentes cadastradas.
	 */
	@GetMapping
	public List<TransacaoRecorrente> findAll() {
		return transacaoRecorrenteService.findAll();
	}

	/**
	 * Busca uma transação recorrente pelo ID.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<TransacaoRecorrente> findById(@PathVariable Long id) {
		TransacaoRecorrente transacao = transacaoRecorrenteService.findById(id);
		return ResponseEntity.ok(transacao);
	}

	/**
	 * Cria uma nova transação recorrente.
	 */
	@PostMapping
	public ResponseEntity<TransacaoRecorrente> save(@Valid @RequestBody TransacaoRecorrente transacaoRecorrente) {
		TransacaoRecorrente nova = transacaoRecorrenteService.save(transacaoRecorrente);
		return ResponseEntity.ok(nova);
	}

	/**
	 * Atualiza uma transação recorrente existente pelo ID.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<TransacaoRecorrente> update(@PathVariable Long id,
	                                                  @RequestBody TransacaoRecorrente transacaoRecorrente) {
		transacaoRecorrenteService.findById(id); // lança NotFound se não existir
		transacaoRecorrente.setId(id);
		TransacaoRecorrente atualizada = transacaoRecorrenteService.save(transacaoRecorrente);
		return ResponseEntity.ok(atualizada);
	}

	/**
	 * Exclui uma transação recorrente pelo ID.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		transacaoRecorrenteService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
