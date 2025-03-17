package app.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import app.entities.Categoria;
import app.entities.Conta;
import app.entities.Tag;
import app.entities.Transacao;
import app.entities.Usuario;
import app.enums.TipoTransacao;
import app.exceptions.TransacaoNotFoundException;
import app.repositories.CategoriaRepository;
import app.repositories.ContaRepository;
import app.repositories.TagRepository;
import app.repositories.TransacaoRepository;
import app.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private TagRepository tagRepository;

    /**
     * Retorna todas as transações cadastradas.
     */
    public List<Transacao> findAll() {
        return transacaoRepository.findAll();
    }

    /**
     * Busca uma transação pelo ID. Lança exceção se não for encontrada.
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
     * Salva uma transação no banco, garantindo que usuário, conta e categoria existam.
     * Processa as tags, se enviadas.
     */
    public Transacao save(Transacao transacao) {
        Usuario usuario = usuarioRepository.findById(transacao.getUsuario().getId())
                .orElseThrow(() -> new DataIntegrityViolationException("Usuário não encontrado."));
        Conta conta = contaRepository.findById(transacao.getConta().getId())
                .orElseThrow(() -> new DataIntegrityViolationException("Conta não encontrada."));
        Categoria categoria = categoriaRepository.findById(transacao.getCategoria().getId())
                .orElseThrow(() -> new DataIntegrityViolationException("Categoria não encontrada."));
        
        // Se dataTransacao estiver nula, define o momento atual
        if (transacao.getDataTransacao() == null) {
            transacao.setDataTransacao(LocalDateTime.now());
        }
        // Se tipo estiver nulo, define como SAIDA (exemplo)
        if (transacao.getTipo() == null) {
            transacao.setTipo(TipoTransacao.SAIDA);
        }
        
        transacao.setUsuario(usuario);
        transacao.setConta(conta);
        transacao.setCategoria(categoria);
        
        // Processa as tags, se houver
        if (transacao.getTags() != null && !transacao.getTags().isEmpty()) {
            List<Tag> tagsProcessadas = new ArrayList<>();
            transacao.getTags().forEach(tag -> {
                if (tag.getId() != null) {
                    Tag tagCompleta = tagRepository.findById(tag.getId())
                            .orElseThrow(() -> new DataIntegrityViolationException("Tag não encontrada com ID: " + tag.getId()));
                    tagsProcessadas.add(tagCompleta);
                } else {
                    // Se desejar criar a tag nova caso não tenha id, você pode fazer:
                    Tag novaTag = tagRepository.save(tag);
                    tagsProcessadas.add(novaTag);
                }
            });
            transacao.setTags(tagsProcessadas);
        }
        
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
