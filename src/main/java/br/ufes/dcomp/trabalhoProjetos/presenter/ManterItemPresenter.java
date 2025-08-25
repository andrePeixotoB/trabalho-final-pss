package br.ufes.dcomp.trabalhoProjetos.presenter;

import br.ufes.dcomp.trabalhoProjetos.model.Defeito;
import br.ufes.dcomp.trabalhoProjetos.model.Item;
import br.ufes.dcomp.trabalhoProjetos.repository.DefeitoRepository;
import br.ufes.dcomp.trabalhoProjetos.repository.IDefeitoRepository;
import br.ufes.dcomp.trabalhoProjetos.repository.IItemRepository;
import br.ufes.dcomp.trabalhoProjetos.service.LogService;
import br.ufes.dcomp.trabalhoProjetos.service.ReputacaoService;
import br.ufes.dcomp.trabalhoProjetos.service.SessaoService;
import br.ufes.dcomp.trabalhoProjetos.view.ManterItemView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ManterItemPresenter {
    private final ManterItemView view;
    private final IItemRepository itemRepository;
    private final IDefeitoRepository defeitoRepository;
    private final LogService logService;
    private final SessaoService sessaoService;
    private final ReputacaoService reputacaoService;
    private String estado;

    public ManterItemPresenter(ManterItemView view, IItemRepository itemRepository) {
        this.view = view;
        this.itemRepository = itemRepository;
        this.defeitoRepository = new DefeitoRepository();
        this.logService = LogService.getInstancia();
        this.sessaoService = SessaoService.getInstancia();
        this.reputacaoService = new ReputacaoService();
        this.estado = "inicial";

        carregarDefeitosDisponiveis();
        carregarItensNaTabela();
        configurarListeners();
        gerenciarEstadoComponentes();
    }

    private void configurarListeners() {
        view.getBtnFechar().addActionListener(e -> view.dispose());
        view.getBtnNovo().addActionListener(e -> novoItem());
        view.getBtnSalvar().addActionListener(e -> salvarItem());
        view.getBtnEditar().addActionListener(e -> editarItem());
        view.getBtnExcluir().addActionListener(e -> excluirItem());
        view.getTabelaItens().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    selecionarItem();
                }
            }
        });
    }

    private void salvarItem() {
        try {
            String nome = view.getTxtNome().getText();
            String descricao = view.getTxtDescricao().getText();
            double preco = Double.parseDouble(view.getTxtPrecoBase().getText().replace(",", "."));
            double massa = Double.parseDouble(view.getTxtMassa().getText().replace(",", "."));
            int idVendedor = sessaoService.getUsuarioLogado().getId();

            if (nome.isEmpty()) throw new IllegalArgumentException("O campo 'Nome' é obrigatório.");

            Item item = new Item(nome, descricao, preco, massa, idVendedor);
            item.setDefeitos(view.getListaDefeitos().getSelectedValuesList());

            if (estado.equals("insercao")) {
                itemRepository.salvar(item);
                logService.registrar("INSERCAO_ITEM", sessaoService.getUsuarioLogado().getNome(), "Item '" + nome + "' criado.");

                // CHAMA O SERVIÇO DE REPUTAÇÃO
                reputacaoService.registrarItemCadastrado(sessaoService.getUsuarioLogado());

                JOptionPane.showMessageDialog(view, "Item salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else if (estado.equals("edicao")) {
                item.setId(Integer.parseInt(view.getTxtId().getText()));
                itemRepository.atualizar(item);
                logService.registrar("EDICAO_ITEM", sessaoService.getUsuarioLogado().getNome(), "Item ID " + item.getId() + " atualizado.");
                JOptionPane.showMessageDialog(view, "Item atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }

            this.estado = "inicial";
            limparCampos();
            carregarItensNaTabela();
            gerenciarEstadoComponentes();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(view, "Os campos 'Preço' e 'Massa' devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erro ao salvar item: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarDefeitosDisponiveis() {
        try {
            List<Defeito> defeitos = defeitoRepository.buscarTodos();
            view.getListaDefeitos().setListData(new Vector<>(defeitos));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao carregar lista de defeitos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarItensNaTabela() {
        DefaultTableModel modelo = view.getModeloTabela();
        modelo.setRowCount(0);
        try {
            List<Item> todosItens = itemRepository.buscarTodos();
            int idUsuarioLogado = sessaoService.getUsuarioLogado().getId();
            for (Item item : todosItens) {
                if (item.getIdVendedor() == idUsuarioLogado) {
                    modelo.addRow(new Object[]{
                            item.getId(),
                            item.getNome(),
                            String.format("R$ %.2f", item.getPrecoBase()),
                            item.isDisponivel() ? "Sim" : "Não"
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao carregar seus itens: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void novoItem() {
        this.estado = "insercao";
        limparCampos();
        gerenciarEstadoComponentes();
        view.getTxtNome().requestFocus();
    }

    private void editarItem() {
        if (view.getTabelaItens().getSelectedRow() != -1) {
            this.estado = "edicao";
            gerenciarEstadoComponentes();
            view.getTxtNome().requestFocus();
        } else {
            JOptionPane.showMessageDialog(view, "Selecione um item para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void excluirItem() {
        int linhaSelecionada = view.getTabelaItens().getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um item para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) view.getTabelaItens().getValueAt(linhaSelecionada, 0);
        int confirmacao = JOptionPane.showConfirmDialog(view, "Tem certeza que deseja excluir o item selecionado?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                Item itemParaExcluir = new Item(null, null, 0, 0, 0);
                itemParaExcluir.setId(id);
                itemRepository.deletar(itemParaExcluir);
                carregarItensNaTabela();
                limparCampos();
                this.estado = "inicial";
                gerenciarEstadoComponentes();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Erro ao excluir item: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void selecionarItem() {
        int linhaSelecionada = view.getTabelaItens().getSelectedRow();
        if (linhaSelecionada != -1) {
            int id = (int) view.getTabelaItens().getValueAt(linhaSelecionada, 0);
            try {
                itemRepository.buscarPorId(id).ifPresent(item -> {
                    view.getTxtId().setText(String.valueOf(item.getId()));
                    view.getTxtNome().setText(item.getNome());
                    view.getTxtDescricao().setText(item.getDescricao());
                    view.getTxtPrecoBase().setText(String.format("%.2f", item.getPrecoBase()).replace(",", "."));
                    view.getTxtMassa().setText(String.format("%.3f", item.getMassaEstimadaKg()).replace(",", "."));
                    List<Integer> indicesParaSelecionar = new ArrayList<>();
                    ListModel<Defeito> model = view.getListaDefeitos().getModel();
                    List<Defeito> defeitosDoItem = item.getDefeitos();
                    for (int i = 0; i < model.getSize(); i++) {
                        if (defeitosDoItem.contains(model.getElementAt(i))) {
                            indicesParaSelecionar.add(i);
                        }
                    }
                    view.getListaDefeitos().setSelectedIndices(indicesParaSelecionar.stream().mapToInt(i -> i).toArray());
                    this.estado = "selecao";
                    gerenciarEstadoComponentes();
                });
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Erro ao buscar item: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void gerenciarEstadoComponentes() {
        boolean camposEditaveis = estado.equals("insercao") || estado.equals("edicao");
        view.getTxtNome().setEnabled(camposEditaveis);
        view.getTxtDescricao().setEnabled(camposEditaveis);
        view.getTxtPrecoBase().setEnabled(camposEditaveis);
        view.getTxtMassa().setEnabled(camposEditaveis);
        view.getListaDefeitos().setEnabled(camposEditaveis);
        view.getBtnSalvar().setEnabled(camposEditaveis);
        view.getBtnNovo().setEnabled(!camposEditaveis);
        boolean itemSelecionado = estado.equals("selecao");
        view.getBtnEditar().setEnabled(itemSelecionado);
        view.getBtnExcluir().setEnabled(itemSelecionado);
    }

    private void limparCampos() {
        view.getTxtId().setText("");
        view.getTxtNome().setText("");
        view.getTxtDescricao().setText("");
        view.getTxtPrecoBase().setText("");
        view.getTxtMassa().setText("");
        view.getListaDefeitos().clearSelection();
    }
}