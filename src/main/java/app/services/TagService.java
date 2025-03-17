package app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entities.Tag;
import app.exceptions.TagNotFoundException;
import app.repositories.TagRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public Tag findById(Long id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (tagOptional.isEmpty()) {
            throw new TagNotFoundException("Tag não encontrada com o ID: " + id);
        }
        return tagOptional.get();
    }

    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    public void deleteById(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new TagNotFoundException("Tag não encontrada com o ID: " + id);
        }
        tagRepository.deleteById(id);
    }
}
