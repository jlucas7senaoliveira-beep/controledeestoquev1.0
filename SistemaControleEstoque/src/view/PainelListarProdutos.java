
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

public class PainelListarProdutos extends JPanel {

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

    private static final Color COR_VERMELHO =
            new Color(183, 60, 60);

    private static final Color COR_LARANJA =
            new Color(168, 107, 22);

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JTextField txtPesquisa;
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;
    private JLabel lblTotalProdutos;

    private TelaPrincipal telaPrincipal;

    public PainelListarProdutos() {
        this(null);
    }

    public PainelListarProdutos(TelaPrincipal telaPrincipal) {

        this.telaPrincipal = telaPrincipal;

        setLayout(new BorderLayout(0, 22));
        setBackground(COR_FUNDO);

        setBorder(BorderFactory.createEmptyBorder(
                28, 25, 25, 25));

        criarCabecalho();
        criarConteudo();
        carregarProdutos();
    }

    // CABEÇALHO

    private void criarCabecalho() {

        JPanel cabecalho = new JPanel();

        cabecalho.setOpaque(false);

        cabecalho.setLayout(
                new BoxLayout(cabecalho, BoxLayout.Y_AXIS));

        JLabel lblTitulo =
                new JLabel("Listagem de Produtos");

        lblTitulo.setFont(
                new Font("SansSerif", Font.BOLD, 25));

        lblTitulo.setForeground(COR_AZUL);

        lblTitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel(
                "Consulte e gerencie os produtos cadastrados");

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
                new JScrollPane(tabelaProdutos);

        scrollTabela.setBorder(
                BorderFactory.createLineBorder(
                        COR_BORDA));

        scrollTabela.getViewport()
                .setBackground(Color.WHITE);

        scrollTabela.getVerticalScrollBar()
                .setUnitIncrement(16);

        painel.add(
                scrollTabela, BorderLayout.CENTER);

        JPanel rodape =
                new JPanel(new BorderLayout());

        rodape.setOpaque(false);

        lblTotalProdutos =
                new JLabel("Total de produtos: 0");

        lblTotalProdutos.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        lblTotalProdutos.setForeground(COR_CINZA);

        rodape.add(
                lblTotalProdutos, BorderLayout.WEST);

        painel.add(rodape, BorderLayout.SOUTH);

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
                new Dimension(280, 40));

        txtPesquisa.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COR_BORDA),
                        BorderFactory.createEmptyBorder(
                                5, 12, 5, 12)));

        txtPesquisa.setToolTipText(
                "Digite o nome do produto");

        txtPesquisa.addActionListener(
                e -> pesquisarProdutos());

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
                criarBotao("Pesquisar", true, false);

        btnPesquisar.addActionListener(
                e -> pesquisarProdutos());

        JButton btnAtualizar =
                criarBotao("Atualizar", false, false);

        btnAtualizar.addActionListener(
                e -> carregarProdutos());

        JButton btnEditar =
                criarBotao("Editar", false, false);

        btnEditar.addActionListener(
                e -> editarProduto());

        JButton btnExcluir =
                criarBotao("Excluir", false, true);

        btnExcluir.addActionListener(
                e -> excluirProduto());

        painelBotoes.add(btnPesquisar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);

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
            String texto,
            boolean principal,
            boolean exclusao) {

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

        } else if (exclusao) {

            botao.setBackground(
                    new Color(252, 239, 239));

            botao.setForeground(COR_VERMELHO);

            botao.setBorder(
                    BorderFactory.createLineBorder(
                            new Color(235, 210, 210)));

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

        tabelaProdutos = new JTable();

        tabelaProdutos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        tabelaProdutos.setRowHeight(42);

        tabelaProdutos.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        tabelaProdutos.setForeground(COR_TEXTO);
        tabelaProdutos.setBackground(Color.WHITE);

        tabelaProdutos.setGridColor(
                new Color(235, 239, 244));

        tabelaProdutos.setShowVerticalLines(false);
        tabelaProdutos.setShowHorizontalLines(true);

        tabelaProdutos.setSelectionBackground(
                new Color(224, 235, 247));

        tabelaProdutos.setSelectionForeground(
                COR_AZUL);

        tabelaProdutos.setFillsViewportHeight(true);

        tabelaProdutos.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS);

        tabelaProdutos.getTableHeader()
                .setReorderingAllowed(false);

        tabelaProdutos.getTableHeader()
                .setBackground(
                        new Color(237, 242, 248));

        tabelaProdutos.getTableHeader()
                .setForeground(COR_AZUL);

        tabelaProdutos.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12));

        tabelaProdutos.getTableHeader()
                .setPreferredSize(
                        new Dimension(0, 42));

        configurarTabela();

        tabelaProdutos.getTableHeader()
                .setDefaultRenderer(
                        new RenderizadorCabecalho());
    }

    // COLUNAS DA TABELA

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
        modeloTabela.addColumn("Valor Custo");
        modeloTabela.addColumn("Valor Venda");
        modeloTabela.addColumn("Estoque");
        modeloTabela.addColumn("Est. Mínimo");
        modeloTabela.addColumn("Validade");

        tabelaProdutos.setModel(modeloTabela);

        int[] larguras = {
                45, 220, 140, 190, 115,
                115, 85, 105, 135
        };

        int[] largurasMinimas = {
                35, 120, 85, 100, 85,
                85, 65, 80, 100
        };

        RenderizadorTabela renderizador =
                new RenderizadorTabela();

        for (int i = 0; i < larguras.length; i++) {

            tabelaProdutos.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(larguras[i]);

            tabelaProdutos.getColumnModel()
                    .getColumn(i)
                    .setMinWidth(largurasMinimas[i]);

            tabelaProdutos.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(renderizador);
        }
    }

    // CARREGAR PRODUTOS

    private void carregarProdutos() {

        modeloTabela.setRowCount(0);

        ProdutoDAO produtoDAO = new ProdutoDAO();

        List<Produto> produtos =
                produtoDAO.listar();

        preencherTabela(produtos);

        txtPesquisa.setText("");
        txtPesquisa.requestFocusInWindow();
    }

    // PESQUISAR PRODUTOS

    private void pesquisarProdutos() {

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

        ProdutoDAO produtoDAO = new ProdutoDAO();

        List<Produto> produtos =
                produtoDAO.pesquisarPorNome(pesquisa);

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

        for (Produto produto : produtos) {

            String fornecedor =
                    produto.getNomeFornecedor();

            if (fornecedor == null ||
                    fornecedor.trim().isEmpty()) {

                fornecedor = "Sem fornecedor";
            }

            String validade = "Sem validade";

            if (produto.getDataValidade() != null) {

                validade = produto.getDataValidade()
                        .format(FORMATO_DATA);
            }

            modeloTabela.addRow(new Object[] {

                    produto.getIdProduto(),

                    produto.getNome(),

                    produto.getNomeCategoria(),

                    fornecedor,

                    Formatador.moeda(
                            produto.getValorCusto()),

                    Formatador.moeda(
                            produto.getValorVenda()),

                    produto.getQuantidadeEstoque(),

                    produto.getEstoqueMinimo(),

                    validade
            });
        }

        lblTotalProdutos.setText(
                "Total de produtos exibidos: " +
                        modeloTabela.getRowCount());
    }

    // OBTER PRODUTO SELECIONADO

    private int obterIdProdutoSelecionado() {

        int linhaSelecionada =
                tabelaProdutos.getSelectedRow();

        if (linhaSelecionada == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um produto na tabela.",
                    "Nenhum produto selecionado",
                    JOptionPane.WARNING_MESSAGE);

            return -1;
        }

        int linhaModelo =
                tabelaProdutos.convertRowIndexToModel(
                        linhaSelecionada);

        return Integer.parseInt(
                modeloTabela.getValueAt(
                        linhaModelo, 0).toString());
    }

    // EDITAR PRODUTO

    private void editarProduto() {

        int idProduto =
                obterIdProdutoSelecionado();

        if (idProduto == -1) {
            return;
        }

        if (telaPrincipal == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível abrir a tela de edição.\n" +
                    "Painel principal não identificado.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        telaPrincipal.carregarPainel(
                new PainelEditarProduto(
                        telaPrincipal, idProduto));
    }

    // EXCLUIR PRODUTO

    private void excluirProduto() {

        int idProduto =
                obterIdProdutoSelecionado();

        if (idProduto == -1) {
            return;
        }

        int linhaSelecionada =
                tabelaProdutos.getSelectedRow();

        int linhaModelo =
                tabelaProdutos.convertRowIndexToModel(
                        linhaSelecionada);

        String nomeProduto =
                modeloTabela.getValueAt(
                        linhaModelo, 1).toString();

        int resposta =
                JOptionPane.showConfirmDialog(
                        this,
                        "Deseja realmente excluir o produto " +
                        nomeProduto + "?",
                        "Confirmar exclusão",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

        if (resposta == JOptionPane.YES_OPTION) {

            ProdutoDAO produtoDAO =
                    new ProdutoDAO();

            boolean excluido =
                    produtoDAO.excluirLogico(idProduto);

            if (excluido) {

                JOptionPane.showMessageDialog(
                        this,
                        "Produto excluído com sucesso.",
                        "Exclusão realizada",
                        JOptionPane.INFORMATION_MESSAGE);

                carregarProdutos();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível excluir o produto.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
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

    // ESTILO DAS LINHAS E ALERTAS

    private class RenderizadorTabela
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
                            0, 10, 0, 10));

            setFont(new Font(
                    "SansSerif", Font.PLAIN, 12));

            int colunaModelo =
                    table.convertColumnIndexToModel(column);

            setHorizontalAlignment(
                    SwingConstants.LEFT);

            if (colunaModelo == 0 ||
                    colunaModelo == 4 ||
                    colunaModelo == 5 ||
                    colunaModelo == 6 ||
                    colunaModelo == 7 ||
                    colunaModelo == 8) {

                setHorizontalAlignment(
                        SwingConstants.CENTER);
            }

            if (isSelected) {

                setBackground(
                        table.getSelectionBackground());

                setForeground(
                        table.getSelectionForeground());

                return this;
            }

            if (row % 2 == 0) {

                setBackground(Color.WHITE);

            } else {

                setBackground(
                        new Color(248, 250, 252));
            }

            setForeground(COR_TEXTO);

            // Nome do produto em destaque

            if (colunaModelo == 1) {

                setForeground(COR_AZUL);

                setFont(new Font(
                        "SansSerif", Font.BOLD, 12));
            }

            // Produtos sem fornecedor

            if (colunaModelo == 3 && value != null) {

                if (value.toString().equals(
                        "Sem fornecedor")) {

                    setForeground(COR_CINZA);
                }
            }

            // Destaque para estoque baixo

            if (colunaModelo == 6) {

                int linhaModelo =
                        table.convertRowIndexToModel(row);

                int estoque = Integer.parseInt(
                        modeloTabela.getValueAt(
                                linhaModelo, 6).toString());

                int estoqueMinimo = Integer.parseInt(
                        modeloTabela.getValueAt(
                                linhaModelo, 7).toString());

                if (estoque <= estoqueMinimo) {

                    setForeground(COR_VERMELHO);

                    setFont(new Font(
                            "SansSerif", Font.BOLD, 12));
                }
            }

            // Destaque para datas de validade

            if (colunaModelo == 8 && value != null) {

                String texto = value.toString();

                if (texto.equals("Sem validade")) {

                    setForeground(COR_CINZA);

                    return this;
                }

                try {

                    LocalDate validade =
                            LocalDate.parse(
                                    texto, FORMATO_DATA);

                    LocalDate hoje = LocalDate.now();

                    if (validade.isBefore(hoje)) {

                        setForeground(COR_VERMELHO);

                        setFont(new Font(
                                "SansSerif", Font.BOLD, 12));

                    } else if (!validade.isAfter(
                            hoje.plusDays(7))) {

                        setForeground(COR_LARANJA);

                        setFont(new Font(
                                "SansSerif", Font.BOLD, 12));
                    }

                } catch (DateTimeParseException erro) {

                    setForeground(COR_TEXTO);
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
