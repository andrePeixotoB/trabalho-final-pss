package br.ufes.dcomp.trabalhoProjetos.repository;

import br.ufes.dcomp.trabalhoProjetos.model.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;

public class CsvLogRepository implements ILogRepository {

    private final String nomeArquivo = "sistema.log.csv";

    @Override
    public void salvar(Log log) throws Exception {
        File file = new File(nomeArquivo);
        boolean cabecalhoNecessario = !file.exists() || file.length() == 0;

        try (FileWriter fw = new FileWriter(nomeArquivo, true);
             PrintWriter pw = new PrintWriter(fw)) {

            if (cabecalhoNecessario) {
                pw.println("data;hora;usuario;operacao;mensagem");
            }

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            String linha = String.format("%s;%s;%s;%s;\"%s\"",
                    log.getDataHora().format(dateFormatter),
                    log.getDataHora().format(timeFormatter),
                    log.getNomeUsuario(),
                    log.getTipoOperacao(),
                    log.getMensagem()
            );

            pw.println(linha);

        } catch (IOException e) {
            throw new Exception("Falha ao escrever no log CSV: " + e.getMessage());
        }
    }
}