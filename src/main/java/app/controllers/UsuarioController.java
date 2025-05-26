package app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.entities.Usuario;
import app.enums.StatusUsuario;
import app.services.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@Validated
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public List<Usuario> listAll() {
		return usuarioService.listAll();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> findById(@PathVariable Long id) {
		Usuario usuario = usuarioService.findById(id);
		return ResponseEntity.ok(usuario);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/buscar-por-nome")
	public ResponseEntity<List<Usuario>> findByNome(@RequestParam String nome) {
		List<Usuario> usuarios = usuarioService.findByNome(nome);
		return ResponseEntity.ok(usuarios);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/buscar-por-status")
	public ResponseEntity<List<Usuario>> findByStatus(@RequestParam StatusUsuario status) {
		List<Usuario> usuarios = usuarioService.findByStatus(status);
		return ResponseEntity.ok(usuarios);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/buscar-por-email")
	public ResponseEntity<Usuario> findByEmail(@RequestParam String email) {
		Usuario usuario = usuarioService.findByEmail(email);
		return ResponseEntity.ok(usuario);
	}

	@PostMapping
	public ResponseEntity<Usuario> save(@Valid @RequestBody Usuario usuario) {
		Usuario novoUsuario = usuarioService.save(usuario);
		return ResponseEntity.ok(novoUsuario);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Usuario> update(@PathVariable Long id, @RequestBody Usuario usuario) {
		usuario.setId(id); // Garante que o ID seja mantido
		Usuario usuarioAtualizado = usuarioService.update(id, usuario);
		return ResponseEntity.ok(usuarioAtualizado);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		usuarioService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
