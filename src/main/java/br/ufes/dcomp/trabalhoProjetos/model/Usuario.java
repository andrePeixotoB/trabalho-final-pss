package br.ufes.dcomp.trabalhoProjetos.model;

public class Usuario {

    private int id;
    private String nome;
    private String email;
    private String senha;
    private double reputacaoVendedor; // Nível Bronze: 0-4.99, etc.
    private double reputacaoComprador;

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.reputacaoVendedor = 0; // Começa com 0 estrelas
        this.reputacaoComprador = 0;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public double getReputacaoVendedor() {
        return reputacaoVendedor;
    }

    public void setReputacaoVendedor(double reputacaoVendedor) {
        this.reputacaoVendedor = reputacaoVendedor;
    }

    public double getReputacaoComprador() {
        return reputacaoComprador;
    }

    public void setReputacaoComprador(double reputacaoComprador) {
        this.reputacaoComprador = reputacaoComprador;
    }
}
