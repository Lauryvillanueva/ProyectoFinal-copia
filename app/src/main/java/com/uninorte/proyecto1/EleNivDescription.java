package com.uninorte.proyecto1;

import com.orm.SugarRecord;

/**
 * Created by daniel on 17/04/17.
 */

public class EleNivDescription extends SugarRecord {
    private Long elemento,nivel;
    private String description;

    public EleNivDescription(Long elemento, Long nivel, String description) {
        this.elemento = elemento;
        this.nivel = nivel;
        this.description = description;
    }

    public EleNivDescription() {
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
