package com.uninorte.proyecto1;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.util.List;

/**
 * Created by daniel on 16/04/17.
 */

public class Categoria extends SugarRecord {
    @Unique
    private String name;

    private int peso;
    private String rubrica;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Categoria(String name, int peso, Rubrica rubrica) {
        this.name = name;
        this.peso=peso;
        this.rubrica = rubrica.getKey();
    }

    public Categoria() {    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPeso() { return peso;   }

    public void setPeso(int peso) { this.peso = peso; }

    public String getRubrica() {
        return rubrica;
    }

    public void setRubrica(Rubrica rubrica) {
        this.rubrica = rubrica.getKey();
    }

    public List<Elemento> getElementos(){
        return Elemento.find(Elemento.class,"categoria = ?",""+this.getId());
    }
}
