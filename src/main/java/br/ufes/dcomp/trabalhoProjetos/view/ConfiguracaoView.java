package br.ufes.dcomp.trabalhoProjetos.view;

import javax.swing.*;
import java.awt.*;

public class ConfiguracaoView extends JInternalFrame {

    private JComboBox<String> comboFormatoLog;
    private JButton btnSalvar;
    private JButton btnFechar;

    public ConfiguracaoView() {
        super("Configurações", false, true, false, true);

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        painel.add(new JLabel("Formato do Log do Sistema:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        comboFormatoLog = new JComboBox<>(new String[]{"CSV", "JSON"});
        painel.add(comboFormatoLog, gbc);

        JPanel painelBotoes = new JPanel();
        btnSalvar = new JButton("Salvar");
        btnFechar = new JButton("Fechar");
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnFechar);

        add(painel, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        pack();
        setLocation(30, 30);
        setVisible(true);
    }

    public JComboBox<String> getComboFormatoLog() {
        return comboFormatoLog;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public JButton getBtnFechar() {
        return btnFechar;
    }
}