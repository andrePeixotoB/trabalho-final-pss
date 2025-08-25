package br.ufes.dcomp.trabalhoProjetos.repository;

import br.ufes.dcomp.trabalhoProjetos.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository implements IUsuarioRepository {
    private final String urlBanco;

    public UsuarioRepository() {
        this.urlBanco = "jdbc:sqlite:mvp-database.db";
        criarTabela();
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(urlBanco);
    }

    private void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nome TEXT NOT NULL,"
                + " email TEXT NOT NULL UNIQUE,"
                + " senha TEXT NOT NULL,"
                + " reputacao_vendedor REAL NOT NULL DEFAULT 0,"
                + " reputacao_comprador REAL NOT NULL DEFAULT 0"
                + ");";
        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar/atualizar tabela de usuários: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios(nome, email, senha, reputacao_vendedor, reputacao_comprador) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getSenha());
            pstmt.setDouble(4, usuario.getReputacaoVendedor());
            pstmt.setDouble(5, usuario.getReputacaoComprador());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ?, reputacao_vendedor = ?, reputacao_comprador = ? WHERE id = ?";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getSenha());
            pstmt.setDouble(4, usuario.getReputacaoVendedor());
            pstmt.setDouble(5, usuario.getReputacaoComprador());
            pstmt.setInt(6, usuario.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deletar(Usuario usuario) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuario.getId());
            pstmt.executeUpdate();
        }
    }

    private Usuario extrairUsuarioDoResultSet(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario(
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("senha")
        );
        usuario.setId(rs.getInt("id"));
        usuario.setReputacaoVendedor(rs.getDouble("reputacao_vendedor"));
        usuario.setReputacaoComprador(rs.getDouble("reputacao_comprador"));
        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        Usuario usuario = null;
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                usuario = extrairUsuarioDoResultSet(rs);
            }
        }
        return Optional.ofNullable(usuario);
    }

    @Override
    public List<Usuario> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM usuarios";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(extrairUsuarioDoResultSet(rs));
            }
        }
        return usuarios;
    }
}