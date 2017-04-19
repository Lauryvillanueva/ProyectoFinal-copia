package com.uninorte.proyecto1;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by daniel on 19/04/17.
 */

public class Evaluacion extends SugarRecord {
    @Unique
    private String name;
    private Long materia;
    private Long rubrica;

    public Evaluacion(String name, Materia materia, Rubrica rubrica) {
        this.name = name;
        this.materia = materia.getId();
        this.rubrica = rubrica.getId();
    }

    public Evaluacion() {
    }

    public Long getRubrica() {
        return rubrica;
    }

    public void setRubrica(Rubrica rubrica) {
        this.rubrica = rubrica.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia.getId();
    }
}
