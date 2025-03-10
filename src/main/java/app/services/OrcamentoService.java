package app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entities.Orcamento;
import app.exceptions.OrcamentoNotFoundException;
import app.repositories.OrcamentoRepository;
import app.repositories.UsuarioRepository;

@Service
public class OrcamentoService {

	@Autowired
	private OrcamentoRepository orcamentoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	/**
	 * Retorna todos os orçamentos cadastrados.
	 */
	public List<Orcamento> findAll() {
		return orcamentoRepository.findAll();
	}

	/**
	 * Retorna os orçamentos de um usuário específico pelo ID do usuário.
	 */
	public List<Orcamento> findByUsuarioId(Long usuarioId) {
		if (!usuarioRepository.existsById(usuarioId)) {
			throw new OrcamentoNotFoundException("Usuário não encontrado para o ID: " + usuarioId);
		}
		return orcamentoRepository.findByUsuarioId(usuarioId);
	}

	/**
	 * Busca um orçamento pelo ID. Lança exceção se não for encontrado.
	 */
	public Orcamento findById(Long id) {
		return orcamentoRepository.findById(id).orElseThrow(() -> new OrcamentoNotFoundException(id));
	}

	/**
	 * Salva um orçamento no banco de dados.
	 */
	public Orcamento save(Orcamento orcamento) {
		return orcamentoRepository.save(orcamento);
	}

	/**
	 * Exclui um orçamento pelo ID. Lança exceção se não for encontrado.
	 */
	public void deleteById(Long id) {
		if (!orcamentoRepository.existsById(id)) {
			throw new OrcamentoNotFoundException(id);
		}
		orcamentoRepository.deleteById(id);
	}
}
