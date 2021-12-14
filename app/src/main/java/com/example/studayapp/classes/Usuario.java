package com.example.studayapp.classes;

public class Usuario {

    private static Integer idUsuario;
    private static String email;
    private static String senha;
    private static int tipo; // 1 aluno | 2 professor | 3 admin

    public Usuario(Integer idUsuario, String email, String senha, int tipo) {
        this.idUsuario = idUsuario;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }

    public Usuario(String email, String senha, int tipo) {
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }

    public Usuario() {}

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
