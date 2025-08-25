package br.ufes.dcomp.trabalhoProjetos.presenter;

import br.ufes.dcomp.trabalhoProjetos.model.Defeito;
import br.ufes.dcomp.trabalhoProjetos.repository.IDefeitoRepository;
import br.ufes.dcomp.trabalhoProjetos.service.LogService;
import br.ufes.dcomp.trabalhoProjetos.view.ManterDefeitosView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ManterDefeitosPresenter {
    private final ManterDefeitosView view;
    private final IDefeitoRepository defeitoRepository;

    public ManterDefeitosPresenter(ManterDefeitosView view, IDefeitoRepository defeitoRepository) {
        this.view = view;
        this.defeitoRepository = defeitoRepository;

        configurarListeners();
        carregarDefeitos();
    }

    private void configurarListeners() {
        view.getBtnFechar().addActionListener(e -> view.dispose());
        view.getBtnSalvar().addActionListener(e -> salvar());
        view.getBtnExcluir().addActionListener(e -> excluir());
    }

    private void carregarDefeitos() {
        DefaultTableModel modelo = view.getModeloTabela();
        modelo.setRowCount(0);
        try {
            List<Defeito> defeitos = defeitoRepository.buscarTodos();
            for (Defeito d : defeitos) {
                modelo.addRow(new Object[]{
                        d.getId(),
                        d.getDescricao(),
                        String.format("%.0f%%", d.getPercentualDesconto() * 100)
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao carregar defeitos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvar() {
        try {
            String descricao = view.getTxtDescricao().getText();
            double percentual = Double.parseDouble(view.getTxtPercentual().getText().replace(",", "."));
            if (descricao.isBlank() || percentual <= 0 || percentual >= 1) {
                throw new IllegalArgumentException("Dados do defeito inválidos.");
            }
            Defeito novoDefeito = new Defeito(descricao, percentual);
            defeitoRepository.salvar(novoDefeito);
            LogService.getInstancia().registrar("CADASTRO_DEFEITO", "Admin", "Defeito '" + descricao + "' criado.");

            view.getTxtDescricao().setText("");
            view.getTxtPercentual().setText("");
            carregarDefeitos();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(view, "O percentual deve ser um número válido (ex: 0.15).", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao salvar defeito: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        int linha = view.getTabelaDefeitos().getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um defeito para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) view.getTabelaDefeitos().getValueAt(linha, 0);
        String desc = (String) view.getTabelaDefeitos().getValueAt(linha, 1);

        int confirm = JOptionPane.showConfirmDialog(view, "Deseja mesmo excluir o defeito '" + desc + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                defeitoRepository.deletar(id);
                LogService.getInstancia().registrar("EXCLUSAO_DEFEITO", "Admin", "Defeito '" + desc + "' (ID " + id + ") excluído.");
                carregarDefeitos();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Erro ao excluir defeito: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}