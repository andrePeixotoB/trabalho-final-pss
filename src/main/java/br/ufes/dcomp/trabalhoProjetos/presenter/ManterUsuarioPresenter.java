package br.ufes.dcomp.trabalhoProjetos.presenter;

import br.ufes.dcomp.trabalhoProjetos.model.Usuario;
import br.ufes.dcomp.trabalhoProjetos.repository.IUsuarioRepository;
import br.ufes.dcomp.trabalhoProjetos.service.LogService;
import br.ufes.dcomp.trabalhoProjetos.view.ManterUsuarioView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ManterUsuarioPresenter {

    private final ManterUsuarioView view;
    private final IUsuarioRepository usuarioRepository;
    private final LogService logService;
    private String estado;

    public ManterUsuarioPresenter(ManterUsuarioView view, IUsuarioRepository usuarioRepository) {
        this.view = view;
        this.usuarioRepository = usuarioRepository;
        this.logService = LogService.getInstancia();
        this.estado = "inicial";

        carregarUsuariosNaTabela();
        configurarListeners();
        gerenciarEstadoComponentes();
    }

    private void configurarListeners() {
        view.getBtnFechar().addActionListener(e -> view.dispose());
        view.getBtnNovo().addActionListener(e -> novoUsuario());
        view.getBtnSalvar().addActionListener(e -> salvarUsuario());
        view.getBtnEditar().addActionListener(e -> editarUsuario());
        view.getBtnExcluir().addActionListener(e -> excluirUsuario());
        view.getTabelaUsuarios().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    selecionarUsuario();
                }
            }
        });
    }

    private void novoUsuario() {
        this.estado = "insercao";
        limparCampos();
        gerenciarEstadoComponentes();
        view.getTxtNome().requestFocus();
    }

    private void editarUsuario() {
        if (view.getTabelaUsuarios().getSelectedRow() != -1) {
            this.estado = "edicao";
            gerenciarEstadoComponentes();
            view.getTxtNome().requestFocus();
        } else {
            JOptionPane.showMessageDialog(view, "Selecione um usuário para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void salvarUsuario() {
        String nome = view.getTxtNome().getText();
        String email = view.getTxtEmail().getText();
        String senha = new String(view.getTxtSenha().getPassword());

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (estado.equals("insercao")) {
                Usuario novoUsuario = new Usuario(nome, email, senha);
                usuarioRepository.salvar(novoUsuario);
                logService.registrar("INSERCAO_USUARIO", "Admin", "Usuário '" + nome + "' criado.");
                JOptionPane.showMessageDialog(view, "Usuário salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else if (estado.equals("edicao")) {
                int id = Integer.parseInt(view.getTxtId().getText());
                Usuario usuarioExistente = new Usuario(nome, email, senha);
                usuarioExistente.setId(id);
                usuarioRepository.atualizar(usuarioExistente);
                logService.registrar("EDICAO_USUARIO", "Admin", "Usuário ID " + id + " atualizado.");
                JOptionPane.showMessageDialog(view, "Usuário atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }

            this.estado = "inicial";
            limparCampos();
            carregarUsuariosNaTabela();
            gerenciarEstadoComponentes();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erro ao salvar usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            logService.registrar("ERRO", "Admin", "Falha ao salvar usuário: " + ex.getMessage());
        }
    }

    private void excluirUsuario() {
        int linhaSelecionada = view.getTabelaUsuarios().getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um usuário para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) view.getTabelaUsuarios().getValueAt(linhaSelecionada, 0);
        String nome = (String) view.getTabelaUsuarios().getValueAt(linhaSelecionada, 1);
        int confirmacao = JOptionPane.showConfirmDialog(view, "Tem certeza que deseja excluir o usuário selecionado?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                Usuario usuarioParaExcluir = new Usuario(null, null, null);
                usuarioParaExcluir.setId(id);
                usuarioRepository.deletar(usuarioParaExcluir);
                logService.registrar("EXCLUSAO_USUARIO", "Admin", "Usuário '" + nome + "' (ID " + id + ") excluído.");
                carregarUsuariosNaTabela();
                limparCampos();
                this.estado = "inicial";
                gerenciarEstadoComponentes();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Erro ao excluir usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                logService.registrar("ERRO", "Admin", "Falha ao excluir usuário ID " + id + ": " + ex.getMessage());
            }
        }
    }

    private void selecionarUsuario() {
        int linhaSelecionada = view.getTabelaUsuarios().getSelectedRow();
        if (linhaSelecionada != -1) {
            int id = (int) view.getTabelaUsuarios().getValueAt(linhaSelecionada, 0);
            try {
                usuarioRepository.buscarPorId(id).ifPresent(usuario -> {
                    view.getTxtId().setText(String.valueOf(usuario.getId()));
                    view.getTxtNome().setText(usuario.getNome());
                    view.getTxtEmail().setText(usuario.getEmail());
                    view.getTxtSenha().setText(usuario.getSenha());
                    this.estado = "selecao";
                    gerenciarEstadoComponentes();
                });
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Erro ao buscar usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarUsuariosNaTabela() {
        DefaultTableModel modelo = view.getModeloTabela();
        modelo.setRowCount(0);

        try {
            List<Usuario> usuarios = usuarioRepository.buscarTodos();
            for (Usuario u : usuarios) {
                modelo.addRow(new Object[]{u.getId(), u.getNome(), u.getEmail()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao carregar usuários: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void gerenciarEstadoComponentes() {
        boolean camposEditaveis = estado.equals("insercao") || estado.equals("edicao");
        view.getTxtNome().setEnabled(camposEditaveis);
        view.getTxtEmail().setEnabled(camposEditaveis);
        view.getTxtSenha().setEnabled(camposEditaveis);

        view.getBtnSalvar().setEnabled(camposEditaveis);
        view.getBtnNovo().setEnabled(!camposEditaveis);

        boolean itemSelecionado = estado.equals("selecao");
        view.getBtnEditar().setEnabled(itemSelecionado);
        view.getBtnExcluir().setEnabled(itemSelecionado);
    }

    private void limparCampos() {
        view.getTxtId().setText("");
        view.getTxtNome().setText("");
        view.getTxtEmail().setText("");
        view.getTxtSenha().setText("");
    }
}