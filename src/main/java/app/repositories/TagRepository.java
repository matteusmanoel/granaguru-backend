package app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import app.entities.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
	
	Optional<Tag> findByNome(String nome);

    // Se precisar de métodos customizados, adicione aqui.
}


