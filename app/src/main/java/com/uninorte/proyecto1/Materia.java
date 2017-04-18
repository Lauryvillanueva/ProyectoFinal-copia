package com.uninorte.proyecto1;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.io.Serializable;
import java.util.List;

/**
 * Created by daniel on 3/04/17.
 */

public class Materia extends SugarRecord implements Serializable{
    @Unique
    private String name;

    public Materia(String namemat) {
        name = namemat;
    }

    public Materia(){}

    public List<Estudiante> getEstudiantes(){
        return Estudiante.find(Estudiante.class,"materia = ?",""+this.getId());
    }


    public String getName() {
        return name;
    }

    public void setName(String namemat) {
        name = namemat;
    }


}
