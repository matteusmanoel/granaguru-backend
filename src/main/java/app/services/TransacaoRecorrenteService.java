package app.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import app.entities.TransacaoRecorrente;
import app.repositories.TransacaoRecorrenteRepository;

@Service
public class TransacaoRecorrenteService {

    @Autowired
    private TransacaoRecorrenteRepository transacaoRecorrenteRepository;

    public List<TransacaoRecorrente> listarTodas() {
        return transacaoRecorrenteRepository.findAll();
    }

    public Optional<TransacaoRecorrente> buscarPorId(Long id) {
        return transacaoRecorrenteRepository.findById(id);
    }

    public TransacaoRecorrente salvar(TransacaoRecorrente transacaoRecorrente) {
        return transacaoRecorrenteRepository.save(transacaoRecorrente);
    }

    public void excluir(Long id) {
        transacaoRecorrenteRepository.deleteById(id);
    }
}
