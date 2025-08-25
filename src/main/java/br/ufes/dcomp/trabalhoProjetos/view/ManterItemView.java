package br.ufes.dcomp.trabalhoProjetos.view;

import br.ufes.dcomp.trabalhoProjetos.model.Defeito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManterItemView extends JInternalFrame {

    private JTextField txtId;
    private JTextField txtNome;
    private JTextArea txtDescricao;
    private JTextField txtPrecoBase;
    private JTextField txtMassa;
    private JList<Defeito> listaDefeitos;
    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JButton btnFechar;
    private JTable tabelaItens;
    private DefaultTableModel modeloTabela;

    public ManterItemView() {
        super("Manter Meus Itens", true, true, true, true);

        JPanel painelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0: ID e Nome
        gbc.gridx = 0; gbc.gridy = 0; painelFormulario.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0;
        txtId = new JTextField(5); txtId.setEnabled(false); painelFormulario.add(txtId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0;
        txtNome = new JTextField(20); painelFormulario.add(txtNome, gbc);
        gbc.weightx = 0;

        // Linha 1: Preço e Massa
        gbc.gridx = 0; gbc.gridy = 1; painelFormulario.add(new JLabel("Preço Base (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; txtPrecoBase = new JTextField(10); painelFormulario.add(txtPrecoBase, gbc);
        gbc.gridx = 2; gbc.gridy = 1; painelFormulario.add(new JLabel("Massa (kg):"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; txtMassa = new JTextField(10); painelFormulario.add(txtMassa, gbc);

        // Linha 2: Descrição
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.NORTHWEST; painelFormulario.add(new JLabel("Descrição:"), gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3; gbc.ipady = 40;
        txtDescricao = new JTextArea();
        painelFormulario.add(new JScrollPane(txtDescricao), gbc);
        gbc.gridwidth = 1; gbc.ipady = 0;

        // Linha 3: Lista de Defeitos
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.NORTHWEST; painelFormulario.add(new JLabel("Defeitos:"), gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 3; gbc.ipady = 60;
        listaDefeitos = new JList<>();
        listaDefeitos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        painelFormulario.add(new JScrollPane(listaDefeitos), gbc);
        gbc.gridwidth = 1; gbc.ipady = 0;

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        btnFechar = new JButton("Fechar");
        painelBotoes.add(btnNovo);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnFechar);

        modeloTabela = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Nome", "Preço Base", "Disponível"}) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaItens = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaItens);

        setLayout(new BorderLayout(5, 5));
        add(painelFormulario, BorderLayout.NORTH);
        add(scrollTabela, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        setSize(750, 600);

        setVisible(true);
    }

    public JTextField getTxtId() {
        return txtId;
    }

    public JTextField getTxtNome() {
        return txtNome;
    }
    public JTextArea getTxtDescricao() {
        return txtDescricao;
    }
    public JTextField getTxtPrecoBase() {
        return txtPrecoBase;
    }
    public JTextField getTxtMassa() {
        return txtMassa;
    }
    public JList<Defeito> getListaDefeitos() {
        return listaDefeitos;
    }
    public JButton getBtnNovo() {
        return btnNovo;
    }
    public JButton getBtnSalvar() {
        return btnSalvar;
    }
    public JButton getBtnEditar() {
        return btnEditar;
    }
    public JButton getBtnExcluir() {
        return btnExcluir;
    }
    public JButton getBtnFechar() {
        return btnFechar;
    }
    public JTable getTabelaItens() {
        return tabelaItens;
    }
    public DefaultTableModel getModeloTabela() {
        return modeloTabela;
    }
}