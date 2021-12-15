package com.example.studayapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Aluno extends Usuario implements Serializable {

    private Integer idAluno;
    private String nomeCompleto;
    private String whatsapp;
    private Date dataNascimento;
    private List<ProfessorFavorito> professoresFavoritos;

    public Aluno(Integer idAluno, String nomeCompleto, String whatsapp, Date dataNascimento, Integer idUsuario, String email, String senha, int tipo) {
        super(idUsuario, email, senha, tipo);
        this.idAluno = idAluno;
        this.nomeCompleto = nomeCompleto;
        this.whatsapp = whatsapp;
        this.dataNascimento = dataNascimento;
    }

    public Aluno(String nomeCompleto, String whatsapp, Date dataNascimento, String email, String senha, int tipo) {
        super(email, senha, tipo);
        this.nomeCompleto = nomeCompleto;
        this.whatsapp = whatsapp;
        this.dataNascimento = dataNascimento;
    }

    public Aluno(String nomeCompleto, String whatsapp, Date dataNascimento) {
        this.nomeCompleto = nomeCompleto;
        this.whatsapp = whatsapp;
        this.dataNascimento = dataNascimento;
    }

    public Aluno() {}

    public Integer getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(Integer idAluno) {
        this.idAluno = idAluno;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

}
