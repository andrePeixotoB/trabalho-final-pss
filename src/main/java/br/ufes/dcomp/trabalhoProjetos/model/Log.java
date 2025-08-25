package br.ufes.dcomp.trabalhoProjetos.model;

import java.time.LocalDateTime;

public class Log {

    private String tipoOperacao;
    private String nomeUsuario;
    private LocalDateTime dataHora;
    private String mensagem;

    public Log(String tipoOperacao, String nomeUsuario, String mensagem) {
        this.tipoOperacao = tipoOperacao;
        this.nomeUsuario = nomeUsuario;
        this.mensagem = mensagem;
        this.dataHora = LocalDateTime.now();
    }

    public String getTipoOperacao() {
        return tipoOperacao;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getMensagem() {
        return mensagem;
    }
}