package app.dto;

import java.time.LocalDateTime;
import app.entities.Categoria;
import app.entities.Conta;
import app.entities.TransacaoRecorrente;
import app.entities.Usuario;
import app.enums.Periodicidade;
import app.enums.TipoTransacao;

public class TransacaoRecorrenteDTO {
    private Long usuarioId;
    private Long contaId;
    private Long categoriaId;
    private Double valor;
    private TipoTransacao tipo;
    private String descricao;
    private Periodicidade periodicidade;
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;
    private Integer totalParcelas;
    private boolean despesaFixa;
    // getters & setters omitidos

    public TransacaoRecorrente toEntity() {
        TransacaoRecorrente tr = new TransacaoRecorrente();

        // setando apenas o ID nas entidades relacionadas
        Usuario u = new Usuario();
        u.setId(this.usuarioId);
        tr.setUsuario(u);

        Conta c = new Conta();
        c.setId(this.contaId);
        tr.setConta(c);

        Categoria cat = new Categoria();
        cat.setId(this.categoriaId);
        tr.setCategoria(cat);

        tr.setValor(this.valor);
        tr.setTipo(this.tipo);
        tr.setDescricao(this.descricao);
        tr.setPeriodicidade(this.periodicidade);
        tr.setDataInicial(this.dataInicial != null ? this.dataInicial : LocalDateTime.now());
        tr.setDataFinal(this.dataFinal);
        tr.setTotalParcelas(this.totalParcelas);
        tr.setDespesaFixa(this.despesaFixa);
        // proximaExecucao será calculada no service
        return tr;
    }
}
