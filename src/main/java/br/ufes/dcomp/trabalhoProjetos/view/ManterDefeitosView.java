package br.ufes.dcomp.trabalhoProjetos.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManterDefeitosView extends JInternalFrame {

    private JTextField txtDescricao;
    private JTextField txtPercentual;
    private JButton btnSalvar;
    private JButton btnExcluir;
    private JButton btnFechar;
    private JTable tabelaDefeitos;
    private DefaultTableModel modeloTabela;

    public ManterDefeitosView() {
        super("Gerenciar Defeitos", false, true, false, true);

        JPanel painelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelNorte.add(new JLabel("Descrição:"));
        txtDescricao = new JTextField(30);
        painelNorte.add(txtDescricao);

        painelNorte.add(new JLabel("Percentual (ex: 0.15):"));
        txtPercentual = new JTextField(5);
        painelNorte.add(txtPercentual);

        btnSalvar = new JButton("Salvar Novo");
        painelNorte.add(btnSalvar);

        JPanel painelSul = new JPanel();
        btnExcluir = new JButton("Excluir Selecionado");
        btnFechar = new JButton("Fechar");
        painelSul.add(btnExcluir);
        painelSul.add(btnFechar);

        modeloTabela = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Descrição", "Desconto"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaDefeitos = new JTable(modeloTabela);

        add(painelNorte, BorderLayout.NORTH);
        add(new JScrollPane(tabelaDefeitos), BorderLayout.CENTER);
        add(painelSul, BorderLayout.SOUTH);

        setSize(700, 400);
        setVisible(true);
    }

    public JTextField getTxtDescricao() {
        return txtDescricao;
    }

    public JTextField getTxtPercentual() {
        return txtPercentual;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public JButton getBtnExcluir() {
        return btnExcluir;
    }

    public JButton getBtnFechar() {
        return btnFechar;
    }

    public JTable getTabelaDefeitos() {
        return tabelaDefeitos;
    }

    public DefaultTableModel getModeloTabela() {
        return modeloTabela;
    }
}
