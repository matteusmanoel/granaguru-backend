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

import app.entities.Transacao;
import app.enums.TipoTransacao;
import app.exceptions.TransacaoNotFoundException;
import app.services.TransacaoService;

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
	 * Busca uma transação pelo ID. Retorna 404 se não for encontrada.
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
	 * Retorna todas as transações associadas a uma conta específica.
	 */
	@GetMapping("/conta/{contaId}")
	public ResponseEntity<List<Transacao>> findByContaId(@PathVariable Long contaId) {
		List<Transacao> transacoes = transacaoService.findByContaId(contaId);
		return ResponseEntity.ok(transacoes);
	}

	/**
	 * Retorna todas as transações associadas a uma categoria específica.
	 */
	@GetMapping("/categoria/{categoriaId}")
	public ResponseEntity<List<Transacao>> findByCategoriaId(@PathVariable Long categoriaId) {
		List<Transacao> transacoes = transacaoService.findByCategoriaId(categoriaId);
		return ResponseEntity.ok(transacoes);
	}

	/**
	 * Retorna todas as transações de um usuário filtradas por tipo (Receita ou
	 * Despesa).
	 */
	@GetMapping("/usuario/{usuarioId}/tipo/{tipo}")
	public ResponseEntity<List<Transacao>> findByUsuarioAndTipo(@PathVariable Long usuarioId,
			@PathVariable TipoTransacao tipo) {
		List<Transacao> transacoes = transacaoService.findByUsuarioAndTipo(usuarioId, tipo);
		return ResponseEntity.ok(transacoes);
	}

	/**
	 * Cria uma nova transação. Retorna erro 400 caso algum dos IDs seja inválido.
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
	 * Atualiza uma transação existente pelo ID. Retorna erro 404 caso o ID não
	 * exista.
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
	 * Exclui uma transação pelo ID. Retorna erro 404 se não for encontrada.
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