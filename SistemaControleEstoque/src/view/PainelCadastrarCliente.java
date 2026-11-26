
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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dao.ClienteDAO;
import model.Cliente;

public class PainelCadastrarCliente extends JPanel {

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
    private JTextField txtCpf;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTextField txtCidade;

    public PainelCadastrarCliente() {

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
                new JLabel("Cadastro de Clientes");

        titulo.setFont(
                new Font("SansSerif", Font.BOLD, 25));

        titulo.setForeground(COR_AZUL);

        titulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel(
                "Preencha as informações para cadastrar um novo cliente");

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

        painel.add(criarTituloSecao(
                "Informações pessoais"));

        painel.add(Box.createVerticalStrut(18));

        txtNome = criarCampoTexto();
        txtCpf = criarCampoTexto();
        txtCidade = criarCampoTexto();

        JPanel linha1 = criarLinha(
                criarGrupoCampo(
                        "Nome completo", txtNome),

                criarGrupoCampo(
                        "CPF", txtCpf));

        painel.add(linha1);
        painel.add(Box.createVerticalStrut(18));

        JPanel linha2 = criarLinha(
                criarGrupoCampo(
                        "Cidade", txtCidade));

        painel.add(linha2);
        painel.add(Box.createVerticalStrut(25));

        painel.add(criarDivisor());
        painel.add(Box.createVerticalStrut(22));

        painel.add(criarTituloSecao(
                "Informações de contato"));

        painel.add(Box.createVerticalStrut(18));

        txtTelefone = criarCampoTexto();
        txtEmail = criarCampoTexto();

        JPanel linha3 = criarLinha(
                criarGrupoCampo(
                        "Telefone", txtTelefone),

                criarGrupoCampo(
                        "E-mail", txtEmail));

        painel.add(linha3);
        painel.add(Box.createVerticalStrut(25));

        painel.add(criarDivisor());
        painel.add(Box.createVerticalStrut(22));

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
                criarBotao("Salvar cliente", true);

        btnSalvar.addActionListener(
                e -> salvarCliente());

        botoes.add(btnLimpar);
        botoes.add(btnSalvar);

        painel.add(botoes);
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

        return campo;
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
                new Dimension(145, 40));

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

    // SALVAR CLIENTE

    private void salvarCliente() {

        String nome =
                txtNome.getText().trim();

        String cpf =
                txtCpf.getText().trim();

        String telefone =
                txtTelefone.getText().trim();

        String email =
                txtEmail.getText().trim();

        String cidade =
                txtCidade.getText().trim();

        if (nome.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe o nome do cliente.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE);

            txtNome.requestFocus();

            return;
        }

        if (!email.isEmpty() && !email.contains("@")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe um e-mail válido.",
                    "E-mail inválido",
                    JOptionPane.WARNING_MESSAGE);

            txtEmail.requestFocus();

            return;
        }

        Cliente cliente = new Cliente(
                nome,
                cpf,
                telefone,
                email,
                cidade);

        ClienteDAO clienteDAO = new ClienteDAO();

        boolean cadastrado =
                clienteDAO.cadastrar(cliente);

        if (cadastrado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente cadastrado com sucesso.",
                    "Cadastro realizado",
                    JOptionPane.INFORMATION_MESSAGE);

            limparCampos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível cadastrar o cliente.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // LIMPAR CAMPOS

    private void limparCampos() {

        txtNome.setText("");
        txtCpf.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtCidade.setText("");

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
