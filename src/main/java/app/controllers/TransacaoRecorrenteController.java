package app.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import app.entities.TransacaoRecorrente;
import app.services.TransacaoRecorrenteService;

@RestController
@RequestMapping("/transacoes-recorrentes")
public class TransacaoRecorrenteController {

    @Autowired
    private TransacaoRecorrenteService transacaoRecorrenteService;

    @GetMapping
    public List<TransacaoRecorrente> listarTodas() {
        return transacaoRecorrenteService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransacaoRecorrente> buscarPorId(@PathVariable Long id) {
        Optional<TransacaoRecorrente> transacaoRecorrente = transacaoRecorrenteService.buscarPorId(id);
        return transacaoRecorrente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public TransacaoRecorrente salvar(@RequestBody TransacaoRecorrente transacaoRecorrente) {
        return transacaoRecorrenteService.salvar(transacaoRecorrente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (transacaoRecorrenteService.buscarPorId(id).isPresent()) {
            transacaoRecorrenteService.excluir(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
