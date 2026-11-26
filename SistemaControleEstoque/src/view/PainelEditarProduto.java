
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dao.CategoriaDAO;
import dao.FornecedorDAO;
import dao.ProdutoDAO;
import model.Categoria;
import model.Fornecedor;
import model.Produto;

public class PainelEditarProduto extends JPanel {

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

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    private JTextField txtNome;
    private JTextField txtValorCusto;
    private JTextField txtValorVenda;
    private JTextField txtQuantidade;
    private JTextField txtEstoqueMinimo;
    private JTextField txtDataValidade;

    private JTextArea txtDescricao;

    private JComboBox<Categoria> comboCategoria;
    private JComboBox<Fornecedor> comboFornecedor;

    private int idProduto;
    private TelaPrincipal telaPrincipal;

    public PainelEditarProduto() {

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

        carregarCategorias();
        carregarFornecedores();
    }

    public PainelEditarProduto(
            TelaPrincipal telaPrincipal,
            int idProduto) {

        this();

        this.telaPrincipal = telaPrincipal;
        this.idProduto = idProduto;

        carregarProduto();
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
                new JLabel("Editar Produto");

        titulo.setFont(
                new Font("SansSerif", Font.BOLD, 25));

        titulo.setForeground(COR_AZUL);

        titulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel(
                "Atualize as informações do produto cadastrado");

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
                "Informações do produto"));

        painel.add(Box.createVerticalStrut(16));

        txtNome = criarCampoTexto();

        comboCategoria = criarCombo();

        comboFornecedor = criarCombo();

        txtDataValidade = criarCampoTexto();

        txtDataValidade.setToolTipText(
                "Formato: dd/MM/aaaa. Exemplo: 30/11/2026");

        JPanel linha1 = criarLinha(
                criarGrupoCampo(
                        "Nome do produto", txtNome),

                criarGrupoCampo(
                        "Categoria", comboCategoria));

        JPanel linha2 = criarLinha(
                criarGrupoCampo(
                        "Fornecedor (opcional)",
                        comboFornecedor),

                criarGrupoCampo(
                        "Data de validade (opcional)",
                        txtDataValidade));

        painel.add(linha1);
        painel.add(Box.createVerticalStrut(16));

        painel.add(linha2);
        painel.add(Box.createVerticalStrut(25));

        painel.add(criarDivisor());
        painel.add(Box.createVerticalStrut(22));

        painel.add(criarTituloSecao(
                "Valores e estoque"));

        painel.add(Box.createVerticalStrut(16));

        txtValorCusto = criarCampoTexto();

        txtValorVenda = criarCampoTexto();

        txtQuantidade = criarCampoTexto();

        txtEstoqueMinimo = criarCampoTexto();

        JPanel linha3 = criarLinha(
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

        painel.add(criarTituloSecao(
                "Descrição do produto"));

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

        scrollDescricao.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        painel.add(scrollDescricao);

        painel.add(Box.createVerticalStrut(25));

        JPanel botoes =
                new JPanel(new FlowLayout(
                        FlowLayout.RIGHT, 12, 0));

        botoes.setOpaque(false);

        botoes.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JButton btnVoltar =
                criarBotao("Voltar", false);

        btnVoltar.addActionListener(
                e -> voltar());

        JButton btnSalvar =
                criarBotao("Salvar alterações", true);

        btnSalvar.addActionListener(
                e -> salvarAlteracoes());

        botoes.add(btnVoltar);
        botoes.add(btnSalvar);

        painel.add(botoes);
    }

    // TÍTULOS CENTRALIZADOS

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
                new Dimension(200, 38));

        campo.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                COR_BORDA),

                        BorderFactory.createEmptyBorder(
                                5, 10, 5, 10)));

        return campo;
    }

    // CAIXAS DE SELEÇÃO

    private <T> JComboBox<T> criarCombo() {

        JComboBox<T> combo = new JComboBox<>();

        combo.setFont(
                new Font("SansSerif", Font.PLAIN, 13));

        combo.setForeground(COR_AZUL);
        combo.setBackground(COR_CAMPO);

        combo.setPreferredSize(
                new Dimension(200, 38));

        return combo;
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

        JPanel linha =
                new JPanel(new GridBagLayout());

        linha.setOpaque(false);

        linha.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        linha.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 70));

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridy = 0;

        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        int espacamento =
                campos.length == 2 ? 10 : 8;

        for (int i = 0; i < campos.length; i++) {

            gbc.gridx = i;

            gbc.insets = new Insets(
                    0,
                    i == 0 ? 0 : espacamento,
                    0,
                    i == campos.length - 1
                            ? 0 : espacamento);

            linha.add(campos[i], gbc);
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
                new Dimension(160, 40));

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

    // CARREGAR CATEGORIAS

    private void carregarCategorias() {

        CategoriaDAO categoriaDAO =
                new CategoriaDAO();

        comboCategoria.removeAllItems();

        for (Categoria categoria :
                categoriaDAO.listar()) {

            comboCategoria.addItem(categoria);
        }
    }

    // CARREGAR FORNECEDORES

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
                                list,
                                value,
                                index,
                                isSelected,
                                cellHasFocus);

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

    // CARREGAR DADOS DO PRODUTO

    private void carregarProduto() {

        ProdutoDAO produtoDAO = new ProdutoDAO();

        Produto produto =
                produtoDAO.buscarPorId(idProduto);

        if (produto == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Produto não encontrado.");

            return;
        }

        txtNome.setText(produto.getNome());

        txtDescricao.setText(
                produto.getDescricao() == null
                        ? ""
                        : produto.getDescricao());

        txtValorCusto.setText(
                produto.getValorCusto().toString());

        txtValorVenda.setText(
                produto.getValorVenda().toString());

        txtQuantidade.setText(
                String.valueOf(
                        produto.getQuantidadeEstoque()));

        txtEstoqueMinimo.setText(
                String.valueOf(
                        produto.getEstoqueMinimo()));

        if (produto.getDataValidade() != null) {

            txtDataValidade.setText(
                    produto.getDataValidade()
                            .format(FORMATO_DATA));

        } else {

            txtDataValidade.setText("");
        }

        selecionarCategoria(
                produto.getIdCategoria());

        selecionarFornecedor(
                produto.getIdFornecedor());
    }

    // SELECIONAR CATEGORIA ATUAL

    private void selecionarCategoria(int idCategoria) {

        for (int i = 0;
                i < comboCategoria.getItemCount();
                i++) {

            Categoria categoria =
                    comboCategoria.getItemAt(i);

            if (categoria != null &&
                    categoria.getIdCategoria()
                            == idCategoria) {

                comboCategoria.setSelectedIndex(i);

                return;
            }
        }
    }

    // SELECIONAR FORNECEDOR ATUAL

    private void selecionarFornecedor(int idFornecedor) {

        if (idFornecedor <= 0) {

            comboFornecedor.setSelectedIndex(0);

            return;
        }

        for (int i = 0;
                i < comboFornecedor.getItemCount();
                i++) {

            Fornecedor fornecedor =
                    comboFornecedor.getItemAt(i);

            if (fornecedor != null &&
                    fornecedor.getIdFornecedor()
                            == idFornecedor) {

                comboFornecedor.setSelectedIndex(i);

                return;
            }
        }

        comboFornecedor.setSelectedIndex(0);
    }

    // SALVAR ALTERAÇÕES

    private void salvarAlteracoes() {

        String nome =
                txtNome.getText().trim();

        String descricao =
                txtDescricao.getText().trim();

        String valorCustoTexto =
                txtValorCusto.getText()
                        .trim().replace(",", ".");

        String valorVendaTexto =
                txtValorVenda.getText()
                        .trim().replace(",", ".");

        String quantidadeTexto =
                txtQuantidade.getText().trim();

        String estoqueMinimoTexto =
                txtEstoqueMinimo.getText().trim();

        String validadeTexto =
                txtDataValidade.getText().trim();

        if (nome.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe o nome do produto.");

            txtNome.requestFocus();

            return;
        }

        if (comboCategoria.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma categoria.");

            return;
        }

        LocalDate dataValidade = null;

        if (!validadeTexto.isEmpty()) {

            try {

                dataValidade = LocalDate.parse(
                        validadeTexto,
                        FORMATO_DATA);

            } catch (DateTimeParseException erro) {

                JOptionPane.showMessageDialog(
                        this,
                        "Informe uma data válida.\n" +
                        "Utilize o formato dd/MM/aaaa.",
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

                return;
            }

            if (valorVenda.compareTo(
                    BigDecimal.ZERO) < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "O valor de venda não pode ser negativo.");

                return;
            }

            if (quantidade < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "A quantidade não pode ser negativa.");

                return;
            }

            if (estoqueMinimo < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "O estoque mínimo não pode ser negativo.");

                return;
            }

            Categoria categoria =
                    (Categoria) comboCategoria
                            .getSelectedItem();

            Fornecedor fornecedor =
                    (Fornecedor) comboFornecedor
                            .getSelectedItem();

            int idFornecedor = 0;

            if (fornecedor != null) {

                idFornecedor =
                        fornecedor.getIdFornecedor();
            }

            Produto produto = new Produto();

            produto.setIdProduto(idProduto);
            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setValorCusto(valorCusto);
            produto.setValorVenda(valorVenda);
            produto.setQuantidadeEstoque(quantidade);
            produto.setEstoqueMinimo(estoqueMinimo);

            produto.setIdCategoria(
                    categoria.getIdCategoria());

            produto.setIdFornecedor(idFornecedor);

            produto.setDataValidade(dataValidade);

            ProdutoDAO produtoDAO = new ProdutoDAO();

            boolean atualizado =
                    produtoDAO.atualizar(produto);

            if (atualizado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Produto atualizado com sucesso.",
                        "Atualização realizada",
                        JOptionPane.INFORMATION_MESSAGE);

                voltar();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível atualizar o produto.",
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

    // VOLTAR PARA A LISTAGEM

    private void voltar() {

        if (telaPrincipal != null) {

            telaPrincipal.carregarPainel(
                    new PainelListarProdutos(
                            telaPrincipal));
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
