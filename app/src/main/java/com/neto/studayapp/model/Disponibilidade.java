package com.neto.studayapp.model;

public class Disponibilidade {

    private String diaDaSemana;
    private String horarioInicio;
    private String horarioFinal;

    public Disponibilidade(String diaDaSemana, String horarioInicio, String horarioFinal) {
        this.diaDaSemana = diaDaSemana;
        this.horarioInicio = horarioInicio;
        this.horarioFinal = horarioFinal;
    }

    public Disponibilidade() {}

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
