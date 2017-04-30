package com.uninorte.proyecto1;

import com.orm.SugarRecord;

/**
 * Created by daniel on 27/04/17.
 */

public class NotaEstudElemento extends SugarRecord {
    private Long estudiante,evaluacion,elemento;
    private double nota;

    public NotaEstudElemento() {
    }

    public NotaEstudElemento(Long estudiante, Long evaluacion, Long elemento, double nota) {
        this.estudiante = estudiante;
        this.evaluacion = evaluacion;
        this.elemento = elemento;
        this.nota = nota;
    }

    public Long getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Long estudiante) {
        this.estudiante = estudiante;
    }

    public Long getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Long evaluacion) {
        this.evaluacion = evaluacion;
    }

    public Long getElemento() {
        return elemento;
    }

    public void setElemento(Long elemento) {
        this.elemento = elemento;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }


}

