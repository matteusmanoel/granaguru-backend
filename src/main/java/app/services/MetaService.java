package app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entities.Meta;
import app.entities.Usuario;
import app.enums.StatusMeta;
import app.repositories.MetaRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MetaService {

    @Autowired
    private MetaRepository metaRepository;

    @Autowired
    private UsuarioService usuarioService;

    public List<Meta> listAll() {
        return metaRepository.findAll();
    }

    public Meta findById(Long id) {
        Optional<Meta> metaOptional = metaRepository.findById(id);
        if (metaOptional.isEmpty()) {
            throw new RuntimeException("Meta não encontrada com o ID: " + id);
        }
        return metaOptional.get();
    }

    public Meta create(Meta meta) {
        if (meta.getUsuario() == null || meta.getUsuario().getId() == null) {
            throw new RuntimeException("O usuário deve ser informado com um ID válido.");
        }

        Usuario usuarioCompleto = usuarioService.findById(meta.getUsuario().getId());

        boolean existe = metaRepository.existsByDescricaoAndUsuario(meta.getDescricao(), usuarioCompleto);
        if (existe) {
            throw new RuntimeException("Já existe uma meta com essa descrição para este usuário.");
        }

        meta.setUsuario(usuarioCompleto);
        return metaRepository.save(meta);
    }

    public Meta update(Long id, Meta metaAtualizada) {
        Meta metaExistente = findById(id);

        boolean descricaoMudou = metaAtualizada.getDescricao() != null
                && !metaAtualizada.getDescricao().equals(metaExistente.getDescricao());

        metaExistente.setDescricao(metaAtualizada.getDescricao());
        metaExistente.setValorObjetivo(metaAtualizada.getValorObjetivo());
        metaExistente.setValorAtual(metaAtualizada.getValorAtual());
        metaExistente.setDataInicio(metaAtualizada.getDataInicio());
        metaExistente.setDataTermino(metaAtualizada.getDataTermino());
        metaExistente.setStatus(metaAtualizada.getStatus());

        if (metaAtualizada.getUsuario() != null && metaAtualizada.getUsuario().getId() != null) {
            Usuario usuarioCompleto = usuarioService.findById(metaAtualizada.getUsuario().getId());

            if (descricaoMudou) {
                boolean existe = metaRepository.existsByDescricaoAndUsuario(metaAtualizada.getDescricao(), usuarioCompleto);
                if (existe) {
                    throw new RuntimeException("Já existe uma meta com essa descrição para este usuário.");
                }
            }

            metaExistente.setUsuario(usuarioCompleto);
        }

        return metaRepository.save(metaExistente);
    }

    public void delete(Long id) {
        Meta meta = findById(id);
        metaRepository.delete(meta);
    }

    public List<Meta> buscarPorDescricao(String descricao) {
        return metaRepository.findByDescricaoContainingIgnoreCase(descricao);
    }

    public List<Meta> buscarPorStatus(StatusMeta status) {
        return metaRepository.findByStatus(status);
    }

    public List<Meta> buscarConcluidas() {
        return metaRepository.findConcluidas();
    }
}
