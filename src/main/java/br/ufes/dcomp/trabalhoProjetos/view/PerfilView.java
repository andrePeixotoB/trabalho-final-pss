package br.ufes.dcomp.trabalhoProjetos.view;

import javax.swing.*;
import java.awt.*;

public class PerfilView extends JInternalFrame {

    private JLabel lblNomeUsuario;
    private JLabel lblNivelComprador;
    private JProgressBar progressComprador;
    private JLabel lblNivelVendedor;
    private JProgressBar progressVendedor;
    private JButton btnFechar;

    public PerfilView() {
        super("Meu Perfil", false, true, false, true);

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblNomeUsuario = new JLabel("Nome do Usuário");
        lblNomeUsuario.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; painel.add(lblNomeUsuario, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        painel.add(new JLabel("Reputação como Comprador:"), gbc);
        gbc.gridy = 2;
        lblNivelComprador = new JLabel("Nível (0.00 / 5.00)");
        painel.add(lblNivelComprador, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        progressComprador = new JProgressBar(0, 500); // 500 = 5.00 * 100
        painel.add(progressComprador, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        painel.add(new JLabel("Reputação como Vendedor:"), gbc);
        gbc.gridy = 4;
        lblNivelVendedor = new JLabel("Nível (0.00 / 5.00)");
        painel.add(lblNivelVendedor, gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0;
        progressVendedor = new JProgressBar(0, 500);
        painel.add(progressVendedor, gbc);

        JPanel painelSul = new JPanel();
        btnFechar = new JButton("Fechar");
        painelSul.add(btnFechar);

        add(painel, BorderLayout.CENTER);
        add(painelSul, BorderLayout.SOUTH);

        setSize(500, 300);
        setVisible(true);
    }

    public JLabel getLblNomeUsuario() {
        return lblNomeUsuario;
    }
    public JLabel getLblNivelComprador() {
        return lblNivelComprador;
    }
    public JProgressBar getProgressComprador() {
        return progressComprador;
    }
    public JLabel getLblNivelVendedor() {
        return lblNivelVendedor;
    }
    public JProgressBar getProgressVendedor() {
        return progressVendedor;
    }
    public JButton getBtnFechar() {
        return btnFechar;
    }
}