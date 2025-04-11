package app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.entities.Conta;
import app.services.ContaService;

@RestController
@RequestMapping("/contas")
@CrossOrigin(origins = "http://localhost:4200")
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
	 * Busca uma conta pelo ID.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Conta> findById(@PathVariable Long id) {
		Conta conta = contaService.findById(id);
		return ResponseEntity.ok(conta);
	}

	/**
	 * Retorna as contas de um usuário específico pelo ID do usuário.
	 */
	@GetMapping("/usuario/{usuarioId}")
	public List<Conta> findByUsuarioId(@PathVariable Long usuarioId) {
		return contaService.findByUsuarioId(usuarioId);
	}

	/**
	 * Cria uma nova conta.
	 */
	@PostMapping
	public ResponseEntity<Conta> save(@RequestBody Conta conta) {
		Conta novaConta = contaService.save(conta);
		return ResponseEntity.ok(novaConta);
	}

	/**
	 * Atualiza uma conta existente pelo ID.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Conta> update(@PathVariable Long id, @RequestBody Conta conta) {
		contaService.findById(id); // se não existir, lança ContaNotFoundException
		conta.setId(id);
		Conta atualizada = contaService.save(conta);
		return ResponseEntity.ok(atualizada);
	}

	/**
	 * Exclui uma conta pelo ID.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		contaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
