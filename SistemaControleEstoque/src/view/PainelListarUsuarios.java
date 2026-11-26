
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
import java.awt.Window;
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
import javax.swing.SwingUtilities;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.UsuarioDAO;
import model.Usuario;

public class PainelListarUsuarios extends JPanel {

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
    private JTable tabelaUsuarios;
    private DefaultTableModel modeloTabela;

    private JLabel lblTotalUsuarios;
    private JLabel lblUsuariosAtivos;
    private JLabel lblUsuariosInativos;

    public PainelListarUsuarios() {

        setLayout(new BorderLayout(0, 22));
        setBackground(COR_FUNDO);

        setBorder(
                BorderFactory.createEmptyBorder(
                        28, 25, 25, 25));

        criarCabecalho();
        criarConteudo();
        carregarUsuarios();
    }

    // CABEÇALHO

    private void criarCabecalho() {

        JPanel cabecalho = new JPanel();

        cabecalho.setOpaque(false);

        cabecalho.setLayout(
                new BoxLayout(cabecalho, BoxLayout.Y_AXIS));

        JLabel lblTitulo =
                new JLabel("Listagem de Usuários");

        lblTitulo.setFont(
                new Font("SansSerif", Font.BOLD, 25));

        lblTitulo.setForeground(COR_AZUL);

        lblTitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel(
                "Consulte e gerencie os usuários do sistema");

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
                new JScrollPane(tabelaUsuarios);

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
                new JLabel("Pesquisar usuário");

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
                "Digite o nome do usuário");

        txtPesquisa.addActionListener(
                e -> pesquisarUsuarios());

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
                e -> pesquisarUsuarios());

        JButton btnAtualizar =
                criarBotao("Atualizar", false, false);

        btnAtualizar.addActionListener(
                e -> carregarUsuarios());

        JButton btnEditar =
                criarBotao("Editar", false, false);

        btnEditar.addActionListener(
                e -> editarUsuarioSelecionado());

        JButton btnExcluir =
                criarBotao("Excluir", false, true);

        btnExcluir.addActionListener(
                e -> excluirUsuarioSelecionado());

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

        tabelaUsuarios = new JTable();

        tabelaUsuarios.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        tabelaUsuarios.setRowHeight(42);

        tabelaUsuarios.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        tabelaUsuarios.setForeground(COR_TEXTO);
        tabelaUsuarios.setBackground(Color.WHITE);

        tabelaUsuarios.setGridColor(
                new Color(235, 239, 244));

        tabelaUsuarios.setShowVerticalLines(false);
        tabelaUsuarios.setShowHorizontalLines(true);

        tabelaUsuarios.setSelectionBackground(
                new Color(224, 235, 247));

        tabelaUsuarios.setSelectionForeground(
                COR_AZUL);

        tabelaUsuarios.setFillsViewportHeight(true);

        tabelaUsuarios.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS);

        tabelaUsuarios.getTableHeader()
                .setReorderingAllowed(false);

        tabelaUsuarios.getTableHeader()
                .setBackground(
                        new Color(237, 242, 248));

        tabelaUsuarios.getTableHeader()
                .setForeground(COR_AZUL);

        tabelaUsuarios.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12));

        tabelaUsuarios.getTableHeader()
                .setPreferredSize(
                        new Dimension(0, 42));

        configurarTabela();

        tabelaUsuarios.getTableHeader()
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
        modeloTabela.addColumn("Login");
        modeloTabela.addColumn("Perfil");
        modeloTabela.addColumn("Status");

        tabelaUsuarios.setModel(modeloTabela);

        int[] larguras = {
                55, 300, 220, 190, 130
        };

        int[] largurasMinimas = {
                35, 130, 100, 130, 85
        };

        for (int i = 0; i < larguras.length; i++) {

            tabelaUsuarios.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(larguras[i]);

            tabelaUsuarios.getColumnModel()
                    .getColumn(i)
                    .setMinWidth(largurasMinimas[i]);
        }

        aplicarDestaqueUsuariosInativos();
    }

    // RODAPÉ DA TABELA

    private JPanel criarRodape() {

        JPanel rodape =
                new JPanel(new BorderLayout(15, 0));

        rodape.setOpaque(false);

        lblTotalUsuarios =
                new JLabel("Total de usuários exibidos: 0");

        lblTotalUsuarios.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        lblTotalUsuarios.setForeground(COR_CINZA);

        JPanel painelContadores =
                new JPanel(new FlowLayout(
                        FlowLayout.RIGHT, 20, 0));

        painelContadores.setOpaque(false);

        lblUsuariosAtivos =
                new JLabel("Ativos: 0");

        lblUsuariosAtivos.setFont(
                new Font("SansSerif", Font.BOLD, 12));

        lblUsuariosAtivos.setForeground(COR_VERDE);

        lblUsuariosInativos =
                new JLabel("Inativos: 0");

        lblUsuariosInativos.setFont(
                new Font("SansSerif", Font.BOLD, 12));

        lblUsuariosInativos.setForeground(COR_VERMELHO);

        painelContadores.add(lblUsuariosAtivos);
        painelContadores.add(lblUsuariosInativos);

        rodape.add(
                lblTotalUsuarios, BorderLayout.WEST);

        rodape.add(
                painelContadores, BorderLayout.EAST);

        return rodape;
    }

    // CARREGAR USUÁRIOS

    private void carregarUsuarios() {

        modeloTabela.setRowCount(0);

        UsuarioDAO dao =
                new UsuarioDAO();

        List<Usuario> usuarios =
                dao.listar();

        preencherTabela(usuarios);

        txtPesquisa.setText("");
        txtPesquisa.requestFocusInWindow();
    }

    // PESQUISAR USUÁRIOS

    private void pesquisarUsuarios() {

        String pesquisa =
                txtPesquisa.getText().trim();

        if (pesquisa.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Digite o nome do usuário para pesquisar.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE);

            txtPesquisa.requestFocusInWindow();

            return;
        }

        modeloTabela.setRowCount(0);

        UsuarioDAO dao =
                new UsuarioDAO();

        List<Usuario> usuarios =
                dao.pesquisarPorNome(pesquisa);

        preencherTabela(usuarios);

        if (usuarios.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Nenhum usuário encontrado.",
                    "Resultado da pesquisa",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // PREENCHER TABELA

    private void preencherTabela(List<Usuario> usuarios) {

        int totalAtivos = 0;
        int totalInativos = 0;

        for (Usuario usuario : usuarios) {

            String status =
                    usuario.isAtivo()
                            ? "Ativo"
                            : "Inativo";

            if (usuario.isAtivo()) {

                totalAtivos++;

            } else {

                totalInativos++;
            }

            modeloTabela.addRow(new Object[] {

                    usuario.getIdUsuario(),

                    usuario.getNome(),

                    usuario.getLogin(),

                    usuario.getPerfil(),

                    status
            });
        }

        lblTotalUsuarios.setText(
                "Total de usuários exibidos: " +
                        modeloTabela.getRowCount());

        lblUsuariosAtivos.setText(
                "Ativos: " + totalAtivos);

        lblUsuariosInativos.setText(
                "Inativos: " + totalInativos);
    }

    // OBTER USUÁRIO SELECIONADO

    private int obterIdSelecionado() {

        int linhaSelecionada =
                tabelaUsuarios.getSelectedRow();

        if (linhaSelecionada == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um usuário na tabela.",
                    "Nenhum usuário selecionado",
                    JOptionPane.WARNING_MESSAGE);

            return -1;
        }

        int linhaModelo =
                tabelaUsuarios.convertRowIndexToModel(
                        linhaSelecionada);

        return Integer.parseInt(
                modeloTabela.getValueAt(
                        linhaModelo, 0).toString());
    }

    // OBTER NOME DO USUÁRIO SELECIONADO

    private String obterNomeSelecionado() {

        int linhaSelecionada =
                tabelaUsuarios.getSelectedRow();

        if (linhaSelecionada == -1) {

            return "";
        }

        int linhaModelo =
                tabelaUsuarios.convertRowIndexToModel(
                        linhaSelecionada);

        return modeloTabela.getValueAt(
                linhaModelo, 1).toString();
    }

    // EDITAR USUÁRIO

    private void editarUsuarioSelecionado() {

        int idUsuario =
                obterIdSelecionado();

        if (idUsuario == -1) {

            return;
        }

        Window janela =
                SwingUtilities.getWindowAncestor(this);

        if (janela instanceof TelaPrincipal) {

            TelaPrincipal telaPrincipal =
                    (TelaPrincipal) janela;

            telaPrincipal.carregarPainel(
                    new PainelEditarUsuario(idUsuario));

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível abrir a tela de edição.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // EXCLUIR USUÁRIO

    private void excluirUsuarioSelecionado() {

        int idUsuario =
                obterIdSelecionado();

        if (idUsuario == -1) {

            return;
        }

        String nomeUsuario =
                obterNomeSelecionado();

        int resposta =
                JOptionPane.showConfirmDialog(
                        this,
                        "Deseja realmente excluir o usuário " +
                        nomeUsuario + "?\n" +
                        "O usuário será desativado no sistema.",
                        "Confirmar exclusão",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

        if (resposta == JOptionPane.YES_OPTION) {

            UsuarioDAO dao =
                    new UsuarioDAO();

            boolean excluido =
                    dao.excluirLogico(idUsuario);

            if (excluido) {

                JOptionPane.showMessageDialog(
                        this,
                        "Usuário excluído com sucesso.",
                        "Exclusão realizada",
                        JOptionPane.INFORMATION_MESSAGE);

                carregarUsuarios();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível excluir o usuário.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // APLICAR RENDERIZADOR

    private void aplicarDestaqueUsuariosInativos() {

        RenderizadorTabela renderizador =
                new RenderizadorTabela();

        for (int i = 0;
                i < tabelaUsuarios.getColumnCount();
                i++) {

            tabelaUsuarios.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(renderizador);
        }
    }

    // ESTILO DO CABEÇALHO

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

            int linhaModelo =
                    table.convertRowIndexToModel(row);

            Object status =
                    table.getModel().getValueAt(
                            linhaModelo, 4);

            boolean inativo =
                    "Inativo".equalsIgnoreCase(
                            String.valueOf(status));

            setHorizontalAlignment(
                    SwingConstants.LEFT);

            // CENTRALIZAR ID, PERFIL E STATUS

            if (colunaModelo == 0 ||
                    colunaModelo == 3 ||
                    colunaModelo == 4) {

                setHorizontalAlignment(
                        SwingConstants.CENTER);
            }

            // DESTACAR NOME DO USUÁRIO

            if (colunaModelo == 1) {

                setFont(new Font(
                        "SansSerif", Font.BOLD, 12));
            }

            // CORES DAS LINHAS

            if (isSelected) {

                setBackground(
                        table.getSelectionBackground());

                setForeground(
                        table.getSelectionForeground());

            } else if (inativo) {

                setBackground(
                        new Color(252, 239, 239));

                setForeground(COR_VERMELHO);

            } else {

                if (row % 2 == 0) {

                    setBackground(Color.WHITE);

                } else {

                    setBackground(
                            new Color(248, 250, 252));
                }

                setForeground(COR_TEXTO);
            }

            // COR DO NOME

            if (colunaModelo == 1 && !inativo) {

                setForeground(COR_AZUL);
            }

            // DESTACAR PERFIL

            if (colunaModelo == 3) {

                setFont(new Font(
                        "SansSerif", Font.BOLD, 12));

                String perfil =
                        String.valueOf(value);

                if (!inativo &&
                        ("ADMINISTRADOR".equalsIgnoreCase(perfil)
                        || "ADMIN".equalsIgnoreCase(perfil))) {

                    setForeground(COR_AZUL);
                }
            }

            // DESTACAR STATUS

            if (colunaModelo == 4) {

                setFont(new Font(
                        "SansSerif", Font.BOLD, 12));

                if (inativo) {

                    setForeground(COR_VERMELHO);

                    if (!isSelected) {

                        setBackground(
                                new Color(248, 220, 220));
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
