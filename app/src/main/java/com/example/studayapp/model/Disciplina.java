package com.example.studayapp.model;

public class Disciplina {

    private  Integer idDisciplina;
    private String nome;
    private float custo;
    private Disponibilidade disponibilidade;

    public Disciplina(Integer idDisciplina, String nome, float custo, Disponibilidade disponibilidade) {
        this.idDisciplina = idDisciplina;
        this.nome = nome;
        this.custo = custo;
        this.disponibilidade = disponibilidade;
    }

    public Disciplina(String nome, float custo, Disponibilidade disponibilidade) {
        this.nome = nome;
        this.custo = custo;
        this.disponibilidade = disponibilidade;
    }

    public Integer getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(Integer idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getCusto() {
        return custo;
    }

    public void setCusto(float custo) {
        this.custo = custo;
    }

    public Disponibilidade getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(Disponibilidade disponibilidade) {
        this.disponibilidade = disponibilidade;
    }
}
