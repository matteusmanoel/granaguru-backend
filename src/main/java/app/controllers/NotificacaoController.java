package app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.entities.Notificacao;
import app.exceptions.NotificacaoNotFoundException;
import app.services.NotificacaoService;

@RestController
@RequestMapping("/notificacoes")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificacaoController {

	@Autowired
	private NotificacaoService notificacaoService;

	/**
	 * Retorna todas as notificações cadastradas.
	 */
	@GetMapping
	public List<Notificacao> findAll() {
		return notificacaoService.findAll();
	}

	/**
	 * Busca uma notificação pelo ID. Retorna erro 404 caso não seja encontrada.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Notificacao> findById(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(notificacaoService.findById(id));
		} catch (NotificacaoNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Retorna as notificações de um usuário específico pelo ID do usuário.
	 */
	@GetMapping("/usuario/{usuarioId}")
	public List<Notificacao> findByUsuarioId(@PathVariable Long usuarioId) {
		return notificacaoService.findByUsuarioId(usuarioId);
	}

	/**
	 * Cria uma nova notificação.
	 */
	@PostMapping
	public ResponseEntity<?> save(@RequestBody Notificacao notificacao) {
		try {
			return ResponseEntity.ok(notificacaoService.save(notificacao));
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body("Erro ao salvar: " + e.getMessage());
		}
	}

	/**
	 * Atualiza uma notificação existente pelo ID. Retorna erro 404 caso o ID não
	 * exista.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Notificacao> update(@PathVariable Long id, @RequestBody Notificacao notificacao) {
		try {
			notificacaoService.findById(id);
			notificacao.setId(id);
			return ResponseEntity.ok(notificacaoService.save(notificacao));
		} catch (NotificacaoNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	/**
	 * Exclui uma notificação pelo ID. Retorna erro 404 se não for encontrada.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		try {
			notificacaoService.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (NotificacaoNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
