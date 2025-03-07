package app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entities.Usuario;
import app.enums.StatusUsuario;
import app.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado com o ID: " + id);
        }
        return usuarioOptional.get();
    }

    public Usuario create(Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findByEmailIgnoreCase(usuario.getEmail());
        if (usuarioExistente != null) {
            throw new RuntimeException("Já existe um usuário cadastrado com este email.");
        }

        if (usuario.getStatus() == null) {
            usuario.setStatus(StatusUsuario.ATIVO);
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario update(Long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = findById(id);

        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setSenha(usuarioAtualizado.getSenha());

        if (usuarioAtualizado.getStatus() != null) {
            usuarioExistente.setStatus(usuarioAtualizado.getStatus());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public void delete(Long id) {
        Usuario usuario = findById(id);
        usuarioRepository.delete(usuario);
    }

    public List<Usuario> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Usuario> buscarPorStatus(StatusUsuario status) {
        return usuarioRepository.buscarPorStatus(status);
    }
}
