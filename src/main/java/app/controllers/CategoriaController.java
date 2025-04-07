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

import app.entities.Categoria;
import app.exceptions.CategoriaNotFoundException;
import app.services.CategoriaService;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoriaController {

	@Autowired
	private CategoriaService categoriaService;

	/**
	 * Retorna todas as categorias cadastradas.
	 */
	@GetMapping
	public List<Categoria> findAll() {
		return categoriaService.findAll();
	}

	/**
	 * Busca uma categoria pelo ID. Retorna erro 404 caso não seja encontrada.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Categoria> findById(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(categoriaService.findById(id));
		} catch (CategoriaNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Retorna as categorias de um usuário específico pelo ID do usuário.
	 */
	@GetMapping("/usuario/{usuarioId}")
	public List<Categoria> findByUsuarioId(@PathVariable Long usuarioId) {
		return categoriaService.findByUsuarioId(usuarioId);
	}

	/**
	 * Cria uma nova categoria.
	 */
	@PostMapping
	public ResponseEntity<?> save(@RequestBody Categoria categoria) {
		try {
			return ResponseEntity.ok(categoriaService.save(categoria));
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body("Erro ao salvar: " + e.getMessage());
		}
	}

	/**
	 * Atualiza uma categoria existente pelo ID. Retorna erro 404 caso o ID não
	 * exista.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Categoria> update(@PathVariable Long id, @RequestBody Categoria categoria) {
		try {
			categoriaService.findById(id);
			categoria.setId(id);
			return ResponseEntity.ok(categoriaService.save(categoria));
		} catch (CategoriaNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	/**
	 * Exclui uma categoria pelo ID. Retorna erro 404 se não for encontrada.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		try {
			categoriaService.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (CategoriaNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}