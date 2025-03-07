package app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.entities.Meta;
import app.entities.Usuario;
import app.enums.StatusMeta;

@Repository
public interface MetaRepository extends JpaRepository<Meta, Long> {

    List<Meta> findByDescricaoContainingIgnoreCase(String descricao);

    List<Meta> findByStatus(StatusMeta status);

    @Query("SELECT m FROM Meta m WHERE m.valorAtual >= m.valorObjetivo")
    List<Meta> findConcluidas();

    boolean existsByDescricaoAndUsuario(String descricao, Usuario usuario);
}
