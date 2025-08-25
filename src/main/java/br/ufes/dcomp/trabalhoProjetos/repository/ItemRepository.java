package br.ufes.dcomp.trabalhoProjetos.repository;

import br.ufes.dcomp.trabalhoProjetos.model.Defeito;
import br.ufes.dcomp.trabalhoProjetos.model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemRepository implements IItemRepository {
    private final String urlBanco;

    public ItemRepository() {
        this.urlBanco = "jdbc:sqlite:mvp-database.db";
        criarTabelas();
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(urlBanco);
    }

    private void criarTabelas() {
        String sqlItens = "CREATE TABLE IF NOT EXISTS itens ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nome TEXT NOT NULL,"
                + " descricao TEXT,"
                + " preco_base REAL NOT NULL,"
                + " massa_kg REAL NOT NULL,"
                + " id_vendedor INTEGER NOT NULL,"
                + " disponivel INTEGER NOT NULL DEFAULT 1,"
                + " FOREIGN KEY(id_vendedor) REFERENCES usuarios(id)"
                + ");";

        String sqlItemDefeitos = "CREATE TABLE IF NOT EXISTS item_defeitos ("
                + " id_item INTEGER NOT NULL,"
                + " id_defeito INTEGER NOT NULL,"
                + " PRIMARY KEY (id_item, id_defeito),"
                + " FOREIGN KEY (id_item) REFERENCES itens(id) ON DELETE CASCADE,"
                + " FOREIGN KEY (id_defeito) REFERENCES defeitos(id) ON DELETE CASCADE"
                + ");";

        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;"); // Habilita chaves estrangeiras no SQLite
            stmt.execute(sqlItens);
            stmt.execute(sqlItemDefeitos);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabelas: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Item item) throws SQLException {
        String sql = "INSERT INTO itens(nome, descricao, preco_base, massa_kg, id_vendedor, disponivel) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = conectar()) {
            conn.setAutoCommit(false); // Inicia transação

            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, item.getNome());
                pstmt.setString(2, item.getDescricao());
                pstmt.setDouble(3, item.getPrecoBase());
                pstmt.setDouble(4, item.getMassaEstimadaKg());
                pstmt.setInt(5, item.getIdVendedor());
                pstmt.setInt(6, item.isDisponivel() ? 1 : 0);
                pstmt.executeUpdate();

                // Recupera o ID do item recém-criado
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getInt(1));
                }

                salvarDefeitosDoItem(conn, item);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    @Override
    public void atualizar(Item item) throws SQLException {
        String sql = "UPDATE itens SET nome = ?, descricao = ?, preco_base = ?, massa_kg = ?, id_vendedor = ?, disponivel = ? WHERE id = ?";
        try (Connection conn = conectar()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, item.getNome());
                pstmt.setString(2, item.getDescricao());
                pstmt.setDouble(3, item.getPrecoBase());
                pstmt.setDouble(4, item.getMassaEstimadaKg());
                pstmt.setInt(5, item.getIdVendedor());
                pstmt.setInt(6, item.isDisponivel() ? 1 : 0);
                pstmt.setInt(7, item.getId());
                pstmt.executeUpdate();

                // Deleta defeitos antigos e insere os novos
                deletarDefeitosDoItem(conn, item.getId());
                salvarDefeitosDoItem(conn, item);

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private void salvarDefeitosDoItem(Connection conn, Item item) throws SQLException {
        String sql = "INSERT INTO item_defeitos(id_item, id_defeito) VALUES(?, ?)";
        for (Defeito defeito : item.getDefeitos()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, item.getId());
                pstmt.setInt(2, defeito.getId());
                pstmt.executeUpdate();
            }
        }
    }

    private void deletarDefeitosDoItem(Connection conn, int idItem) throws SQLException {
        String sql = "DELETE FROM item_defeitos WHERE id_item = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idItem);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deletar(Item item) throws SQLException {
        String sql = "DELETE FROM itens WHERE id = ?";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // A tabela de junção deletará em cascata
            pstmt.setInt(1, item.getId());
            pstmt.executeUpdate();
        }
    }

    private void carregarDefeitosParaItem(Item item) throws SQLException {
        String sql = "SELECT d.id, d.descricao, d.percentual_desconto " +
                "FROM defeitos d " +
                "JOIN item_defeitos id ON d.id = id.id_defeito " +
                "WHERE id.id_item = ?";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, item.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Defeito defeito = new Defeito(rs.getString("descricao"), rs.getDouble("percentual_desconto"));
                defeito.setId(rs.getInt("id"));
                item.getDefeitos().add(defeito);
            }
        }
    }

    private Item extrairItemDoResultSet(ResultSet rs) throws SQLException {
        Item item = new Item(
                rs.getString("nome"),
                rs.getString("descricao"),
                rs.getDouble("preco_base"),
                rs.getDouble("massa_kg"),
                rs.getInt("id_vendedor")
        );
        item.setId(rs.getInt("id"));
        item.setDisponivel(rs.getInt("disponivel") == 1);
        carregarDefeitosParaItem(item); // Carrega a lista de defeitos
        return item;
    }

    @Override
    public Optional<Item> buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM itens WHERE id = ?";
        Item item = null;
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                item = extrairItemDoResultSet(rs);
            }
        }
        return Optional.ofNullable(item);
    }

    @Override
    public List<Item> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM itens";
        List<Item> itens = new ArrayList<>();
        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                itens.add(extrairItemDoResultSet(rs));
            }
        }
        return itens;
    }
}