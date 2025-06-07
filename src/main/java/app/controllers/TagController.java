package app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.entities.Tag;
import app.services.TagService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tags")
@CrossOrigin(origins = "*")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * Lista todas as tags
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public List<Tag> findAll() {
        return tagService.findAll();
    }

    /**
     * Busca tag por ID. Retorna 404 se não existir.
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Tag> findById(@PathVariable Long id) {
        Tag tag = tagService.findById(id); // lança TagNotFoundException se não existir
        return ResponseEntity.ok(tag);
    }

    /**
     * Cria nova tag
     */
    @PostMapping
    public ResponseEntity<Tag> create(@Valid @RequestBody Tag tag) {
        Tag novaTag = tagService.save(tag);
        return ResponseEntity.ok(novaTag);
    }

    /**
     * Atualiza tag existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Tag> update(@PathVariable Long id, @Valid @RequestBody Tag tag) {
        tag.setId(id);
        Tag tagAtualizada = tagService.save(tag);
        return ResponseEntity.ok(tagAtualizada);
    }

    /**
     * Exclui tag por ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.deleteById(id); // lança TagNotFoundException se não existir
        return ResponseEntity.noContent().build();
    }
}
