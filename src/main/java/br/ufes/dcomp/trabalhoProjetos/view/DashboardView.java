package br.ufes.dcomp.trabalhoProjetos.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardView extends JInternalFrame {

    private JButton btnGerenciarUsuarios;
    private JButton btnGerenciarItens;
    private JButton btnVerCatalogo;
    private JButton btnConfiguracoes;

    public DashboardView() {
        super("Dashboard", false, false, false, false);
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        this.setBorder(null);

        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBorder(new EmptyBorder(40, 40, 40, 40)); // Aumenta a margem
        painelPrincipal.setOpaque(false); // Fundo transparente

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;              // Faz os botões preencherem o espaço
        gbc.insets = new Insets(10, 10, 10, 10);         // Espaçamento entre botões
        gbc.weightx = 1.0;                               // Distribui o espaço horizontalmente
        gbc.weighty = 1.0;                               // Distribui o espaço verticalmente

        btnGerenciarUsuarios = criarBotaoDashboard(
                "Gerenciar Usuários",
                "Acesse para criar, editar e remover usuários do sistema."
        );
        btnGerenciarItens = criarBotaoDashboard(
                "Gerenciar Meus Itens",
                "Cadastre novos itens para venda e gerencie seu estoque."
        );
        btnVerCatalogo = criarBotaoDashboard(
                "Ver Catálogo",
                "Explore os itens à venda, faça ofertas e participe da economia circular."
        );
        btnConfiguracoes = criarBotaoDashboard(
                "Configurações",
                "Ajuste as preferências do sistema, como o formato do log."
        );

        gbc.gridx = 0; gbc.gridy = 0;
        painelPrincipal.add(btnGerenciarUsuarios, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        painelPrincipal.add(btnGerenciarItens, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painelPrincipal.add(btnVerCatalogo, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        painelPrincipal.add(btnConfiguracoes, gbc);

        add(painelPrincipal);
        pack();
    }

    private JButton criarBotaoDashboard(String titulo, String descricao) {
        String textoHtml = "<html><body style='text-align: center;'>"
                + "<b>" + titulo + "</b><br>"
                + "<p style='font-size: 8px; color: #555555;'>"
                + descricao + "</p>"
                + "</body></html>";

        JButton botao = new JButton(textoHtml);

        // Estilização
        botao.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setBackground(new Color(255, 255, 255, 200));
        botao.setForeground(new Color(50, 50, 50));

        // Borda
        Border bordaComposta = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );
        botao.setBorder(bordaComposta);

        return botao;
    }

    public JButton getBtnGerenciarUsuarios() {
        return btnGerenciarUsuarios;
    }

    public JButton getBtnGerenciarItens() {
        return btnGerenciarItens;
    }

    public JButton getBtnVerCatalogo() {
        return btnVerCatalogo;
    }

    public JButton getBtnConfiguracoes() {
        return btnConfiguracoes;
    }
}
