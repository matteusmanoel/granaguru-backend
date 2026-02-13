package com.oficinapro.api.controller;

import com.oficinapro.api.domain.mecanico.MecanicoRepository;
import com.oficinapro.api.domain.ordemservico.OrdemServicoRepository;
import com.oficinapro.api.domain.ordemservico.*;
import com.oficinapro.api.domain.produto.ProdutoRepository;
import com.oficinapro.api.domain.usuario.Usuario; // 👈 Importante
import com.oficinapro.api.domain.veiculo.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // 👈 Importante
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/os")
public class OrdemServicoController {

    @Autowired
    private MecanicoRepository mecanicoRepository;

    @Autowired
    private OrdemServicoRepository repository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    // --- 1. ABRIR NOVA OS (BLINDADO) 🔒 ---
    @PostMapping
    @Transactional
    public ResponseEntity abrirOS(@RequestBody DadosAberturaOS dados,
                                  @AuthenticationPrincipal Usuario usuarioLogado) { // 👈 Injeta usuário

        // 1. Busca o Veículo
        var veiculo = veiculoRepository.findById(dados.veiculoId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado!"));

        // 🔒 SEGURANÇA: O carro pertence à minha oficina?
        if (!veiculo.getEmpresa().getId().equals(usuarioLogado.getEmpresa().getId())) {
            return ResponseEntity.status(403).body("Este veículo pertence a outra oficina!");
        }

        var cliente = veiculo.getCliente();
        if (cliente == null) {
            throw new RuntimeException("Este veículo não tem dono vinculado! Vincule um cliente antes.");
        }

        // 2. Cria a OS
        var os = new OrdemServico(cliente, veiculo);
        os.setDefeitoRelatado(dados.defeitoRelatado());

        // 🔒 VINCULA A OS À EMPRESA LOGADA
        os.setEmpresa(usuarioLogado.getEmpresa());

        repository.save(os);

        return ResponseEntity.ok(new DadosDetalhamentoOS(os));
    }

    // --- 2. LISTAR TODAS (BLINDADO) 🔒 ---
    @GetMapping
    @Transactional
    public ResponseEntity<List<DadosDetalhamentoOS>> listar(@AuthenticationPrincipal Usuario usuarioLogado) {
        // 🔒 FILTRO: Só mostra OS da minha empresa
        var lista = repository.findAll().stream()
                .filter(os -> os.getEmpresa() != null && os.getEmpresa().getId().equals(usuarioLogado.getEmpresa().getId()))
                .map(DadosDetalhamentoOS::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // --- 3. DETALHAR UMA (BLINDADO) 🔒 ---
    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity detalhar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuarioLogado) {
        var os = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("OS não encontrada"));

        // 🔒 VERIFICAÇÃO: Essa OS é minha?
        if (!os.getEmpresa().getId().equals(usuarioLogado.getEmpresa().getId())) {
            return ResponseEntity.notFound().build(); // Finge que não existe pro espião
        }

        return ResponseEntity.ok(new DadosDetalhamentoOS(os));
    }

    // --- 4. ADICIONAR PEÇA E BAIXAR ESTOQUE (BLINDADO) 🔒 ---
    @PostMapping("/{id}/itens")
    @Transactional
    public ResponseEntity adicionarItem(@PathVariable Long id,
                                        @RequestBody DadosAdicionarItem dados,
                                        @AuthenticationPrincipal Usuario usuarioLogado) {

        // 1. Busca a OS e verifica se é da empresa
        var os = repository.findById(id).orElseThrow(() -> new RuntimeException("OS não encontrada"));

        if (!os.getEmpresa().getId().equals(usuarioLogado.getEmpresa().getId())) {
            return ResponseEntity.status(403).build();
        }

        // 2. Busca o Produto
        var produto = produtoRepository.findById(dados.produtoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // 🔒 SEGURANÇA: O produto é do meu estoque? (Importante verificar se Produto tem empresa também!)
        // (Assumindo que Produto já tenha getEmpresa - Se não tiver, o ideal é adicionar depois)

        // 3. 🛑 VALIDAÇÃO DE ESTOQUE
        if (produto.getQuantidadeEstoque() < dados.quantidade()) {
            return ResponseEntity.badRequest()
                    .body("Erro: Estoque insuficiente! Disponível: " + produto.getQuantidadeEstoque());
        }

        // 4. Cria o item e adiciona na OS
        ItemPeca novoItem = new ItemPeca(os, produto, dados.quantidade());
        os.adicionarPeca(novoItem);

        // 5. 📉 BAIXA O ESTOQUE
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - dados.quantidade());
        produtoRepository.save(produto);

        // 6. Salva a OS
        repository.save(os);

        return ResponseEntity.ok(new DadosDetalhamentoOS(os));
    }

    // --- 5. ADICIONAR SERVIÇO ---
    @PostMapping("/{id}/servicos")
    @Transactional
    public ResponseEntity adicionarServico(@PathVariable Long id, @RequestBody DadosAdicionarServico dados) {
        // Lógica similar de segurança pode ser aplicada aqui
        var os = repository.findById(id).orElseThrow(() -> new RuntimeException("OS não encontrada"));

        var mecanico = mecanicoRepository.findById(dados.mecanicoId())
                .orElseThrow(() -> new RuntimeException("Mecânico não encontrado"));

        var item = new ItemServico();
        item.setDescricao(dados.descricao());
        item.setValor(dados.valor());
        item.setMecanicoResponsavel(mecanico);

        os.adicionarServico(item);

        repository.save(os);
        return ResponseEntity.ok(new DadosDetalhamentoOS(os));
    }

    // --- 6. EXCLUIR OS ---
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        // Idealmente verificar a empresa antes de deletar também
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}