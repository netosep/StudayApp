package com.example.studayapp.model;

public class Disponibilidade {

    private Integer idDisponibilidade;
    private String diaDaSemana;
    private String horarioInicio;
    private String horarioFinal;

    public Disponibilidade(Integer idDisponibilidade, String diaDaSemana, String horarioInicio, String horarioFinal) {
        this.idDisponibilidade = idDisponibilidade;
        this.diaDaSemana = diaDaSemana;
        this.horarioInicio = horarioInicio;
        this.horarioFinal = horarioFinal;
    }

    public Disponibilidade(String diaDaSemana, String horarioInicio, String horarioFinal) {
        this.diaDaSemana = diaDaSemana;
        this.horarioInicio = horarioInicio;
        this.horarioFinal = horarioFinal;
    }

    public Disponibilidade() {}

    public Integer getIdDisponibilidade() {
        return idDisponibilidade;
    }

    public void setIdDisponibilidade(Integer idDisponibilidade) {
        this.idDisponibilidade = idDisponibilidade;
    }

    public String getDiaDaSemana() {
        return diaDaSemana;
    }

    public void setDiaDaSemana(String diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioFinal() {
        return horarioFinal;
    }

    public void setHorarioFinal(String horarioFinal) {
        this.horarioFinal = horarioFinal;
    }
}
