package app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.entities.Categoria;
import app.services.CategoriaService;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*")
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
	 * Busca uma categoria pelo ID.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Categoria> findById(@PathVariable Long id) {
		Categoria categoria = categoriaService.findById(id);
		return ResponseEntity.ok(categoria);
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
	public ResponseEntity<Categoria> save(@RequestBody Categoria categoria) {
		Categoria nova = categoriaService.save(categoria);
		return ResponseEntity.ok(nova);
	}

	/**
	 * Atualiza uma categoria existente pelo ID.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Categoria> update(@PathVariable Long id, @RequestBody Categoria categoria) {
		categoriaService.findById(id); // lança exceção se não existir
		categoria.setId(id);
		Categoria atualizada = categoriaService.save(categoria);
		return ResponseEntity.ok(atualizada);
	}

	/**
	 * Exclui uma categoria pelo ID.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		categoriaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
