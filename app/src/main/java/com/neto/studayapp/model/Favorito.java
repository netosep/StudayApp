package com.neto.studayapp.model;

import java.io.Serializable;

public class Favorito implements Serializable {

    private String uuidFavorito;
    private String uuidAluno;
    private String uuidProfessor;
    private Professor professor;

    public Favorito() {
    }

    public Favorito(String uuidFavorito, String uuidAluno, String uuidProfessor, Professor professor) {
        this.uuidFavorito = uuidFavorito;
        this.uuidAluno = uuidAluno;
        this.uuidProfessor = uuidProfessor;
        this.professor = professor;
    }

    public String getUuidFavorito() {
        return uuidFavorito;
    }

    public void setUuidFavorito(String uuidFavorito) {
        this.uuidFavorito = uuidFavorito;
    }

    public String getUuidAluno() {
        return uuidAluno;
    }

    public void setUuidAluno(String uuidAluno) {
        this.uuidAluno = uuidAluno;
    }

    public String getUuidProfessor() {
        return uuidProfessor;
    }

    public void setUuidProfessor(String uuidProfessor) {
        this.uuidProfessor = uuidProfessor;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
