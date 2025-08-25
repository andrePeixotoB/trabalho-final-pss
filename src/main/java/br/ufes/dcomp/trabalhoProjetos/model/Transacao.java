package br.ufes.dcomp.trabalhoProjetos.model;

import java.time.LocalDate;

public class Transacao {

    private int id;
    private int idItem;
    private int idVendedor;
    private int idComprador;
    private double valorFinal;
    private LocalDate dataTransacao;

    public Transacao(int idItem, int idVendedor, int idComprador, double valorFinal) {
        this.idItem = idItem;
        this.idVendedor = idVendedor;
        this.idComprador = idComprador;
        this.valorFinal = valorFinal;
        this.dataTransacao = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public int getIdComprador() {
        return idComprador;
    }

    public void setIdComprador(int idComprador) {
        this.idComprador = idComprador;
    }

    public double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public LocalDate getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(LocalDate dataTransacao) {
        this.dataTransacao = dataTransacao;
    }
}
