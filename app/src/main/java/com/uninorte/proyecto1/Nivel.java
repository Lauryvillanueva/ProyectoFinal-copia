package com.uninorte.proyecto1;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by daniel on 16/04/17.
 */

public class Nivel extends SugarRecord {
    @Unique
    private String name;
    private String key;

    private Long rubrica;

    public Nivel(String name, Rubrica rubrica) {
        this.name = name;
        this.rubrica = rubrica.getId();
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Nivel() {    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRubrica() {
        return rubrica;
    }

    public void setRubrica(Rubrica rubrica) {
        this.rubrica = rubrica.getId();
    }
}
