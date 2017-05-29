package com.uninorte.proyecto1;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by daniel on 19/04/17.
 */

public class Evaluacion extends SugarRecord {
    @Unique
    private String name;
    private String materia;
    private String rubrica;
    private String key;

    public Evaluacion(String name, Materia materia, String rubrica) {
        this.name = name;
        this.materia = materia.getKey();
        this.rubrica = rubrica;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Evaluacion() {
    }

    public String getRubrica() {
        return rubrica;
    }

    public void setRubrica(String rubrica) {
        this.rubrica = rubrica;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia.getKey();
    }




}
