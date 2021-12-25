package com.neto.studayapp.model;

import java.util.List;

public class Disciplina {

    private Integer idDisciplina;
    private String nome;
    private double custo;
    private List<Disponibilidade> disponibilidade;

    public Disciplina(Integer idDisciplina, String nome, double custo, List<Disponibilidade> disponibilidade) {
        this.idDisciplina = idDisciplina;
        this.nome = nome;
        this.custo = custo;
        this.disponibilidade = disponibilidade;
    }

    public Disciplina(String nome, double custo, List<Disponibilidade> disponibilidade) {
        this.nome = nome;
        this.custo = custo;
        this.disponibilidade = disponibilidade;
    }

    public Disciplina() {}

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

    public double getCusto() {
        return custo;
    }

    public void setCusto(double custo) {
        this.custo = custo;
    }

    public List<Disponibilidade> getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(List<Disponibilidade> disponibilidade) {
        this.disponibilidade = disponibilidade;
    }
}
