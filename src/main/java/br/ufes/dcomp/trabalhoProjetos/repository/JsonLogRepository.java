package br.ufes.dcomp.trabalhoProjetos.repository;

import br.ufes.dcomp.trabalhoProjetos.model.Log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;

public class JsonLogRepository implements ILogRepository {

    private final String nomeArquivo = "sistema.log.json";

    @Override
    public void salvar(Log log) throws Exception {
        try (FileWriter fw = new FileWriter(nomeArquivo, true);
             PrintWriter pw = new PrintWriter(fw)) {

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            String json = String.format(
                    "{\"data_hora\": \"%s\", \"usuario\": \"%s\", \"operacao\": \"%s\", \"mensagem\": \"%s\"},",
                    log.getDataHora().format(dateTimeFormatter),
                    log.getNomeUsuario(),
                    log.getTipoOperacao(),
                    log.getMensagem().replace("\"", "\\\"")
            );

            pw.println(json);

        } catch (IOException e) {
            throw new Exception("Falha ao escrever no log JSON: " + e.getMessage());
        }
    }
}