package br.ufes.dcomp.trabalhoProjetos.service;

import br.ufes.dcomp.trabalhoProjetos.model.Log;
import br.ufes.dcomp.trabalhoProjetos.repository.CsvLogRepository;
import br.ufes.dcomp.trabalhoProjetos.repository.ILogRepository;
import br.ufes.dcomp.trabalhoProjetos.repository.JsonLogRepository;

public class LogService {

    private static LogService instancia;
    private ILogRepository logRepository;
    private String tipoLogAtual;

    private LogService() {
        // Padrão CSV
        this.logRepository = new CsvLogRepository();
        this.tipoLogAtual = "CSV";
    }

    public static synchronized LogService getInstancia() {
        if (instancia == null) {
            instancia = new LogService();
        }
        return instancia;
    }

    public void registrar(String tipoOperacao, String nomeUsuario, String mensagem) {
        try {
            Log log = new Log(tipoOperacao, nomeUsuario, mensagem);
            logRepository.salvar(log);
        } catch (Exception e) {
            System.err.println("Falha CRÍTICA no sistema de log: " + e.getMessage());
        }
    }

    public void setTipoLog(String tipo) throws Exception {
        if (tipo == null || (!tipo.equalsIgnoreCase("CSV") && !tipo.equalsIgnoreCase("JSON"))) {
            throw new Exception("Tipo de log inválido. Use 'CSV' ou 'JSON'.");
        }

        if (tipo.equalsIgnoreCase(this.tipoLogAtual)) {
            return;
        }

        if (tipo.equalsIgnoreCase("CSV")) {
            this.logRepository = new CsvLogRepository();
            this.tipoLogAtual = "CSV";
        } else {
            this.logRepository = new JsonLogRepository();
            this.tipoLogAtual = "JSON";
        }

        registrar("CONFIGURACAO", "SISTEMA", "Formato de log alterado para " + tipo.toUpperCase());
    }

    public String getTipoLogAtual() {
        return tipoLogAtual;
    }
}