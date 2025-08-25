package br.ufes.dcomp.trabalhoProjetos.repository;

import br.ufes.dcomp.trabalhoProjetos.model.Item;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IItemRepository {
    void salvar(Item item) throws SQLException;
    void atualizar(Item item) throws SQLException;
    void deletar(Item item) throws SQLException;
    Optional<Item> buscarPorId(int id) throws SQLException;
    List<Item> buscarTodos() throws SQLException;
}