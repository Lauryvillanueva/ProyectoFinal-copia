package com.uninorte.proyecto1;

import com.orm.SugarRecord;

/**
 * Created by daniel on 17/04/17.
 */

public class ElemenNivel extends SugarRecord {
    private Long elemento,nivel;
    private String description;

    public ElemenNivel(Long elemento, Long nivel, String description) {
        this.elemento = elemento;
        this.nivel = nivel;
        this.description = description;
    }

    public ElemenNivel() {    }

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
