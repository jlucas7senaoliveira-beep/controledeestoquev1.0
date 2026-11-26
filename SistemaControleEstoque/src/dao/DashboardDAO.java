
package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import model.Produto;

public class DashboardDAO {

    // Conta todos os produtos ativos.
    public int contarProdutosAtivos() {

        String sql =
                "SELECT COUNT(*) AS total " +
                "FROM produtos " +
                "WHERE ativo = TRUE";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException erro) {
            System.out.println(
                    "Erro ao contar produtos: " +
                    erro.getMessage());
        }

        return 0;
    }

    // Conta os clientes ativos.
    public int contarClientesAtivos() {

        String sql =
                "SELECT COUNT(*) AS total " +
                "FROM clientes " +
                "WHERE ativo = TRUE";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException erro) {
            System.out.println(
                    "Erro ao contar clientes: " +
                    erro.getMessage());
        }

        return 0;
    }

    // Conta produtos com estoque baixo.
    public int contarProdutosEstoqueBaixo() {

        String sql =
                "SELECT COUNT(*) AS total " +
                "FROM produtos " +
                "WHERE ativo = TRUE " +
                "AND quantidade_estoque <= estoque_minimo";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException erro) {
            System.out.println(
                    "Erro ao contar produtos com estoque baixo: " +
                    erro.getMessage());
        }

        return 0;
    }

    // Calcula o valor total do estoque.
    public BigDecimal calcularValorTotalEstoque() {

        String sql =
                "SELECT SUM(quantidade_estoque * valor_venda) AS total " +
                "FROM produtos " +
                "WHERE ativo = TRUE";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            if (rs.next()) {

                BigDecimal total =
                        rs.getBigDecimal("total");

                if (total != null) {
                    return total;
                }
            }

        } catch (SQLException erro) {
            System.out.println(
                    "Erro ao calcular valor total do estoque: " +
                    erro.getMessage());
        }

        return BigDecimal.ZERO;
    }

    // Lista produtos com estoque baixo.
    public List<Produto> listarProdutosEstoqueBaixo() {

        List<Produto> produtos = new ArrayList<>();

        String sql =
                "SELECT id_produto, nome, " +
                "quantidade_estoque, estoque_minimo, valor_venda " +
                "FROM produtos " +
                "WHERE ativo = TRUE " +
                "AND quantidade_estoque <= estoque_minimo " +
                "ORDER BY nome";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                Produto produto = new Produto();

                produto.setIdProduto(
                        rs.getInt("id_produto"));

                produto.setNome(
                        rs.getString("nome"));

                produto.setQuantidadeEstoque(
                        rs.getInt("quantidade_estoque"));

                produto.setEstoqueMinimo(
                        rs.getInt("estoque_minimo"));

                produto.setValorVenda(
                        rs.getBigDecimal("valor_venda"));

                produtos.add(produto);
            }

        } catch (SQLException erro) {
            System.out.println(
                    "Erro ao listar produtos com estoque baixo: " +
                    erro.getMessage());
        }

        return produtos;
    }

    // Conta produtos que vencem entre hoje e os próximos 7 dias.
    public int contarProdutosAVencer() {

        String sql =
                "SELECT COUNT(*) AS total " +
                "FROM produtos " +
                "WHERE ativo = TRUE " +
                "AND quantidade_estoque > 0 " +
                "AND data_validade IS NOT NULL " +
                "AND data_validade BETWEEN CURDATE() " +
                "AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException erro) {
            System.out.println(
                    "Erro ao contar produtos a vencer: " +
                    erro.getMessage());
        }

        return 0;
    }

    // Lista produtos próximos do vencimento.
    public List<Produto> listarProdutosAVencer() {

        List<Produto> produtos = new ArrayList<>();

        String sql =
                "SELECT id_produto, nome, quantidade_estoque, " +
                "data_validade " +
                "FROM produtos " +
                "WHERE ativo = TRUE " +
                "AND quantidade_estoque > 0 " +
                "AND data_validade IS NOT NULL " +
                "AND data_validade BETWEEN CURDATE() " +
                "AND DATE_ADD(CURDATE(), INTERVAL 7 DAY) " +
                "ORDER BY data_validade ASC, nome ASC";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                Produto produto = new Produto();

                produto.setIdProduto(
                        rs.getInt("id_produto"));

                produto.setNome(
                        rs.getString("nome"));

                produto.setQuantidadeEstoque(
                        rs.getInt("quantidade_estoque"));

                Date validade =
                        rs.getDate("data_validade");

                if (validade != null) {
                    produto.setDataValidade(
                            validade.toLocalDate());
                }

                produtos.add(produto);
            }

        } catch (SQLException erro) {
            System.out.println(
                    "Erro ao listar produtos a vencer: " +
                    erro.getMessage());
        }

        return produtos;
    }

    // Conta produtos cuja validade já passou.
    public int contarProdutosVencidos() {

        String sql =
                "SELECT COUNT(*) AS total " +
                "FROM produtos " +
                "WHERE ativo = TRUE " +
                "AND quantidade_estoque > 0 " +
                "AND data_validade IS NOT NULL " +
                "AND data_validade < CURDATE()";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException erro) {
            System.out.println(
                    "Erro ao contar produtos vencidos: " +
                    erro.getMessage());
        }

        return 0;
    }

    // Lista produtos que já estão vencidos.
    public List<Produto> listarProdutosVencidos() {

        List<Produto> produtos = new ArrayList<>();

        String sql =
                "SELECT id_produto, nome, quantidade_estoque, " +
                "data_validade " +
                "FROM produtos " +
                "WHERE ativo = TRUE " +
                "AND quantidade_estoque > 0 " +
                "AND data_validade IS NOT NULL " +
                "AND data_validade < CURDATE() " +
                "ORDER BY data_validade ASC, nome ASC";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                Produto produto = new Produto();

                produto.setIdProduto(
                        rs.getInt("id_produto"));

                produto.setNome(
                        rs.getString("nome"));

                produto.setQuantidadeEstoque(
                        rs.getInt("quantidade_estoque"));

                Date validade =
                        rs.getDate("data_validade");

                if (validade != null) {
                    produto.setDataValidade(
                            validade.toLocalDate());
                }

                produtos.add(produto);
            }

        } catch (SQLException erro) {
            System.out.println(
                    "Erro ao listar produtos vencidos: " +
                    erro.getMessage());
        }

        return produtos;
    }
}
