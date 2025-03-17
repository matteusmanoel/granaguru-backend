package app.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.entities.Categoria;
import app.entities.Conta;
import app.entities.Transacao;
import app.entities.TransacaoRecorrente;
import app.entities.Usuario;
import app.enums.Periodicidade;
import app.enums.TipoTransacao;
import app.exceptions.TransacaoNotFoundException;
import app.repositories.CategoriaRepository;
import app.repositories.ContaRepository;
import app.repositories.TransacaoRecorrenteRepository;
import app.repositories.TransacaoRepository;
import app.repositories.UsuarioRepository;

@Service
public class TransacaoService {

	@Autowired
	private TransacaoRepository transacaoRepository;

	@Autowired
	private TransacaoRecorrenteService transacaoRecorrenteService;

	@Autowired
	private TransacaoRecorrenteRepository transacaoRecorrenteRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	/**
	 * Retorna todas as transações cadastradas no banco. Gera registros na tabela
	 * transacoes sob demanda, conforme a coluna proximaExecucao da tabela
	 * transacoes_recorrentes.
	 */
	public List<Transacao> findAll() {
		// 🔹 Busca transações recorrentes pendentes de execução
		List<TransacaoRecorrente> recorrenciasPendentes = transacaoRecorrenteRepository.findRecorrenciasParaProcessar();

		// 🔹 Processa cada transação recorrente pendente e gera novas transações, se
		// necessário
		for (TransacaoRecorrente recorrente : recorrenciasPendentes) {
			if (!transacaoRepository.existsByTransacaoRecorrenteAndDataTransacao(recorrente,
					recorrente.getProximaExecucao())) {
				transacaoRecorrenteService.processarTransacoesRecorrentes(recorrente);
			}
		}

		// 🔹 Retorna todas as transações após o processamento das recorrentes
		return transacaoRepository.findAll();
	}

	/**
	 * Busca uma transação pelo ID. Lança exceção se não for encontrada.
	 */
	public Transacao findById(Long id) {
		return transacaoRepository.findById(id).orElseThrow(() -> new TransacaoNotFoundException(id));
	}

	/**
	 * Retorna transações associadas a uma conta específica.
	 */
	public List<Transacao> findByContaId(Long contaId) {
		return transacaoRepository.findByContaId(contaId);
	}

	/**
	 * Retorna transações associadas a uma categoria específica.
	 */
	public List<Transacao> findByCategoriaId(Long categoriaId) {
		return transacaoRepository.findByCategoriaId(categoriaId);
	}

	/**
	 * Retorna transações filtradas pelo usuário e pelo tipo (Receita ou Despesa).
	 */
	public List<Transacao> findByUsuarioAndTipo(Long usuarioId, TipoTransacao tipo) {
		return transacaoRepository.findByUsuarioAndTipo(usuarioId, tipo);
	}

	/**
	 * Retorna transações filtradas pela periodicidade.
	 */
	public List<Transacao> findByPeriodicidade(Periodicidade periodicidade) {
		return transacaoRepository.findByPeriodicidade(periodicidade);
	}

	/**
	 * Retorna transações filtradas por usuário e periodicidade.
	 */
	public List<Transacao> findByUsuarioAndPeriodicidade(Long usuarioId, Periodicidade periodicidade) {
		return transacaoRepository.findByUsuarioAndPeriodicidade(usuarioId, periodicidade);
	}

	/**
	 * Salva uma transação no banco, garantindo que usuário, conta e categoria
	 * existam.
	 */
	@Transactional
	public Transacao save(Transacao transacao) {
		if (transacao.getDataTransacao() == null) {
			transacao.setDataTransacao(LocalDateTime.now());
		}

		if (transacao.getParcelaAtual() == null) {
			transacao.setParcelaAtual(1);
		}

		Usuario usuario = usuarioRepository.findById(transacao.getUsuario().getId())
				.orElseThrow(() -> new DataIntegrityViolationException("Usuário não encontrado."));
		Conta conta = contaRepository.findById(transacao.getConta().getId())
				.orElseThrow(() -> new DataIntegrityViolationException("Conta não encontrada."));
		Categoria categoria = categoriaRepository.findById(transacao.getCategoria().getId())
				.orElseThrow(() -> new DataIntegrityViolationException("Categoria não encontrada."));

		if (transacao.getTipo() == null) {
			transacao.setTipo(TipoTransacao.SAIDA);
		}

		transacao.setUsuario(usuario);
		transacao.setConta(conta);
		transacao.setCategoria(categoria);

		return transacaoRepository.save(transacao);
	}

	/**
	 * Exclui uma transação pelo ID. Lança exceção se não for encontrada.
	 */
	public void deleteById(Long id) {
		if (!transacaoRepository.existsById(id)) {
			throw new TransacaoNotFoundException(id);
		}
		transacaoRepository.deleteById(id);
	}

}
