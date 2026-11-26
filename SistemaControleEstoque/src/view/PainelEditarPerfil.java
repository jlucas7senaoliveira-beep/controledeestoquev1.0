
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dao.UsuarioDAO;
import model.Usuario;

public class PainelEditarPerfil extends JPanel {

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
    private JPasswordField txtNovaSenha;
    private JPasswordField txtConfirmarSenha;

    private JLabel lblPerfil;
    private JButton btnSalvar;

    private Usuario usuarioLogado;

    public PainelEditarPerfil(Usuario usuarioLogado) {

        this.usuarioLogado = usuarioLogado;

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

        carregarDados();
    }

    // CABEÇALHO

    private JPanel criarCabecalho() {

        JPanel painel = new JPanel();

        painel.setOpaque(false);

        painel.setLayout(
                new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JLabel titulo = new JLabel("Meu Perfil");

        titulo.setFont(
                new Font("SansSerif", Font.BOLD, 25));

        titulo.setForeground(COR_AZUL);

        titulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel(
                "Gerencie suas informações pessoais e sua senha de acesso");

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

        // IDENTIFICAÇÃO DO PERFIL

        painel.add(criarIdentificacaoPerfil());

        painel.add(Box.createVerticalStrut(25));

        painel.add(criarDivisor());

        painel.add(Box.createVerticalStrut(22));

        // INFORMAÇÕES PESSOAIS

        painel.add(criarTituloSecao(
                "Informações pessoais"));

        painel.add(Box.createVerticalStrut(18));

        txtNome = criarCampoTexto();

        txtLogin = criarCampoTexto();

        txtNome.setToolTipText(
                "Informe seu nome completo");

        txtLogin.setToolTipText(
                "Informe seu login de acesso ao sistema");

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

        txtNovaSenha = criarCampoSenha();

        txtConfirmarSenha = criarCampoSenha();

        txtNovaSenha.setToolTipText(
                "Deixe em branco para manter a senha atual");

        txtConfirmarSenha.setToolTipText(
                "Confirme a nova senha");

        JPanel linha2 = criarLinha(

                criarGrupoCampo(
                        "Nova senha (opcional)",
                        txtNovaSenha),

                criarGrupoCampo(
                        "Confirmar nova senha",
                        txtConfirmarSenha));

        painel.add(linha2);

        painel.add(Box.createVerticalStrut(12));

        painel.add(criarOrientacaoSenha());

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

        JButton btnLimparSenha =
                criarBotao("Limpar campos de senha", false);

        btnLimparSenha.addActionListener(
                e -> limparSenha());

        btnSalvar =
                criarBotao("Salvar alterações", true);

        btnSalvar.addActionListener(
                e -> salvarPerfil());

        botoes.add(btnLimparSenha);
        botoes.add(btnSalvar);

        painel.add(botoes);
    }

    // IDENTIFICAÇÃO DO PERFIL ATUAL

    private JPanel criarIdentificacaoPerfil() {

        PainelArredondado painel =
                new PainelArredondado(
                        new Color(237, 242, 248), 12);

        painel.setLayout(
                new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 15, 18, 15));

        painel.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        painel.setPreferredSize(
                new Dimension(800, 105));

        painel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 105));

        JLabel titulo =
                new JLabel("Seu perfil de acesso");

        titulo.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        titulo.setForeground(COR_CINZA);

        titulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        lblPerfil = new JLabel("Não identificado");

        lblPerfil.setFont(
                new Font("SansSerif", Font.BOLD, 20));

        lblPerfil.setForeground(COR_AZUL);

        lblPerfil.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JLabel descricao = new JLabel(
                "O perfil de acesso é definido pela administração do sistema.");

        descricao.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        descricao.setForeground(COR_CINZA);

        descricao.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        painel.add(titulo);
        painel.add(Box.createVerticalStrut(5));
        painel.add(lblPerfil);
        painel.add(Box.createVerticalStrut(5));
        painel.add(descricao);

        return painel;
    }

    // ORIENTAÇÃO SOBRE A SENHA

    private JPanel criarOrientacaoSenha() {

        JPanel painel = new JPanel();

        painel.setLayout(
                new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.setBackground(
                new Color(237, 242, 248));

        painel.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 15, 12, 15));

        painel.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        painel.setPreferredSize(
                new Dimension(800, 65));

        painel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 65));

        JLabel linha1 = new JLabel(
                "Deixe os campos vazios para manter sua senha atual.");

        linha1.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        linha1.setForeground(COR_AZUL);

        linha1.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JLabel linha2 = new JLabel(
                "A nova senha deve conter pelo menos 6 caracteres.");

        linha2.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        linha2.setForeground(COR_CINZA);

        linha2.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        painel.add(linha1);
        painel.add(Box.createVerticalStrut(5));
        painel.add(linha2);

        return painel;
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
                        principal ? 180 : 200, 40));

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

    // CARREGAR DADOS DO USUÁRIO LOGADO

    private void carregarDados() {

        if (usuarioLogado == null) {

            btnSalvar.setEnabled(false);

            txtNome.setEditable(false);
            txtLogin.setEditable(false);
            txtNovaSenha.setEditable(false);
            txtConfirmarSenha.setEditable(false);

            lblPerfil.setText("Não identificado");

            return;
        }

        txtNome.setText(usuarioLogado.getNome());

        txtLogin.setText(usuarioLogado.getLogin());

        String perfil = usuarioLogado.getPerfil();

        if ("ADMINISTRADOR".equalsIgnoreCase(perfil)
                || "ADMIN".equalsIgnoreCase(perfil)) {

            lblPerfil.setText("Administrador");

        } else if ("OPERADOR".equalsIgnoreCase(perfil)) {

            lblPerfil.setText("Operador");

        } else {

            lblPerfil.setText(
                    perfil == null
                            ? "Não identificado"
                            : perfil);
        }
    }

    // SALVAR PERFIL

    private void salvarPerfil() {

        if (usuarioLogado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Usuário logado não encontrado.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        String nome =
                txtNome.getText().trim();

        String login =
                txtLogin.getText().trim();

        String novaSenha =
                new String(
                        txtNovaSenha.getPassword()).trim();

        String confirmarSenha =
                new String(
                        txtConfirmarSenha.getPassword()).trim();

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

        // VALIDAR SENHA APENAS SE HOUVER ALTERAÇÃO

        if (!novaSenha.isEmpty()
                || !confirmarSenha.isEmpty()) {

            if (novaSenha.length() < 6) {

                JOptionPane.showMessageDialog(
                        this,
                        "A senha deve ter pelo menos 6 caracteres.",
                        "Senha inválida",
                        JOptionPane.WARNING_MESSAGE);

                txtNovaSenha.requestFocus();

                return;
            }

            if (!novaSenha.equals(confirmarSenha)) {

                JOptionPane.showMessageDialog(
                        this,
                        "As senhas não conferem.",
                        "Senhas diferentes",
                        JOptionPane.WARNING_MESSAGE);

                txtConfirmarSenha.requestFocus();

                return;
            }
        }

        // GUARDAR OS DADOS ANTERIORES

        String nomeAnterior =
                usuarioLogado.getNome();

        String loginAnterior =
                usuarioLogado.getLogin();

        usuarioLogado.setNome(nome);
        usuarioLogado.setLogin(login);

        UsuarioDAO dao = new UsuarioDAO();

        boolean atualizado =
                dao.atualizarPerfil(
                        usuarioLogado, novaSenha);

        if (atualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Perfil atualizado com sucesso.",
                    "Atualização realizada",
                    JOptionPane.INFORMATION_MESSAGE);

            limparSenha();

        } else {

            // RESTAURAR DADOS EM CASO DE FALHA

            usuarioLogado.setNome(nomeAnterior);
            usuarioLogado.setLogin(loginAnterior);

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível atualizar o perfil.",
                    "Erro na atualização",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // LIMPAR CAMPOS DE SENHA

    private void limparSenha() {

        txtNovaSenha.setText("");

        txtConfirmarSenha.setText("");

        txtNovaSenha.requestFocusInWindow();
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
