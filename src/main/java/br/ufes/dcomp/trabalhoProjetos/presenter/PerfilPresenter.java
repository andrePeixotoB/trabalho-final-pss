package br.ufes.dcomp.trabalhoProjetos.presenter;

import br.ufes.dcomp.trabalhoProjetos.model.Usuario;
import br.ufes.dcomp.trabalhoProjetos.service.ReputacaoService;
import br.ufes.dcomp.trabalhoProjetos.service.SessaoService;
import br.ufes.dcomp.trabalhoProjetos.view.PerfilView;

public class PerfilPresenter {

    private final PerfilView view;
    private final SessaoService sessaoService;
    private final ReputacaoService reputacaoService;
    private final Usuario usuario;

    public PerfilPresenter() {
        this.view = new PerfilView();
        this.sessaoService = SessaoService.getInstancia();
        this.reputacaoService = new ReputacaoService();
        this.usuario = sessaoService.getUsuarioLogado();

        carregarDados();
        configurarListeners();
    }

    public PerfilView getView() {
        return view;
    }

    private void configurarListeners() {
        view.getBtnFechar().addActionListener(e -> view.dispose());
    }

    private void carregarDados() {
        view.getLblNomeUsuario().setText(usuario.getNome());

        // Reputação de Comprador
        double repComprador = usuario.getReputacaoComprador();
        String nivelComprador = reputacaoService.getNomeNivel(repComprador);
        double progressoComprador = repComprador % 5.0; // Progresso dentro do nível atual
        view.getLblNivelComprador().setText(nivelComprador + String.format(" (%.2f / 5.00)", progressoComprador));
        view.getProgressComprador().setValue((int) (progressoComprador * 100));

        // Reputação de Vendedor
        double repVendedor = usuario.getReputacaoVendedor();
        String nivelVendedor = reputacaoService.getNomeNivel(repVendedor);
        double progressoVendedor = repVendedor % 5.0; // Progresso dentro do nível atual
        view.getLblNivelVendedor().setText(nivelVendedor + String.format(" (%.2f / 5.00)", progressoVendedor));
        view.getProgressVendedor().setValue((int) (progressoVendedor * 100));
    }
}