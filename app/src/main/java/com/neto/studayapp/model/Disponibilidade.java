package com.neto.studayapp.model;

public class Disponibilidade {

    private String diaDe;
    private String diaAte;
    private String horaDas;
    private String horaAte;

    public Disponibilidade() {
    }

    public Disponibilidade(String diaDe, String diaAte, String horaDas, String horaAte) {
        this.diaDe = diaDe;
        this.diaAte = diaAte;
        this.horaDas = horaDas;
        this.horaAte = horaAte;
    }

    public String getDiaDe() {
        return diaDe;
    }

    public void setDiaDe(String diaDe) {
        this.diaDe = diaDe;
    }

    public String getDiaAte() {
        return diaAte;
    }

    public void setDiaAte(String diaAte) {
        this.diaAte = diaAte;
    }

    public String getHoraDas() {
        return horaDas;
    }

    public void setHoraDas(String horaDas) {
        this.horaDas = horaDas;
    }

    public String getHoraAte() {
        return horaAte;
    }

    public void setHoraAte(String horaAte) {
        this.horaAte = horaAte;
    }
}
