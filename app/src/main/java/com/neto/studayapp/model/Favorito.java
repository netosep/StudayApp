package com.neto.studayapp.model;

public class Favorito {

    private Integer idFavorito;
    private Aluno aluno;
    private Professor professor;

    public Favorito(Integer idFavorito, Aluno aluno, Professor professor) {
        this.idFavorito = idFavorito;
        this.aluno = aluno;
        this.professor = professor;
    }

    public Favorito(Aluno aluno, Professor professor) {
        this.aluno = aluno;
        this.professor = professor;
    }

    public Integer getIdFavorito() {
        return idFavorito;
    }

    public void setIdFavorito(Integer idFavorito) {
        this.idFavorito = idFavorito;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
