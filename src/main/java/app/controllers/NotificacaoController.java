package app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.entities.Notificacao;
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
	 * Busca uma notificação pelo ID.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Notificacao> findById(@PathVariable Long id) {
		Notificacao notificacao = notificacaoService.findById(id);
		return ResponseEntity.ok(notificacao);
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
	public ResponseEntity<Notificacao> save(@RequestBody Notificacao notificacao) {
		Notificacao nova = notificacaoService.save(notificacao);
		return ResponseEntity.ok(nova);
	}

	/**
	 * Atualiza uma notificação existente pelo ID.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Notificacao> update(@PathVariable Long id, @RequestBody Notificacao notificacao) {
		notificacaoService.findById(id); // valida se existe
		notificacao.setId(id);
		Notificacao atualizada = notificacaoService.save(notificacao);
		return ResponseEntity.ok(atualizada);
	}

	/**
	 * Exclui uma notificação pelo ID.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		notificacaoService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
