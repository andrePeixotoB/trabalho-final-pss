package br.ufes.dcomp.trabalhoProjetos.repository;

import br.ufes.dcomp.trabalhoProjetos.model.Log;

public interface ILogRepository {
    void salvar(Log log) throws Exception;
}