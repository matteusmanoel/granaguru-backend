
package app.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import app.entities.Conta;
import app.repositories.ContaRepository;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    public List<Conta> listarTodas() {
        return contaRepository.findAll();
    }

    public List<Conta> listarPorUsuario(Long usuarioId) {
        return contaRepository.findByUsuarioId(usuarioId);
    }

    public Optional<Conta> buscarPorId(Long id) {
        return contaRepository.findById(id);
    }

    public Conta salvar(Conta conta) {
        return contaRepository.save(conta);
    }

    public void excluir(Long id) {
        contaRepository.deleteById(id);
    }
}