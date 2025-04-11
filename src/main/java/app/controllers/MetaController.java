package app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.entities.Meta;
import app.services.MetaService;
import jakarta.validation.Valid;

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
     * Busca uma meta pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Meta> findById(@PathVariable Long id) {
        return ResponseEntity.ok(metaService.findById(id));
    }

    /**
     * Cria uma nova meta.
     */
    @PostMapping
    public ResponseEntity<Meta> save(@Valid @RequestBody Meta meta) {

        Meta novaMeta = metaService.save(meta);
        return ResponseEntity.ok(novaMeta);
    }

    /**
     * Atualiza uma meta existente pelo ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Meta> update(@PathVariable Long id, @Valid @RequestBody Meta metaAtualizada) {
        Meta atualizada = metaService.update(id, metaAtualizada);
        return ResponseEntity.ok(atualizada);
    }

    /**
     * Exclui uma meta pelo ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        metaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
