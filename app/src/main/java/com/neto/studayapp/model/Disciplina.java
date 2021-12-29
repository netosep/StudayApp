package com.neto.studayapp.model;

import java.util.List;

public class Disciplina {

    private String uuidProfessor;
    private String nome;
    private double custo;
    private List<Disponibilidade> disponibilidade;

    public Disciplina(String uuidProfessor, String nome, double custo, List<Disponibilidade> disponibilidade) {
        this.uuidProfessor = uuidProfessor;
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

    public String getUuidProfessor() {
        return uuidProfessor;
    }

    public void setUuidProfessor(String uuidProfessor) {
        this.uuidProfessor = uuidProfessor;
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
