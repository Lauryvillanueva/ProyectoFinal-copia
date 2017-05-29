package com.uninorte.proyecto1;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.util.List;

/**
 * Created by daniel on 16/04/17.
 */

public class Elemento extends SugarRecord {
    @Unique
    private String name;
    private int peso;
    private String categoria;
    private String key;

    public Elemento(String name, int peso, Categoria categoria) {
        this.name = name;
        this.peso = peso;
        this.categoria = categoria.getKey();
    }

    public Elemento() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria.getKey();
    }

    public List<ElemenNivel> getDescriptions(){
        return ElemenNivel.find(ElemenNivel.class,"elemento = ?",new String[] {""+this.getId()},null,"nivel",null);
    }
}
