package br.ufes.dcomp.trabalhoProjetos.repository;

import br.ufes.dcomp.trabalhoProjetos.model.Transacao;
import java.sql.SQLException;

public interface ITransacaoRepository {
    void salvar(Transacao transacao) throws SQLException;
}