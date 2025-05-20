package app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import app.entities.Conta;
import app.entities.Usuario;
import app.exceptions.ContaNotFoundException;
import app.exceptions.UsuarioNotFoundException;
import app.repositories.ContaRepository;
import app.repositories.UsuarioRepository;

@Service
public class ContaService {

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	/**
	 * Retorna todas as contas cadastradas.
	 */
	public List<Conta> findAll() {
		return contaRepository.findAll();
	}

	/**
	 * Retorna as contas de um usuário específico pelo ID do usuário.
	 */
	public List<Conta> findByUsuarioId(Long usuarioId) {
		if (!usuarioRepository.existsById(usuarioId)) {
			throw new ContaNotFoundException("Usuário não encontrado para o ID: " + usuarioId);
		}
		return contaRepository.findByUsuarioId(usuarioId);
	}

	/**
	 * Busca uma conta pelo ID. Lança exceção se não for encontrada.
	 */
	public Conta findById(Long id) {
		return contaRepository.findById(id).orElseThrow(() -> new ContaNotFoundException(id));
	}

	/**
	 * Salva uma conta no banco de dados, garantindo que está associada a um usuario
	 * valido.
	 */
	public Conta save(Conta conta) {
		if (conta.getUsuario() == null || conta.getUsuario().getId() == null) {
			throw new DataIntegrityViolationException("A conta precisa estar associada a um usuário válido.");
		}

		Usuario usuario = usuarioRepository.findById(conta.getUsuario().getId())
				.orElseThrow(() -> new UsuarioNotFoundException(
						"Usuário não encontrado para o ID: " + conta.getUsuario().getId()));

		conta.setUsuario(usuario);
		return contaRepository.save(conta);
	}

	/**
	 * Exclui uma conta pelo ID. Lança exceção se não for encontrada.
	 */
	public void deleteById(Long id) {
		if (!contaRepository.existsById(id)) {
			throw new ContaNotFoundException(id);
		}
		contaRepository.deleteById(id);
	}
}