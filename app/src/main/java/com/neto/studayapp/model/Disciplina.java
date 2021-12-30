package com.neto.studayapp.model;

import java.util.List;
import java.util.UUID;

public class Disciplina {

    private String uuid;
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

    public Disciplina() {
        this.uuid = String.valueOf(UUID.randomUUID());
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

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
