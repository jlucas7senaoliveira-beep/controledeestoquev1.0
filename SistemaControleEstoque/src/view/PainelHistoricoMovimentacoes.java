
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
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.MovimentacaoEstoqueDAO;
import model.MovimentacaoEstoque;

public class PainelHistoricoMovimentacoes extends JPanel {

    private static final long serialVersionUID = 1L;

    // CORES DO SISTEMA

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

    // COMPONENTES

    private JTextField txtPesquisa;
    private JComboBox<String> comboTipo;

    private JTextField txtDataInicial;
    private JTextField txtDataFinal;

    private JTable tabelaMovimentacoes;
    private DefaultTableModel modeloTabela;

    private JLabel lblTotalMovimentacoes;
    private JLabel lblTotalEntradas;
    private JLabel lblTotalSaidas;
    private JLabel lblRodape;

    private List<MovimentacaoEstoque> movimentacoes =
            new ArrayList<>();

    private final MovimentacaoEstoqueDAO dao =
            new MovimentacaoEstoqueDAO();

    private final DateTimeFormatter formatoData =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    private final DateTimeFormatter formatoDataHora =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final NumberFormat formatoMoeda =
            NumberFormat.getCurrencyInstance(
                    new Locale("pt", "BR"));

    // CONSTRUTOR

    public PainelHistoricoMovimentacoes() {

        setLayout(new BorderLayout(0, 22));
        setBackground(COR_FUNDO);

        setBorder(
                BorderFactory.createEmptyBorder(
                        28, 25, 25, 25));

        JPanel superior = new JPanel();

        superior.setOpaque(false);

        superior.setLayout(
                new BoxLayout(superior, BoxLayout.Y_AXIS));

        superior.add(criarCabecalho());

        superior.add(Box.createVerticalStrut(22));

        superior.add(criarIndicadores());

        add(superior, BorderLayout.NORTH);

        criarConteudo();

        carregarMovimentacoes();
    }

    // CABEÇALHO

    private JPanel criarCabecalho() {

        JPanel painel = new JPanel();

        painel.setOpaque(false);

        painel.setLayout(
                new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JLabel titulo = new JLabel(
                "Histórico de Movimentações");

        titulo.setFont(
                new Font("SansSerif", Font.BOLD, 25));

        titulo.setForeground(COR_AZUL);

        titulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel(
                "Acompanhe as entradas e saídas do estoque");

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

    // INDICADORES

    private JPanel criarIndicadores() {

        JPanel painel = new JPanel(
                new GridLayout(1, 3, 18, 0));

        painel.setOpaque(false);

        painel.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        painel.setPreferredSize(
                new Dimension(800, 105));

        painel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 105));

        lblTotalMovimentacoes =
                new JLabel("0");

        lblTotalEntradas =
                new JLabel("0");

        lblTotalSaidas =
                new JLabel("0");

        painel.add(criarCard(
                "Total de movimentações",
                lblTotalMovimentacoes,
                COR_AZUL));

        painel.add(criarCard(
                "Entradas",
                lblTotalEntradas,
                COR_VERDE));

        painel.add(criarCard(
                "Saídas",
                lblTotalSaidas,
                COR_VERMELHO));

        return painel;
    }

    // CARDS DOS INDICADORES

    private JPanel criarCard(
            String titulo,
            JLabel valor,
            Color corValor) {

        PainelArredondado card =
                new PainelArredondado(Color.WHITE, 16);

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS));

        card.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 10, 15, 10));

        JLabel lblTitulo = new JLabel(titulo);

        lblTitulo.setFont(
                new Font("SansSerif", Font.PLAIN, 13));

        lblTitulo.setForeground(COR_CINZA);

        lblTitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        valor.setFont(
                new Font("SansSerif", Font.BOLD, 30));

        valor.setForeground(corValor);

        valor.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());

        card.add(lblTitulo);

        card.add(Box.createVerticalStrut(8));

        card.add(valor);

        card.add(Box.createVerticalGlue());

        return card;
    }

    // CONTEÚDO PRINCIPAL

    private void criarConteudo() {

        PainelArredondado painel =
                new PainelArredondado(Color.WHITE, 18);

        painel.setLayout(new BorderLayout(0, 18));

        painel.setBorder(
                BorderFactory.createEmptyBorder(
                        24, 24, 20, 24));

        painel.add(
                criarFiltros(), BorderLayout.NORTH);

        criarTabela();

        JScrollPane scrollTabela =
                new JScrollPane(tabelaMovimentacoes);

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

    // FILTROS

    private JPanel criarFiltros() {

        JPanel painel = new JPanel();

        painel.setOpaque(false);

        painel.setLayout(
                new BoxLayout(painel, BoxLayout.Y_AXIS));

        txtPesquisa = criarCampoTexto();

        txtPesquisa.setToolTipText(
                "Digite o nome do produto");

        txtPesquisa.addActionListener(
                e -> pesquisarMovimentacoes());

        comboTipo = new JComboBox<>(
                new String[] {
                        "Todos",
                        "Entrada",
                        "Saída"
                });

        comboTipo.setFont(
                new Font("SansSerif", Font.PLAIN, 13));

        comboTipo.setBackground(COR_CAMPO);

        comboTipo.setForeground(COR_AZUL);

        comboTipo.setPreferredSize(
                new Dimension(160, 38));

        JPanel linha1 = new JPanel(
                new GridLayout(1, 2, 18, 0));

        linha1.setOpaque(false);

        linha1.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        linha1.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 62));

        linha1.add(criarGrupoCampo(
                "Pesquisar produto", txtPesquisa));

        linha1.add(criarGrupoCampo(
                "Tipo de movimentação", comboTipo));

        painel.add(linha1);

        painel.add(Box.createVerticalStrut(14));

        // FILTROS DE DATA

        txtDataInicial = criarCampoTexto();

        txtDataFinal = criarCampoTexto();

        txtDataInicial.setToolTipText("dd/mm/aaaa");

        txtDataFinal.setToolTipText("dd/mm/aaaa");

        JPanel linha2 = new JPanel(
                new GridLayout(1, 2, 18, 0));

        linha2.setOpaque(false);

        linha2.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        linha2.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 62));

        linha2.add(criarGrupoCampo(
                "Data inicial (dd/mm/aaaa)",
                txtDataInicial));

        linha2.add(criarGrupoCampo(
                "Data final (dd/mm/aaaa)",
                txtDataFinal));

        painel.add(linha2);

        painel.add(Box.createVerticalStrut(16));

        // BOTÕES DE PESQUISA

        JPanel botoes = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT, 10, 0));

        botoes.setOpaque(false);

        botoes.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        JButton btnPesquisar =
                criarBotao("Pesquisar", true);

        btnPesquisar.addActionListener(
                e -> pesquisarMovimentacoes());

        JButton btnLimpar =
                criarBotao("Limpar filtros", false);

        btnLimpar.addActionListener(
                e -> limparFiltros());

        JButton btnAtualizar =
                criarBotao("Atualizar", false);

        btnAtualizar.addActionListener(
                e -> pesquisarMovimentacoes());

        botoes.add(btnLimpar);

        botoes.add(btnAtualizar);

        botoes.add(btnPesquisar);

        painel.add(botoes);

        return painel;
    }

    // CAMPOS DE TEXTO

    private JTextField criarCampoTexto() {

        JTextField campo = new JTextField();

        campo.setFont(
                new Font("SansSerif", Font.PLAIN, 13));

        campo.setForeground(COR_AZUL);

        campo.setBackground(COR_CAMPO);

        campo.setPreferredSize(
                new Dimension(250, 38));

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

    // CRIAR TABELA

    private void criarTabela() {

        modeloTabela = new DefaultTableModel(

                new Object[] {
                        "ID",
                        "Data e horário",
                        "Produto",
                        "Tipo",
                        "Qtd.",
                        "Valor unit.",
                        "Valor total",
                        "Responsável"
                }, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(
                    int row, int column) {

                return false;
            }
        };

        tabelaMovimentacoes =
                new JTable(modeloTabela);

        tabelaMovimentacoes.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        tabelaMovimentacoes.setRowHeight(42);

        tabelaMovimentacoes.setForeground(COR_TEXTO);

        tabelaMovimentacoes.setBackground(Color.WHITE);

        tabelaMovimentacoes.setGridColor(
                new Color(235, 239, 244));

        tabelaMovimentacoes.setShowVerticalLines(false);

        tabelaMovimentacoes.setShowHorizontalLines(true);

        tabelaMovimentacoes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        tabelaMovimentacoes.setSelectionBackground(
                new Color(224, 235, 247));

        tabelaMovimentacoes.setSelectionForeground(
                COR_AZUL);

        tabelaMovimentacoes.setFillsViewportHeight(true);

        tabelaMovimentacoes.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF);

        tabelaMovimentacoes.getTableHeader()
                .setReorderingAllowed(false);

        tabelaMovimentacoes.getTableHeader()
                .setPreferredSize(
                        new Dimension(0, 42));

        tabelaMovimentacoes.getTableHeader()
                .setDefaultRenderer(
                        new RenderizadorCabecalho());

        int[] larguras = {
                45, 135, 165, 85,
                55, 95, 100, 135
        };

        RenderizadorTabela renderizador =
                new RenderizadorTabela();

        for (int i = 0; i < larguras.length; i++) {

            tabelaMovimentacoes.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(larguras[i]);

            tabelaMovimentacoes.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(renderizador);
        }

        // AJUSTAR TABELA AO ESPAÇO DISPONÍVEL

        tabelaMovimentacoes.addComponentListener(
                new java.awt.event.ComponentAdapter() {

                    @Override
                    public void componentResized(
                            java.awt.event.ComponentEvent e) {

                        if (tabelaMovimentacoes.getParent()
                                != null) {

                            int larguraDisponivel =
                                    tabelaMovimentacoes
                                            .getParent()
                                            .getWidth();

                            int larguraMinima = 815;

                            int modo = larguraDisponivel
                                    >= larguraMinima
                                    ? JTable.AUTO_RESIZE_ALL_COLUMNS
                                    : JTable.AUTO_RESIZE_OFF;

                            if (tabelaMovimentacoes
                                    .getAutoResizeMode() != modo) {

                                tabelaMovimentacoes
                                        .setAutoResizeMode(modo);
                            }
                        }
                    }
                });
    }

    // RODAPÉ

    private JPanel criarRodape() {

        JPanel painel =
                new JPanel(new BorderLayout());

        painel.setOpaque(false);

        lblRodape = new JLabel(
                "Total de movimentações exibidas: 0");

        lblRodape.setFont(
                new Font("SansSerif", Font.PLAIN, 12));

        lblRodape.setForeground(COR_CINZA);

        JButton btnDetalhes =
                criarBotao("Ver detalhes", false);

        btnDetalhes.addActionListener(
                e -> visualizarDetalhes());

        painel.add(lblRodape, BorderLayout.WEST);

        painel.add(btnDetalhes, BorderLayout.EAST);

        return painel;
    }

    // BOTÕES

    private JButton criarBotao(
            String texto, boolean principal) {

        JButton botao = new JButton(texto);

        botao.setFont(
                new Font("SansSerif", Font.BOLD, 12));

        botao.setPreferredSize(
                new Dimension(130, 38));

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

    // CARREGAR TODAS AS MOVIMENTAÇÕES

    private void carregarMovimentacoes() {

        try {

            preencherTabela(
                    dao.listarMovimentacoes());

        } catch (IllegalStateException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    erro.getMessage(),
                    "Erro na consulta",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // PESQUISAR MOVIMENTAÇÕES

    private void pesquisarMovimentacoes() {

        String nomeProduto =
                txtPesquisa.getText().trim();

        String tipo =
                (String) comboTipo.getSelectedItem();

        LocalDate dataInicial;
        LocalDate dataFinal;

        try {

            dataInicial = converterData(
                    txtDataInicial.getText().trim());

            dataFinal = converterData(
                    txtDataFinal.getText().trim());

        } catch (DateTimeParseException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe datas válidas no formato dd/mm/aaaa.",
                    "Data inválida",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        if (dataInicial != null
                && dataFinal != null
                && dataInicial.isAfter(dataFinal)) {

            JOptionPane.showMessageDialog(
                    this,
                    "A data inicial não pode ser posterior à data final.",
                    "Período inválido",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        try {

            List<MovimentacaoEstoque> resultado =
                    dao.pesquisarMovimentacoes(
                            nomeProduto,
                            tipo,
                            dataInicial,
                            dataFinal);

            preencherTabela(resultado);

        } catch (IllegalStateException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    erro.getMessage(),
                    "Erro na consulta",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // CONVERTER DATA

    private LocalDate converterData(String texto) {

        if (texto == null || texto.isEmpty()) {

            return null;
        }

        return LocalDate.parse(texto, formatoData);
    }

    // LIMPAR FILTROS

    private void limparFiltros() {

        txtPesquisa.setText("");

        comboTipo.setSelectedIndex(0);

        txtDataInicial.setText("");

        txtDataFinal.setText("");

        carregarMovimentacoes();

        txtPesquisa.requestFocusInWindow();
    }

    // PREENCHER TABELA

    private void preencherTabela(
            List<MovimentacaoEstoque> lista) {

        movimentacoes = new ArrayList<>(lista);

        modeloTabela.setRowCount(0);

        int totalEntradas = 0;
        int totalSaidas = 0;

        for (MovimentacaoEstoque movimentacao : lista) {

            boolean entrada =
                    "ENTRADA".equalsIgnoreCase(
                            movimentacao.getTipo());

            if (entrada) {

                totalEntradas++;

            } else if ("SAIDA".equalsIgnoreCase(
                    movimentacao.getTipo())) {

                totalSaidas++;
            }

            String tipo =
                    entrada ? "Entrada" : "Saída";

            String dataHora =
                    movimentacao.getCriadoEm() == null
                            ? "Não informada"
                            : movimentacao.getCriadoEm()
                                    .format(formatoDataHora);

            modeloTabela.addRow(new Object[] {

                    movimentacao.getIdMovimentacao(),

                    dataHora,

                    movimentacao.getNomeProduto(),

                    tipo,

                    (entrada ? "+" : "-")
                            + movimentacao.getQuantidade(),

                    formatarMoeda(
                            movimentacao.getValorUnitario()),

                    formatarMoeda(
                            movimentacao.getValorTotal()),

                    movimentacao.getNomeUsuario()
            });
        }

        // ATUALIZAR INDICADORES

        lblTotalMovimentacoes.setText(
                String.valueOf(lista.size()));

        lblTotalEntradas.setText(
                String.valueOf(totalEntradas));

        lblTotalSaidas.setText(
                String.valueOf(totalSaidas));

        lblRodape.setText(
                "Total de movimentações exibidas: "
                        + lista.size());
    }

    // FORMATAR VALORES

    private String formatarMoeda(BigDecimal valor) {

        if (valor == null) {

            valor = BigDecimal.ZERO;
        }

        return formatoMoeda.format(valor);
    }

    // VISUALIZAR DETALHES

    private void visualizarDetalhes() {

        int linhaSelecionada =
                tabelaMovimentacoes.getSelectedRow();

        if (linhaSelecionada == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma movimentação na tabela.",
                    "Nenhuma movimentação selecionada",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        int linhaModelo =
                tabelaMovimentacoes.convertRowIndexToModel(
                        linhaSelecionada);

        MovimentacaoEstoque movimentacao =
                movimentacoes.get(linhaModelo);

        String observacao =
                movimentacao.getObservacao();

        if (observacao == null
                || observacao.trim().isEmpty()) {

            observacao = "Nenhuma observação registrada.";
        }

        String dataHora =
                movimentacao.getCriadoEm() == null
                        ? "Não informada"
                        : movimentacao.getCriadoEm()
                                .format(formatoDataHora);

        String tipo =
                "ENTRADA".equalsIgnoreCase(
                        movimentacao.getTipo())
                                ? "Entrada"
                                : "Saída";

        String detalhes =
                "ID da movimentação: "
                + movimentacao.getIdMovimentacao()

                + "\n\nProduto: "
                + movimentacao.getNomeProduto()

                + "\nTipo: "
                + tipo

                + "\nQuantidade: "
                + movimentacao.getQuantidade()

                + "\nValor unitário: "
                + formatarMoeda(
                        movimentacao.getValorUnitario())

                + "\nValor total: "
                + formatarMoeda(
                        movimentacao.getValorTotal())

                + "\nData e horário: "
                + dataHora

                + "\nResponsável: "
                + movimentacao.getNomeUsuario()

                + "\n\nObservação:\n"
                + observacao;

        JTextArea areaDetalhes =
                new JTextArea(detalhes);

        areaDetalhes.setEditable(false);

        areaDetalhes.setLineWrap(true);

        areaDetalhes.setWrapStyleWord(true);

        areaDetalhes.setFont(
                new Font("SansSerif", Font.PLAIN, 14));

        areaDetalhes.setForeground(COR_AZUL);

        areaDetalhes.setBackground(COR_CAMPO);

        areaDetalhes.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15));

        areaDetalhes.setCaretPosition(0);

        JScrollPane scroll =
                new JScrollPane(areaDetalhes);

        scroll.setPreferredSize(
                new Dimension(450, 330));

        scroll.setBorder(
                BorderFactory.createLineBorder(
                        COR_BORDA));

        JOptionPane.showMessageDialog(
                this,
                scroll,
                "Detalhes da movimentação",
                JOptionPane.PLAIN_MESSAGE);
    }

    // RENDERIZADOR DO CABEÇALHO

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

    // RENDERIZADOR DA TABELA

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
                            0, 8, 0, 8));

            setFont(new Font(
                    "SansSerif", Font.PLAIN, 12));

            setHorizontalAlignment(
                    SwingConstants.CENTER);

            int colunaModelo =
                    table.convertColumnIndexToModel(column);

            int linhaModelo =
                    table.convertRowIndexToModel(row);

            String tipo = String.valueOf(
                    table.getModel().getValueAt(
                            linhaModelo, 3));

            boolean entrada =
                    "Entrada".equalsIgnoreCase(tipo);

            if (isSelected) {

                setBackground(
                        table.getSelectionBackground());

                setForeground(
                        table.getSelectionForeground());

            } else {

                setBackground(
                        row % 2 == 0
                                ? Color.WHITE
                                : new Color(248, 250, 252));

                setForeground(COR_TEXTO);
            }

            // PRODUTO E RESPONSÁVEL

            if (colunaModelo == 2
                    || colunaModelo == 7) {

                setHorizontalAlignment(
                        SwingConstants.LEFT);
            }

            // NOME DO PRODUTO

            if (colunaModelo == 2) {

                setForeground(COR_AZUL);

                setFont(new Font(
                        "SansSerif", Font.BOLD, 12));
            }

            // TIPO DE MOVIMENTAÇÃO

            if (colunaModelo == 3) {

                setForeground(
                        entrada
                                ? COR_VERDE
                                : COR_VERMELHO);

                setFont(new Font(
                        "SansSerif", Font.BOLD, 12));

                if (!isSelected) {

                    setBackground(
                            entrada
                                    ? new Color(234, 246, 239)
                                    : new Color(252, 239, 239));
                }
            }

            // QUANTIDADE

            if (colunaModelo == 4) {

                setForeground(
                        entrada
                                ? COR_VERDE
                                : COR_VERMELHO);

                setFont(new Font(
                        "SansSerif", Font.BOLD, 12));
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
