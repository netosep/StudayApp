package com.neto.studayapp.model;

import java.util.List;
import java.util.UUID;

public class Disciplina {

    private String uuid;
    private String uuidProfessor;
    private String nome;
    private Disponibilidade disponibilidade;

    public Disciplina() {

    }

    public Disciplina(String uuid, String uuidProfessor, String nome, Disponibilidade disponibilidade) {
        this.uuid = uuid;
        this.uuidProfessor = uuidProfessor;
        this.nome = nome;
        this.disponibilidade = disponibilidade;
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

    public Disponibilidade getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(Disponibilidade disponibilidade) {
        this.disponibilidade = disponibilidade;
    }
}
