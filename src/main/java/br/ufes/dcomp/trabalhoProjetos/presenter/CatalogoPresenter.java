package br.ufes.dcomp.trabalhoProjetos.presenter;

import br.ufes.dcomp.trabalhoProjetos.model.Item;
import br.ufes.dcomp.trabalhoProjetos.model.Transacao;
import br.ufes.dcomp.trabalhoProjetos.model.Usuario;
import br.ufes.dcomp.trabalhoProjetos.repository.*;
import br.ufes.dcomp.trabalhoProjetos.service.*;
import br.ufes.dcomp.trabalhoProjetos.view.CatalogoView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogoPresenter {
    private final CatalogoView view;
    private final IItemRepository itemRepository;
    private final IUsuarioRepository usuarioRepository;
    private final ITransacaoRepository transacaoRepository;
    private final CalculadoraService calculadoraService;
    private final SessaoService sessaoService;
    private final LogService logService;
    private final ReputacaoService reputacaoService;

    public CatalogoPresenter(CatalogoView view, IItemRepository itemRepository, IUsuarioRepository usuarioRepository) {
        this.view = view;
        this.itemRepository = itemRepository;
        this.usuarioRepository = usuarioRepository;
        this.transacaoRepository = new TransacaoRepository();
        this.calculadoraService = new CalculadoraService();
        this.sessaoService = SessaoService.getInstancia();
        this.logService = LogService.getInstancia();
        this.reputacaoService = new ReputacaoService();

        atualizarLabelUsuario();
        configurarListeners();
        carregarItensDisponiveis();
    }

    private void configurarListeners() {
        view.getBtnAtualizar().addActionListener(e -> carregarItensDisponiveis());
        view.getBtnComprar().addActionListener(e -> fazerOferta());
    }

    private void carregarItensDisponiveis() {
        DefaultTableModel modelo = view.getModeloTabela();
        modelo.setRowCount(0);

        try {
            List<Item> todosItens = itemRepository.buscarTodos();
            for (Item item : todosItens) {
                if (item.isDisponivel()) {
                    calculadoraService.calcularIndicadores(item);
                    double precoFinal = calculadoraService.calcularPrecoFinal(item);
                    String nomeVendedor = usuarioRepository.buscarPorId(item.getIdVendedor()).map(Usuario::getNome).orElse("Desconhecido");
                    String defeitosStr = item.getDefeitos().stream().map(d -> d.getDescricao()).collect(Collectors.joining(", "));

                    modelo.addRow(new Object[]{
                            item.getId(),
                            item.getNome(),
                            "Defeitos: " + (defeitosStr.isEmpty() ? "Nenhum" : defeitosStr),
                            nomeVendedor,
                            String.format("%.2f", precoFinal),
                            String.format("%.4f", item.getGwpEvitado()),
                            String.format("%.2f", item.getMci())
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao carregar itens: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fazerOferta() {
        if (!sessaoService.isLogado()) {
            JOptionPane.showMessageDialog(view, "Você precisa estar logado para fazer uma oferta.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int linhaSelecionada = view.getTabelaItens().getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um item para fazer uma oferta.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idItem = (int) view.getTabelaItens().getValueAt(linhaSelecionada, 0);
        Usuario comprador = sessaoService.getUsuarioLogado();

        try {
            Item item = itemRepository.buscarPorId(idItem).orElseThrow(() -> new Exception("Item não encontrado. Pode já ter sido vendido."));

            if (item.getIdVendedor() == comprador.getId()) {
                JOptionPane.showMessageDialog(view, "Você não pode comprar seu próprio item.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double precoFinal = calculadoraService.calcularPrecoFinal(item);
            String ofertaStr = JOptionPane.showInputDialog(view, "Preço Final: R$ " + String.format("%.2f", precoFinal) + ".\nQual sua oferta (1%-20% abaixo)?", "Fazer Oferta", JOptionPane.PLAIN_MESSAGE);
            // Usuário cancelou
            if (ofertaStr == null)
                return;

            double valorOferta = Double.parseDouble(ofertaStr.replace(",", "."));
            double limiteInferior = precoFinal * 0.80; // 20% de desconto

            if (valorOferta >= limiteInferior && valorOferta <= precoFinal) {
                // 1. Atualiza o item para indisponível
                item.setDisponivel(false);
                itemRepository.atualizar(item);

                // 2. Registra a transação no banco de dados
                Transacao transacao = new Transacao(item.getId(), item.getIdVendedor(), comprador.getId(), valorOferta);
                transacaoRepository.salvar(transacao);

                // 3. Atribui pontos de reputação ao vendedor e comprador
                Usuario vendedor = usuarioRepository.buscarPorId(item.getIdVendedor()).orElseThrow(() -> new Exception("Vendedor do item não encontrado."));
                reputacaoService.registrarVendaConcluida(vendedor, comprador);

                logService.registrar("VENDA_CONCLUIDA", comprador.getNome(), "Item ID " + item.getId() + " comprado por R$ " + String.format("%.2f", valorOferta));
                JOptionPane.showMessageDialog(view, "Parabéns! Sua oferta foi aceita e a compra foi concluída.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarItensDisponiveis(); // Atualiza o catálogo para remover o item vendido
            } else {
                JOptionPane.showMessageDialog(view, "Oferta fora da faixa permitida (até 20% de desconto sobre o preço final).", "Oferta Inválida", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(view, "Por favor, insira um valor numérico válido para a oferta.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erro ao processar oferta: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarLabelUsuario() {
        if (sessaoService.isLogado()) {
            view.getLblUsuarioLogado().setText("Usuário Logado: " + sessaoService.getUsuarioLogado().getNome());
        } else {
            view.getLblUsuarioLogado().setText("Usuário: Ninguém logado");
        }
    }
}