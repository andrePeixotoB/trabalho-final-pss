package br.ufes.dcomp.trabalhoProjetos.service;

import br.ufes.dcomp.trabalhoProjetos.model.Usuario;
import br.ufes.dcomp.trabalhoProjetos.repository.IUsuarioRepository;
import br.ufes.dcomp.trabalhoProjetos.repository.UsuarioRepository;

public class ReputacaoService {

    private final IUsuarioRepository usuarioRepository;

    public ReputacaoService() {
        this.usuarioRepository = new UsuarioRepository();
    }

    public void registrarVendaConcluida(Usuario vendedor, Usuario comprador) {
        double novaRepVendedor = vendedor.getReputacaoVendedor() + 0.5;
        vendedor.setReputacaoVendedor(novaRepVendedor);

        double novaRepComprador = comprador.getReputacaoComprador() + 0.5;
        comprador.setReputacaoComprador(novaRepComprador);

        try {
            usuarioRepository.atualizar(vendedor);
            usuarioRepository.atualizar(comprador);
            LogService.getInstancia().registrar("REPUTACAO", "SISTEMA",
                    "Vendedor " + vendedor.getNome() + " ganhou +0.5 estrela. Comprador " + comprador.getNome() + " ganhou +0.5 estrela.");
        } catch (Exception e) {
            LogService.getInstancia().registrar("ERRO", "SISTEMA", "Falha ao atualizar reputação após venda: " + e.getMessage());
        }
    }

    public void registrarItemCadastrado(Usuario vendedor) {
        double novaRepVendedor = vendedor.getReputacaoVendedor() + 0.05;
        vendedor.setReputacaoVendedor(novaRepVendedor);
        try {
            usuarioRepository.atualizar(vendedor);
            LogService.getInstancia().registrar("REPUTACAO", "SISTEMA", "Vendedor " + vendedor.getNome() + " ganhou +0.05 estrela por cadastro de item.");
        } catch (Exception e) {
            LogService.getInstancia().registrar("ERRO", "SISTEMA", "Falha ao atualizar reputação após cadastro: " + e.getMessage());
        }
    }

    public String getNomeNivel(double pontuacao) {
        if (pontuacao < 5.0) return "Bronze";
        if (pontuacao < 10.0) return "Prata";
        return "Ouro";
    }
}