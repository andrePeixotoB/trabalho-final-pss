package br.ufes.dcomp.trabalhoProjetos.model;

import java.util.Objects;

public class Defeito {

    private int id;
    private String descricao;
    private double percentualDesconto;

    public Defeito(String descricao, double percentualDesconto) {
        this.descricao = descricao;
        this.percentualDesconto = percentualDesconto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPercentualDesconto() {
        return percentualDesconto;
    }

    public void setPercentualDesconto(double percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    @Override
    public String toString() {
        return descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Defeito defeito = (Defeito) o;
        return id == defeito.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
