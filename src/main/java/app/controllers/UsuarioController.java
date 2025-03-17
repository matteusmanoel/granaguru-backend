package app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import app.exceptions.UsuarioNotFoundException;
import app.services.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@Validated
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public List<Usuario> listAll() {
		return usuarioService.listAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Usuario> findById(@PathVariable Long id) {
		try {
			Usuario usuario = usuarioService.findById(id);
			return ResponseEntity.ok(usuario);
		} catch (UsuarioNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/buscar-por-nome")
	public ResponseEntity<List<Usuario>> findByNome(@RequestParam String nome) {
		List<Usuario> usuarios = usuarioService.findByNome(nome);

		if (usuarios.isEmpty()) {
			throw new UsuarioNotFoundException("Nenhum usuário encontrado com o nome: " + nome);
		}

		return ResponseEntity.ok(usuarios);
	}

	@GetMapping("/buscar-por-status")
	public ResponseEntity<List<Usuario>> findByStatus(@RequestParam StatusUsuario status) {
		List<Usuario> usuarios = usuarioService.findByStatus(status);

		if (usuarios.isEmpty()) {
			throw new UsuarioNotFoundException("Nenhum usuário encontrado com o status: " + status);
		}

		return ResponseEntity.ok(usuarios);
	}

	@GetMapping("/buscar-por-email")
	public ResponseEntity<Usuario> findByEmail(@RequestParam String email) {
		try {
			Usuario usuario = usuarioService.findByEmail(email);
			return ResponseEntity.ok(usuario);
		} catch (UsuarioNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody Usuario usuario) {
		try {
			Usuario novoUsuario = usuarioService.save(usuario);
			return ResponseEntity.ok(novoUsuario);
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body("Erro: E-mail já cadastrado ou dados inválidos.");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Erro ao salvar o usuário.");
		}
	} // Apenas validação de e-mail e tratamento de erros comuns para a criação de
		// usuario

	@PutMapping("/{id}")
	public ResponseEntity<Usuario> update(@PathVariable Long id, @RequestBody Usuario usuario) {
		try {
			Usuario usuarioExistente = usuarioService.findById(id); // Verifica se o usuário existe

			// Permitir atualização sem necessidade de mudar o e-mail
			if (!usuarioExistente.getEmail().equalsIgnoreCase(usuario.getEmail())) {
				// Buscar o usuário pelo novo e-mail
				Usuario usuarioEmailExistente = usuarioService.findByEmail(usuario.getEmail());

				// Se encontrou outro usuário com esse e-mail e ele não for o mesmo que está
				// sendo atualizado
				if (usuarioEmailExistente != null && !usuarioEmailExistente.getId().equals(id)) {
					throw new DataIntegrityViolationException("Já existe um usuário cadastrado com este email.");
				}
			}

			usuario.setId(id); // Mantém o ID original
			return ResponseEntity.ok(usuarioService.save(usuario));

		} catch (UsuarioNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		try {
			usuarioService.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (UsuarioNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}
