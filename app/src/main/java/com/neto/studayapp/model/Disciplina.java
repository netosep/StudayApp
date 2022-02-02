package com.neto.studayapp.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Disciplina implements Serializable {

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

    @NonNull
    @Override
    public String toString() {
        return "Disciplina{" +
                "uuid='" + uuid + '\'' +
                ", uuidProfessor='" + uuidProfessor + '\'' +
                ", nome='" + nome + '\'' +
                ", disponibilidade=" + disponibilidade +
                '}';
    }

}
