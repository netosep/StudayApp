package com.neto.studayapp.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Professor implements Serializable {

    private String uuidProfessor;
    private String nomeCompleto;
    private String email;
    private String whatsapp;
    private Date dataNascimento;
    private String sexo;
    private Double valorAula;
    private String descricao;
    private String biografia;
    private String urlFotoPerfil;
    private int nivelAcesso;

    public Professor() {
    }

    public Professor(String nomeCompleto, String email, String whatsapp, Date dataNascimento, String sexo,
                     String descricao, String biografia, String urlFotoPerfil, int nivelAcesso) {
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.whatsapp = whatsapp;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.descricao = descricao;
        this.biografia = biografia;
        this.urlFotoPerfil = urlFotoPerfil;
        this.nivelAcesso = nivelAcesso;
    }

    public String getUuidProfessor() {
        return uuidProfessor;
    }

    public void setUuidProfessor(String uuidProfessor) {
        this.uuidProfessor = uuidProfessor;
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

    public String getSexo() {
        return sexo;
    }

    public Double getValorAula() {
        return valorAula;
    }

    public void setValorAula(Double valorAula) {
        this.valorAula = valorAula;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getUrlFotoPerfil() {
        return urlFotoPerfil;
    }

    public void setUrlFotoPerfil(String urlFotoPerfil) {
        this.urlFotoPerfil = urlFotoPerfil;
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
        return "Professor{" +
                "uuidProfessor='" + uuidProfessor + '\'' +
                ", nomeCompleto='" + nomeCompleto + '\'' +
                ", email='" + email + '\'' +
                ", whatsapp='" + whatsapp + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", sexo='" + sexo + '\'' +
                ", valorAula=" + valorAula +
                ", descricao='" + descricao + '\'' +
                ", biografia='" + biografia + '\'' +
                ", urlFotoPerfil='" + urlFotoPerfil + '\'' +
                ", nivelAcesso=" + nivelAcesso +
                '}';
    }
}
