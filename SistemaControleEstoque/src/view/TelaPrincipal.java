
package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import model.Usuario;

public class TelaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    // CORES DO SISTEMA

    private static final Color COR_FUNDO =
            new Color(237, 241, 246);

    private static final Color COR_AZUL =
            new Color(25, 57, 94);

    private static final Color COR_AZUL_HOVER =
            new Color(43, 77, 116);

    private static final Color COR_AZUL_SELECIONADO =
            new Color(52, 87, 126);

    private static final Color COR_TEXTO_MENU =
            new Color(226, 234, 243);

    private static final Color COR_TEXTO_CLARO =
            new Color(175, 196, 219);

    private static final Color COR_CINZA =
            new Color(100, 116, 139);

    private static final Color COR_BORDA =
            new Color(220, 227, 235);

    private static final Color COR_BRANCO =
            Color.WHITE;

    private static final Color COR_VERMELHO =
            new Color(243, 181, 181);

    // COMPONENTES

    private JPanel contentPane;
    private JPanel painelConteudo;
    private JPanel painelMenu;

    private JLabel lblUsuarioLogado;
    private JLabel lblPerfilLogado;

    private Usuario usuarioLogado;

    private boolean isAdmin;

    private JButton botaoSelecionado;

    private final Map<String, JButton> botoesMenu =
            new LinkedHashMap<>();

    private final Map<String, JPanel> submenus =
            new LinkedHashMap<>();

    private final Map<String, JButton> botoesGrupos =
            new LinkedHashMap<>();

    // EXECUÇÃO

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            try {

                TelaLogin telaLogin =
                        new TelaLogin();

                telaLogin.setVisible(true);

            } catch (Exception e) {

                e.printStackTrace();
            }
        });
    }

    public TelaPrincipal() {

        this(null);
    }

    // CONSTRUTOR

    public TelaPrincipal(Usuario usuarioLogado) {

        this.usuarioLogado = usuarioLogado;

        isAdmin = usuarioLogado != null
                && "ADMIN".equalsIgnoreCase(
                        usuarioLogado.getPerfil());

        setTitle(
                "Sistema de Controle de Estoque - Lanchonete");

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);

        setSize(1200, 780);

        setMinimumSize(
                new Dimension(1000, 650));

        setLocationRelativeTo(null);

        contentPane =
                new JPanel(new BorderLayout());

        contentPane.setBackground(COR_FUNDO);

        setContentPane(contentPane);

        // MENU LATERAL

        contentPane.add(
                criarMenuLateral(),
                BorderLayout.WEST);

        // ÁREA PRINCIPAL

        JPanel areaPrincipal =
                new JPanel(new BorderLayout());

        areaPrincipal.setBackground(COR_FUNDO);

        areaPrincipal.add(
                criarCabecalho(),
                BorderLayout.NORTH);

        painelConteudo =
                new JPanel(new BorderLayout());

        painelConteudo.setBackground(COR_FUNDO);

        areaPrincipal.add(
                painelConteudo,
                BorderLayout.CENTER);

        contentPane.add(
                areaPrincipal,
                BorderLayout.CENTER);

        // EXIBIÇÃO INICIAL

        exibirUsuarioLogado();

        carregarPainel(new PainelDashboard());

        // ABRIR MAXIMIZADO

        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    // MENU LATERAL

    private JPanel criarMenuLateral() {

        JPanel lateral =
                new JPanel(new BorderLayout());

        lateral.setBackground(COR_AZUL);

        lateral.setPreferredSize(
                new Dimension(220, 0));

        // IDENTIDADE VISUAL

        lateral.add(
                criarIdentidadeVisual(),
                BorderLayout.NORTH);

        // MENU DE NAVEGAÇÃO

        painelMenu = new JPanel();

        painelMenu.setBackground(COR_AZUL);

        painelMenu.setLayout(
                new BoxLayout(
                        painelMenu,
                        BoxLayout.Y_AXIS));

        painelMenu.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 10, 15, 10));

        JLabel lblNavegacao =
                new JLabel("NAVEGAÇÃO");

        lblNavegacao.setFont(
                new Font("SansSerif", Font.BOLD, 10));

        lblNavegacao.setForeground(
                COR_TEXTO_CLARO);

        lblNavegacao.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 12, 0, 0));

        lblNavegacao.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        painelMenu.add(lblNavegacao);

        painelMenu.add(
                Box.createVerticalStrut(12));

        // INÍCIO

        JButton btnDashboard = criarBotaoMenu(

                "Painel inicial",

                "dashboard",

                false,

                () -> carregarPainel(
                        new PainelDashboard()));

        painelMenu.add(btnDashboard);

        painelMenu.add(
                Box.createVerticalStrut(8));

        // PRODUTOS

        JPanel menuProdutos =
                criarGrupoMenu("Produtos");

        menuProdutos.add(
                criarBotaoMenu(

                        "Cadastrar produto",

                        "cadastrarProduto",

                        true,

                        () -> carregarPainel(
                                new PainelCadastrarProduto())));

        menuProdutos.add(
                criarBotaoMenu(

                        "Listar produtos",

                        "listarProdutos",

                        true,

                        () -> carregarPainel(
                                new PainelListarProdutos(
                                        TelaPrincipal.this))));

        painelMenu.add(
                Box.createVerticalStrut(5));

        // CLIENTES

        JPanel menuClientes =
                criarGrupoMenu("Clientes");

        menuClientes.add(
                criarBotaoMenu(

                        "Cadastrar cliente",

                        "cadastrarCliente",

                        true,

                        () -> carregarPainel(
                                new PainelCadastrarCliente())));

        menuClientes.add(
                criarBotaoMenu(

                        "Listar clientes",

                        "listarClientes",

                        true,

                        () -> carregarPainel(
                                new PainelListarClientes(
                                        TelaPrincipal.this))));

        painelMenu.add(
                Box.createVerticalStrut(5));

        // ESTOQUE

        JPanel menuEstoque =
                criarGrupoMenu("Estoque");

        // CONSULTAR ESTOQUE

        menuEstoque.add(
                criarBotaoMenu(

                        "Consultar estoque",

                        "listarEstoque",

                        true,

                        () -> carregarPainel(
                                new PainelListarEstoque())));

        // ENTRADA COM USUÁRIO LOGADO

        menuEstoque.add(
                criarBotaoMenu(

                        "Entrada de estoque",

                        "entradaEstoque",

                        true,

                        () -> carregarPainel(
                                new PainelEntradaEstoque(
                                        usuarioLogado))));

        // SAÍDA COM USUÁRIO LOGADO

        menuEstoque.add(
                criarBotaoMenu(

                        "Saída de estoque",

                        "saidaEstoque",

                        true,

                        () -> carregarPainel(
                                new PainelSaidaEstoque(
                                        usuarioLogado))));

        // HISTÓRICO DE MOVIMENTAÇÕES

        menuEstoque.add(
                criarBotaoMenu(

                        "Histórico de estoque",

                        "historicoMovimentacoes",

                        true,

                        () -> carregarPainel(
                                new PainelHistoricoMovimentacoes())));

        painelMenu.add(
                Box.createVerticalStrut(5));

        // USUÁRIOS
        // APENAS ADMINISTRADORES

        if (isAdmin) {

            JPanel menuUsuarios =
                    criarGrupoMenu("Usuários");

            menuUsuarios.add(
                    criarBotaoMenu(

                            "Cadastrar usuário",

                            "cadastrarUsuario",

                            true,

                            () -> carregarPainel(
                                    new PainelNovoUsuario())));

            menuUsuarios.add(
                    criarBotaoMenu(

                            "Listar usuários",

                            "listarUsuarios",

                            true,

                            () -> carregarPainel(
                                    new PainelListarUsuarios())));
        }

        painelMenu.add(
                Box.createVerticalGlue());

        // ROLAGEM DO MENU LATERAL

        JScrollPane scrollMenu =
                new JScrollPane(painelMenu);

        scrollMenu.setBorder(null);

        scrollMenu.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollMenu.getViewport()
                .setBackground(COR_AZUL);

        scrollMenu.getVerticalScrollBar()
                .setUnitIncrement(16);

        lateral.add(
                scrollMenu,
                BorderLayout.CENTER);

        // OPÇÕES INFERIORES

        lateral.add(
                criarMenuInferior(),
                BorderLayout.SOUTH);

        return lateral;
    }

    // IDENTIDADE VISUAL DO MENU

    private JPanel criarIdentidadeVisual() {

        JPanel painel =
                new JPanel(new BorderLayout(12, 0));

        painel.setBackground(COR_AZUL);

        painel.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createMatteBorder(
                                0, 0, 1, 0,
                                COR_AZUL_HOVER),

                        BorderFactory.createEmptyBorder(
                                22, 15, 22, 15)));

        LogoEstoque logo = new LogoEstoque();

        painel.add(logo, BorderLayout.WEST);

        JPanel textos = new JPanel();

        textos.setOpaque(false);

        textos.setLayout(
                new BoxLayout(
                        textos,
                        BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel(
                "<html>Controle de<br>Estoque</html>");

        titulo.setFont(
                new Font("SansSerif", Font.BOLD, 17));

        titulo.setForeground(COR_BRANCO);

        JLabel subtitulo =
                new JLabel("Lanchonete");

        subtitulo.setFont(
                new Font("SansSerif", Font.PLAIN, 11));

        subtitulo.setForeground(COR_TEXTO_CLARO);

        textos.add(titulo);

        textos.add(Box.createVerticalStrut(5));

        textos.add(subtitulo);

        painel.add(
                textos, BorderLayout.CENTER);

        return painel;
    }

    // CRIAR GRUPO EXPANSÍVEL

    private JPanel criarGrupoMenu(String titulo) {

        JButton botaoGrupo =
                new JButton(titulo + "   +");

        botaoGrupo.setFont(
                new Font("SansSerif", Font.BOLD, 13));

        botaoGrupo.setForeground(COR_TEXTO_MENU);

        botaoGrupo.setBackground(COR_AZUL);

        botaoGrupo.setHorizontalAlignment(
                SwingConstants.LEFT);

        botaoGrupo.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 12, 0, 10));

        botaoGrupo.setFocusPainted(false);

        botaoGrupo.setOpaque(true);

        botaoGrupo.setContentAreaFilled(true);

        botaoGrupo.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE, 43));

        botaoGrupo.setPreferredSize(
                new Dimension(200, 43));

        botaoGrupo.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        botaoGrupo.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR));

        JPanel submenu = new JPanel();

        submenu.setBackground(COR_AZUL);

        submenu.setLayout(
                new BoxLayout(
                        submenu, BoxLayout.Y_AXIS));

        submenu.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        submenu.setVisible(false);

        submenus.put(titulo, submenu);

        botoesGrupos.put(titulo, botaoGrupo);

        botaoGrupo.addActionListener(e -> {

            boolean abrir = !submenu.isVisible();

            definirGrupoAberto(titulo, abrir);
        });

        botaoGrupo.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(MouseEvent e) {

                        botaoGrupo.setBackground(
                                COR_AZUL_HOVER);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                        botaoGrupo.setBackground(
                                submenu.isVisible()
                                        ? COR_AZUL_HOVER
                                        : COR_AZUL);
                    }
                });

        painelMenu.add(botaoGrupo);

        painelMenu.add(submenu);

        return submenu;
    }

    // ABRIR OU FECHAR GRUPO

    private void definirGrupoAberto(
            String titulo, boolean aberto) {

        JPanel submenu = submenus.get(titulo);

        JButton botao = botoesGrupos.get(titulo);

        if (submenu == null || botao == null) {

            return;
        }

        submenu.setVisible(aberto);

        botao.setText(
                titulo + (aberto ? "   −" : "   +"));

        botao.setBackground(
                aberto
                        ? COR_AZUL_HOVER
                        : COR_AZUL);

        painelMenu.revalidate();

        painelMenu.repaint();
    }

    // CRIAR BOTÃO DE NAVEGAÇÃO

    private JButton criarBotaoMenu(
            String texto,
            String identificador,
            boolean submenu,
            Runnable acao) {

        JButton botao = new JButton(texto);

        botao.setFont(
                new Font(
                        "SansSerif",
                        submenu ? Font.PLAIN : Font.BOLD,
                        13));

        botao.setBackground(COR_AZUL);

        botao.setForeground(COR_TEXTO_MENU);

        botao.setHorizontalAlignment(
                SwingConstants.LEFT);

        botao.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        submenu ? 25 : 12,
                        0,
                        8));

        botao.setFocusPainted(false);

        botao.setOpaque(true);

        botao.setContentAreaFilled(true);

        botao.setPreferredSize(
                new Dimension(
                        200, submenu ? 38 : 43));

        botao.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        submenu ? 38 : 43));

        botao.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        botao.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR));

        botao.addActionListener(e -> acao.run());

        // EFEITO AO PASSAR O MOUSE

        botao.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(MouseEvent e) {

                        if (botao != botaoSelecionado) {

                            botao.setBackground(
                                    COR_AZUL_HOVER);
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                        if (botao == botaoSelecionado) {

                            botao.setBackground(
                                    COR_AZUL_SELECIONADO);

                        } else {

                            botao.setBackground(COR_AZUL);
                        }
                    }
                });

        botoesMenu.put(identificador, botao);

        return botao;
    }

    // MENU INFERIOR

    private JPanel criarMenuInferior() {

        JPanel painel = new JPanel();

        painel.setBackground(COR_AZUL);

        painel.setLayout(
                new BoxLayout(
                        painel, BoxLayout.Y_AXIS));

        painel.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createMatteBorder(
                                1, 0, 0, 0,
                                COR_AZUL_HOVER),

                        BorderFactory.createEmptyBorder(
                                12, 10, 15, 10)));

        JLabel titulo =
                new JLabel("SISTEMA");

        titulo.setFont(
                new Font("SansSerif", Font.BOLD, 10));

        titulo.setForeground(COR_TEXTO_CLARO);

        titulo.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 12, 0, 0));

        titulo.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        painel.add(titulo);

        painel.add(Box.createVerticalStrut(8));

        JButton btnPerfil =
                criarBotaoMenu(

                        "Meu perfil",

                        "meuPerfil",

                        false,

                        () -> carregarPainel(
                                new PainelEditarPerfil(
                                        usuarioLogado)));

        painel.add(btnPerfil);

        painel.add(Box.createVerticalStrut(5));

        JButton btnSair =
                criarBotaoMenu(

                        "Sair do sistema",

                        "sair",

                        false,

                        () -> sair());

        btnSair.setForeground(COR_VERMELHO);

        painel.add(btnSair);

        return painel;
    }

    // CABEÇALHO SUPERIOR

    private JPanel criarCabecalho() {

        JPanel painel =
                new JPanel(new BorderLayout(20, 0));

        painel.setBackground(COR_BRANCO);

        painel.setPreferredSize(
                new Dimension(0, 75));

        painel.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createMatteBorder(
                                0, 0, 1, 0,
                                COR_BORDA),

                        BorderFactory.createEmptyBorder(
                                12, 25, 12, 25)));

        // TÍTULO DO SISTEMA

        JPanel painelTitulo = new JPanel();

        painelTitulo.setOpaque(false);

        painelTitulo.setLayout(
                new BoxLayout(
                        painelTitulo,
                        BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel(
                "Sistema de Controle de Estoque");

        titulo.setFont(
                new Font("SansSerif", Font.BOLD, 20));

        titulo.setForeground(COR_AZUL);

        JLabel subtitulo = new JLabel(
                "Gerenciamento de lanchonete");

        subtitulo.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        subtitulo.setForeground(COR_CINZA);

        painelTitulo.add(
                Box.createVerticalGlue());

        painelTitulo.add(titulo);

        painelTitulo.add(
                Box.createVerticalStrut(4));

        painelTitulo.add(subtitulo);

        painelTitulo.add(
                Box.createVerticalGlue());

        painel.add(
                painelTitulo, BorderLayout.CENTER);

        // USUÁRIO LOGADO

        JPanel painelUsuario = new JPanel();

        painelUsuario.setOpaque(false);

        painelUsuario.setLayout(
                new BoxLayout(
                        painelUsuario,
                        BoxLayout.Y_AXIS));

        lblUsuarioLogado = new JLabel();

        lblUsuarioLogado.setFont(
                new Font("SansSerif", Font.BOLD, 14));

        lblUsuarioLogado.setForeground(COR_AZUL);

        lblUsuarioLogado.setAlignmentX(
                Component.RIGHT_ALIGNMENT);

        lblPerfilLogado = new JLabel();

        lblPerfilLogado.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        lblPerfilLogado.setForeground(COR_CINZA);

        lblPerfilLogado.setAlignmentX(
                Component.RIGHT_ALIGNMENT);

        painelUsuario.add(
                Box.createVerticalGlue());

        painelUsuario.add(lblUsuarioLogado);

        painelUsuario.add(
                Box.createVerticalStrut(5));

        painelUsuario.add(lblPerfilLogado);

        painelUsuario.add(
                Box.createVerticalGlue());

        painel.add(
                painelUsuario, BorderLayout.EAST);

        return painel;
    }

    // TROCAR PAINÉIS DO SISTEMA

    public void carregarPainel(JPanel painel) {

        if (painel == null) {

            return;
        }

        // PROTEGER TELAS ADMINISTRATIVAS

        if (!isAdmin &&

                (painel instanceof PainelNovoUsuario
                || painel instanceof PainelListarUsuarios
                || painel instanceof PainelEditarUsuario)) {

            JOptionPane.showMessageDialog(

                    this,

                    "Você não possui permissão para acessar esta tela.",

                    "Acesso negado",

                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        painelConteudo.removeAll();

        painelConteudo.add(
                painel, BorderLayout.CENTER);

        painelConteudo.revalidate();

        painelConteudo.repaint();

        atualizarMenuSelecionado(painel);

        exibirUsuarioLogado();
    }

    // IDENTIFICAR A PÁGINA ATUAL

    private void atualizarMenuSelecionado(
            JPanel painel) {

        String identificador = null;
        String grupo = null;

        if (painel instanceof PainelDashboard) {

            identificador = "dashboard";

        } else if (
                painel instanceof PainelCadastrarProduto) {

            identificador = "cadastrarProduto";

            grupo = "Produtos";

        } else if (
                painel instanceof PainelListarProdutos
                || painel instanceof PainelEditarProduto) {

            identificador = "listarProdutos";

            grupo = "Produtos";

        } else if (
                painel instanceof PainelCadastrarCliente) {

            identificador = "cadastrarCliente";

            grupo = "Clientes";

        } else if (
                painel instanceof PainelListarClientes
                || painel instanceof PainelEditarCliente) {

            identificador = "listarClientes";

            grupo = "Clientes";

        } else if (
                painel instanceof PainelListarEstoque) {

            identificador = "listarEstoque";

            grupo = "Estoque";

        } else if (
                painel instanceof PainelEntradaEstoque) {

            identificador = "entradaEstoque";

            grupo = "Estoque";

        } else if (
                painel instanceof PainelSaidaEstoque) {

            identificador = "saidaEstoque";

            grupo = "Estoque";

        } else if (
                painel instanceof PainelHistoricoMovimentacoes) {

            identificador = "historicoMovimentacoes";

            grupo = "Estoque";

        } else if (
                painel instanceof PainelNovoUsuario) {

            identificador = "cadastrarUsuario";

            grupo = "Usuários";

        } else if (
                painel instanceof PainelListarUsuarios
                || painel instanceof PainelEditarUsuario) {

            identificador = "listarUsuarios";

            grupo = "Usuários";

        } else if (
                painel instanceof PainelEditarPerfil) {

            identificador = "meuPerfil";
        }

        // REMOVER SELEÇÃO ANTERIOR

        if (botaoSelecionado != null) {

            botaoSelecionado.setBackground(
                    COR_AZUL);

            botaoSelecionado.setForeground(
                    COR_TEXTO_MENU);
        }

        botaoSelecionado = null;

        // DESTACAR PÁGINA ATUAL

        if (identificador != null) {

            botaoSelecionado =
                    botoesMenu.get(identificador);

            if (botaoSelecionado != null) {

                botaoSelecionado.setBackground(
                        COR_AZUL_SELECIONADO);

                botaoSelecionado.setForeground(
                        COR_BRANCO);
            }
        }

        // ABRIR GRUPO DA PÁGINA ATUAL

        if (grupo != null) {

            definirGrupoAberto(grupo, true);
        }
    }

    // EXIBIR USUÁRIO LOGADO

    private void exibirUsuarioLogado() {

        if (usuarioLogado == null) {

            lblUsuarioLogado.setText(
                    "Usuário não identificado");

            lblPerfilLogado.setText(
                    "Perfil não identificado");

            return;
        }

        lblUsuarioLogado.setText(
                usuarioLogado.getNome());

        String perfil =
                usuarioLogado.getPerfil();

        if ("ADMIN".equalsIgnoreCase(perfil)) {

            lblPerfilLogado.setText(
                    "Perfil administrativo");

        } else if (
                "OPERADOR".equalsIgnoreCase(perfil)) {

            lblPerfilLogado.setText(
                    "Perfil de operador");

        } else {

            lblPerfilLogado.setText(
                    "Perfil: " +
                    (perfil == null
                            ? "Não identificado"
                            : perfil));
        }
    }

    // ENCERRAR O SISTEMA

    private void sair() {

        int resposta =
                JOptionPane.showConfirmDialog(

                        this,

                        "Deseja realmente sair do sistema?",

                        "Confirmar saída",

                        JOptionPane.YES_NO_OPTION,

                        JOptionPane.QUESTION_MESSAGE);

        if (resposta == JOptionPane.YES_OPTION) {

            System.exit(0);
        }
    }

    // LOGOTIPO DO SISTEMA

    private static class LogoEstoque extends JPanel {

        private static final long serialVersionUID = 1L;

        public LogoEstoque() {

            setOpaque(false);

            setPreferredSize(
                    new Dimension(43, 43));

            setMinimumSize(
                    new Dimension(43, 43));
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            double escala =
                    Math.min(
                            getWidth() / 100.0,
                            getHeight() / 100.0);

            g2.translate(
                    (getWidth() - 100 * escala) / 2,
                    (getHeight() - 100 * escala) / 2);

            g2.scale(escala, escala);

            // FUNDO DO LOGOTIPO

            g2.setColor(COR_AZUL_HOVER);

            g2.fillRoundRect(
                    0, 0, 100, 100, 22, 22);

            g2.setColor(Color.WHITE);

            g2.setStroke(new BasicStroke(
                    4.0f,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND));

            // DESENHO DA CAIXA

            Path2D caixa = new Path2D.Double();

            caixa.moveTo(50, 22);

            caixa.lineTo(77, 37);

            caixa.lineTo(77, 65);

            caixa.lineTo(50, 80);

            caixa.lineTo(23, 65);

            caixa.lineTo(23, 37);

            caixa.closePath();

            g2.draw(caixa);

            Path2D linhas = new Path2D.Double();

            linhas.moveTo(23, 37);

            linhas.lineTo(50, 52);

            linhas.lineTo(77, 37);

            linhas.moveTo(50, 52);

            linhas.lineTo(50, 80);

            linhas.moveTo(36, 30);

            linhas.lineTo(63, 45);

            g2.draw(linhas);

            g2.dispose();
        }
    }
}
