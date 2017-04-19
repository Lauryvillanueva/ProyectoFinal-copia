package com.uninorte.proyecto1;

import com.orm.SugarRecord;

/**
 * Created by daniel on 17/04/17.
 */

public class EleNivDescription extends SugarRecord {
    private Long elemento,nivel;
    private String description;
    private int nota;

    public EleNivDescription(Long elemento, Long nivel, String description) {
        this.elemento = elemento;
        this.nivel = nivel;
        this.description = description;
        this.nota=0;
    }

    public EleNivDescription(Long elemento, Long nivel, int nota) {
        this.elemento = elemento;
        this.nivel = nivel;
        this.nota = nota;
    }

    public EleNivDescription() {
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
