package app.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.convert.Jsr310Converters.InstantToLocalDateTimeConverter;
import org.springframework.stereotype.Service;

import app.entities.Categoria;
import app.entities.Conta;
import app.entities.Transacao;
import app.entities.Usuario;
import app.enums.TipoTransacao;
import app.exceptions.TransacaoNotFoundException;
import app.repositories.CategoriaRepository;
import app.repositories.ContaRepository;
import app.repositories.TransacaoRepository;
import app.repositories.UsuarioRepository;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Retorna todas as transações cadastradas no banco.
     */
    public List<Transacao> findAll() {
        return transacaoRepository.findAll();
    }

    /**
     * Busca uma transação pelo ID.
     * Lança exceção se não for encontrada.
     */
    public Transacao findById(Long id) {
        return transacaoRepository.findById(id)
                .orElseThrow(() -> new TransacaoNotFoundException(id));
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
     * Salva uma transação no banco, garantindo que
     * usuário, conta e categoria existam.
     */
    public Transacao save(Transacao transacao) {
        Usuario usuario = usuarioRepository.findById(transacao.getUsuario().getId())
                .orElseThrow(() -> new DataIntegrityViolationException("Usuário não encontrado."));
        Conta conta = contaRepository.findById(transacao.getConta().getId())
                .orElseThrow(() -> new DataIntegrityViolationException("Conta não encontrada."));
        Categoria categoria = categoriaRepository.findById(transacao.getCategoria().getId())
                .orElseThrow(() -> new DataIntegrityViolationException("Categoria não encontrada."));
        
        if(transacao.getDataTransacao() == null) { //Caso dataTransacao venha null no JSON, define o momento atual
        	transacao.setDataTransacao(LocalDateTime.now()); 
        } 
        
        if(transacao.getTipo() == null) { // Caso tipo venha null no JSON, define como sendo saída
        	transacao.setTipo(TipoTransacao.SAIDA); 
        }

        transacao.setUsuario(usuario);
        transacao.setConta(conta);
        transacao.setCategoria(categoria);

        return transacaoRepository.save(transacao);
    }

    /**
     * Exclui uma transação pelo ID.
     * Lança exceção se não for encontrada.
     */
    public void deleteById(Long id) {
        if (!transacaoRepository.existsById(id)) {
            throw new TransacaoNotFoundException(id);
        }
        transacaoRepository.deleteById(id);
    }
}