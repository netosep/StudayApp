package com.neto.studayapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Professor implements Serializable {

    private String nomeCompleto;
    private String whatsapp;
    private Date dataNascimento;
    private String sexo;
    private String descricao;
    private String biografia;
    private String urlFotoPerfil;
    private int nivelAcesso;
    private List<Avaliacao> avaliacoes;

    public Professor(String nomeCompleto, String whatsapp, Date dataNascimento, String sexo, String descricao, String biografia, String urlFotoPerfil, int nivelAcesso, List<Avaliacao> avaliacoes) {
        this.nomeCompleto = nomeCompleto;
        this.whatsapp = whatsapp;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.descricao = descricao;
        this.biografia = biografia;
        this.urlFotoPerfil = urlFotoPerfil;
        this.nivelAcesso = nivelAcesso;
        this.avaliacoes = avaliacoes;
    }

    public Professor() {}

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

    public String getSexo() {
        return sexo;
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

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

}
