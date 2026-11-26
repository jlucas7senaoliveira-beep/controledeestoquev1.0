
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
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dao.UsuarioDAO;
import model.Usuario;

public class TelaLogin extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Color COR_AZUL =
            new Color(25, 57, 94);

    private static final Color COR_AZUL_CLARO =
            new Color(43, 77, 116);

    private static final Color COR_CINZA =
            new Color(100, 116, 139);

    private static final Color COR_BORDA =
            new Color(220, 227, 235);

    private static final Color COR_CAMPO =
            new Color(248, 250, 252);

    private static final Color COR_TEXTO_CLARO =
            new Color(214, 227, 241);

    private static final int LARGURA_FORMULARIO = 340;

    private JPanel contentPane;

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JCheckBox chkMostrarSenha;

    private char caractereSenha;

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            try {

                TelaLogin frame = new TelaLogin();

                frame.setVisible(true);

            } catch (Exception e) {

                e.printStackTrace();
            }
        });
    }

    // CONSTRUTOR

    public TelaLogin() {

        setTitle("Sistema de Controle de Estoque - Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(false);

        contentPane = new JPanel(new BorderLayout());

        contentPane.setBackground(Color.WHITE);

        setContentPane(contentPane);

        contentPane.add(
                criarPainelIdentidade(),
                BorderLayout.WEST);

        contentPane.add(
                criarPainelLogin(),
                BorderLayout.CENTER);

        contentPane.setPreferredSize(
                new Dimension(850, 510));

        pack();

        setLocationRelativeTo(null);

        EventQueue.invokeLater(
                () -> txtLogin.requestFocusInWindow());
    }

    // PAINEL ESQUERDO - IDENTIDADE VISUAL

    private JPanel criarPainelIdentidade() {

        JPanel painel = new JPanel(new BorderLayout());

        painel.setBackground(COR_AZUL);

        painel.setPreferredSize(
                new Dimension(350, 510));

        painel.setBorder(
                BorderFactory.createEmptyBorder(
                        35, 35, 35, 35));

        JPanel centro = new JPanel();

        centro.setOpaque(false);

        centro.setLayout(
                new BoxLayout(centro, BoxLayout.Y_AXIS));

        centro.add(Box.createVerticalGlue());

        // LOGOTIPO DO SISTEMA

        LogoEstoque logo = new LogoEstoque();

        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        centro.add(logo);

        centro.add(Box.createVerticalStrut(25));

        // TÍTULO

        JLabel titulo = new JLabel(
                "<html>Controle de<br>Estoque</html>");

        titulo.setFont(
                new Font("SansSerif", Font.BOLD, 34));

        titulo.setForeground(Color.WHITE);

        titulo.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        centro.add(titulo);

        centro.add(Box.createVerticalStrut(20));

        // DETALHE DECORATIVO

        JPanel detalhe = new JPanel();

        detalhe.setBackground(
                new Color(120, 150, 182));

        detalhe.setPreferredSize(
                new Dimension(45, 3));

        detalhe.setMaximumSize(
                new Dimension(45, 3));

        detalhe.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        centro.add(detalhe);

        centro.add(Box.createVerticalStrut(20));

        // DESCRIÇÃO

        JLabel descricao = new JLabel(
                "<html>Gerencie seus produtos, clientes<br>"
                + "e movimentações em um só lugar.</html>");

        descricao.setFont(
                new Font("SansSerif", Font.PLAIN, 14));

        descricao.setForeground(COR_TEXTO_CLARO);

        descricao.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        centro.add(descricao);

        centro.add(Box.createVerticalGlue());

        painel.add(centro, BorderLayout.CENTER);

        // RODAPÉ DO PAINEL

        JLabel rodape = new JLabel(
                "<html>Sistema de gerenciamento<br>"
                + "para lanchonetes</html>");

        rodape.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        rodape.setForeground(
                new Color(182, 201, 223));

        painel.add(rodape, BorderLayout.SOUTH);

        return painel;
    }

    // PAINEL DIREITO - FORMULÁRIO DE LOGIN

    private JPanel criarPainelLogin() {

        JPanel painel = new JPanel(new GridBagLayout());

        painel.setBackground(Color.WHITE);

        painel.setBorder(
                BorderFactory.createEmptyBorder(
                        25, 35, 25, 35));

        JPanel formulario = new JPanel();

        formulario.setOpaque(false);

        formulario.setLayout(
                new BoxLayout(
                        formulario, BoxLayout.Y_AXIS));

        // CABEÇALHO DO FORMULÁRIO

        JLabel titulo = new JLabel("Bem-vindo!");

        titulo.setFont(
                new Font("SansSerif", Font.BOLD, 30));

        titulo.setForeground(COR_AZUL);

        titulo.setHorizontalAlignment(
                SwingConstants.CENTER);

        titulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        formulario.add(titulo);

        formulario.add(Box.createVerticalStrut(8));

        JLabel subtitulo = new JLabel(
                "Informe suas credenciais para acessar o sistema.");

        subtitulo.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        subtitulo.setForeground(COR_CINZA);

        subtitulo.setHorizontalAlignment(
                SwingConstants.CENTER);

        subtitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        formulario.add(subtitulo);

        formulario.add(Box.createVerticalStrut(28));

        // CAMPO LOGIN

        txtLogin = new JTextField();

        estilizarCampo(txtLogin);

        txtLogin.setToolTipText(
                "Digite seu login de acesso");

        formulario.add(
                criarGrupoCampo("Login", txtLogin));

        formulario.add(Box.createVerticalStrut(16));

        // CAMPO SENHA

        txtSenha = new JPasswordField();

        estilizarCampo(txtSenha);

        caractereSenha = txtSenha.getEchoChar();

        txtSenha.setToolTipText(
                "Digite sua senha de acesso");

        formulario.add(
                criarGrupoCampo("Senha", txtSenha));

        formulario.add(Box.createVerticalStrut(10));

        // MOSTRAR SENHA

        chkMostrarSenha =
                new JCheckBox("Mostrar senha");

        chkMostrarSenha.setOpaque(false);

        chkMostrarSenha.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        chkMostrarSenha.setForeground(COR_CINZA);

        chkMostrarSenha.setFocusPainted(false);

        chkMostrarSenha.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR));

        chkMostrarSenha.addActionListener(
                e -> mostrarSenha());

        JPanel painelCheckbox =
                new JPanel(new BorderLayout());

        painelCheckbox.setOpaque(false);

        definirTamanhoCentralizado(
                painelCheckbox, 25);

        painelCheckbox.add(
                chkMostrarSenha, BorderLayout.WEST);

        formulario.add(painelCheckbox);

        formulario.add(Box.createVerticalStrut(20));

        // BOTÃO ENTRAR

        JButton btnEntrar =
                criarBotao("Entrar", true);

        btnEntrar.addActionListener(
                e -> entrar());

        formulario.add(btnEntrar);

        formulario.add(Box.createVerticalStrut(10));

        // BOTÃO SAIR

        JButton btnSair =
                criarBotao("Sair", false);

        btnSair.addActionListener(
                e -> sair());

        formulario.add(btnSair);

        formulario.add(Box.createVerticalStrut(20));

        // DIVISÓRIA

        JPanel divisor = new JPanel();

        divisor.setBackground(COR_BORDA);

        definirTamanhoCentralizado(divisor, 1);

        formulario.add(divisor);

        formulario.add(Box.createVerticalStrut(14));

        // RODAPÉ DO FORMULÁRIO

        JLabel rodape = new JLabel(
                "Sistema de Controle de Estoque");

        rodape.setFont(
                new Font("SansSerif", Font.PLAIN, 11));

        rodape.setForeground(
                new Color(148, 163, 184));

        rodape.setHorizontalAlignment(
                SwingConstants.CENTER);

        rodape.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        formulario.add(rodape);

        // ENTRAR PRESSIONANDO ENTER

        getRootPane().setDefaultButton(btnEntrar);

        painel.add(formulario);

        return painel;
    }

    // PADRONIZAR TAMANHO E CENTRALIZAÇÃO

    private void definirTamanhoCentralizado(
            Component componente, int altura) {

        Dimension tamanho = new Dimension(
                LARGURA_FORMULARIO, altura);

        componente.setPreferredSize(tamanho);

        componente.setMinimumSize(tamanho);

        componente.setMaximumSize(tamanho);

        if (componente instanceof javax.swing.JComponent) {

            ((javax.swing.JComponent) componente)
                    .setAlignmentX(
                            Component.CENTER_ALIGNMENT);
        }
    }

    // ESTILIZAR CAMPOS

    private void estilizarCampo(JTextField campo) {

        campo.setFont(
                new Font("SansSerif", Font.PLAIN, 14));

        campo.setForeground(COR_AZUL);

        campo.setBackground(COR_CAMPO);

        campo.setCaretColor(COR_AZUL);

        campo.setPreferredSize(
                new Dimension(
                        LARGURA_FORMULARIO, 42));

        campo.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                COR_BORDA),

                        BorderFactory.createEmptyBorder(
                                5, 12, 5, 12)));
    }

    // AGRUPAR CAMPO E TÍTULO

    private JPanel criarGrupoCampo(
            String titulo, Component campo) {

        JPanel grupo =
                new JPanel(new BorderLayout(0, 7));

        grupo.setOpaque(false);

        definirTamanhoCentralizado(grupo, 65);

        JLabel label = new JLabel(titulo);

        label.setFont(
                new Font("SansSerif", Font.BOLD, 12));

        label.setForeground(COR_CINZA);

        label.setHorizontalAlignment(
                SwingConstants.LEFT);

        grupo.add(label, BorderLayout.NORTH);

        grupo.add(campo, BorderLayout.CENTER);

        return grupo;
    }

    // PADRÃO VISUAL DOS BOTÕES

    private JButton criarBotao(
            String texto, boolean principal) {

        JButton botao = new JButton(texto);

        botao.setFont(
                new Font("SansSerif", Font.BOLD, 14));

        botao.setHorizontalAlignment(
                SwingConstants.CENTER);

        definirTamanhoCentralizado(botao, 42);

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

    // AUTENTICAÇÃO

    private void entrar() {

        String login =
                txtLogin.getText().trim();

        String senha =
                new String(
                        txtSenha.getPassword()).trim();

        if (login.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe o login.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE);

            txtLogin.requestFocusInWindow();

            return;
        }

        if (senha.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe a senha.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE);

            txtSenha.requestFocusInWindow();

            return;
        }

        UsuarioDAO usuarioDAO =
                new UsuarioDAO();

        Usuario usuario =
                usuarioDAO.autenticar(login, senha);

        if (usuario != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Bem-vindo, " + usuario.getNome() + "!",
                    "Login realizado",
                    JOptionPane.INFORMATION_MESSAGE);

            TelaPrincipal telaPrincipal =
                    new TelaPrincipal(usuario);

            telaPrincipal.setVisible(true);

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Login ou senha inválidos.",
                    "Acesso negado",
                    JOptionPane.ERROR_MESSAGE);

            txtSenha.setText("");

            chkMostrarSenha.setSelected(false);

            mostrarSenha();

            txtLogin.requestFocusInWindow();
        }
    }

    // MOSTRAR OU OCULTAR SENHA

    private void mostrarSenha() {

        if (chkMostrarSenha.isSelected()) {

            txtSenha.setEchoChar((char) 0);

        } else {

            txtSenha.setEchoChar(caractereSenha);
        }
    }

    // ENCERRAR SISTEMA

    private void sair() {

        int resposta =
                JOptionPane.showConfirmDialog(
                        this,
                        "Deseja realmente sair?",
                        "Confirmar saída",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

        if (resposta == JOptionPane.YES_OPTION) {

            System.exit(0);
        }
    }

    // LOGOTIPO DESENHADO EM JAVA

    private static class LogoEstoque extends JPanel {

        private static final long serialVersionUID = 1L;

        public LogoEstoque() {

            setOpaque(false);

            setPreferredSize(
                    new Dimension(90, 90));

            setMinimumSize(
                    new Dimension(90, 90));

            setMaximumSize(
                    new Dimension(90, 90));
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.scale(
                    getWidth() / 100.0,
                    getHeight() / 100.0);

            // FUNDO ARREDONDADO DO LOGOTIPO

            g2.setColor(COR_AZUL_CLARO);

            g2.fillRoundRect(
                    0, 0, 100, 100, 22, 22);

            // CONTORNO DA CAIXA

            g2.setColor(Color.WHITE);

            g2.setStroke(new BasicStroke(
                    3.0f,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND));

            Path2D caixa = new Path2D.Double();

            caixa.moveTo(50, 22);

            caixa.lineTo(77, 37);

            caixa.lineTo(77, 65);

            caixa.lineTo(50, 80);

            caixa.lineTo(23, 65);

            caixa.lineTo(23, 37);

            caixa.closePath();

            g2.draw(caixa);

            // LINHAS INTERNAS DA CAIXA

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
