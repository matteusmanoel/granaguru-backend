package app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import app.entities.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    // Se precisar de métodos customizados, adicione aqui.
}
