package com.example.studayapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Professor extends Usuario implements Serializable {

    private Integer idProfessor;
    private String nomeCompleto;
    private String whatsapp;
    private Date dataNascimento;
    private String sexo;
    private String descricao;
    private String biografia;
    private String urlFotoPerfil;
    private List<Avaliacao> avaliacoes;

    public Professor(Integer idProfessor, String nomeCompleto, String whatsapp, Date dataNascimento, String sexo, String descricao, String biografia, String urlFotoPerfil, List<Avaliacao> avaliacoes, Integer idUsuario, String email, String senha, int tipo) {
        super(idUsuario, email, senha, tipo);
        this.idProfessor = idProfessor;
        this.nomeCompleto = nomeCompleto;
        this.whatsapp = whatsapp;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.descricao = descricao;
        this.biografia = biografia;
        this.urlFotoPerfil = urlFotoPerfil;
        this.avaliacoes = avaliacoes;
    }

    public Professor(String nomeCompleto, String whatsapp, Date dataNascimento, String sexo, String descricao, String biografia, String urlFotoPerfil, List<Avaliacao> avaliacoes, String email, String senha, int tipo) {
        super(email, senha, tipo);
        this.nomeCompleto = nomeCompleto;
        this.whatsapp = whatsapp;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.descricao = descricao;
        this.biografia = biografia;
        this.urlFotoPerfil = urlFotoPerfil;
        this.avaliacoes = avaliacoes;
    }

    public Professor(String nomeCompleto, String whatsapp, Date dataNascimento, String sexo, String descricao, String biografia, String urlFotoPerfil, List<Avaliacao> avaliacoes) {
        this.nomeCompleto = nomeCompleto;
        this.whatsapp = whatsapp;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.descricao = descricao;
        this.biografia = biografia;
        this.urlFotoPerfil = urlFotoPerfil;
        this.avaliacoes = avaliacoes;
    }

    public Professor() {}

    public Integer getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(Integer idProfessor) {
        this.idProfessor = idProfessor;
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

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

}
