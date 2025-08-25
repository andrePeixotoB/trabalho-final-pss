package br.ufes.dcomp.trabalhoProjetos.repository;

import br.ufes.dcomp.trabalhoProjetos.model.Defeito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DefeitoRepository implements IDefeitoRepository {

    private final String urlBanco;

    public DefeitoRepository() {
        this.urlBanco = "jdbc:sqlite:mvp-database.db";
        criarTabela();
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(urlBanco);
    }

    private void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS defeitos ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " descricao TEXT NOT NULL UNIQUE,"
                + " percentual_desconto REAL NOT NULL"
                + ");";
        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela de defeitos: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Defeito defeito) throws SQLException {
        String sql = "INSERT INTO defeitos(descricao, percentual_desconto) VALUES(?, ?)";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, defeito.getDescricao());
            pstmt.setDouble(2, defeito.getPercentualDesconto());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deletar(int idDefeito) throws SQLException {
        String sql = "DELETE FROM defeitos WHERE id = ?";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idDefeito);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Defeito> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM defeitos";
        List<Defeito> defeitos = new ArrayList<>();
        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Defeito defeito = new Defeito(
                        rs.getString("descricao"),
                        rs.getDouble("percentual_desconto")
                );
                defeito.setId(rs.getInt("id"));
                defeitos.add(defeito);
            }
        }
        return defeitos;
    }
}