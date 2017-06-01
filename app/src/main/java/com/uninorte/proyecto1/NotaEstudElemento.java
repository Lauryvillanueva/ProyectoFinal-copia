package com.uninorte.proyecto1;

import com.orm.SugarRecord;

/**
 * Created by daniel on 27/04/17.
 */

public class NotaEstudElemento extends SugarRecord {
    private String estudiante,evaluacion,elemento;
    private double nota;


    public NotaEstudElemento() {
    }

    public NotaEstudElemento(String estudiante, String evaluacion, String elemento, double nota) {
        this.estudiante = estudiante;
        this.evaluacion = evaluacion;
        this.elemento = elemento;
        this.nota = nota;
    }

    public String getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(String estudiante) {
        this.estudiante = estudiante;
    }

    public String getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(String evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getElemento() {
        return elemento;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }


}

