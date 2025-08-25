package br.ufes.dcomp.trabalhoProjetos.presenter;

import br.ufes.dcomp.trabalhoProjetos.view.DashboardView;
import br.ufes.dcomp.trabalhoProjetos.view.MainView;

public class DashboardPresenter {

    private final DashboardView view;
    private final MainView mainView;

    public DashboardPresenter(DashboardView view, MainView mainView) {
        this.view = view;
        this.mainView = mainView;

        configurarListeners();
    }

    private void configurarListeners() {
        view.getBtnGerenciarUsuarios().addActionListener(e -> mainView.abrirManterUsuarios());
        view.getBtnGerenciarItens().addActionListener(e -> mainView.abrirManterItens());
        view.getBtnVerCatalogo().addActionListener(e -> mainView.abrirCatalogo());
        view.getBtnConfiguracoes().addActionListener(e -> mainView.abrirConfiguracoes());
    }
}