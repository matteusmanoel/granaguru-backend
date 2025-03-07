package app.service;

import java.util.List;
import org.springframework.stereotype.Service;

import app.entities.Notificacao;
import app.repository.NotificacaoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    public List<Notificacao> listarNotificacoesDoUsuario(Long usuarioId) {
        return notificacaoRepository.findByUsuarioId(usuarioId);
    }

    public Notificacao criarNotificacao(Notificacao notificacao) {
        return notificacaoRepository.save(notificacao);
    }
}