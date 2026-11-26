
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.MovimentacaoEstoque;

public class MovimentacaoEstoqueDAO {

    // REGISTRAR ENTRADA DE ESTOQUE

    public boolean registrarEntrada(
            MovimentacaoEstoque movimentacao) {

        return registrarMovimentacao(
                movimentacao, "ENTRADA");
    }

    // REGISTRAR SAÍDA DE ESTOQUE

    public boolean registrarSaida(
            MovimentacaoEstoque movimentacao) {

        return registrarMovimentacao(
                movimentacao, "SAIDA");
    }

    // REGISTRAR MOVIMENTAÇÃO

    private boolean registrarMovimentacao(
            MovimentacaoEstoque movimentacao,
            String tipo) {

        if (movimentacao == null
                || movimentacao.getIdProduto() <= 0
                || movimentacao.getQuantidade() <= 0
                || movimentacao.getValorUnitario() == null
                || movimentacao.getValorUnitario().signum() < 0) {

            return false;
        }

        String sqlConsultaEstoque =
                "SELECT quantidade_estoque FROM produtos "
                + "WHERE id_produto = ? FOR UPDATE";

        String sqlMovimentacao =
                "INSERT INTO movimentacoes_estoque "
                + "(id_produto, tipo, quantidade, "
                + "valor_unitario, observacao, id_usuario) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        String sqlAtualizaProduto;

        if ("ENTRADA".equals(tipo)) {

            sqlAtualizaProduto =
                    "UPDATE produtos "
                    + "SET quantidade_estoque = "
                    + "quantidade_estoque + ? "
                    + "WHERE id_produto = ?";

        } else {

            sqlAtualizaProduto =
                    "UPDATE produtos "
                    + "SET quantidade_estoque = "
                    + "quantidade_estoque - ? "
                    + "WHERE id_produto = ?";
        }

        Connection con = null;

        try {

            con = ConexaoDAO.conectar();

            con.setAutoCommit(false);

            // CONSULTAR E BLOQUEAR O PRODUTO

            int estoqueAtual;

            try (PreparedStatement stmtConsulta =
                    con.prepareStatement(sqlConsultaEstoque)) {

                stmtConsulta.setInt(
                        1, movimentacao.getIdProduto());

                try (ResultSet rs =
                        stmtConsulta.executeQuery()) {

                    if (!rs.next()) {

                        con.rollback();

                        return false;
                    }

                    estoqueAtual =
                            rs.getInt("quantidade_estoque");
                }
            }

            // IMPEDIR SAÍDA SUPERIOR AO ESTOQUE

            if ("SAIDA".equals(tipo)
                    && movimentacao.getQuantidade()
                    > estoqueAtual) {

                con.rollback();

                return false;
            }

            // REGISTRAR HISTÓRICO DA MOVIMENTAÇÃO

            try (PreparedStatement stmtMov =
                    con.prepareStatement(sqlMovimentacao)) {

                stmtMov.setInt(
                        1, movimentacao.getIdProduto());

                stmtMov.setString(
                        2, tipo);

                stmtMov.setInt(
                        3, movimentacao.getQuantidade());

                stmtMov.setBigDecimal(
                        4, movimentacao.getValorUnitario());

                stmtMov.setString(
                        5, movimentacao.getObservacao());

                // USUÁRIO RESPONSÁVEL

                if (movimentacao.getIdUsuario() != null) {

                    stmtMov.setInt(
                            6, movimentacao.getIdUsuario());

                } else {

                    stmtMov.setNull(
                            6, Types.INTEGER);
                }

                stmtMov.executeUpdate();
            }

            // ATUALIZAR QUANTIDADE DO PRODUTO

            try (PreparedStatement stmtProd =
                    con.prepareStatement(sqlAtualizaProduto)) {

                stmtProd.setInt(
                        1, movimentacao.getQuantidade());

                stmtProd.setInt(
                        2, movimentacao.getIdProduto());

                int linhasAfetadas =
                        stmtProd.executeUpdate();

                if (linhasAfetadas != 1) {

                    con.rollback();

                    return false;
                }
            }

            // CONFIRMAR TRANSAÇÃO

            con.commit();

            return true;

        } catch (SQLException erro) {

            if (con != null) {

                try {

                    con.rollback();

                } catch (SQLException e) {

                    System.out.println(
                            "Erro ao desfazer movimentação: "
                            + e.getMessage());
                }
            }

            System.out.println(
                    "Erro ao registrar movimentação: "
                    + erro.getMessage());

            return false;

        } finally {

            if (con != null) {

                try {

                    con.close();

                } catch (SQLException e) {

                    System.out.println(
                            "Erro ao fechar conexão: "
                            + e.getMessage());
                }
            }
        }
    }

    // LISTAR TODAS AS MOVIMENTAÇÕES

    public List<MovimentacaoEstoque> listarMovimentacoes() {

        return pesquisarMovimentacoes(
                null, null, null, null);
    }

    // PESQUISAR MOVIMENTAÇÕES COM FILTROS

    public List<MovimentacaoEstoque> pesquisarMovimentacoes(
            String nomeProduto,
            String tipo,
            LocalDate dataInicial,
            LocalDate dataFinal) {

        List<MovimentacaoEstoque> movimentacoes =
                new ArrayList<>();

        StringBuilder sql = new StringBuilder();

        sql.append(
                "SELECT m.id_movimentacao, "
                + "m.id_produto, "
                + "p.nome AS nome_produto, "
                + "m.tipo, "
                + "m.quantidade, "
                + "m.valor_unitario, "
                + "m.observacao, "
                + "m.criado_em, "
                + "m.id_usuario, "
                + "u.nome AS nome_usuario "
                + "FROM movimentacoes_estoque m "
                + "INNER JOIN produtos p "
                + "ON m.id_produto = p.id_produto "
                + "LEFT JOIN usuarios u "
                + "ON m.id_usuario = u.id_usuario "
                + "WHERE 1 = 1 ");

        List<Object> parametros =
                new ArrayList<>();

        // FILTRO POR PRODUTO

        if (nomeProduto != null
                && !nomeProduto.trim().isEmpty()) {

            sql.append("AND p.nome LIKE ? ");

            parametros.add(
                    "%" + nomeProduto.trim() + "%");
        }

        // FILTRO POR TIPO

        if (tipo != null
                && !tipo.trim().isEmpty()
                && !"TODOS".equalsIgnoreCase(tipo)) {

            String tipoFiltro = tipo.trim();

            if ("SAÍDA".equalsIgnoreCase(tipoFiltro)) {

                tipoFiltro = "SAIDA";
            }

            sql.append("AND m.tipo = ? ");

            parametros.add(
                    tipoFiltro.toUpperCase(
                            java.util.Locale.ROOT));
        }

        // FILTRO POR DATA INICIAL

        if (dataInicial != null) {

            sql.append("AND m.criado_em >= ? ");

            parametros.add(
                    Timestamp.valueOf(
                            dataInicial.atStartOfDay()));
        }

        // FILTRO POR DATA FINAL

        if (dataFinal != null) {

            sql.append("AND m.criado_em < ? ");

            parametros.add(
                    Timestamp.valueOf(
                            dataFinal.plusDays(1)
                                    .atStartOfDay()));
        }

        // MOVIMENTAÇÕES MAIS RECENTES PRIMEIRO

        sql.append(
                "ORDER BY m.criado_em DESC, "
                + "m.id_movimentacao DESC");

        try (Connection con =
                    ConexaoDAO.conectar();

             PreparedStatement stmt =
                    con.prepareStatement(sql.toString())) {

            // PREENCHER PARÂMETROS DOS FILTROS

            for (int i = 0; i < parametros.size(); i++) {

                stmt.setObject(
                        i + 1, parametros.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    MovimentacaoEstoque movimentacao =
                            mapearMovimentacao(rs);

                    movimentacoes.add(movimentacao);
                }
            }

        } catch (SQLException erro) {

            System.out.println(
                    "Erro ao consultar movimentações: "
                    + erro.getMessage());

            throw new IllegalStateException(
                    "Não foi possível consultar o histórico.",
                    erro);
        }

        return movimentacoes;
    }

    // BUSCAR MOVIMENTAÇÃO PELO ID

    public MovimentacaoEstoque buscarPorId(
            int idMovimentacao) {

        String sql =
                "SELECT m.id_movimentacao, "
                + "m.id_produto, "
                + "p.nome AS nome_produto, "
                + "m.tipo, "
                + "m.quantidade, "
                + "m.valor_unitario, "
                + "m.observacao, "
                + "m.criado_em, "
                + "m.id_usuario, "
                + "u.nome AS nome_usuario "
                + "FROM movimentacoes_estoque m "
                + "INNER JOIN produtos p "
                + "ON m.id_produto = p.id_produto "
                + "LEFT JOIN usuarios u "
                + "ON m.id_usuario = u.id_usuario "
                + "WHERE m.id_movimentacao = ?";

        try (Connection con =
                    ConexaoDAO.conectar();

             PreparedStatement stmt =
                    con.prepareStatement(sql)) {

            stmt.setInt(1, idMovimentacao);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    return mapearMovimentacao(rs);
                }
            }

        } catch (SQLException erro) {

            System.out.println(
                    "Erro ao buscar movimentação: "
                    + erro.getMessage());

            throw new IllegalStateException(
                    "Não foi possível buscar a movimentação.",
                    erro);
        }

        return null;
    }

    // CONVERTER RESULTADO DO BANCO EM OBJETO

    private MovimentacaoEstoque mapearMovimentacao(
            ResultSet rs) throws SQLException {

        MovimentacaoEstoque movimentacao =
                new MovimentacaoEstoque();

        movimentacao.setIdMovimentacao(
                rs.getInt("id_movimentacao"));

        movimentacao.setIdProduto(
                rs.getInt("id_produto"));

        movimentacao.setNomeProduto(
                rs.getString("nome_produto"));

        movimentacao.setTipo(
                rs.getString("tipo"));

        movimentacao.setQuantidade(
                rs.getInt("quantidade"));

        movimentacao.setValorUnitario(
                rs.getBigDecimal("valor_unitario"));

        movimentacao.setObservacao(
                rs.getString("observacao"));

        // IDENTIFICAÇÃO DO USUÁRIO

        int idUsuario = rs.getInt("id_usuario");

        if (rs.wasNull()) {

            movimentacao.setIdUsuario(null);

        } else {

            movimentacao.setIdUsuario(idUsuario);
        }

        String nomeUsuario =
                rs.getString("nome_usuario");

        movimentacao.setNomeUsuario(
                nomeUsuario == null
                        ? "Não identificado"
                        : nomeUsuario);

        // DATA E HORÁRIO

        Timestamp criadoEm =
                rs.getTimestamp("criado_em");

        if (criadoEm != null) {

            movimentacao.setCriadoEm(
                    criadoEm.toLocalDateTime());
        }

        return movimentacao;
    }
}
