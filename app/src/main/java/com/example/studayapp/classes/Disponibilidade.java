package com.example.studayapp.classes;

import java.sql.Time;

public class Disponibilidade {

    private Integer idDisponibilidade;
    private String diaDaSemana;
    private Time horarioInicio;
    private Time horarioFinal;

    public Disponibilidade(Integer idDisponibilidade, String diaDaSemana, Time horarioInicio, Time horarioFinal) {
        this.idDisponibilidade = idDisponibilidade;
        this.diaDaSemana = diaDaSemana;
        this.horarioInicio = horarioInicio;
        this.horarioFinal = horarioFinal;
    }

    public Disponibilidade(String diaDaSemana, Time horarioInicio, Time horarioFinal) {
        this.diaDaSemana = diaDaSemana;
        this.horarioInicio = horarioInicio;
        this.horarioFinal = horarioFinal;
    }

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

    public Time getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(Time horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public Time getHorarioFinal() {
        return horarioFinal;
    }

    public void setHorarioFinal(Time horarioFinal) {
        this.horarioFinal = horarioFinal;
    }
}
