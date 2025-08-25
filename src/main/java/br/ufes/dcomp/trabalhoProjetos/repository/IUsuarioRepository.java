package br.ufes.dcomp.trabalhoProjetos.repository;

import br.ufes.dcomp.trabalhoProjetos.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository {

    void salvar(Usuario usuario) throws Exception;
    void atualizar(Usuario usuario) throws Exception;
    void deletar(Usuario usuario) throws Exception;
    Optional<Usuario> buscarPorId(int id) throws Exception;
    List<Usuario> buscarTodos() throws Exception;

}
