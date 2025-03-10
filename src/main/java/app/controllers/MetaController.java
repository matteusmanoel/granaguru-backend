
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

import app.entities.Meta;
import app.exceptions.MetaNotFoundException;
import app.services.MetaService;

@RestController
@RequestMapping("/metas")
public class MetaController {

	@Autowired
	private MetaService metaService;

	@GetMapping
	public List<Meta> listAll() {
		return metaService.listAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Meta> findById(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(metaService.findById(id));
		} catch (MetaNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<?> save(@RequestBody Meta meta) {
		try {
			return ResponseEntity.ok(metaService.save(meta));
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body("Erro ao salvar: " + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Meta> update(@PathVariable Long id, @RequestBody Meta metaAtualizada) {
		try {
			return ResponseEntity.ok(metaService.update(id, metaAtualizada));
		} catch (MetaNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		try {
			metaService.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (MetaNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}