package com.neto.studayapp.model;

public class Usuario {

    private static Integer idUsuario;
    private static String email;
    private static String senha;
    private static int tipo; // 1 aluno | 2 professor | 3 admin

    public Usuario(Integer idUsuario, String email, String senha, int tipo) {
        Usuario.idUsuario = idUsuario;
        Usuario.email = email;
        Usuario.senha = senha;
        Usuario.tipo = tipo;
    }

    public Usuario(String email, String senha, int tipo) {
        Usuario.email = email;
        Usuario.senha = senha;
        Usuario.tipo = tipo;
    }

    public Usuario() {}

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        Usuario.idUsuario = idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        Usuario.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        Usuario.senha = senha;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        Usuario.tipo = tipo;
    }
}
