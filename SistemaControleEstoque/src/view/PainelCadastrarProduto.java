
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

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
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import dao.CategoriaDAO;
import dao.FornecedorDAO;
import dao.ProdutoDAO;
import model.Categoria;
import model.Fornecedor;
import model.Produto;

public class PainelCadastrarProduto extends JPanel {

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
    private JTextField txtValorCusto;
    private JTextField txtValorVenda;
    private JTextField txtQuantidade;
    private JTextField txtEstoqueMinimo;
    private JTextField txtDataValidade;
    private JTextArea txtDescricao;

    private JComboBox<Categoria> comboCategoria;
    private JComboBox<Fornecedor> comboFornecedor;

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    public PainelCadastrarProduto() {

        setLayout(new BorderLayout());
        setBackground(COR_FUNDO);

        JPanel conteudo = new JPanel();
        conteudo.setOpaque(false);
        conteudo.setLayout(
                new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBorder(
                BorderFactory.createEmptyBorder(
                        28, 25, 30, 25));

        JPanel cabecalho = criarCabecalho();
        conteudo.add(cabecalho);
        conteudo.add(Box.createVerticalStrut(22));

        PainelArredondado formulario =
                new PainelArredondado(Color.WHITE, 18);
        formulario.setLayout(new BorderLayout());
        formulario.setBorder(
                BorderFactory.createEmptyBorder(
                        25, 28, 25, 28));
        formulario.setMaximumSize(
                new Dimension(950, 650));

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
        centralizador.add(formulario);

        conteudo.add(centralizador);

        JScrollPane scroll = new JScrollPane(conteudo);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(COR_FUNDO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);

        carregarCategorias();
        carregarFornecedores();
    }

    // CABEÇALHO

    private JPanel criarCabecalho() {

        JPanel painel = new JPanel();
        painel.setOpaque(false);
        painel.setLayout(
                new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo =
                new JLabel("Cadastro de Produtos");
        titulo.setFont(
                new Font("SansSerif", Font.BOLD, 25));
        titulo.setForeground(COR_AZUL);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel(
                "Preencha as informações para cadastrar um novo produto");
        subtitulo.setFont(
                new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(COR_CINZA);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        painel.add(titulo);
        painel.add(Box.createVerticalStrut(6));
        painel.add(subtitulo);

        return painel;
    }

    // FORMULÁRIO

    private void criarCampos(JPanel painel) {

        JLabel secao1 =
                criarTituloSecao("Informações do produto");
        painel.add(secao1);
        painel.add(Box.createVerticalStrut(16));

        txtNome = criarCampoTexto();
        comboCategoria = criarCombo();
        comboFornecedor = criarCombo();
        txtDataValidade = criarCampoTexto();

        txtDataValidade.setToolTipText(
                "Informe no formato dd/MM/aaaa. Exemplo: 30/11/2026");

        JPanel linha1 = criarLinhaDuasColunas(
                criarGrupoCampo(
                        "Nome do produto", txtNome),
                criarGrupoCampo(
                        "Categoria", comboCategoria));

        JPanel linha2 = criarLinhaDuasColunas(
                criarGrupoCampo(
                        "Fornecedor (opcional)", comboFornecedor),
                criarGrupoCampo(
                        "Data de validade (opcional)", txtDataValidade));

        painel.add(linha1);
        painel.add(Box.createVerticalStrut(16));
        painel.add(linha2);
        painel.add(Box.createVerticalStrut(25));

        painel.add(criarDivisor());
        painel.add(Box.createVerticalStrut(22));

        JLabel secao2 =
                criarTituloSecao("Valores e estoque");
        painel.add(secao2);
        painel.add(Box.createVerticalStrut(16));

        txtValorCusto = criarCampoTexto();
        txtValorVenda = criarCampoTexto();
        txtQuantidade = criarCampoTexto();
        txtEstoqueMinimo = criarCampoTexto();

        JPanel linha3 = criarLinhaQuatroColunas(
                criarGrupoCampo(
                        "Valor de custo", txtValorCusto),
                criarGrupoCampo(
                        "Valor de venda", txtValorVenda),
                criarGrupoCampo(
                        "Quantidade", txtQuantidade),
                criarGrupoCampo(
                        "Estoque mínimo", txtEstoqueMinimo));

        painel.add(linha3);
        painel.add(Box.createVerticalStrut(25));

        painel.add(criarDivisor());
        painel.add(Box.createVerticalStrut(22));

        JLabel secao3 =
                criarTituloSecao("Descrição do produto");
        painel.add(secao3);
        painel.add(Box.createVerticalStrut(12));

        txtDescricao = new JTextArea(4, 30);
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        txtDescricao.setFont(
                new Font("SansSerif", Font.PLAIN, 13));
        txtDescricao.setBackground(COR_CAMPO);
        txtDescricao.setForeground(COR_AZUL);
        txtDescricao.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 12, 10, 12));

        JScrollPane scrollDescricao =
                new JScrollPane(txtDescricao);
        scrollDescricao.setBorder(
                BorderFactory.createLineBorder(
                        COR_BORDA));
        scrollDescricao.setPreferredSize(
                new Dimension(800, 95));
        scrollDescricao.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 95));

        painel.add(scrollDescricao);
        painel.add(Box.createVerticalStrut(25));

        JPanel botoes =
                new JPanel(new FlowLayout(
                        FlowLayout.RIGHT, 12, 0));
        botoes.setOpaque(false);

        JButton btnLimpar =
                criarBotao("Limpar", false);
        btnLimpar.addActionListener(
                e -> limparCampos());

        JButton btnSalvar =
                criarBotao("Salvar produto", true);
        btnSalvar.addActionListener(
                e -> salvarProduto());

        botoes.add(btnLimpar);
        botoes.add(btnSalvar);

        painel.add(botoes);
    }

    // COMPONENTES VISUAIS

    private JLabel criarTituloSecao(String texto) {

        JLabel label = new JLabel(texto);

        label.setFont(
                new Font("SansSerif", Font.BOLD, 16));

        label.setForeground(COR_AZUL);

        label.setHorizontalAlignment(SwingConstants.CENTER);

        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        label.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 30));

        return label;
    }

    private JTextField criarCampoTexto() {

        JTextField campo = new JTextField();
        campo.setFont(
                new Font("SansSerif", Font.PLAIN, 13));
        campo.setForeground(COR_AZUL);
        campo.setBackground(COR_CAMPO);
        campo.setPreferredSize(new Dimension(200, 38));
        campo.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COR_BORDA),
                        BorderFactory.createEmptyBorder(
                                5, 10, 5, 10)));

        return campo;
    }

    private <T> JComboBox<T> criarCombo() {

        JComboBox<T> combo = new JComboBox<>();
        combo.setFont(
                new Font("SansSerif", Font.PLAIN, 13));
        combo.setForeground(COR_AZUL);
        combo.setBackground(COR_CAMPO);
        combo.setPreferredSize(new Dimension(200, 38));

        return combo;
    }

    private JPanel criarGrupoCampo(
            String titulo, Component campo) {

        JPanel grupo = new JPanel(new BorderLayout(0, 7));
        grupo.setOpaque(false);

        JLabel label = new JLabel(titulo);
        label.setFont(
                new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(COR_CINZA);

        grupo.add(label, BorderLayout.NORTH);
        grupo.add(campo, BorderLayout.CENTER);

        return grupo;
    }

    private JPanel criarLinhaDuasColunas(
            JPanel campo1, JPanel campo2) {

        JPanel linha =
                new JPanel(new GridBagLayout());
        linha.setOpaque(false);
        linha.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 70));

        GridBagConstraints gbc =
                new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 10);

        gbc.gridx = 0;
        linha.add(campo1, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 0, 0);
        linha.add(campo2, gbc);

        return linha;
    }

    private JPanel criarLinhaQuatroColunas(
            JPanel campo1,
            JPanel campo2,
            JPanel campo3,
            JPanel campo4) {

        JPanel linha =
                new JPanel(new GridBagLayout());
        linha.setOpaque(false);
        linha.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 70));

        JPanel[] campos = {
                campo1, campo2, campo3, campo4
        };

        GridBagConstraints gbc =
                new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        for (int i = 0; i < campos.length; i++) {
            gbc.gridx = i;
            gbc.insets = new Insets(
                    0,
                    i == 0 ? 0 : 8,
                    0,
                    i == campos.length - 1 ? 0 : 8);

            linha.add(campos[i], gbc);
        }

        return linha;
    }

    private JPanel criarDivisor() {

        JPanel divisor = new JPanel();
        divisor.setBackground(COR_BORDA);
        divisor.setPreferredSize(new Dimension(0, 1));
        divisor.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 1));

        return divisor;
    }

    private JButton criarBotao(
            String texto, boolean principal) {

        JButton botao = new JButton(texto);
        botao.setFont(
                new Font("SansSerif", Font.BOLD, 13));
        botao.setFocusPainted(false);
        botao.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR));
        botao.setPreferredSize(
                new Dimension(145, 40));

        if (principal) {
            botao.setBackground(COR_AZUL);
            botao.setForeground(Color.WHITE);
            botao.setOpaque(true);
            botao.setBorder(
                    BorderFactory.createLineBorder(
                            COR_AZUL));
        } else {
            botao.setBackground(
                    new Color(241, 245, 249));
            botao.setForeground(COR_AZUL);
            botao.setOpaque(true);
            botao.setBorder(
                    BorderFactory.createLineBorder(
                            COR_BORDA));
        }

        return botao;
    }

    // CARREGAMENTO DAS CATEGORIAS

    private void carregarCategorias() {

        CategoriaDAO categoriaDAO =
                new CategoriaDAO();

        comboCategoria.removeAllItems();

        for (Categoria categoria :
                categoriaDAO.listar()) {

            comboCategoria.addItem(categoria);
        }
    }

    // CARREGAMENTO DOS FORNECEDORES

    private void carregarFornecedores() {

        FornecedorDAO fornecedorDAO =
                new FornecedorDAO();

        comboFornecedor.removeAllItems();

        comboFornecedor.addItem(null);

        for (Fornecedor fornecedor :
                fornecedorDAO.listar()) {

            comboFornecedor.addItem(fornecedor);
        }

        comboFornecedor.setRenderer(
                new DefaultListCellRenderer() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus) {

                        super.getListCellRendererComponent(
                                list, value, index,
                                isSelected, cellHasFocus);

                        if (value == null) {
                            setText("Sem fornecedor");
                        } else {
                            setText(value.toString());
                        }

                        return this;
                    }
                });

        comboFornecedor.setSelectedIndex(0);
    }

    // SALVAR PRODUTO - REGRAS ORIGINAIS PRESERVADAS

    private void salvarProduto() {

        String nome = txtNome.getText().trim();
        String descricao = txtDescricao.getText().trim();

        String valorCustoTexto =
                txtValorCusto.getText().trim()
                        .replace(",", ".");

        String valorVendaTexto =
                txtValorVenda.getText().trim()
                        .replace(",", ".");

        String quantidadeTexto =
                txtQuantidade.getText().trim();

        String estoqueMinimoTexto =
                txtEstoqueMinimo.getText().trim();

        String validadeTexto =
                txtDataValidade.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Informe o nome do produto.");
            txtNome.requestFocus();
            return;
        }

        if (valorCustoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Informe o valor de custo.");
            txtValorCusto.requestFocus();
            return;
        }

        if (valorVendaTexto.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Informe o valor de venda.");
            txtValorVenda.requestFocus();
            return;
        }

        if (quantidadeTexto.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Informe a quantidade em estoque.");
            txtQuantidade.requestFocus();
            return;
        }

        if (estoqueMinimoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Informe o estoque mínimo.");
            txtEstoqueMinimo.requestFocus();
            return;
        }

        if (comboCategoria.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(
                    this, "Selecione uma categoria.");
            return;
        }

        LocalDate dataValidade = null;

        if (!validadeTexto.isEmpty()) {

            try {
                dataValidade = LocalDate.parse(
                        validadeTexto, FORMATO_DATA);

            } catch (DateTimeParseException erro) {

                JOptionPane.showMessageDialog(
                        this,
                        "Informe uma data de validade válida.\n" +
                        "Utilize o formato dd/MM/aaaa.\n" +
                        "Exemplo: 30/11/2026.",
                        "Data inválida",
                        JOptionPane.WARNING_MESSAGE);

                txtDataValidade.requestFocus();
                return;
            }
        }

        try {

            BigDecimal valorCusto =
                    new BigDecimal(valorCustoTexto);

            BigDecimal valorVenda =
                    new BigDecimal(valorVendaTexto);

            int quantidade =
                    Integer.parseInt(quantidadeTexto);

            int estoqueMinimo =
                    Integer.parseInt(estoqueMinimoTexto);

            if (valorCusto.compareTo(
                    BigDecimal.ZERO) < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "O valor de custo não pode ser negativo.");

                txtValorCusto.requestFocus();
                return;
            }

            if (valorVenda.compareTo(
                    BigDecimal.ZERO) < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "O valor de venda não pode ser negativo.");

                txtValorVenda.requestFocus();
                return;
            }

            if (quantidade < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "A quantidade não pode ser negativa.");

                txtQuantidade.requestFocus();
                return;
            }

            if (estoqueMinimo < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "O estoque mínimo não pode ser negativo.");

                txtEstoqueMinimo.requestFocus();
                return;
            }

            Categoria categoriaSelecionada =
                    (Categoria) comboCategoria
                            .getSelectedItem();

            Fornecedor fornecedorSelecionado =
                    (Fornecedor) comboFornecedor
                            .getSelectedItem();

            int idFornecedor = 0;

            if (fornecedorSelecionado != null) {
                idFornecedor =
                        fornecedorSelecionado
                                .getIdFornecedor();
            }

            Produto produto = new Produto(
                    nome,
                    descricao,
                    valorCusto,
                    valorVenda,
                    quantidade,
                    estoqueMinimo,
                    categoriaSelecionada.getIdCategoria(),
                    idFornecedor);

            produto.setDataValidade(dataValidade);

            ProdutoDAO produtoDAO = new ProdutoDAO();

            boolean cadastrado =
                    produtoDAO.cadastrar(produto);

            if (cadastrado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Produto cadastrado com sucesso.",
                        "Cadastro realizado",
                        JOptionPane.INFORMATION_MESSAGE);

                limparCampos();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível cadastrar o produto.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    "Digite valores numéricos válidos.",
                    "Erro de conversão",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // LIMPAR CAMPOS

    private void limparCampos() {

        txtNome.setText("");
        txtDescricao.setText("");
        txtValorCusto.setText("");
        txtValorVenda.setText("");
        txtQuantidade.setText("");
        txtEstoqueMinimo.setText("");
        txtDataValidade.setText("");

        if (comboCategoria.getItemCount() > 0) {
            comboCategoria.setSelectedIndex(0);
        }

        if (comboFornecedor.getItemCount() > 0) {
            comboFornecedor.setSelectedIndex(0);
        }

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
