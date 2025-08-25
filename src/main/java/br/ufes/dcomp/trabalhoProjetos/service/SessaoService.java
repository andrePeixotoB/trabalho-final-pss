package br.ufes.dcomp.trabalhoProjetos.service;

import br.ufes.dcomp.trabalhoProjetos.model.Usuario;

public class SessaoService {
    private static SessaoService instancia;
    private Usuario usuarioLogado;

    private SessaoService() {}

    public static synchronized SessaoService getInstancia() {
        if (instancia == null) {
            instancia = new SessaoService();
        }
        return instancia;
    }

    public void login(Usuario usuario) {
        this.usuarioLogado = usuario;
        LogService.getInstancia().registrar("LOGIN", usuario.getNome(), "Usuário logado com sucesso.");
    }

    public void logout() {
        if (this.usuarioLogado != null) {
            LogService.getInstancia().registrar("LOGOUT", usuarioLogado.getNome(), "Usuário deslogado.");
            this.usuarioLogado = null;
        }
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public boolean isLogado() {
        return this.usuarioLogado != null;
    }
}