package app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.entities.Transacao;
import app.enums.Periodicidade;
import app.enums.TipoTransacao;
import app.services.TransacaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/transacoes")
@CrossOrigin(origins = "http://localhost:4200")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @GetMapping
    public List<Transacao> findAll() {
        return transacaoService.findAll();
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<Transacao>> buscarComFiltros(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long contaId,
            @RequestParam(required = false) Long usuarioId
    ) {
        List<Transacao> transacoes = transacaoService.buscarComFiltros(tipo, categoriaId, contaId, usuarioId);
        return transacoes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transacao> findById(@PathVariable Long id) {
        Transacao transacao = transacaoService.findById(id);
        return ResponseEntity.ok(transacao);
    }

    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<Transacao>> findByContaId(@PathVariable Long contaId) {
        List<Transacao> transacoes = transacaoService.findByContaId(contaId);
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Transacao>> findByCategoriaId(@PathVariable Long categoriaId) {
        List<Transacao> transacoes = transacaoService.findByCategoriaId(categoriaId);
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/usuario/{usuarioId}/tipo/{tipo}")
    public ResponseEntity<List<Transacao>> findByUsuarioAndTipo(@PathVariable Long usuarioId,
                                                                 @PathVariable TipoTransacao tipo) {
        List<Transacao> transacoes = transacaoService.findByUsuarioAndTipo(usuarioId, tipo);
        return ResponseEntity.ok(transacoes);
    }

    /**
     * Retorna todas as transações filtradas por periodicidade (DIÁRIA, SEMANAL ou MENSAL)
     */
    @GetMapping("/periodicidade/{periodicidade}")
    public ResponseEntity<List<Transacao>> findByPeriodicidade(@PathVariable Periodicidade periodicidade) {
        List<Transacao> transacoes = transacaoService.findByPeriodicidade(periodicidade);
        return transacoes.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(transacoes);
    }

    /**
     * Retorna todas as transações de um usuário filtradas por periodicidade
     */
    @GetMapping("/usuario/{usuarioId}/periodicidade/{periodicidade}")
    public ResponseEntity<List<Transacao>> findByUsuarioAndPeriodicidade(@PathVariable Long usuarioId,
                                                                          @PathVariable Periodicidade periodicidade) {
        List<Transacao> transacoes = transacaoService.findByUsuarioAndPeriodicidade(usuarioId, periodicidade);
        return ResponseEntity.ok(transacoes);
    }

    /**
     * Cria uma nova transação.
     */
    @PostMapping
    public ResponseEntity<Transacao> save(@Valid @RequestBody Transacao transacao) {
        Transacao nova = transacaoService.save(transacao);
        return ResponseEntity.ok(nova);
    }

    /**
     * Atualiza uma transação existente pelo ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Transacao> update(@PathVariable Long id, @Valid @RequestBody Transacao transacao) {
        transacaoService.findById(id); // lança TransacaoNotFoundException se não existir
        transacao.setId(id);
        Transacao atualizada = transacaoService.save(transacao);
        return ResponseEntity.ok(atualizada);
    }

    /**
     * Exclui uma transação pelo ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        transacaoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
