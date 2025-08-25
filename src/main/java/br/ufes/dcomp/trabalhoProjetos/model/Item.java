package br.ufes.dcomp.trabalhoProjetos.model;

import java.util.ArrayList;
import java.util.List;

public class Item {

    private int id;
    private String nome;
    private String descricao;
    private double precoBase;
    private double massaEstimadaKg;
    private int idVendedor;
    private boolean disponivel;
    private List<Defeito> defeitos;

    // Campos calculados
    private double gwpEvitado;
    private double mci;

    public Item(String nome, String descricao, double precoBase, double massaEstimadaKg, int idVendedor) {
        this.nome = nome;
        this.descricao = descricao;
        this.precoBase = precoBase;
        this.massaEstimadaKg = massaEstimadaKg;
        this.idVendedor = idVendedor;
        this.disponivel = true;
        this.defeitos = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(double precoBase) {
        this.precoBase = precoBase;
    }

    public double getMassaEstimadaKg() {
        return massaEstimadaKg;
    }

    public void setMassaEstimadaKg(double massaEstimadaKg) {
        this.massaEstimadaKg = massaEstimadaKg;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public List<Defeito> getDefeitos() {
        return defeitos;
    }

    public void setDefeitos(List<Defeito> defeitos) {
        this.defeitos = defeitos;
    }

    public double getGwpEvitado() {
        return gwpEvitado;
    }

    public void setGwpEvitado(double gwpEvitado) {
        this.gwpEvitado = gwpEvitado;
    }

    public double getMci() {
        return mci;
    }

    public void setMci(double mci) {
        this.mci = mci;
    }
}
