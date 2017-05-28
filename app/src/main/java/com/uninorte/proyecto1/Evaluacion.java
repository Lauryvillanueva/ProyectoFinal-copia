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
    private String id;

    public Evaluacion(String name, Materia materia, Long rubrica) {
        this.name = name;
        this.materia = materia.getId();
        this.rubrica = rubrica;
    }

    @Override
    public Long getId() {
        return Long.valueOf(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public Evaluacion() {
    }

    public Long getRubrica() {
        return rubrica;
    }

    public void setRubrica(Long rubrica) {
        this.rubrica = rubrica;
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
