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
    private Long categoria;

    public Elemento(String name, int peso, Categoria categoria) {
        this.name = name;
        this.peso = peso;
        this.categoria = categoria.getId();
    }

    public Elemento() {
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

    public Long getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria.getId();
    }

    public List<ElemenNivel> getDescriptions(){
        return ElemenNivel.find(ElemenNivel.class,"elemento = ?",new String[] {""+this.getId()},null,"nivel",null);
    }
}
