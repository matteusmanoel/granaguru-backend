package app.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import app.entities.Categoria;
import app.entities.Conta;
import app.entities.Transacao;
import app.entities.TransacaoRecorrente;
import app.entities.Usuario;
import app.enums.Periodicidade;
import app.exceptions.TransacaoRecorrenteNotFoundException;
import app.repositories.CategoriaRepository;
import app.repositories.ContaRepository;
import app.repositories.TransacaoRecorrenteRepository;
import app.repositories.TransacaoRepository;
import app.repositories.UsuarioRepository;

@Service
public class TransacaoRecorrenteService {

	@Autowired
	private TransacaoRecorrenteRepository transacaoRecorrenteRepository;

	@Autowired
	private TransacaoRepository transacaoRepository;

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
	 * Busca todas as transações recorrentes de uma conta específica.
	 */
	public List<TransacaoRecorrente> findByContaId(Long contaId) {
		List<TransacaoRecorrente> transacoes = transacaoRecorrenteRepository.findByContaId(contaId);
		if (transacoes.isEmpty()) {
			throw new TransacaoRecorrenteNotFoundException(
					"Nenhuma transação recorrente encontrada para a conta ID: " + contaId);
		}
		return transacoes;
	}

	/**
	 * Busca todas as transações recorrentes com uma determinada periodicidade.
	 */
	public List<TransacaoRecorrente> findByPeriodicidade(Periodicidade periodicidade) {
		List<TransacaoRecorrente> transacoes = transacaoRecorrenteRepository.findByPeriodicidade(periodicidade);
		if (transacoes.isEmpty()) {
			throw new TransacaoRecorrenteNotFoundException(
					"Nenhuma transação recorrente encontrada para a periodicidade: " + periodicidade);
		}
		return transacoes;
	}

	/**
	 * Busca todas as transações recorrentes de um usuário com determinada
	 * periodicidade.
	 */
	public List<TransacaoRecorrente> findByUsuarioAndPeriodicidade(Long usuarioId, Periodicidade periodicidade) {
		List<TransacaoRecorrente> transacoes = transacaoRecorrenteRepository.findByUsuarioAndPeriodicidade(usuarioId,
				periodicidade);
		if (transacoes.isEmpty()) {
			throw new TransacaoRecorrenteNotFoundException("Nenhuma transação recorrente encontrada para o usuário ID: "
					+ usuarioId + " e periodicidade: " + periodicidade);
		}
		return transacoes;
	}

	/**
	 * Salva uma transação recorrente no banco, garantindo que usuário, conta e
	 * categoria existam.
	 */
	public TransacaoRecorrente save(TransacaoRecorrente transacaoRecorrente) {
		// Se dataInicial for null, atribuímos a data e hora atual
		if (transacaoRecorrente.getDataInicial() == null) {
			transacaoRecorrente.setDataInicial(LocalDateTime.now());
		}
		if (transacaoRecorrente.getTipo() == null) {
			throw new DataIntegrityViolationException("O tipo da transação (ENTRADA ou SAIDA) deve ser informado.");
		}

		Usuario usuario = usuarioRepository.findById(transacaoRecorrente.getUsuario().getId())
				.orElseThrow(() -> new DataIntegrityViolationException("Usuário não encontrado."));
		Conta conta = contaRepository.findById(transacaoRecorrente.getConta().getId())
				.orElseThrow(() -> new DataIntegrityViolationException("Conta não encontrada."));
		Categoria categoria = categoriaRepository.findById(transacaoRecorrente.getCategoria().getId())
				.orElseThrow(() -> new DataIntegrityViolationException("Categoria não encontrada."));

		transacaoRecorrente.setUsuario(usuario);
		transacaoRecorrente.setConta(conta);
		transacaoRecorrente.setCategoria(categoria);

		// É preciso salvar o registro para gerar um id em transacoes_recorrentes e
		// assim vincular cada uma dass transações individuais a ele
		TransacaoRecorrente savedRecorrente = transacaoRecorrenteRepository.save(transacaoRecorrente);

		// Gerar parcelas
		List<Transacao> parcelas = new ArrayList<>();
		LocalDateTime dataExecucao = transacaoRecorrente.getDataInicial();

		for (int i = 1; i <= transacaoRecorrente.getTotalParcelas(); i++) {
			Transacao transacao = Transacao.builder().usuario(usuario).conta(conta).categoria(categoria)
					.tipo(transacaoRecorrente.getTipo()) // Necessário validação - ENTRADA?, SAIDA?
					.valor(transacaoRecorrente.getValor())
					.descricao(transacaoRecorrente.getDescricao() + " - Parcela " + i + "/"
							+ transacaoRecorrente.getTotalParcelas()) // Ex.: "Curso Online - Parcela 1/3 "
					.dataTransacao(dataExecucao) // Necessário valor padrão para dataInicial (descricao)
													// (i)/(totalParcelas)
					.parcelaAtual(i).transacaoRecorrente(savedRecorrente) // Ligação com a transação recorrente
					.build();

			parcelas.add(transacao);

			// Atualizar a próxima data de execução conforme a periodicidade
			switch (transacaoRecorrente.getPeriodicidade()) {
			case MENSAL:
				dataExecucao = dataExecucao.plusMonths(1);
				break;
			case SEMANAL:
				dataExecucao = dataExecucao.plusWeeks(1);
				break;
			case DIARIA:
				dataExecucao = dataExecucao.plusDays(1);
				break;
			default:
				throw new IllegalArgumentException("Periodicidade inválida.");
			}
		}

		// Salvar todas as parcelas no banco
		transacaoRepository.saveAll(parcelas);

		return savedRecorrente;
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