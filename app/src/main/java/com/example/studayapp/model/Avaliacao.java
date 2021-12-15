package com.example.studayapp.model;

public class Avaliacao {

    private Integer idAvalicao;
    private Aluno aluno;
    private Professor professor;
    private float valor;

    public Avaliacao(Integer idAvalicao, Aluno aluno, Professor professor, float valor) {
        this.idAvalicao = idAvalicao;
        this.aluno = aluno;
        this.professor = professor;
        this.valor = valor;
    }

    public Avaliacao(Aluno aluno, Professor professor, float valor) {
        this.aluno = aluno;
        this.professor = professor;
        this.valor = valor;
    }

    public Integer getIdAvalicao() {
        return idAvalicao;
    }

    public void setIdAvalicao(Integer idAvalicao) {
        this.idAvalicao = idAvalicao;
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

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
}
