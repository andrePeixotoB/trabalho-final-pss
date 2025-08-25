package br.ufes.dcomp.trabalhoProjetos.presenter;

import br.ufes.dcomp.trabalhoProjetos.service.LogService;
import br.ufes.dcomp.trabalhoProjetos.view.ConfiguracaoView;

import javax.swing.*;

public class ConfiguracaoPresenter {

    private final ConfiguracaoView view;
    private final LogService logService;

    public ConfiguracaoPresenter(ConfiguracaoView view) {
        this.view = view;
        this.logService = LogService.getInstancia();

        carregarConfiguracaoAtual();
        configurarListeners();
    }

    private void carregarConfiguracaoAtual() {
        // Define o item selecionado no ComboBox com base na configuração atual do LogService
        view.getComboFormatoLog().setSelectedItem(logService.getTipoLogAtual());
    }

    private void configurarListeners() {
        view.getBtnFechar().addActionListener(e -> view.dispose());

        view.getBtnSalvar().addActionListener(e -> salvarConfiguracao());
    }

    private void salvarConfiguracao() {
        String tipoSelecionado = (String) view.getComboFormatoLog().getSelectedItem();

        try {
            // Delega a mudança do tipo de log para o LogService
            logService.setTipoLog(tipoSelecionado);

            JOptionPane.showMessageDialog(view, "Configuração salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            view.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erro ao salvar configuração: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}