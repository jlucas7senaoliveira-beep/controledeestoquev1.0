
package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.DashboardDAO;
import dao.ProdutoDAO;
import model.Produto;
import util.Formatador;

public class PainelDashboard extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color COR_FUNDO =
            new Color(237, 241, 246);
    private static final Color COR_AZUL =
            new Color(25, 57, 94);
    private static final Color COR_TEXTO =
            new Color(51, 65, 85);
    private static final Color COR_CINZA =
            new Color(100, 116, 139);
    private static final Color COR_BORDA =
            new Color(220, 227, 235);
    private static final Color COR_VERDE =
            new Color(32, 119, 77);
    private static final Color COR_VERMELHO =
            new Color(183, 60, 60);
    private static final Color COR_LARANJA =
            new Color(168, 107, 22);

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JLabel lblTotalProdutos;
    private JLabel lblTotalClientes;
    private JLabel lblEstoqueBaixo;
    private JLabel lblValorEstoque;
    private JLabel lblProdutosAVencer;
    private JLabel lblProdutosVencidos;

    private JTable tabelaAlertas;
    private DefaultTableModel modeloAlertas;

    public PainelDashboard() {
        setLayout(new BorderLayout(0, 18));
        setBackground(COR_FUNDO);
        setBorder(BorderFactory.createEmptyBorder(
                20, 22, 20, 22));

        criarCabecalho();
        criarConteudo();
        carregarDashboard();
    }

    // CABEÇALHO

    private void criarCabecalho() {
        JPanel painelCabecalho =
                new JPanel(new BorderLayout(15, 0));
        painelCabecalho.setOpaque(false);

        JPanel painelTitulos = new JPanel();
        painelTitulos.setOpaque(false);
        painelTitulos.setLayout(
                new javax.swing.BoxLayout(
                        painelTitulos,
                        javax.swing.BoxLayout.Y_AXIS));

        JLabel lblSecao =
                new JLabel("CONTROLE DE ESTOQUE");
        lblSecao.setFont(
                new Font("SansSerif", Font.BOLD, 11));
        lblSecao.setForeground(COR_CINZA);
        lblSecao.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitulo =
                new JLabel("Visão Geral");
        lblTitulo.setFont(
                new Font("SansSerif", Font.BOLD, 26));
        lblTitulo.setForeground(COR_AZUL);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescricao = new JLabel(
                "Acompanhe os principais indicadores do seu estoque");
        lblDescricao.setFont(
                new Font("SansSerif", Font.PLAIN, 12));
        lblDescricao.setForeground(COR_CINZA);
        lblDescricao.setAlignmentX(Component.LEFT_ALIGNMENT);

        painelTitulos.add(lblSecao);
        painelTitulos.add(
                javax.swing.Box.createVerticalStrut(4));
        painelTitulos.add(lblTitulo);
        painelTitulos.add(
                javax.swing.Box.createVerticalStrut(4));
        painelTitulos.add(lblDescricao);

        painelCabecalho.add(
                painelTitulos, BorderLayout.CENTER);

        JButton btnAtualizar =
                new JButton("↻  Atualizar");
        btnAtualizar.setFont(
                new Font("SansSerif", Font.BOLD, 12));
        btnAtualizar.setForeground(COR_AZUL);
        btnAtualizar.setBackground(Color.WHITE);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.setOpaque(true);
        btnAtualizar.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR));
        btnAtualizar.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COR_BORDA),
                        BorderFactory.createEmptyBorder(
                                10, 15, 10, 15)));

        btnAtualizar.addActionListener(
                (ActionEvent e) -> carregarDashboard());

        JPanel painelBotao =
                new JPanel(new BorderLayout());
        painelBotao.setOpaque(false);
        painelBotao.add(
                btnAtualizar, BorderLayout.NORTH);

        painelCabecalho.add(
                painelBotao, BorderLayout.EAST);

        add(painelCabecalho, BorderLayout.NORTH);
    }

    // CONTEÚDO CENTRAL

    private void criarConteudo() {
        JPanel painelCentral =
                new JPanel(new BorderLayout(0, 18));
        painelCentral.setOpaque(false);

        JPanel painelIndicadores =
                new JPanel(new GridLayout(2, 3, 14, 14));
        painelIndicadores.setOpaque(false);

        // Mais altura para evitar cortes nos cartões
        painelIndicadores.setPreferredSize(
                new Dimension(0, 290));

        lblTotalProdutos = new JLabel("0");
        lblTotalClientes = new JLabel("0");
        lblEstoqueBaixo = new JLabel("0");
        lblValorEstoque = new JLabel("R$ 0,00");
        lblProdutosAVencer = new JLabel("0");
        lblProdutosVencidos = new JLabel("0");

        painelIndicadores.add(criarCard(
                "Produtos ativos",
                lblTotalProdutos,
                COR_AZUL,
                "produto"));

        painelIndicadores.add(criarCard(
                "Clientes ativos",
                lblTotalClientes,
                COR_AZUL,
                "clientes"));

        painelIndicadores.add(criarCard(
                "Estoque baixo",
                lblEstoqueBaixo,
                COR_VERMELHO,
                "estoque"));

        painelIndicadores.add(criarCard(
                "Valor do estoque",
                lblValorEstoque,
                COR_VERDE,
                "valor"));

        painelIndicadores.add(criarCard(
                "Produtos a vencer",
                lblProdutosAVencer,
                COR_LARANJA,
                "calendario"));

        painelIndicadores.add(criarCard(
                "Produtos vencidos",
                lblProdutosVencidos,
                COR_VERMELHO,
                "alerta"));

        painelCentral.add(
                painelIndicadores, BorderLayout.NORTH);

        criarTabela(painelCentral);

        add(painelCentral, BorderLayout.CENTER);
    }

    // CARTÕES ARREDONDADOS

    private JPanel criarCard(
            String titulo,
            JLabel lblValor,
            Color cor,
            String tipoIcone) {

        PainelArredondado card =
                new PainelArredondado(Color.WHITE, 18);

        card.setLayout(new BorderLayout(0, 8));
        card.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 17, 15, 17));

        JPanel painelSuperior =
                new JPanel(new BorderLayout());
        painelSuperior.setOpaque(false);

        IconeDashboard icone =
                new IconeDashboard(tipoIcone, cor);

        painelSuperior.add(
                icone, BorderLayout.WEST);

        card.add(
                painelSuperior, BorderLayout.NORTH);

        // Layout corrigido para evitar números cortados
        JPanel painelTexto =
                new JPanel(new BorderLayout(0, 5));
        painelTexto.setOpaque(false);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(
                new Font("SansSerif", Font.PLAIN, 12));
        lblTitulo.setForeground(COR_CINZA);

        lblValor.setFont(
                new Font("SansSerif", Font.BOLD, 23));
        lblValor.setForeground(cor);
        lblValor.setHorizontalAlignment(
                SwingConstants.LEFT);
        lblValor.setVerticalAlignment(
                SwingConstants.CENTER);

        // Reserva espaço suficiente para os números
        lblValor.setPreferredSize(
                new Dimension(0, 36));

        painelTexto.add(
                lblTitulo, BorderLayout.NORTH);
        painelTexto.add(
                lblValor, BorderLayout.CENTER);

        painelTexto.setPreferredSize(
                new Dimension(0, 61));

        card.add(
                painelTexto, BorderLayout.SOUTH);

        return card;
    }

    // TABELA DE ALERTAS

    private void criarTabela(JPanel painelCentral) {
        PainelArredondado painelTabela =
                new PainelArredondado(Color.WHITE, 18);

        painelTabela.setLayout(
                new BorderLayout(0, 15));
        painelTabela.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 18, 18, 18));

        JPanel painelTitulo =
                new JPanel(new BorderLayout());
        painelTitulo.setOpaque(false);

        JPanel painelTextos = new JPanel();
        painelTextos.setOpaque(false);
        painelTextos.setLayout(
                new javax.swing.BoxLayout(
                        painelTextos,
                        javax.swing.BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(
                "Produtos que precisam de atenção");
        lblTitulo.setFont(
                new Font("SansSerif", Font.BOLD, 17));
        lblTitulo.setForeground(COR_AZUL);

        JLabel lblSubtitulo = new JLabel(
                "Alertas de estoque e validade");
        lblSubtitulo.setFont(
                new Font("SansSerif", Font.PLAIN, 12));
        lblSubtitulo.setForeground(COR_CINZA);

        painelTextos.add(lblTitulo);
        painelTextos.add(
                javax.swing.Box.createVerticalStrut(4));
        painelTextos.add(lblSubtitulo);

        painelTitulo.add(
                painelTextos, BorderLayout.WEST);

        painelTabela.add(
                painelTitulo, BorderLayout.NORTH);

        tabelaAlertas = new JTable();
        tabelaAlertas.setRowHeight(38);
        tabelaAlertas.setFont(
                new Font("SansSerif", Font.PLAIN, 12));
        tabelaAlertas.setForeground(COR_TEXTO);
        tabelaAlertas.setBackground(Color.WHITE);
        tabelaAlertas.setGridColor(
                new Color(235, 239, 244));
        tabelaAlertas.setShowVerticalLines(false);
        tabelaAlertas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        tabelaAlertas.setSelectionBackground(
                new Color(224, 235, 247));
        tabelaAlertas.setSelectionForeground(COR_AZUL);
        tabelaAlertas.setFillsViewportHeight(true);

        tabelaAlertas.getTableHeader()
                .setReorderingAllowed(false);
        tabelaAlertas.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12));
        tabelaAlertas.getTableHeader()
                .setBackground(new Color(241, 245, 249));
        tabelaAlertas.getTableHeader()
                .setForeground(COR_AZUL);
        tabelaAlertas.getTableHeader()
                .setPreferredSize(new Dimension(0, 38));

        configurarTabela();

        JScrollPane scroll =
                new JScrollPane(tabelaAlertas);
        scroll.setBorder(
                BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);

        painelTabela.add(
                scroll, BorderLayout.CENTER);

        painelCentral.add(
                painelTabela, BorderLayout.CENTER);
    }

    private void configurarTabela() {
        modeloAlertas = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(
                    int row, int column) {
                return false;
            }
        };

        modeloAlertas.addColumn("ID");
        modeloAlertas.addColumn("Produto");
        modeloAlertas.addColumn("Estoque");
        modeloAlertas.addColumn("Est. Mínimo");
        modeloAlertas.addColumn("Validade");
        modeloAlertas.addColumn("Situação");

        tabelaAlertas.setModel(modeloAlertas);

        int[] larguras = {
                60, 250, 110, 140, 160, 350
        };

        for (int i = 0; i < larguras.length; i++) {
            tabelaAlertas.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(larguras[i]);
        }

        tabelaAlertas.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS);

        RenderizadorTabela renderizador =
                new RenderizadorTabela();

        for (int i = 0; i < 6; i++) {
            tabelaAlertas.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(renderizador);
        }
    }

    // CONSULTAS E ATUALIZAÇÃO DOS DADOS

    private void carregarDashboard() {
        DashboardDAO dao = new DashboardDAO();

        lblTotalProdutos.setText(
                String.valueOf(
                        dao.contarProdutosAtivos()));

        lblTotalClientes.setText(
                String.valueOf(
                        dao.contarClientesAtivos()));

        lblEstoqueBaixo.setText(
                String.valueOf(
                        dao.contarProdutosEstoqueBaixo()));

        lblValorEstoque.setText(
                Formatador.moeda(
                        dao.calcularValorTotalEstoque()));

        lblProdutosAVencer.setText(
                String.valueOf(
                        dao.contarProdutosAVencer()));

        lblProdutosVencidos.setText(
                String.valueOf(
                        dao.contarProdutosVencidos()));

        Map<Integer, AlertaProduto> alertas =
                new LinkedHashMap<>();

        adicionarAlertasEstoque(
                alertas,
                dao.listarProdutosEstoqueBaixo());

        adicionarAlertasValidade(
                alertas,
                dao.listarProdutosAVencer(),
                false);

        adicionarAlertasValidade(
                alertas,
                dao.listarProdutosVencidos(),
                true);

        preencherTabelaAlertas(alertas);
    }

    private void adicionarAlertasEstoque(
            Map<Integer, AlertaProduto> alertas,
            List<Produto> produtos) {

        for (Produto produto : produtos) {
            AlertaProduto alerta =
                    obterOuCriarAlerta(alertas, produto);

            alerta.estoqueBaixo = true;
        }
    }

    private void adicionarAlertasValidade(
            Map<Integer, AlertaProduto> alertas,
            List<Produto> produtos,
            boolean vencido) {

        for (Produto produto : produtos) {
            AlertaProduto alerta =
                    obterOuCriarAlerta(alertas, produto);

            if (vencido) {
                alerta.vencido = true;
            } else {
                alerta.aVencer = true;
            }
        }
    }

    // RECUPERA OS DADOS COMPLETOS DOS PRODUTOS

    private AlertaProduto obterOuCriarAlerta(
            Map<Integer, AlertaProduto> alertas,
            Produto produto) {

        int id = produto.getIdProduto();
        AlertaProduto alerta = alertas.get(id);

        if (alerta == null) {
            alerta = new AlertaProduto();
            alerta.id = id;

            ProdutoDAO produtoDAO = new ProdutoDAO();
            Produto completo = produtoDAO.buscarPorId(id);

            if (completo != null) {
                alerta.nome = completo.getNome();
                alerta.quantidade =
                        completo.getQuantidadeEstoque();
                alerta.estoqueMinimo =
                        completo.getEstoqueMinimo();
                alerta.validade =
                        completo.getDataValidade();
            } else {
                alerta.nome = produto.getNome();
                alerta.quantidade =
                        produto.getQuantidadeEstoque();
                alerta.estoqueMinimo =
                        produto.getEstoqueMinimo();
                alerta.validade =
                        produto.getDataValidade();
            }

            alertas.put(id, alerta);
        }

        return alerta;
    }

    // PREENCHE A TABELA

    private void preencherTabelaAlertas(
            Map<Integer, AlertaProduto> alertas) {

        modeloAlertas.setRowCount(0);
        LocalDate hoje = LocalDate.now();

        for (AlertaProduto alerta : alertas.values()) {
            String validade = "Sem validade";

            if (alerta.validade != null) {
                validade = alerta.validade
                        .format(FORMATO_DATA);
            }

            StringBuilder situacao =
                    new StringBuilder();

            if (alerta.estoqueBaixo) {
                situacao.append("Estoque baixo");
            }

            if (alerta.aVencer &&
                    alerta.validade != null) {

                long dias = ChronoUnit.DAYS.between(
                        hoje, alerta.validade);

                if (situacao.length() > 0) {
                    situacao.append(" | ");
                }

                if (dias == 0) {
                    situacao.append("Vence hoje");
                } else {
                    situacao.append(
                            "Vence em " + dias + " dia(s)");
                }
            }

            if (alerta.vencido &&
                    alerta.validade != null) {

                long dias = ChronoUnit.DAYS.between(
                        alerta.validade, hoje);

                if (situacao.length() > 0) {
                    situacao.append(" | ");
                }

                situacao.append(
                        "Vencido há " + dias + " dia(s)");
            }

            modeloAlertas.addRow(new Object[] {
                    alerta.id,
                    alerta.nome,
                    alerta.quantidade,
                    alerta.estoqueMinimo,
                    validade,
                    situacao.toString()
            });
        }
    }

    private static class AlertaProduto {
        int id;
        String nome;
        int quantidade;
        int estoqueMinimo;
        LocalDate validade;

        boolean estoqueBaixo;
        boolean aVencer;
        boolean vencido;
    }

    // CORES E LINHAS ALTERNADAS DA TABELA

    private class RenderizadorTabela
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

            setBorder(BorderFactory.createEmptyBorder(
                    0, 10, 0, 10));

            if (isSelected) {
                return this;
            }

            if (row % 2 == 0) {
                setBackground(Color.WHITE);
            } else {
                setBackground(
                        new Color(248, 250, 252));
            }

            setForeground(COR_TEXTO);
            setFont(new Font(
                    "SansSerif", Font.PLAIN, 12));

            if (column == 1) {
                setForeground(COR_AZUL);
                setFont(new Font(
                        "SansSerif", Font.BOLD, 12));
            }

            if (column == 5 && value != null) {
                String situacao = value.toString();

                setFont(new Font(
                        "SansSerif", Font.BOLD, 12));

                if (situacao.contains("Estoque baixo") ||
                        situacao.contains("Vencido há")) {
                    setForeground(COR_VERMELHO);
                } else if (situacao.contains("Vence")) {
                    setForeground(COR_LARANJA);
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

    // ÍCONES DESENHADOS EM JAVA SWING

    private static class IconeDashboard
            extends JPanel {

        private static final long serialVersionUID = 1L;

        private final String tipo;
        private final Color cor;

        public IconeDashboard(String tipo, Color cor) {
            this.tipo = tipo;
            this.cor = cor;
            setOpaque(false);
            setPreferredSize(new Dimension(38, 38));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Color fundo = new Color(
                    cor.getRed(),
                    cor.getGreen(),
                    cor.getBlue(),
                    25);

            g2.setColor(fundo);
            g2.fillRoundRect(
                    0, 0, 38, 38, 12, 12);

            g2.setColor(cor);
            g2.setStroke(
                    new BasicStroke(
                            1.8f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND));

            switch (tipo) {
                case "produto":
                    desenharProduto(g2);
                    break;
                case "clientes":
                    desenharClientes(g2);
                    break;
                case "estoque":
                    desenharEstoque(g2);
                    break;
                case "valor":
                    desenharValor(g2);
                    break;
                case "calendario":
                    desenharCalendario(g2);
                    break;
                case "alerta":
                    desenharAlerta(g2);
                    break;
                default:
                    g2.drawOval(12, 12, 14, 14);
                    break;
            }

            g2.dispose();
        }

        private void desenharProduto(Graphics2D g2) {
            g2.drawRect(10, 13, 18, 15);
            g2.drawLine(10, 17, 19, 21);
            g2.drawLine(28, 17, 19, 21);
            g2.drawLine(19, 21, 19, 28);
            g2.drawLine(10, 13, 19, 9);
            g2.drawLine(19, 9, 28, 13);
        }

        private void desenharClientes(Graphics2D g2) {
            g2.drawOval(14, 9, 9, 9);
            g2.drawArc(10, 19, 18, 13, 0, 180);
            g2.drawArc(6, 12, 8, 8, 20, 180);
            g2.drawArc(24, 12, 8, 8, 0, 160);
        }

        private void desenharEstoque(Graphics2D g2) {
            g2.drawLine(9, 12, 9, 26);
            g2.drawLine(9, 26, 29, 26);
            g2.drawLine(13, 14, 19, 20);
            g2.drawLine(19, 20, 26, 14);
            g2.drawLine(26, 14, 26, 19);
            g2.drawLine(26, 14, 21, 14);
        }

        private void desenharValor(Graphics2D g2) {
            g2.drawRoundRect(
                    8, 12, 22, 15, 3, 3);
            g2.drawOval(16, 15, 7, 9);
            g2.drawLine(12, 16, 12, 23);
            g2.drawLine(26, 16, 26, 23);
        }

        private void desenharCalendario(Graphics2D g2) {
            g2.drawRoundRect(
                    9, 11, 20, 18, 3, 3);
            g2.drawLine(9, 17, 29, 17);
            g2.drawLine(14, 8, 14, 14);
            g2.drawLine(24, 8, 24, 14);
            g2.drawOval(17, 20, 7, 7);
        }

        private void desenharAlerta(Graphics2D g2) {
            int[] x = {19, 8, 30};
            int[] y = {9, 29, 29};

            g2.drawPolygon(x, y, 3);
            g2.drawLine(19, 16, 19, 22);
            g2.fillOval(18, 25, 2, 2);
        }
    }
}
