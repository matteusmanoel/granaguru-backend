package app.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
import app.exceptions.TransacaoNotFoundException;
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

	public List<TransacaoRecorrente> findAll() {
		return transacaoRecorrenteRepository.findAll();
	}

	public TransacaoRecorrente findById(Long id) {
		return transacaoRecorrenteRepository.findById(id)
				.orElseThrow(() -> new TransacaoRecorrenteNotFoundException(id));
	}

	public TransacaoRecorrente save(TransacaoRecorrente transacaoRecorrente) {

		if (transacaoRecorrente.getDataInicial() == null) {
			transacaoRecorrente.setDataInicial(LocalDateTime.now());
		}

		// Criar validação em caso de dataInicial já ter ocorrido (além de campo
		// pago/não pago)
		if (transacaoRecorrente.getProximaExecucao() == null) {
			transacaoRecorrente.setProximaExecucao(calcularProximaExecucao(transacaoRecorrente.getDataInicial(),
					transacaoRecorrente.getPeriodicidade()));
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

//		// 🔹 Se for um parcelamento fechado, verifica se todas as parcelas foram
//		// geradas
//		if (!recorrente.isDespesaFixa() && recorrente.getProximaExecucao().isAfter(recorrente.getDataFinal())) {
//			transacaoRecorrenteRepository.delete(recorrente); // Remove da base após todas as parcelas serem criadas
//																// (**Verificar se não é interesante manter o
//																// registro**)
//		} else {
//			transacaoRecorrenteRepository.save(recorrente);
//		}

		TransacaoRecorrente savedRecorrente = transacaoRecorrenteRepository.save(transacaoRecorrente);

		processarTransacoesRecorrentes(savedRecorrente);

		return savedRecorrente;s
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


	public void processarTransacoesRecorrentes(TransacaoRecorrente recorrente) {

		LocalDateTime agora = LocalDateTime.now();

		// 🔹 Se a próxima execução for nula, define a data inicial como próxima
		// execução
		if (recorrente.getProximaExecucao() == null) {
			recorrente.setProximaExecucao(recorrente.getDataInicial());
		}

		// 🔹 Se a transação já deveria ter ocorrido, calcula as ocorrências pendentes
		if (recorrente.getProximaExecucao().isBefore(agora)) {
			LocalDateTime dataExecucao = recorrente.getProximaExecucao();
			int ocorrencias = 0;

			// 🔹 Loop para gerar todas as transações que ficaram pendentes
			while (!dataExecucao.isAfter(agora)) {
				if (!transacaoRepository.existsByTransacaoRecorrenteAndDataTransacao(recorrente, dataExecucao)) {
					criarTransacaoRecorrente(recorrente, dataExecucao); // 🔹 Cria a transação para a data específica
					ocorrencias++;
				}
				dataExecucao = calcularProximaExecucao(dataExecucao, recorrente.getPeriodicidade());
			}

			// 🔹 Atualiza a próxima data de execução
			recorrente.setProximaExecucao(dataExecucao);

			// 🔹 Salva a recorrência atualizada no banco
			transacaoRecorrenteRepository.save(recorrente);

			System.out.println(
					"⚡ Processadas " + ocorrencias + " transações pendentes para " + recorrente.getDescricao());
		}
	}

	/**
	 * Calcula a próxima data de execução de uma transação recorrente com base na
	 * periodicidade.
	 */
	private LocalDateTime calcularProximaExecucao(LocalDateTime dataAtual, Periodicidade periodicidade) {
		switch (periodicidade) {
		case DIARIA:
			return dataAtual.plusDays(1);
		case SEMANAL:
			return dataAtual.plusWeeks(1);
		case MENSAL:
			return dataAtual.plusMonths(1);
		case ANUAL:
			return dataAtual.plusYears(1);
		default:
			throw new IllegalArgumentException("Periodicidade inválida: " + periodicidade);
		}
	}

	private void criarTransacaoRecorrente(TransacaoRecorrente recorrente, LocalDateTime dataExecucao) {
		// 🔹 Verifica se a transação já foi gerada para essa data para evitar
		// duplicação
		if (transacaoRepository.existsByTransacaoRecorrenteAndDataTransacao(recorrente, dataExecucao)) {
			return; // Se já existe uma transação para essa data, não cria outra
		}

		// 🔹 Calcula o número da parcela atual
		Integer parcelaAtual = calcularParcelaAtual(recorrente, dataExecucao);

		// 🔹 Define a descrição correta para despesas fixas ou parcelamentos fechados
		String descricao = recorrente.isDespesaFixa() ? recorrente.getDescricao() // Se for despesa fixa, mantém a
																					// descrição original
				: recorrente.getDescricao() + " - Parcela " + parcelaAtual + "/" + recorrente.getTotalParcelas();

		// 🔹 Criação da transação com os dados da recorrência
		Transacao transacao = Transacao.builder().usuario(recorrente.getUsuario()).conta(recorrente.getConta())
				.categoria(recorrente.getCategoria()).tipo(recorrente.getTipo()).valor(recorrente.getValor())
				.descricao(descricao) // Usa a descrição correta com ou sem parcela
				.parcelaAtual(parcelaAtual).dataTransacao(dataExecucao) // Usa a data correta de execução
				.transacaoRecorrente(recorrente) // Mantém a referência à recorrente
				.build();

		// 🔹 Salva a transação gerada
		transacaoRepository.save(transacao);
	}

	/**
	 * Calcula qual é a parcela atual de uma transação recorrente com base no número
	 * de transações já geradas.
	 */
	private int calcularParcelaAtual(TransacaoRecorrente recorrente, LocalDateTime dataExecucao) {
	    // Verifica se a transação recorrente tem uma data inicial válida
	    if (recorrente.getDataInicial() == null) {
	        throw new IllegalArgumentException("A transação recorrente precisa ter uma data inicial definida.");
	    }

	    // Calcula a diferença de tempo entre a data inicial e a data da transação que será gerada
	    long diferenca = 0;

	    switch (recorrente.getPeriodicidade()) {
	        case DIARIA:
	            diferenca = ChronoUnit.DAYS.between(recorrente.getDataInicial(), dataExecucao);
	            break;
	        case SEMANAL:
	            diferenca = ChronoUnit.WEEKS.between(recorrente.getDataInicial(), dataExecucao);
	            break;
	        case MENSAL:
	            diferenca = ChronoUnit.MONTHS.between(recorrente.getDataInicial(), dataExecucao);
	            break;
	        case ANUAL:
	            diferenca = ChronoUnit.YEARS.between(recorrente.getDataInicial(), dataExecucao);
	            break;
	        default:
	            throw new IllegalArgumentException("Periodicidade desconhecida: " + recorrente.getPeriodicidade());
	    }

	    return (int) diferenca + 1; // Retorna a parcela correta, considerando a primeira como 1
	}


}
