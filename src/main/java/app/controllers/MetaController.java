package app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.entities.Meta;
import app.enums.StatusMeta;
import app.services.MetaService;
import jakarta.validation.Valid;

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
        Meta meta = metaService.findById(id);
        return ResponseEntity.ok(meta);
    }

    @PostMapping
    public ResponseEntity<Meta> create(@Valid @RequestBody Meta meta) {
        Meta novaMeta = metaService.create(meta);
        return ResponseEntity.ok(novaMeta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Meta> update(@PathVariable Long id,
                                       @Valid @RequestBody Meta metaAtualizada) {
        Meta meta = metaService.update(id, metaAtualizada);
        return ResponseEntity.ok(meta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        metaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar-por-descricao")
    public ResponseEntity<List<Meta>> buscarPorDescricao(@RequestParam String descricao) {
        List<Meta> metas = metaService.buscarPorDescricao(descricao);
        return ResponseEntity.ok(metas);
    }

    @GetMapping("/buscar-por-status")
    public ResponseEntity<List<Meta>> buscarPorStatus(@RequestParam StatusMeta status) {
        List<Meta> metas = metaService.buscarPorStatus(status);
        return ResponseEntity.ok(metas);
    }

    @GetMapping("/concluidas")
    public ResponseEntity<List<Meta>> buscarConcluidas() {
        List<Meta> metas = metaService.buscarConcluidas();
        return ResponseEntity.ok(metas);
    }
}
