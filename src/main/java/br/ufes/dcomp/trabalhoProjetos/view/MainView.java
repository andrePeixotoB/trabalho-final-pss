package br.ufes.dcomp.trabalhoProjetos.view;

import br.ufes.dcomp.trabalhoProjetos.model.Usuario;
import br.ufes.dcomp.trabalhoProjetos.presenter.*;
import br.ufes.dcomp.trabalhoProjetos.repository.*;
import br.ufes.dcomp.trabalhoProjetos.service.SessaoService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainView extends JFrame {

    private JDesktopPane desktopPane;
    private JMenuBar menuBar;
    private JLabel statusBar;

    public MainView() {
        super("Sistema de Economia Circular MVP");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        desktopPane = new GradientDesktopPane();
        add(desktopPane, BorderLayout.CENTER);

        statusBar = new JLabel("Pronto");
        add(statusBar, BorderLayout.SOUTH);

        criarMenuBar();
        setJMenuBar(menuBar);

        mostrarDashboard();
    }

    private void mostrarDashboard() {
        DashboardView dashboardView = new DashboardView();
        new DashboardPresenter(dashboardView, this);

        desktopPane.add(dashboardView);

        try {
            dashboardView.setMaximum(true);
        } catch (java.beans.PropertyVetoException e) {
        }

        dashboardView.setVisible(true);
    }

    public void abrirManterUsuarios() {
        abrirJanelaUnica(ManterUsuarioView.class, () -> {
            ManterUsuarioView view = new ManterUsuarioView();
            new ManterUsuarioPresenter(view, new UsuarioRepository());
            return view;
        });
    }

    public void abrirManterItens() {
        if (!SessaoService.getInstancia().isLogado()) {
            JOptionPane.showMessageDialog(this, "Você precisa estar logado para gerenciar seus itens.", "Aviso", JOptionPane.WARNING_MESSAGE);
            fazerLogin();
            if (!SessaoService.getInstancia().isLogado()) return;
        }
        abrirJanelaUnica(ManterItemView.class, () -> {
            ManterItemView view = new ManterItemView();
            new ManterItemPresenter(view, new ItemRepository());
            return view;
        });
    }

    public void abrirCatalogo() {
        abrirJanelaUnica(CatalogoView.class, () -> {
            CatalogoView view = new CatalogoView();
            new CatalogoPresenter(view, new ItemRepository(), new UsuarioRepository());
            return view;
        });
    }

    public void abrirConfiguracoes() {
        abrirJanelaUnica(ConfiguracaoView.class, () -> {
            ConfiguracaoView view = new ConfiguracaoView();
            new ConfiguracaoPresenter(view);
            return view;
        });
    }


    private void criarMenuBar() {
        menuBar = new JMenuBar();

        JMenu menuSistema = new JMenu("Sistema");
        JMenuItem itemLogin = new JMenuItem("Login (Trocar Usuário)");
        JMenuItem itemPerfil = new JMenuItem("Ver Meu Perfil");
        JMenuItem itemSair = new JMenuItem("Sair");
        itemLogin.addActionListener(e -> fazerLogin());
        itemPerfil.addActionListener(e -> abrirPerfil());
        itemSair.addActionListener(e -> System.exit(0));
        menuSistema.add(itemLogin);
        menuSistema.add(itemPerfil);
        menuSistema.addSeparator();
        menuSistema.add(itemSair);

        JMenu menuCadastros = new JMenu("Cadastros");
        JMenuItem itemUsuarios = new JMenuItem("Gerenciar Usuários");
        JMenuItem itemItens = new JMenuItem("Gerenciar Meus Itens");
        itemUsuarios.addActionListener(e -> abrirManterUsuarios());
        itemItens.addActionListener(e -> abrirManterItens());
        menuCadastros.add(itemUsuarios);
        menuCadastros.add(itemItens);

        JMenu menuMarketplace = new JMenu("Marketplace");
        JMenuItem itemCatalogo = new JMenuItem("Ver Catálogo");
        itemCatalogo.addActionListener(e -> abrirCatalogo());
        menuMarketplace.add(itemCatalogo);

        JMenu menuAdmin = new JMenu("Administração");
        JMenuItem itemDefeitos = new JMenuItem("Gerenciar Defeitos");
        itemDefeitos.addActionListener(e -> abrirManterDefeitos());
        menuAdmin.add(itemDefeitos);

        JMenu menuFerramentas = new JMenu("Ferramentas");
        JMenuItem itemConfig = new JMenuItem("Configurações");
        itemConfig.addActionListener(e -> abrirConfiguracoes());
        menuFerramentas.add(itemConfig);

        menuBar.add(menuSistema);
        menuBar.add(menuCadastros);
        menuBar.add(menuMarketplace);
        menuBar.add(menuAdmin);
        menuBar.add(menuFerramentas);
    }


    private void fazerLogin() {
        try {
            IUsuarioRepository repo = new UsuarioRepository();
            List<Usuario> usuarios = repo.buscarTodos();
            if (usuarios.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Não há usuários cadastrados para fazer login.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String[] nomesUsuarios = usuarios.stream().map(u -> u.getId() + " - " + u.getNome()).toArray(String[]::new);
            String selecionado = (String) JOptionPane.showInputDialog(this, "Selecione o usuário para logar:", "Login", JOptionPane.QUESTION_MESSAGE, null, nomesUsuarios, nomesUsuarios[0]);
            if (selecionado != null) {
                int id = Integer.parseInt(selecionado.split(" - ")[0]);
                Usuario usuario = usuarios.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
                SessaoService.getInstancia().login(usuario);
                atualizarStatusBar();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar usuários: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void abrirPerfil() {
        if (!SessaoService.getInstancia().isLogado()) {
            JOptionPane.showMessageDialog(this, "Você precisa estar logado para ver seu perfil.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        abrirJanelaUnica(PerfilView.class, () -> new PerfilPresenter().getView());
    }


    private void abrirManterDefeitos() {
        abrirJanelaUnica(ManterDefeitosView.class, () -> {
            ManterDefeitosView view = new ManterDefeitosView();
            new ManterDefeitosPresenter(view, new DefeitoRepository());
            return view;
        });
    }


    private void atualizarStatusBar() {
        SessaoService sessao = SessaoService.getInstancia();
        if (sessao.isLogado()) {
            statusBar.setText("Usuário logado: " + sessao.getUsuarioLogado().getNome() + " (ID: " + sessao.getUsuarioLogado().getId() + ")");
        } else {
            statusBar.setText("Nenhum usuário logado.");
        }
    }


    private void abrirJanelaUnica(Class<?> classeJanela, java.util.function.Supplier<JInternalFrame> construtor) {
        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            if (classeJanela.isInstance(frame)) {
                try {
                    frame.setSelected(true);
                } catch (java.beans.PropertyVetoException e) {
                }
                return;
            }
        }
        JInternalFrame novaJanela = construtor.get();
        desktopPane.add(novaJanela);
        novaJanela.setVisible(true);
    }

    /**
     * Classe interna para criar um JDesktopPane com fundo gradiente.
     */
    class GradientDesktopPane extends JDesktopPane {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();
            Color color1 = new Color(235, 240, 245);
            Color color2 = new Color(205, 215, 230);
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }
}