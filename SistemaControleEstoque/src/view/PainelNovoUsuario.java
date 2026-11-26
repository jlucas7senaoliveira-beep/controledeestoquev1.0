
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
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dao.UsuarioDAO;
import model.Usuario;
import util.SenhaUtil;

public class PainelNovoUsuario extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color COR_FUNDO =
            new Color(237, 241, 246);

    private static final Color COR_AZUL =
            new Color(25, 57, 94);

    private static final Color COR_CINZA =
            new Color(100, 116, 139);

    private static final Color COR_BORDA =
            new Color(220, 227, 235);

    private static final Color COR_CAMPO =
            new Color(248, 250, 252);

    private JTextField txtNome;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JPasswordField txtConfirmarSenha;
    private JComboBox<String> comboPerfil;
    private JCheckBox chkAtivo;

    public PainelNovoUsuario() {

        setLayout(new BorderLayout());
        setBackground(COR_FUNDO);

        JPanel conteudo = new JPanel();

        conteudo.setOpaque(false);

        conteudo.setLayout(
                new BoxLayout(conteudo, BoxLayout.Y_AXIS));

        conteudo.setBorder(
                BorderFactory.createEmptyBorder(
                        28, 25, 30, 25));

        conteudo.add(criarCabecalho());
        conteudo.add(Box.createVerticalStrut(22));

        PainelArredondado formulario =
                new PainelArredondado(Color.WHITE, 18);

        formulario.setLayout(new BorderLayout());

        formulario.setBorder(
                BorderFactory.createEmptyBorder(
                        25, 28, 25, 28));

        JPanel campos = new JPanel();

        campos.setOpaque(false);

        campos.setLayout(
                new BoxLayout(campos, BoxLayout.Y_AXIS));

        criarCampos(campos);

        formulario.add(campos, BorderLayout.CENTER);

        JPanel centralizador =
                new JPanel(new FlowLayout(
                        FlowLayout.CENTER, 0, 0));

        centralizador.setOpaque(false);

        centralizador.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        centralizador.add(formulario);

        conteudo.add(centralizador);
        conteudo.add(Box.createVerticalGlue());

        JScrollPane scroll =
                new JScrollPane(conteudo);

        scroll.setBorder(null);

        scroll.getViewport()
                .setBackground(COR_FUNDO);

        scroll.getVerticalScrollBar()
                .setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);
    }

    // CABEÇALHO

    private JPanel criarCabecalho() {

        JPanel painel = new JPanel();

        painel.setOpaque(false);

        painel.setLayout(
                new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JLabel titulo =
                new JLabel("Cadastrar Novo Usuário");

        titulo.setFont(
                new Font("SansSerif", Font.BOLD, 25));

        titulo.setForeground(COR_AZUL);

        titulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel(
                "Cadastre usuários e defina suas permissões de acesso");

        subtitulo.setFont(
                new Font("SansSerif", Font.PLAIN, 13));

        subtitulo.setForeground(COR_CINZA);

        subtitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        painel.add(titulo);
        painel.add(Box.createVerticalStrut(6));
        painel.add(subtitulo);

        return painel;
    }

    // FORMULÁRIO

    private void criarCampos(JPanel painel) {

        // INFORMAÇÕES PESSOAIS

        painel.add(criarTituloSecao(
                "Informações pessoais"));

        painel.add(Box.createVerticalStrut(18));

        txtNome = criarCampoTexto();
        txtLogin = criarCampoTexto();

        txtNome.setToolTipText(
                "Informe o nome completo do usuário");

        txtLogin.setToolTipText(
                "Informe o login utilizado para acessar o sistema");

        JPanel linha1 = criarLinha(

                criarGrupoCampo(
                        "Nome completo", txtNome),

                criarGrupoCampo(
                        "Login", txtLogin));

        painel.add(linha1);
        painel.add(Box.createVerticalStrut(25));

        painel.add(criarDivisor());
        painel.add(Box.createVerticalStrut(22));

        // SEGURANÇA

        painel.add(criarTituloSecao("Segurança"));

        painel.add(Box.createVerticalStrut(18));

        txtSenha = criarCampoSenha();

        txtConfirmarSenha = criarCampoSenha();

        JPanel linha2 = criarLinha(

                criarGrupoCampo(
                        "Senha", txtSenha),

                criarGrupoCampo(
                        "Confirmar senha",
                        txtConfirmarSenha));

        painel.add(linha2);
        painel.add(Box.createVerticalStrut(8));

        JLabel lblOrientacaoSenha = new JLabel(
                "A senha deve conter pelo menos 6 caracteres.");

        lblOrientacaoSenha.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        lblOrientacaoSenha.setForeground(COR_CINZA);

        lblOrientacaoSenha.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        painel.add(lblOrientacaoSenha);

        painel.add(Box.createVerticalStrut(25));

        painel.add(criarDivisor());
        painel.add(Box.createVerticalStrut(22));

        // PERFIL E PERMISSÕES

        painel.add(criarTituloSecao(
                "Perfil e permissões"));

        painel.add(Box.createVerticalStrut(18));

        comboPerfil = new JComboBox<>();

        comboPerfil.addItem("ADMINISTRADOR");
        comboPerfil.addItem("OPERADOR");

        comboPerfil.setFont(
                new Font("SansSerif", Font.PLAIN, 13));

        comboPerfil.setForeground(COR_AZUL);
        comboPerfil.setBackground(COR_CAMPO);

        comboPerfil.setPreferredSize(
                new Dimension(390, 38));

        chkAtivo = new JCheckBox("Usuário ativo");

        chkAtivo.setSelected(true);

        chkAtivo.setOpaque(false);

        chkAtivo.setFont(
                new Font("SansSerif", Font.BOLD, 13));

        chkAtivo.setForeground(COR_AZUL);

        chkAtivo.setFocusPainted(false);

        chkAtivo.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR));

        JPanel painelStatus =
                new JPanel(new BorderLayout());

        painelStatus.setBackground(COR_CAMPO);

        painelStatus.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                COR_BORDA),

                        BorderFactory.createEmptyBorder(
                                0, 10, 0, 10)));

        painelStatus.setPreferredSize(
                new Dimension(390, 38));

        painelStatus.add(
                chkAtivo, BorderLayout.CENTER);

        JPanel linha3 = criarLinha(

                criarGrupoCampo(
                        "Perfil de acesso", comboPerfil),

                criarGrupoCampo(
                        "Situação da conta", painelStatus));

        painel.add(linha3);
        painel.add(Box.createVerticalStrut(12));

        // DESCRIÇÃO DO PERFIL

        JLabel lblDescricaoPerfil = new JLabel();

        lblDescricaoPerfil.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        lblDescricaoPerfil.setForeground(COR_AZUL);

        lblDescricaoPerfil.setHorizontalAlignment(
                SwingConstants.CENTER);

        JPanel painelDescricao =
                new JPanel(new BorderLayout());

        painelDescricao.setBackground(
                new Color(237, 242, 248));

        painelDescricao.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 12, 10, 12));

        painelDescricao.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        painelDescricao.setPreferredSize(
                new Dimension(800, 44));

        painelDescricao.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 44));

        painelDescricao.add(
                lblDescricaoPerfil, BorderLayout.CENTER);

        comboPerfil.addActionListener(
                e -> atualizarDescricaoPerfil(
                        lblDescricaoPerfil));

        atualizarDescricaoPerfil(lblDescricaoPerfil);

        painel.add(painelDescricao);

        painel.add(Box.createVerticalStrut(25));

        painel.add(criarDivisor());
        painel.add(Box.createVerticalStrut(22));

        // BOTÕES

        JPanel botoes =
                new JPanel(new FlowLayout(
                        FlowLayout.RIGHT, 12, 0));

        botoes.setOpaque(false);

        botoes.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JButton btnLimpar =
                criarBotao("Limpar", false);

        btnLimpar.addActionListener(
                e -> limparCampos());

        JButton btnSalvar =
                criarBotao("Salvar usuário", true);

        btnSalvar.addActionListener(
                e -> salvarUsuario());

        botoes.add(btnLimpar);
        botoes.add(btnSalvar);

        painel.add(botoes);
    }

    // DESCRIÇÃO DO PERFIL SELECIONADO

    private void atualizarDescricaoPerfil(JLabel label) {

        String perfil =
                (String) comboPerfil.getSelectedItem();

        if ("ADMINISTRADOR".equals(perfil)) {

            label.setText(
                    "Administrador: perfil de gerenciamento do sistema.");

        } else {

            label.setText(
                    "Operador: perfil de acesso às operações permitidas.");
        }
    }

    // TÍTULOS DAS SEÇÕES

    private JLabel criarTituloSecao(String texto) {

        JLabel label = new JLabel(texto);

        label.setFont(
                new Font("SansSerif", Font.BOLD, 16));

        label.setForeground(COR_AZUL);

        label.setHorizontalAlignment(
                SwingConstants.CENTER);

        label.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        label.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 30));

        return label;
    }

    // CAMPOS DE TEXTO

    private JTextField criarCampoTexto() {

        JTextField campo = new JTextField();

        estilizarCampo(campo);

        return campo;
    }

    // CAMPOS DE SENHA

    private JPasswordField criarCampoSenha() {

        JPasswordField campo =
                new JPasswordField();

        estilizarCampo(campo);

        return campo;
    }

    // ESTILO DOS CAMPOS

    private void estilizarCampo(JTextField campo) {

        campo.setFont(
                new Font("SansSerif", Font.PLAIN, 13));

        campo.setForeground(COR_AZUL);

        campo.setBackground(COR_CAMPO);

        campo.setPreferredSize(
                new Dimension(390, 38));

        campo.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                COR_BORDA),

                        BorderFactory.createEmptyBorder(
                                5, 10, 5, 10)));
    }

    // AGRUPAMENTO DOS CAMPOS

    private JPanel criarGrupoCampo(
            String titulo, Component campo) {

        JPanel grupo =
                new JPanel(new BorderLayout(0, 7));

        grupo.setOpaque(false);

        JLabel label = new JLabel(titulo);

        label.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        label.setForeground(COR_CINZA);

        grupo.add(label, BorderLayout.NORTH);
        grupo.add(campo, BorderLayout.CENTER);

        return grupo;
    }

    // ORGANIZAÇÃO DAS LINHAS

    private JPanel criarLinha(JPanel... campos) {

        JPanel linha = new JPanel(
                new GridLayout(1, campos.length, 20, 0));

        linha.setOpaque(false);

        linha.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        linha.setPreferredSize(
                new Dimension(800, 62));

        linha.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 62));

        for (JPanel campo : campos) {

            linha.add(campo);
        }

        return linha;
    }

    // DIVISÓRIAS

    private JPanel criarDivisor() {

        JPanel divisor = new JPanel();

        divisor.setBackground(COR_BORDA);

        divisor.setPreferredSize(
                new Dimension(0, 1));

        divisor.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 1));

        divisor.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        return divisor;
    }

    // BOTÕES

    private JButton criarBotao(
            String texto, boolean principal) {

        JButton botao = new JButton(texto);

        botao.setFont(
                new Font("SansSerif", Font.BOLD, 13));

        botao.setFocusPainted(false);
        botao.setOpaque(true);
        botao.setContentAreaFilled(true);

        botao.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR));

        botao.setPreferredSize(
                new Dimension(
                        principal ? 160 : 145, 40));

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

    // SALVAR USUÁRIO

    private void salvarUsuario() {

        String nome =
                txtNome.getText().trim();

        String login =
                txtLogin.getText().trim();

        String senha =
                new String(
                        txtSenha.getPassword()).trim();

        String confirmarSenha =
                new String(
                        txtConfirmarSenha.getPassword()).trim();

        String perfil =
                comboPerfil.getSelectedItem().toString();

        boolean ativo =
                chkAtivo.isSelected();

        if (nome.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe o nome.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE);

            txtNome.requestFocus();

            return;
        }

        if (login.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe o login.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE);

            txtLogin.requestFocus();

            return;
        }

        if (senha.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe a senha.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE);

            txtSenha.requestFocus();

            return;
        }

        if (senha.length() < 6) {

            JOptionPane.showMessageDialog(
                    this,
                    "A senha deve ter pelo menos 6 caracteres.",
                    "Senha inválida",
                    JOptionPane.WARNING_MESSAGE);

            txtSenha.requestFocus();

            return;
        }

        if (!senha.equals(confirmarSenha)) {

            JOptionPane.showMessageDialog(
                    this,
                    "As senhas não conferem.",
                    "Senhas diferentes",
                    JOptionPane.WARNING_MESSAGE);

            txtConfirmarSenha.requestFocus();

            return;
        }

        String senhaHash =
                SenhaUtil.gerarHash(senha);

        Usuario usuario = new Usuario(

                nome,

                login,

                senhaHash,

                perfil,

                ativo
        );

        UsuarioDAO dao =
                new UsuarioDAO();

        boolean cadastrado =
                dao.cadastrar(usuario);

        if (cadastrado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Usuário cadastrado com sucesso.",
                    "Cadastro realizado",
                    JOptionPane.INFORMATION_MESSAGE);

            limparCampos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível cadastrar o usuário.\n" +
                    "Verifique se o login já existe.",
                    "Erro no cadastro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // LIMPAR CAMPOS

    private void limparCampos() {

        txtNome.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        txtConfirmarSenha.setText("");

        comboPerfil.setSelectedIndex(0);

        chkAtivo.setSelected(true);

        txtNome.requestFocus();
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
