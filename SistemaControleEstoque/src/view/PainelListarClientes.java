
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

import dao.ClienteDAO;
import model.Cliente;

public class PainelListarClientes extends JPanel {

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

    private JTextField txtPesquisa;
    private JTable tabelaClientes;
    private DefaultTableModel modeloTabela;
    private JLabel lblTotalClientes;

    private TelaPrincipal telaPrincipal;

    public PainelListarClientes() {
        this(null);
    }

    public PainelListarClientes(TelaPrincipal telaPrincipal) {

        this.telaPrincipal = telaPrincipal;

        setLayout(new BorderLayout(0, 22));
        setBackground(COR_FUNDO);

        setBorder(BorderFactory.createEmptyBorder(
                28, 25, 25, 25));

        criarCabecalho();
        criarConteudo();
        carregarClientes();
    }

    // CABEÇALHO

    private void criarCabecalho() {

        JPanel cabecalho = new JPanel();

        cabecalho.setOpaque(false);

        cabecalho.setLayout(
                new BoxLayout(cabecalho, BoxLayout.Y_AXIS));

        JLabel lblTitulo =
                new JLabel("Listagem de Clientes");

        lblTitulo.setFont(
                new Font("SansSerif", Font.BOLD, 25));

        lblTitulo.setForeground(COR_AZUL);

        lblTitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel(
                "Consulte e gerencie os clientes cadastrados");

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
                new JScrollPane(tabelaClientes);

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

        lblTotalClientes =
                new JLabel("Total de clientes exibidos: 0");

        lblTotalClientes.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        lblTotalClientes.setForeground(COR_CINZA);

        rodape.add(
                lblTotalClientes, BorderLayout.WEST);

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
                new JLabel("Pesquisar cliente");

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
                "Digite o nome do cliente");

        txtPesquisa.addActionListener(
                e -> pesquisarClientes());

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
                e -> pesquisarClientes());

        JButton btnAtualizar =
                criarBotao("Atualizar", false, false);

        btnAtualizar.addActionListener(
                e -> carregarClientes());

        JButton btnEditar =
                criarBotao("Editar", false, false);

        btnEditar.addActionListener(
                e -> editarCliente());

        JButton btnExcluir =
                criarBotao("Excluir", false, true);

        btnExcluir.addActionListener(
                e -> excluirCliente());

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

        tabelaClientes = new JTable();

        tabelaClientes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        tabelaClientes.setRowHeight(42);

        tabelaClientes.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        tabelaClientes.setForeground(COR_TEXTO);
        tabelaClientes.setBackground(Color.WHITE);

        tabelaClientes.setGridColor(
                new Color(235, 239, 244));

        tabelaClientes.setShowVerticalLines(false);
        tabelaClientes.setShowHorizontalLines(true);

        tabelaClientes.setSelectionBackground(
                new Color(224, 235, 247));

        tabelaClientes.setSelectionForeground(
                COR_AZUL);

        tabelaClientes.setFillsViewportHeight(true);

        tabelaClientes.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS);

        tabelaClientes.getTableHeader()
                .setReorderingAllowed(false);

        tabelaClientes.getTableHeader()
                .setBackground(
                        new Color(237, 242, 248));

        tabelaClientes.getTableHeader()
                .setForeground(COR_AZUL);

        tabelaClientes.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12));

        tabelaClientes.getTableHeader()
                .setPreferredSize(
                        new Dimension(0, 42));

        configurarTabela();

        tabelaClientes.getTableHeader()
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
        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("CPF");
        modeloTabela.addColumn("Telefone");
        modeloTabela.addColumn("E-mail");
        modeloTabela.addColumn("Cidade");

        tabelaClientes.setModel(modeloTabela);

        int[] larguras = {
                55, 250, 160, 160, 280, 180
        };

        int[] largurasMinimas = {
                35, 130, 110, 110, 150, 100
        };

        RenderizadorTabela renderizador =
                new RenderizadorTabela();

        for (int i = 0; i < larguras.length; i++) {

            tabelaClientes.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(larguras[i]);

            tabelaClientes.getColumnModel()
                    .getColumn(i)
                    .setMinWidth(largurasMinimas[i]);

            tabelaClientes.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(renderizador);
        }
    }

    // CARREGAR CLIENTES

    private void carregarClientes() {

        modeloTabela.setRowCount(0);

        ClienteDAO clienteDAO = new ClienteDAO();

        List<Cliente> clientes =
                clienteDAO.listar();

        preencherTabela(clientes);

        txtPesquisa.setText("");
        txtPesquisa.requestFocusInWindow();
    }

    // PESQUISAR CLIENTES

    private void pesquisarClientes() {

        String pesquisa =
                txtPesquisa.getText().trim();

        if (pesquisa.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Digite o nome do cliente para pesquisar.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE);

            txtPesquisa.requestFocusInWindow();

            return;
        }

        modeloTabela.setRowCount(0);

        ClienteDAO clienteDAO = new ClienteDAO();

        List<Cliente> clientes =
                clienteDAO.pesquisarPorNome(pesquisa);

        preencherTabela(clientes);

        if (clientes.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Nenhum cliente encontrado.",
                    "Resultado da pesquisa",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // PREENCHER TABELA

    private void preencherTabela(List<Cliente> clientes) {

        for (Cliente cliente : clientes) {

            modeloTabela.addRow(new Object[] {

                    cliente.getIdCliente(),

                    cliente.getNome(),

                    cliente.getCpf(),

                    cliente.getTelefone(),

                    cliente.getEmail(),

                    cliente.getCidade()
            });
        }

        lblTotalClientes.setText(
                "Total de clientes exibidos: " +
                        modeloTabela.getRowCount());
    }

    // OBTER ID DO CLIENTE SELECIONADO

    private int obterIdClienteSelecionado() {

        int linhaSelecionada =
                tabelaClientes.getSelectedRow();

        if (linhaSelecionada == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um cliente na tabela.",
                    "Nenhum cliente selecionado",
                    JOptionPane.WARNING_MESSAGE);

            return -1;
        }

        int linhaModelo =
                tabelaClientes.convertRowIndexToModel(
                        linhaSelecionada);

        return Integer.parseInt(
                modeloTabela.getValueAt(
                        linhaModelo, 0).toString());
    }

    // OBTER NOME DO CLIENTE SELECIONADO

    private String obterNomeClienteSelecionado() {

        int linhaSelecionada =
                tabelaClientes.getSelectedRow();

        if (linhaSelecionada == -1) {

            return "";
        }

        int linhaModelo =
                tabelaClientes.convertRowIndexToModel(
                        linhaSelecionada);

        return modeloTabela.getValueAt(
                linhaModelo, 1).toString();
    }

    // EDITAR CLIENTE

    private void editarCliente() {

        int idCliente =
                obterIdClienteSelecionado();

        if (idCliente == -1) {

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
                new PainelEditarCliente(
                        telaPrincipal, idCliente));
    }

    // EXCLUIR CLIENTE

    private void excluirCliente() {

        int idCliente =
                obterIdClienteSelecionado();

        if (idCliente == -1) {

            return;
        }

        String nomeCliente =
                obterNomeClienteSelecionado();

        int resposta =
                JOptionPane.showConfirmDialog(
                        this,
                        "Deseja realmente excluir o cliente " +
                        nomeCliente + "?",
                        "Confirmar exclusão",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

        if (resposta == JOptionPane.YES_OPTION) {

            ClienteDAO clienteDAO =
                    new ClienteDAO();

            boolean excluido =
                    clienteDAO.excluirLogico(idCliente);

            if (excluido) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cliente excluído com sucesso.",
                        "Exclusão realizada",
                        JOptionPane.INFORMATION_MESSAGE);

                carregarClientes();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível excluir o cliente.",
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
                            0, 10, 0, 10));

            setFont(new Font(
                    "SansSerif", Font.PLAIN, 12));

            int colunaModelo =
                    table.convertColumnIndexToModel(column);

            setHorizontalAlignment(
                    SwingConstants.LEFT);

            // Centralizar ID, CPF e telefone

            if (colunaModelo == 0 ||
                    colunaModelo == 2 ||
                    colunaModelo == 3) {

                setHorizontalAlignment(
                        SwingConstants.CENTER);
            }

            // Destacar nome do cliente

            if (colunaModelo == 1) {

                setFont(new Font(
                        "SansSerif", Font.BOLD, 12));
            }

            // Exibir campos não preenchidos

            boolean vazio = value == null ||
                    value.toString().trim().isEmpty();

            if (vazio) {

                setText("Não informado");
            }

            // Linha selecionada

            if (isSelected) {

                setBackground(
                        table.getSelectionBackground());

                setForeground(
                        table.getSelectionForeground());

                return this;
            }

            // Alternar cores das linhas

            if (row % 2 == 0) {

                setBackground(Color.WHITE);

            } else {

                setBackground(
                        new Color(248, 250, 252));
            }

            setForeground(COR_TEXTO);

            if (colunaModelo == 1) {

                setForeground(COR_AZUL);
            }

            if (vazio) {

                setForeground(COR_CINZA);
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
