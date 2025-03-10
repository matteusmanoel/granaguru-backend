package app.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import app.entities.Categoria;
import app.entities.Conta;
import app.entities.TransacaoRecorrente;
import app.entities.Usuario;
import app.exceptions.TransacaoRecorrenteNotFoundException;
import app.repositories.CategoriaRepository;
import app.repositories.ContaRepository;
import app.repositories.TransacaoRecorrenteRepository;
import app.repositories.UsuarioRepository;

@Service
public class TransacaoRecorrenteService {

	@Autowired
	private TransacaoRecorrenteRepository transacaoRecorrenteRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	/**
	 * Retorna todas as transações recorrentes cadastradas no banco.
	 */
	public List<TransacaoRecorrente> findAll() {
		return transacaoRecorrenteRepository.findAll();
	}

	/**
	 * Busca uma transação recorrente pelo ID. Lança exceção se não for encontrada.
	 */
	public TransacaoRecorrente findById(Long id) {
		return transacaoRecorrenteRepository.findById(id)
				.orElseThrow(() -> new TransacaoRecorrenteNotFoundException(id));
	}

	/**
	 * Salva uma transação recorrente no banco, garantindo que usuário, conta e
	 * categoria existam.
	 */
	public TransacaoRecorrente save(TransacaoRecorrente transacaoRecorrente) {
		Usuario usuario = usuarioRepository.findById(transacaoRecorrente.getUsuario().getId())
				.orElseThrow(() -> new DataIntegrityViolationException("Usuário não encontrado."));
		Conta conta = contaRepository.findById(transacaoRecorrente.getConta().getId())
				.orElseThrow(() -> new DataIntegrityViolationException("Conta não encontrada."));
		Categoria categoria = categoriaRepository.findById(transacaoRecorrente.getCategoria().getId())
				.orElseThrow(() -> new DataIntegrityViolationException("Categoria não encontrada."));

		transacaoRecorrente.setUsuario(usuario);
		transacaoRecorrente.setConta(conta);
		transacaoRecorrente.setCategoria(categoria);

		return transacaoRecorrenteRepository.save(transacaoRecorrente);
	}

	/**
	 * Exclui uma transação recorrente pelo ID. Lança exceção se não for encontrada.
	 */
	public void deleteById(Long id) {
		if (!transacaoRecorrenteRepository.existsById(id)) {
			throw new TransacaoRecorrenteNotFoundException(id);
		}
		transacaoRecorrenteRepository.deleteById(id);
	}
}