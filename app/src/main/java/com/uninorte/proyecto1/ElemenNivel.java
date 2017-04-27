package com.uninorte.proyecto1;

import com.orm.SugarRecord;

/**
 * Created by daniel on 17/04/17.
 */

public class ElemenNivel extends SugarRecord {
    private Long elemento,nivel,estudiante,materia;
    private String description;
    private int nota;

    public ElemenNivel(Long elemento, Long nivel, String description) {
        this.elemento = elemento;
        this.nivel = nivel;
        this.description = description;
        this.nota=0;
        this.estudiante=new Long("-1");
        this.materia=new Long("-1");
    }

    public ElemenNivel() {
    }

    public Long getEstudiante() {
        return estudiante;
    }

    public Long getMateria() {
        return materia;
    }

    public void setMateria(Long materia) {
        this.materia = materia;
    }

    public void setEstudiante(Long estudiante) {
        this.estudiante = estudiante;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public Long getElemento() {
        return elemento;
    }

    public void setElemento(Long elemento) {
        this.elemento = elemento;
    }

    public Long getNivel() {
        return nivel;
    }

    public void setNivel(Long nivel) {
        this.nivel = nivel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
