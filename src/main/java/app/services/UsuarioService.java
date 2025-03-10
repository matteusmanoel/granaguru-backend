package app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

	public List<Usuario> listAll() {
		return usuarioRepository.findAll();
	}

	public Usuario findById(Long id) {
		return usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNotFoundException(id)); // Usa a
																									// exceção
																									// personalizada
																									// (app.exceptions)
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

	public Usuario save(Usuario usuario) {
		Usuario usuarioExistente = usuarioRepository.findByEmailIgnoreCase(usuario.getEmail());
		if (usuarioExistente != null) {
			throw new DataIntegrityViolationException("Já existe um usuário cadastrado com este email.");
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

	public void deleteById(Long id) {
		Usuario usuario = findById(id);
		usuarioRepository.deleteById(id);
	}

}
