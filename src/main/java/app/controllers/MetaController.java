package app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.entities.Meta;
import app.exceptions.MetaNotFoundException;
import app.services.MetaService;

@RestController
@RequestMapping("/metas")
@CrossOrigin(origins = "http://localhost:4200") 
public class MetaController {

    @Autowired
    private MetaService metaService;

    /**
     * Retorna todas as metas cadastradas.
     */
    @GetMapping
    public List<Meta> listAll() {
        return metaService.listAll();
    }

    /**
     * Retorna todas as metas em andamento para um usuário específico.
     */
    @GetMapping("/usuario/{usuarioId}/em-andamento")
    public ResponseEntity<List<Meta>> buscarMetasEmAndamento(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(metaService.buscarMetasEmAndamento(usuarioId));
    }

    /**
     * Busca uma meta pelo ID. Retorna 404 se não for encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Meta> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(metaService.findById(id));
        } catch (MetaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cria uma nova meta. Retorna erro 400 caso já exista uma com a mesma descrição.
     */
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Meta meta) {
        try {
            return ResponseEntity.ok(metaService.save(meta));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Erro ao salvar: " + e.getMessage());
        }
    }

    /**
     * Atualiza uma meta existente pelo ID. Retorna erro 404 caso o ID não exista.
     */
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

    /**
     * Exclui uma meta pelo ID. Retorna erro 404 se não for encontrada.
     */
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
