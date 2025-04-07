package app.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import app.entities.Tag;
import app.services.TagService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tags")
@CrossOrigin(origins = "http://localhost:4200")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public List<Tag> findAll() {
        return tagService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> findById(@PathVariable Long id) {
        Tag tag = tagService.findById(id);
        return ResponseEntity.ok(tag);
    }

    @PostMapping
    public ResponseEntity<Tag> create(@Valid @RequestBody Tag tag) {
        Tag novaTag = tagService.save(tag);
        return ResponseEntity.ok(novaTag);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tag> update(@PathVariable Long id, @Valid @RequestBody Tag tag) {
        // Aqui, para atualizar, você pode setar o id recebido
        tag.setId(id);
        Tag tagAtualizada = tagService.save(tag);
        return ResponseEntity.ok(tagAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
