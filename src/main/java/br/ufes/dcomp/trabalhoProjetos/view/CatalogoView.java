package br.ufes.dcomp.trabalhoProjetos.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CatalogoView extends JInternalFrame {

    private JTable tabelaItens;
    private DefaultTableModel modeloTabela;
    private JButton btnComprar;
    private JButton btnAtualizar;
    private JLabel lblUsuarioLogado;

    public CatalogoView() {
        super("Catálogo de Itens", true, true, true, true);

        JPanel painelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblUsuarioLogado = new JLabel("Usuário: Ninguém logado");
        painelNorte.add(lblUsuarioLogado);

        JPanel painelSul = new JPanel();
        btnComprar = new JButton("Fazer Oferta");
        btnAtualizar = new JButton("Atualizar Catálogo");
        painelSul.add(btnComprar);
        painelSul.add(btnAtualizar);

        modeloTabela = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                        "ID",
                        "Item",
                        "Descrição",
                        "Vendedor",
                        "Preço (R$)",
                        "GWP Evitado (kg CO2e)",
                        "MCI"
                }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaItens = new JTable(modeloTabela);
        tabelaItens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(painelNorte, BorderLayout.NORTH);
        add(new JScrollPane(tabelaItens), BorderLayout.CENTER);
        add(painelSul, BorderLayout.SOUTH);

        setSize(850, 500);
        setVisible(true);
    }

    public JTable getTabelaItens() {
        return tabelaItens;
    }

    public DefaultTableModel getModeloTabela() {
        return modeloTabela;
    }

    public JButton getBtnComprar() {
        return btnComprar;
    }

    public JButton getBtnAtualizar() {
        return btnAtualizar;
    }

    public JLabel getLblUsuarioLogado() {
        return lblUsuarioLogado;
    }
}
