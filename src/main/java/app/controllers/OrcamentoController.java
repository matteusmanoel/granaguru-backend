package app.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import app.entities.Orcamento;
import app.exceptions.OrcamentoNotFoundException;
import app.services.OrcamentoService;
import org.springframework.dao.DataIntegrityViolationException;

@RestController
@RequestMapping("/orcamentos")
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
	 * Busca um orçamento pelo ID. Retorna erro 404 caso não seja encontrado.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Orcamento> findById(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(orcamentoService.findById(id));
		} catch (OrcamentoNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
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
	public ResponseEntity<?> save(@RequestBody Orcamento orcamento) {
		try {
			return ResponseEntity.ok(orcamentoService.save(orcamento));
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body("Erro ao salvar: " + e.getMessage());
		}
	}

	/**
	 * Atualiza um orçamento existente pelo ID. Retorna erro 404 caso o ID não
	 * exista.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Orcamento> update(@PathVariable Long id, @RequestBody Orcamento orcamento) {
		try {
			orcamentoService.findById(id);
			orcamento.setId(id);
			return ResponseEntity.ok(orcamentoService.save(orcamento));
		} catch (OrcamentoNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	/**
	 * Exclui um orçamento pelo ID. Retorna erro 404 se não for encontrado.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		try {
			orcamentoService.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (OrcamentoNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
