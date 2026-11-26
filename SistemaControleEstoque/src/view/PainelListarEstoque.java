
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.ProdutoDAO;
import model.Produto;
import util.Formatador;

public class PainelListarEstoque extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color COR_FUNDO =
            new Color(237, 241, 246);

    private static final Color COR_AZUL =
            new Color(25, 57, 94);

    private static final Color COR_CINZA =
            new Color(100, 116, 139);

    private static final Color COR_TEXTO =
            new Color(51, 65, 85);

    private static final Color COR_BORDA =
            new Color(220, 227, 235);

    private static final Color COR_CAMPO =
            new Color(248, 250, 252);

    private static final Color COR_VERDE =
            new Color(40, 122, 80);

    private static final Color COR_VERMELHO =
            new Color(183, 60, 60);

    private JTextField txtPesquisa;
    private JTable tabelaEstoque;
    private DefaultTableModel modeloTabela;

    private JLabel lblTotalProdutos;
    private JLabel lblEstoqueBaixo;

    public PainelListarEstoque() {

        setLayout(new BorderLayout(0, 22));
        setBackground(COR_FUNDO);

        setBorder(
                BorderFactory.createEmptyBorder(
                        28, 25, 25, 25));

        criarCabecalho();
        criarConteudo();
        carregarEstoque();
    }

    // CABEÇALHO

    private void criarCabecalho() {

        JPanel cabecalho = new JPanel();

        cabecalho.setOpaque(false);

        cabecalho.setLayout(
                new BoxLayout(cabecalho, BoxLayout.Y_AXIS));

        JLabel lblTitulo =
                new JLabel("Listagem de Estoque");

        lblTitulo.setFont(
                new Font("SansSerif", Font.BOLD, 25));

        lblTitulo.setForeground(COR_AZUL);

        lblTitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel(
                "Consulte as quantidades e acompanhe a situação do estoque");

        lblSubtitulo.setFont(
                new Font("SansSerif", Font.PLAIN, 13));

        lblSubtitulo.setForeground(COR_CINZA);

        lblSubtitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        cabecalho.add(lblTitulo);
        cabecalho.add(Box.createVerticalStrut(6));
        cabecalho.add(lblSubtitulo);

        add(cabecalho, BorderLayout.NORTH);
    }

    // CONTEÚDO PRINCIPAL

    private void criarConteudo() {

        PainelArredondado painel =
                new PainelArredondado(Color.WHITE, 18);

        painel.setLayout(new BorderLayout(0, 20));

        painel.setBorder(
                BorderFactory.createEmptyBorder(
                        24, 24, 20, 24));

        painel.add(
                criarBarraFerramentas(),
                BorderLayout.NORTH);

        criarTabela();

        JScrollPane scrollTabela =
                new JScrollPane(tabelaEstoque);

        scrollTabela.setBorder(
                BorderFactory.createLineBorder(
                        COR_BORDA));

        scrollTabela.getViewport()
                .setBackground(Color.WHITE);

        scrollTabela.getVerticalScrollBar()
                .setUnitIncrement(16);

        painel.add(
                scrollTabela, BorderLayout.CENTER);

        painel.add(
                criarRodape(), BorderLayout.SOUTH);

        add(painel, BorderLayout.CENTER);
    }

    // PESQUISA E BOTÕES

    private JPanel criarBarraFerramentas() {

        JPanel painel =
                new JPanel(new BorderLayout(18, 0));

        painel.setOpaque(false);

        JPanel painelPesquisa =
                new JPanel(new BorderLayout(0, 8));

        painelPesquisa.setOpaque(false);

        JLabel lblPesquisar =
                new JLabel("Pesquisar produto");

        lblPesquisar.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        lblPesquisar.setForeground(COR_CINZA);

        txtPesquisa = new JTextField();

        txtPesquisa.setFont(
                new Font("SansSerif", Font.PLAIN, 13));

        txtPesquisa.setForeground(COR_AZUL);
        txtPesquisa.setBackground(COR_CAMPO);

        txtPesquisa.setPreferredSize(
                new Dimension(330, 40));

        txtPesquisa.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                COR_BORDA),

                        BorderFactory.createEmptyBorder(
                                5, 12, 5, 12)));

        txtPesquisa.setToolTipText(
                "Digite o nome do produto");

        txtPesquisa.addActionListener(
                e -> pesquisarEstoque());

        painelPesquisa.add(
                lblPesquisar, BorderLayout.NORTH);

        painelPesquisa.add(
                txtPesquisa, BorderLayout.CENTER);

        painel.add(
                painelPesquisa, BorderLayout.CENTER);

        JPanel painelBotoes =
                new JPanel(new FlowLayout(
                        FlowLayout.RIGHT, 10, 0));

        painelBotoes.setOpaque(false);

        JButton btnPesquisar =
                criarBotao("Pesquisar", true);

        btnPesquisar.addActionListener(
                e -> pesquisarEstoque());

        JButton btnAtualizar =
                criarBotao("Atualizar", false);

        btnAtualizar.addActionListener(
                e -> carregarEstoque());

        painelBotoes.add(btnPesquisar);
        painelBotoes.add(btnAtualizar);

        JPanel alinhadorBotoes =
                new JPanel(new BorderLayout());

        alinhadorBotoes.setOpaque(false);

        alinhadorBotoes.add(
                painelBotoes, BorderLayout.SOUTH);

        painel.add(
                alinhadorBotoes, BorderLayout.EAST);

        return painel;
    }

    // PADRÃO VISUAL DOS BOTÕES

    private JButton criarBotao(
            String texto, boolean principal) {

        JButton botao = new JButton(texto);

        botao.setFont(
                new Font("SansSerif", Font.BOLD, 12));

        botao.setPreferredSize(
                new Dimension(115, 40));

        botao.setFocusPainted(false);
        botao.setOpaque(true);
        botao.setContentAreaFilled(true);

        botao.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR));

        if (principal) {

            botao.setBackground(COR_AZUL);
            botao.setForeground(Color.WHITE);

            botao.setBorder(
                    BorderFactory.createLineBorder(
                            COR_AZUL));

        } else {

            botao.setBackground(
                    new Color(241, 245, 249));

            botao.setForeground(COR_AZUL);

            botao.setBorder(
                    BorderFactory.createLineBorder(
                            COR_BORDA));
        }

        return botao;
    }

    // CRIAÇÃO DA TABELA

    private void criarTabela() {

        tabelaEstoque = new JTable();

        tabelaEstoque.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        tabelaEstoque.setRowHeight(42);

        tabelaEstoque.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        tabelaEstoque.setForeground(COR_TEXTO);
        tabelaEstoque.setBackground(Color.WHITE);

        tabelaEstoque.setGridColor(
                new Color(235, 239, 244));

        tabelaEstoque.setShowVerticalLines(false);
        tabelaEstoque.setShowHorizontalLines(true);

        tabelaEstoque.setSelectionBackground(
                new Color(224, 235, 247));

        tabelaEstoque.setSelectionForeground(
                COR_AZUL);

        tabelaEstoque.setFillsViewportHeight(true);

        tabelaEstoque.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS);

        tabelaEstoque.getTableHeader()
                .setReorderingAllowed(false);

        tabelaEstoque.getTableHeader()
                .setBackground(
                        new Color(237, 242, 248));

        tabelaEstoque.getTableHeader()
                .setForeground(COR_AZUL);

        tabelaEstoque.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12));

        tabelaEstoque.getTableHeader()
                .setPreferredSize(
                        new Dimension(0, 42));

        configurarTabela();

        tabelaEstoque.getTableHeader()
                .setDefaultRenderer(
                        new RenderizadorCabecalho());
    }

    // CONFIGURAÇÃO DAS COLUNAS

    private void configurarTabela() {

        modeloTabela = new DefaultTableModel() {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(
                    int row, int column) {

                return false;
            }
        };

        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Produto");
        modeloTabela.addColumn("Categoria");
        modeloTabela.addColumn("Fornecedor");
        modeloTabela.addColumn("Valor Venda");
        modeloTabela.addColumn("Estoque Atual");
        modeloTabela.addColumn("Estoque Mínimo");
        modeloTabela.addColumn("Situação");

        tabelaEstoque.setModel(modeloTabela);

        int[] larguras = {
                45,
                220,
                140,
                170,
                110,
                110,
                120,
                130
        };

        int[] largurasMinimas = {
                30,
                100,
                75,
                90,
                80,
                90,
                100,
                100
        };

        RenderizadorTabela renderizador =
                new RenderizadorTabela();

        for (int i = 0; i < larguras.length; i++) {

            tabelaEstoque.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(larguras[i]);

            tabelaEstoque.getColumnModel()
                    .getColumn(i)
                    .setMinWidth(largurasMinimas[i]);

            tabelaEstoque.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(renderizador);
        }
    }

    // RODAPÉ DA TABELA

    private JPanel criarRodape() {

        JPanel rodape =
                new JPanel(new BorderLayout(15, 0));

        rodape.setOpaque(false);

        lblTotalProdutos =
                new JLabel("Total de produtos exibidos: 0");

        lblTotalProdutos.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        lblTotalProdutos.setForeground(COR_CINZA);

        lblEstoqueBaixo =
                new JLabel("Produtos com estoque baixo: 0");

        lblEstoqueBaixo.setFont(
                new Font("SansSerif", Font.BOLD, 12));

        lblEstoqueBaixo.setForeground(COR_VERMELHO);

        rodape.add(
                lblTotalProdutos, BorderLayout.WEST);

        rodape.add(
                lblEstoqueBaixo, BorderLayout.EAST);

        return rodape;
    }

    // CARREGAR ESTOQUE

    private void carregarEstoque() {

        modeloTabela.setRowCount(0);

        ProdutoDAO produtoDAO =
                new ProdutoDAO();

        List<Produto> produtos =
                produtoDAO.listarEstoque();

        preencherTabela(produtos);

        txtPesquisa.setText("");
        txtPesquisa.requestFocusInWindow();
    }

    // PESQUISAR PRODUTOS

    private void pesquisarEstoque() {

        String pesquisa =
                txtPesquisa.getText().trim();

        if (pesquisa.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Digite o nome do produto para pesquisar.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE);

            txtPesquisa.requestFocusInWindow();

            return;
        }

        modeloTabela.setRowCount(0);

        ProdutoDAO produtoDAO =
                new ProdutoDAO();

        List<Produto> produtos =
                produtoDAO.pesquisarEstoquePorNome(pesquisa);

        preencherTabela(produtos);

        if (produtos.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Nenhum produto encontrado.",
                    "Resultado da pesquisa",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // PREENCHER TABELA

    private void preencherTabela(List<Produto> produtos) {

        int totalEstoqueBaixo = 0;

        for (Produto produto : produtos) {

            String situacao = "Normal";

            if (produto.getQuantidadeEstoque()
                    <= produto.getEstoqueMinimo()) {

                situacao = "Estoque Baixo";

                totalEstoqueBaixo++;
            }

            modeloTabela.addRow(new Object[] {

                    produto.getIdProduto(),

                    produto.getNome(),

                    produto.getNomeCategoria(),

                    produto.getNomeFornecedor(),

                    Formatador.moeda(
                            produto.getValorVenda()),

                    produto.getQuantidadeEstoque(),

                    produto.getEstoqueMinimo(),

                    situacao
            });
        }

        lblTotalProdutos.setText(
                "Total de produtos exibidos: " +
                        modeloTabela.getRowCount());

        lblEstoqueBaixo.setText(
                "Produtos com estoque baixo: " +
                        totalEstoqueBaixo);

        if (totalEstoqueBaixo > 0) {

            lblEstoqueBaixo.setForeground(
                    COR_VERMELHO);

        } else {

            lblEstoqueBaixo.setForeground(
                    COR_VERDE);
        }
    }

    // ESTILO DO CABEÇALHO DA TABELA

    private static class RenderizadorCabecalho
            extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {

            super.getTableCellRendererComponent(
                    table, value, isSelected,
                    hasFocus, row, column);

            setOpaque(true);

            setBackground(
                    new Color(237, 242, 248));

            setForeground(COR_AZUL);

            setFont(new Font(
                    "SansSerif", Font.BOLD, 12));

            setHorizontalAlignment(
                    SwingConstants.CENTER);

            setBorder(
                    BorderFactory.createMatteBorder(
                            0, 0, 1, 0, COR_BORDA));

            return this;
        }
    }

    // ESTILO DAS LINHAS DA TABELA

    private static class RenderizadorTabela
            extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {

            super.getTableCellRendererComponent(
                    table, value, isSelected,
                    hasFocus, row, column);

            setBorder(
                    BorderFactory.createEmptyBorder(
                            0, 8, 0, 8));

            setFont(new Font(
                    "SansSerif", Font.PLAIN, 12));

            int colunaModelo =
                    table.convertColumnIndexToModel(column);

            int linhaModelo =
                    table.convertRowIndexToModel(row);

            setHorizontalAlignment(
                    SwingConstants.LEFT);

            // CENTRALIZAR COLUNAS NUMÉRICAS E SITUAÇÃO

            if (colunaModelo == 0 ||
                    colunaModelo >= 4) {

                setHorizontalAlignment(
                        SwingConstants.CENTER);
            }

            // DESTACAR NOME DO PRODUTO

            if (colunaModelo == 1) {

                setFont(new Font(
                        "SansSerif", Font.BOLD, 12));
            }

            // FORNECEDOR NÃO INFORMADO

            boolean semFornecedor =
                    colunaModelo == 3 &&
                    (value == null ||
                    value.toString().trim().isEmpty());

            if (semFornecedor) {

                setText("Sem fornecedor");
            }

            // LINHA SELECIONADA

            if (isSelected) {

                setBackground(
                        table.getSelectionBackground());

                setForeground(
                        table.getSelectionForeground());

            } else {

                // ALTERNAR CORES DAS LINHAS

                if (row % 2 == 0) {

                    setBackground(Color.WHITE);

                } else {

                    setBackground(
                            new Color(248, 250, 252));
                }

                setForeground(COR_TEXTO);
            }

            // COR DO NOME DO PRODUTO

            if (colunaModelo == 1) {

                setForeground(COR_AZUL);
            }

            if (semFornecedor && !isSelected) {

                setForeground(COR_CINZA);
            }

            // VERIFICAR SITUAÇÃO DO ESTOQUE

            Object situacao =
                    table.getModel().getValueAt(
                            linhaModelo, 7);

            boolean estoqueBaixo =
                    "Estoque Baixo".equals(situacao);

            // DESTACAR QUANTIDADE ATUAL

            if (colunaModelo == 5) {

                setFont(new Font(
                        "SansSerif", Font.BOLD, 12));

                if (estoqueBaixo) {

                    setForeground(COR_VERMELHO);

                } else {

                    setForeground(COR_AZUL);
                }
            }

            // DESTACAR SITUAÇÃO

            if (colunaModelo == 7) {

                setFont(new Font(
                        "SansSerif", Font.BOLD, 12));

                if (estoqueBaixo) {

                    setForeground(COR_VERMELHO);

                    if (!isSelected) {

                        setBackground(
                                new Color(252, 239, 239));
                    }

                } else {

                    setForeground(COR_VERDE);

                    if (!isSelected) {

                        setBackground(
                                new Color(234, 246, 239));
                    }
                }
            }

            return this;
        }
    }

    // PAINEL COM CANTOS ARREDONDADOS

    private static class PainelArredondado
            extends JPanel {

        private static final long serialVersionUID = 1L;

        private final Color corFundo;
        private final int raio;

        public PainelArredondado(
                Color corFundo, int raio) {

            this.corFundo = corFundo;
            this.raio = raio;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(corFundo);

            g2.fillRoundRect(
                    0, 0,
                    getWidth() - 1,
                    getHeight() - 1,
                    raio, raio);

            g2.dispose();

            super.paintComponent(g);
        }
    }
}
