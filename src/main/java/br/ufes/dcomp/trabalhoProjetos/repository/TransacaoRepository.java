package br.ufes.dcomp.trabalhoProjetos.repository;

import br.ufes.dcomp.trabalhoProjetos.model.Transacao;

import java.sql.*;
import java.time.format.DateTimeFormatter;

public class TransacaoRepository implements ITransacaoRepository {

    private final String urlBanco;

    public TransacaoRepository() {
        this.urlBanco = "jdbc:sqlite:mvp-database.db";
        criarTabela();
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(urlBanco);
    }

    private void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS transacoes ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " id_item INTEGER NOT NULL,"
                + " id_vendedor INTEGER NOT NULL,"
                + " id_comprador INTEGER NOT NULL,"
                + " valor_final REAL NOT NULL,"
                + " data_transacao TEXT NOT NULL,"
                + " FOREIGN KEY(id_item) REFERENCES itens(id),"
                + " FOREIGN KEY(id_vendedor) REFERENCES usuarios(id),"
                + " FOREIGN KEY(id_comprador) REFERENCES usuarios(id)"
                + ");";
        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela de transações: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Transacao transacao) throws SQLException {
        String sql = "INSERT INTO transacoes(id_item, id_vendedor, id_comprador, valor_final, data_transacao) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transacao.getIdItem());
            pstmt.setInt(2, transacao.getIdVendedor());
            pstmt.setInt(3, transacao.getIdComprador());
            pstmt.setDouble(4, transacao.getValorFinal());
            pstmt.setString(5, transacao.getDataTransacao().format(DateTimeFormatter.ISO_LOCAL_DATE)); // Formato AAAA-MM-DD
            pstmt.executeUpdate();
        }
    }
}