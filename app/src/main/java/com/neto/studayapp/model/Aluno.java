package com.neto.studayapp.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Aluno implements Serializable {

    private String uuidAluno;
    private String nomeCompleto;
    private String email;
    private String whatsapp;
    private Date dataNascimento;
    private int nivelAcesso;

    public Aluno(String nomeCompleto, String whatsapp, Date dataNascimento, int nivelAcesso) {
        this.nomeCompleto = nomeCompleto;
        this.whatsapp = whatsapp;
        this.dataNascimento = dataNascimento;
        this.nivelAcesso = nivelAcesso;
    }

    public Aluno() {
    }

    public String getUuidAluno() {
        return uuidAluno;
    }

    public void setUuidAluno(String uuidAluno) {
        this.uuidAluno = uuidAluno;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(int nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }

    @NonNull
    @Override
    public String toString() {
        return "Aluno{" +
                "uuidAluno='" + uuidAluno + '\'' +
                ", nomeCompleto='" + nomeCompleto + '\'' +
                ", email='" + email + '\'' +
                ", whatsapp='" + whatsapp + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", nivelAcesso=" + nivelAcesso +
                '}';
    }
}
