package app.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import app.entities.Transacao;
import app.enums.Periodicidade;
import app.enums.TipoTransacao;
import app.exceptions.TransacaoNotFoundException;
import app.services.TransacaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @GetMapping
    public List<Transacao> findAll() {
        return transacaoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transacao> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(transacaoService.findById(id));
        } catch (TransacaoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
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
	 * Retorna todas as transações filtradas por periodicidade (DIÁRIA, SEMANAL ou
	 * MENSAL)
	 */
	@GetMapping("/periodicidade/{periodicidade}")
	public ResponseEntity<List<Transacao>> findByPeriodicidade(@PathVariable Periodicidade periodicidade) {
	    List<Transacao> transacoes = transacaoService.findByPeriodicidade(periodicidade);
	    if (transacoes.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Retorna 204 se não encontrar registros
	    }
	    return ResponseEntity.ok(transacoes);
	}


	/**
	 * Retorna todas as transações de um usuário filtradas por periodicidade
	 * (DIÁRIA, SEMANAL ou MENSAL)
	 */
	@GetMapping("/usuario/{usuarioId}/periodicidade/{periodicidade}")
	public ResponseEntity<List<Transacao>> findByUsuarioAndPeriodicidade(@PathVariable Long usuarioId,
			@PathVariable Periodicidade periodicidade) {
		return ResponseEntity.ok(transacaoService.findByUsuarioAndPeriodicidade(usuarioId, periodicidade));
	}

	/**
	 * Cria uma nova transação. Retorna erro 400 caso algum dos IDs seja inválido.
	 */
	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody Transacao transacao) {
		try {
			return ResponseEntity.ok(transacaoService.save(transacao));
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body("Erro ao salvar: " + e.getMessage());
		}
	}

	/**
	 * Atualiza uma transação existente pelo ID. Retorna erro 404 caso o ID não
	 * exista.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Transacao> update(@PathVariable Long id, @Valid @RequestBody Transacao transacao) {
		try {
			transacaoService.findById(id);
			transacao.setId(id);
			return ResponseEntity.ok(transacaoService.save(transacao));
		} catch (TransacaoNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            transacaoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (TransacaoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
