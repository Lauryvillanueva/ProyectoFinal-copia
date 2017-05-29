package com.uninorte.proyecto1;

import com.orm.SugarRecord;

/**
 * Created by daniel on 17/04/17.
 */

public class ElemenNivel extends SugarRecord {
    private String elemento,nivel;
    private String description;
    private String key;

    public ElemenNivel(String elemento, String nivel, String description) {
        this.elemento = elemento;
        this.nivel = nivel;
        this.description = description;
    }

    public ElemenNivel() {    }

    public String getElemento() {
        return elemento;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
