package com.neto.studayapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Aluno implements Serializable {

    private String nomeCompleto;
    private String whatsapp;
    private Date dataNascimento;
    private int nivelAcesso;

    // ver sobre favoritos
    private List<Favorito> professoresFavoritos;

    public Aluno(String nomeCompleto, String whatsapp, Date dataNascimento, int nivelAcesso) {
        this.nomeCompleto = nomeCompleto;
        this.whatsapp = whatsapp;
        this.dataNascimento = dataNascimento;
        this.nivelAcesso = nivelAcesso;
    }

    public Aluno() {}

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

    public int getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(int nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }
}
