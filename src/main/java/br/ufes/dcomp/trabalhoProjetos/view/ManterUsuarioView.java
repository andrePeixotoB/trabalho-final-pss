package br.ufes.dcomp.trabalhoProjetos.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManterUsuarioView extends JInternalFrame {

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JButton btnFechar;
    private JTable tabelaUsuarios;
    private DefaultTableModel modeloTabela;

    public ManterUsuarioView() {
        super("Manter Usuários", true, true, true, true);

        JPanel painelNorte = new JPanel(new GridBagLayout());
        JPanel painelSul = new JPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Componentes do formulário
        gbc.gridx = 0; gbc.gridy = 0; painelNorte.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; txtId = new JTextField(5); txtId.setEnabled(false); painelNorte.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painelNorte.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNome = new JTextField(30); painelNorte.add(txtNome, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0; gbc.gridy = 2; painelNorte.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtEmail = new JTextField(30); painelNorte.add(txtEmail, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0; gbc.gridy = 3; painelNorte.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtSenha = new JPasswordField(30); painelNorte.add(txtSenha, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;

        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        btnFechar = new JButton("Fechar");

        painelSul.add(btnNovo);
        painelSul.add(btnSalvar);
        painelSul.add(btnEditar);
        painelSul.add(btnExcluir);
        painelSul.add(btnFechar);

        modeloTabela = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nome", "Email"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna a tabela não editável
            }
        };
        tabelaUsuarios = new JTable(modeloTabela);
        tabelaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);

        add(painelNorte, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(painelSul, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    public JTextField getTxtId() {
        return txtId;
    }
    public JTextField getTxtNome() {
        return txtNome;
    }
    public JTextField getTxtEmail() {
        return txtEmail;
    }
    public JPasswordField getTxtSenha() {
        return txtSenha;
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
    public JTable getTabelaUsuarios() {
        return tabelaUsuarios;
    }
    public DefaultTableModel getModeloTabela() {
        return modeloTabela;
    }
}
