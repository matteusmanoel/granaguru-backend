package app.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import app.entities.Orcamento;
import app.services.OrcamentoService;

@RestController
@RequestMapping("/orcamentos")
@CrossOrigin(origins = "http://localhost:4200")
public class OrcamentoController {

	@Autowired
	private OrcamentoService orcamentoService;

	/**
	 * Retorna todos os orçamentos cadastrados.
	 */
	@GetMapping
	public List<Orcamento> findAll() {
		return orcamentoService.findAll();
	}

	/**
	 * Busca um orçamento pelo ID.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Orcamento> findById(@PathVariable Long id) {
		Orcamento orcamento = orcamentoService.findById(id);
		return ResponseEntity.ok(orcamento);
	}

	/**
	 * Retorna os orçamentos de um usuário específico pelo ID do usuário.
	 */
	@GetMapping("/usuario/{usuarioId}")
	public List<Orcamento> findByUsuarioId(@PathVariable Long usuarioId) {
		return orcamentoService.findByUsuarioId(usuarioId);
	}

	/**
	 * Cria um novo orçamento.
	 */
	@PostMapping
	public ResponseEntity<Orcamento> save(@RequestBody Orcamento orcamento) {
		Orcamento novo = orcamentoService.save(orcamento);
		return ResponseEntity.ok(novo);
	}

	/**
	 * Atualiza um orçamento existente pelo ID.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Orcamento> update(@PathVariable Long id, @RequestBody Orcamento orcamento) {
		orcamentoService.findById(id); // lança OrcamentoNotFoundException se não existir
		orcamento.setId(id);
		Orcamento atualizado = orcamentoService.save(orcamento);
		return ResponseEntity.ok(atualizado);
	}

	/**
	 * Exclui um orçamento pelo ID.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		orcamentoService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
