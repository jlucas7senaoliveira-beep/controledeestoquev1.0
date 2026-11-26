
package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimentacaoEstoque {

    private int idMovimentacao;

    private int idProduto;
    private String nomeProduto;

    private Integer idUsuario;
    private String nomeUsuario;

    private String tipo;
    private int quantidade;

    private BigDecimal valorUnitario;

    private String observacao;

    private LocalDateTime criadoEm;

    // CONSTRUTOR VAZIO

    public MovimentacaoEstoque() {

    }

    // CONSTRUTOR ORIGINAL

    public MovimentacaoEstoque(
            int idProduto,
            String tipo,
            int quantidade,
            BigDecimal valorUnitario,
            String observacao) {

        this.idProduto = idProduto;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.observacao = observacao;
    }

    // CONSTRUTOR COM USUÁRIO RESPONSÁVEL

    public MovimentacaoEstoque(
            int idProduto,
            String tipo,
            int quantidade,
            BigDecimal valorUnitario,
            String observacao,
            Integer idUsuario) {

        this(idProduto, tipo, quantidade,
                valorUnitario, observacao);

        this.idUsuario = idUsuario;
    }

    // ID DA MOVIMENTAÇÃO

    public int getIdMovimentacao() {
        return idMovimentacao;
    }

    public void setIdMovimentacao(int idMovimentacao) {
        this.idMovimentacao = idMovimentacao;
    }

    // ID DO PRODUTO

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    // NOME DO PRODUTO

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    // ID DO USUÁRIO RESPONSÁVEL

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    // NOME DO USUÁRIO RESPONSÁVEL

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    // TIPO DA MOVIMENTAÇÃO

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // QUANTIDADE

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    // VALOR UNITÁRIO

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    // CALCULAR VALOR TOTAL

    public BigDecimal getValorTotal() {

        if (valorUnitario == null) {
            return BigDecimal.ZERO;
        }

        return valorUnitario.multiply(
                BigDecimal.valueOf(quantidade));
    }

    // OBSERVAÇÃO

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    // DATA E HORÁRIO DA MOVIMENTAÇÃO

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
