
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

import app.entities.Conta;
import app.exceptions.ContaNotFoundException;
import app.services.ContaService;

@RestController
@RequestMapping("/contas")
public class ContaController {

	@Autowired
	private ContaService contaService;

	/**
	 * Retorna todas as contas cadastradas.
	 */
	@GetMapping
	public List<Conta> findAll() {
		return contaService.findAll();
	}

	/**
	 * Retorna as contas de um usuário específico pelo ID do usuário.
	 */
	@GetMapping("/usuario/{usuarioId}")
	public List<Conta> findByUsuarioId(@PathVariable Long usuarioId) {
		return contaService.findByUsuarioId(usuarioId);
	}

	/**
	 * Busca uma conta pelo ID. Retorna erro 404 caso não seja encontrada.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Conta> findById(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(contaService.findById(id));
		} catch (ContaNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Cria uma nova conta.
	 */
	@PostMapping
	public ResponseEntity<?> save(@RequestBody Conta conta) {
		try {
			return ResponseEntity.ok(contaService.save(conta));
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body("Erro ao salvar: " + e.getMessage());
		}
	}

	/**
	 * Atualiza uma conta existente pelo ID. Retorna erro 404 caso o ID não exista.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Conta> update(@PathVariable Long id, @RequestBody Conta conta) {
		try {
			contaService.findById(id);
			conta.setId(id);
			return ResponseEntity.ok(contaService.save(conta));
		} catch (ContaNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	/**
	 * Exclui uma conta pelo ID. Retorna erro 404 se não for encontrada.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		try {
			contaService.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (ContaNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
