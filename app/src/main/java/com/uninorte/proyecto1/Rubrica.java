package com.uninorte.proyecto1;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.util.List;

/**
 * Created by daniel on 16/04/17.
 */

public class Rubrica extends SugarRecord {
    @Unique
    private String name;
    private String id;

    @Override
    public Long getId() {
        return Long.valueOf(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public Rubrica(String name) {
        this.name = name;
    }

    public Rubrica() {    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Nivel> getNiveles(){
        return Nivel.find(Nivel.class,"rubrica = ?",""+this.getId());
    }

    public List<Categoria> getCategorias(){
        return Categoria.find(Categoria.class,"rubrica = ?",""+this.getId());
    }
}
