package app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entities.Notificacao;
import app.exceptions.NotificacaoNotFoundException;
import app.repositories.NotificacaoRepository;
import app.repositories.UsuarioRepository;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Retorna todas as notificações cadastradas.
     */
    public List<Notificacao> findAll() {
        return notificacaoRepository.findAll();
    }

    /**
     * Retorna as notificações de um usuário específico pelo ID do usuário.
     */
    public List<Notificacao> findByUsuarioId(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new NotificacaoNotFoundException("Usuário não encontrado para o ID: " + usuarioId);
        }
        return notificacaoRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Busca uma notificação pelo ID.
     * Lança exceção se não for encontrada.
     */
    public Notificacao findById(Long id) {
        return notificacaoRepository.findById(id)
                .orElseThrow(() -> new NotificacaoNotFoundException(id));
    }

    /**
     * Salva uma notificação no banco de dados.
     */
    public Notificacao save(Notificacao notificacao) {
        return notificacaoRepository.save(notificacao);
    }

    /**
     * Exclui uma notificação pelo ID.
     * Lança exceção se não for encontrada.
     */
    public void deleteById(Long id) {
        if (!notificacaoRepository.existsById(id)) {
            throw new NotificacaoNotFoundException(id);
        }
        notificacaoRepository.deleteById(id);
    }
}
