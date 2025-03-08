package app.services;

import app.entities.Orcamento;
import app.repositories.OrcamentoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;

    public OrcamentoService(OrcamentoRepository orcamentoRepository) {
        this.orcamentoRepository = orcamentoRepository;
    }

    public List<Orcamento> listarTodos() {
        return orcamentoRepository.findAll();
    }

    public List<Orcamento> listarPorUsuario(Long usuarioId) {
        return orcamentoRepository.findByUsuarioId(usuarioId);
    }

    public Optional<Orcamento> buscarPorId(Long id) {
        return orcamentoRepository.findById(id);
    }

    public Orcamento salvar(Orcamento orcamento) {
        return orcamentoRepository.save(orcamento);
    }

    public void excluir(Long id) {
        orcamentoRepository.deleteById(id);
    }
}