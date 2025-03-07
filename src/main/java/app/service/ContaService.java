package app.service;

import java.util.List;
import org.springframework.stereotype.Service;

import app.entities.Conta;
import app.repository.ContaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;

    public List<Conta> listarContasDoUsuario(Long usuarioId) {
        return contaRepository.findByUsuarioId(usuarioId);
    }

    public Conta criarConta(Conta conta) {
        return contaRepository.save(conta);
    }
}