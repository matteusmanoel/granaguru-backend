package app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.entities.TransacaoRecorrente;
import app.exceptions.TransacaoRecorrenteNotFoundException;
import app.services.TransacaoRecorrenteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/transacoes-recorrentes")
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
	 * Busca uma transação recorrente pelo ID. Retorna 404 se não for encontrada.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<TransacaoRecorrente> findById(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(transacaoRecorrenteService.findById(id));
		} catch (TransacaoRecorrenteNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Cria uma nova transação recorrente. Retorna erro 400 caso algum dos IDs seja
	 * inválido.
	 */
	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody TransacaoRecorrente transacaoRecorrente) {
		try {
			return ResponseEntity.ok(transacaoRecorrenteService.save(transacaoRecorrente));
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body("Erro ao salvar: " + e.getMessage());
		}
	}

	/**
	 * Atualiza uma transação recorrente existente pelo ID. Retorna erro 404 caso o
	 * ID não exista.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<TransacaoRecorrente> update(@PathVariable Long id,
			@RequestBody TransacaoRecorrente transacaoRecorrente) {
		try {
			transacaoRecorrenteService.findById(id);
			transacaoRecorrente.setId(id);
			return ResponseEntity.ok(transacaoRecorrenteService.save(transacaoRecorrente));
		} catch (TransacaoRecorrenteNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	/**
	 * Exclui uma transação recorrente pelo ID. Retorna erro 404 se não for
	 * encontrada.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		try {
			transacaoRecorrenteService.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (TransacaoRecorrenteNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
