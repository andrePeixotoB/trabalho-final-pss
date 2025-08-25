package br.ufes.dcomp.trabalhoProjetos.repository;

import br.ufes.dcomp.trabalhoProjetos.model.Defeito;

import java.sql.SQLException;
import java.util.List;

public interface IDefeitoRepository {
    void salvar(Defeito defeito) throws SQLException;
    void deletar(int idDefeito) throws SQLException;
    List<Defeito> buscarTodos() throws SQLException;
}