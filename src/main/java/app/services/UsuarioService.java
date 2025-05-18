package app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import app.entities.Usuario;
import app.enums.StatusUsuario;
import app.exceptions.UsuarioNotFoundException;
import app.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com o ID: " + id));
    }

    public Usuario findByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email);
        if (usuario == null) {
            throw new UsuarioNotFoundException("Usuário não encontrado com o email: " + email);
        }
        return usuario;
    }

    public List<Usuario> findByNome(String nome) {
        List<Usuario> usuarios = usuarioRepository.findByNomeContainingIgnoreCase(nome);
        if (usuarios.isEmpty()) {
            throw new UsuarioNotFoundException("Nenhum usuário encontrado com o nome: " + nome);
        }
        return usuarios;
    }

    public List<Usuario> findByStatus(StatusUsuario status) {
        List<Usuario> usuarios = usuarioRepository.findByStatus(status);
        if (usuarios.isEmpty()) {
            throw new UsuarioNotFoundException("Nenhum usuário encontrado com o status: " + status);
        }
        return usuarios;
    }

    /* ------------------------------------------------------------------ */
    /* Criação                                                             */
    /* ------------------------------------------------------------------ */
    public Usuario save(Usuario usuario) {
        Usuario existente = usuarioRepository.findByEmailIgnoreCase(usuario.getEmail());
        if (existente != null && !existente.getId().equals(usuario.getId())) {
            throw new DataIntegrityViolationException("Já existe um usuário cadastrado com este email.");
        }

        if (usuario.getStatus() == null) {
            usuario.setStatus(StatusUsuario.ATIVO);
        }

        /* ✔️ codifica a senha se ainda não estiver em BCrypt */
        usuario.setSenha(encodeIfNeeded(usuario.getSenha()));

        return usuarioRepository.save(usuario);
    }

    /* ------------------------------------------------------------------ */
    /* Atualização                                                         */
    /* ------------------------------------------------------------------ */
    public Usuario update(Long id, Usuario usuarioAtualizado) {
        Usuario existente = findById(id);

        existente.setNome(usuarioAtualizado.getNome());
        existente.setEmail(usuarioAtualizado.getEmail());

        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isBlank()) {
            existente.setSenha(encodeIfNeeded(usuarioAtualizado.getSenha()));
        }

        if (usuarioAtualizado.getStatus() != null) {
            existente.setStatus(usuarioAtualizado.getStatus());
        }

        return usuarioRepository.save(existente);
    }

    /* ------------------------------------------------------------------ */
    /* Remoção                                                             */
    /* ------------------------------------------------------------------ */
    public void deleteById(Long id) {
        findById(id);                       // lança 404 se não existir
        usuarioRepository.deleteById(id);
    }

    /* ------------------------------------------------------------------ */
    /* Helper privado                                                      */
    /* ------------------------------------------------------------------ */
    private String encodeIfNeeded(String rawOrEncoded) {
        return rawOrEncoded != null && !rawOrEncoded.startsWith("$2a$")
               ? passwordEncoder.encode(rawOrEncoded)
               : rawOrEncoded;
    }
}
