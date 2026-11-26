
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
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dao.MovimentacaoEstoqueDAO;
import dao.ProdutoDAO;
import model.MovimentacaoEstoque;
import model.Produto;
import model.Usuario;

public class PainelEntradaEstoque extends JPanel {

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

    private JComboBox<Produto> comboProduto;
    private JTextField txtQuantidade;
    private JTextField txtValorUnitario;
    private JTextArea txtObservacao;

    private final Usuario usuarioLogado;

    // CONSTRUTOR ORIGINAL

    public PainelEntradaEstoque() {

        this(null);
    }

    // CONSTRUTOR COM USUÁRIO LOGADO

    public PainelEntradaEstoque(Usuario usuarioLogado) {

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

        carregarProdutos();
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
                new JLabel("Entrada de Estoque");

        titulo.setFont(
                new Font("SansSerif", Font.BOLD, 25));

        titulo.setForeground(COR_AZUL);

        titulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel(
                "Registre a entrada de produtos no estoque");

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

    // IDENTIFICAÇÃO DO RESPONSÁVEL

    private JPanel criarPainelResponsavel() {

        PainelArredondado painel =
                new PainelArredondado(
                        new Color(237, 242, 248), 12);

        painel.setLayout(
                new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 15, 12, 15));

        painel.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        painel.setPreferredSize(
                new Dimension(800, 70));

        painel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 70));

        JLabel lblTitulo = new JLabel(
                "Responsável pela movimentação");

        lblTitulo.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        lblTitulo.setForeground(COR_CINZA);

        lblTitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        String nomeResponsavel =
                usuarioLogado != null
                        ? usuarioLogado.getNome()
                        : "Usuário não identificado";

        JLabel lblResponsavel =
                new JLabel(nomeResponsavel);

        lblResponsavel.setFont(
                new Font("SansSerif", Font.BOLD, 16));

        lblResponsavel.setForeground(COR_AZUL);

        lblResponsavel.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        painel.add(lblTitulo);

        painel.add(Box.createVerticalStrut(5));

        painel.add(lblResponsavel);

        return painel;
    }

    // FORMULÁRIO

    private void criarCampos(JPanel painel) {

        // RESPONSÁVEL PELA ENTRADA

        painel.add(criarPainelResponsavel());

        painel.add(Box.createVerticalStrut(22));

        painel.add(criarDivisor());

        painel.add(Box.createVerticalStrut(22));

        // INFORMAÇÕES DA ENTRADA

        painel.add(criarTituloSecao(
                "Informações da entrada"));

        painel.add(Box.createVerticalStrut(18));

        comboProduto = new JComboBox<>();

        comboProduto.setFont(
                new Font("SansSerif", Font.PLAIN, 13));

        comboProduto.setForeground(COR_AZUL);
        comboProduto.setBackground(COR_CAMPO);

        comboProduto.setPreferredSize(
                new Dimension(800, 38));

        JPanel linha1 = criarLinha(
                criarGrupoCampo(
                        "Produto", comboProduto));

        painel.add(linha1);
        painel.add(Box.createVerticalStrut(18));

        txtQuantidade = criarCampoTexto();

        txtValorUnitario = criarCampoTexto();

        txtQuantidade.setToolTipText(
                "Informe a quantidade de produtos recebidos");

        txtValorUnitario.setToolTipText(
                "Informe o valor unitário do produto");

        JPanel linha2 = criarLinha(

                criarGrupoCampo(
                        "Quantidade", txtQuantidade),

                criarGrupoCampo(
                        "Valor unitário (R$)",
                        txtValorUnitario));

        painel.add(linha2);
        painel.add(Box.createVerticalStrut(25));

        painel.add(criarDivisor());
        painel.add(Box.createVerticalStrut(22));

        // OBSERVAÇÕES

        painel.add(criarTituloSecao(
                "Observações da movimentação"));

        painel.add(Box.createVerticalStrut(16));

        JLabel lblObservacao =
                new JLabel("Observação (opcional)");

        lblObservacao.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        lblObservacao.setForeground(COR_CINZA);

        txtObservacao = new JTextArea(4, 30);

        txtObservacao.setLineWrap(true);
        txtObservacao.setWrapStyleWord(true);

        txtObservacao.setFont(
                new Font("SansSerif", Font.PLAIN, 13));

        txtObservacao.setForeground(COR_AZUL);
        txtObservacao.setBackground(COR_CAMPO);

        txtObservacao.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 12, 10, 12));

        txtObservacao.setToolTipText(
                "Informe detalhes sobre a entrada, se necessário");

        JScrollPane scrollObservacao =
                new JScrollPane(txtObservacao);

        scrollObservacao.setBorder(
                BorderFactory.createLineBorder(
                        COR_BORDA));

        scrollObservacao.setPreferredSize(
                new Dimension(800, 100));

        JPanel grupoObservacao =
                new JPanel(new BorderLayout(0, 7));

        grupoObservacao.setOpaque(false);

        grupoObservacao.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        grupoObservacao.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 125));

        grupoObservacao.add(
                lblObservacao, BorderLayout.NORTH);

        grupoObservacao.add(
                scrollObservacao, BorderLayout.CENTER);

        painel.add(grupoObservacao);

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

        JButton btnRegistrar =
                criarBotao("Registrar entrada", true);

        btnRegistrar.addActionListener(
                e -> registrarEntrada());

        botoes.add(btnLimpar);
        botoes.add(btnRegistrar);

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
                new Dimension(
                        principal ? 170 : 145, 40));

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

    // CARREGAR PRODUTOS

    private void carregarProdutos() {

        ProdutoDAO produtoDAO =
                new ProdutoDAO();

        comboProduto.removeAllItems();

        for (Produto produto :
                produtoDAO.listarParaCombo()) {

            comboProduto.addItem(produto);
        }
    }

    // REGISTRAR ENTRADA

    private void registrarEntrada() {

        // VERIFICAR USUÁRIO RESPONSÁVEL

        if (usuarioLogado == null
                || usuarioLogado.getIdUsuario() <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível identificar o usuário logado.",
                    "Usuário não identificado",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        // VERIFICAR PRODUTO

        Produto produtoSelecionado =
                (Produto) comboProduto.getSelectedItem();

        if (produtoSelecionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um produto.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        String quantidadeTexto =
                txtQuantidade.getText().trim();

        String valorTexto =
                txtValorUnitario.getText()
                        .trim().replace(",", ".");

        String observacao =
                txtObservacao.getText().trim();

        if (quantidadeTexto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe a quantidade.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE);

            txtQuantidade.requestFocus();

            return;
        }

        if (valorTexto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe o valor unitário.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE);

            txtValorUnitario.requestFocus();

            return;
        }

        // LIMITE DE CARACTERES DA OBSERVAÇÃO

        if (observacao.length() > 255) {

            JOptionPane.showMessageDialog(
                    this,
                    "A observação deve ter no máximo 255 caracteres.",
                    "Observação muito longa",
                    JOptionPane.WARNING_MESSAGE);

            txtObservacao.requestFocus();

            return;
        }

        try {

            int quantidade =
                    Integer.parseInt(quantidadeTexto);

            BigDecimal valorUnitario =
                    new BigDecimal(valorTexto);

            if (quantidade <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "A quantidade deve ser maior que zero.",
                        "Quantidade inválida",
                        JOptionPane.WARNING_MESSAGE);

                txtQuantidade.requestFocus();

                return;
            }

            if (valorUnitario.compareTo(
                    BigDecimal.ZERO) < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "O valor unitário não pode ser negativo.",
                        "Valor inválido",
                        JOptionPane.WARNING_MESSAGE);

                txtValorUnitario.requestFocus();

                return;
            }

            // CRIAR MOVIMENTAÇÃO COM O RESPONSÁVEL

            MovimentacaoEstoque movimentacao =
                    new MovimentacaoEstoque(

                            produtoSelecionado.getIdProduto(),

                            "ENTRADA",

                            quantidade,

                            valorUnitario,

                            observacao,

                            usuarioLogado.getIdUsuario()
                    );

            MovimentacaoEstoqueDAO dao =
                    new MovimentacaoEstoqueDAO();

            boolean registrado =
                    dao.registrarEntrada(movimentacao);

            if (registrado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Entrada registrada com sucesso.",
                        "Entrada realizada",
                        JOptionPane.INFORMATION_MESSAGE);

                limparCampos();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível registrar a entrada.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    "Digite quantidade e valor válidos.",
                    "Valores inválidos",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // LIMPAR CAMPOS

    private void limparCampos() {

        txtQuantidade.setText("");
        txtValorUnitario.setText("");
        txtObservacao.setText("");

        if (comboProduto.getItemCount() > 0) {

            comboProduto.setSelectedIndex(0);
        }

        txtQuantidade.requestFocus();
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
