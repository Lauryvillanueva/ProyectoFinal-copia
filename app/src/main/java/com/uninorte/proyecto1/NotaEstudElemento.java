package com.uninorte.proyecto1;

import com.orm.SugarRecord;

/**
 * Created by daniel on 27/04/17.
 */

public class NotaEstudElemento extends SugarRecord {
    private Long elemennivel,estudiante,materia;

    public NotaEstudElemento(Long elemennivel, Long estudiante, Long materia) {
        this.elemennivel = elemennivel;
        this.estudiante = estudiante;
        this.materia = materia;
    }

    public NotaEstudElemento() {
    }

    public Long getElemennivel() {
        return elemennivel;
    }

    public void setElemennivel(Long elemennivel) {
        this.elemennivel = elemennivel;
    }

    public Long getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Long estudiante) {
        this.estudiante = estudiante;
    }

    public Long getMateria() {
        return materia;
    }

    public void setMateria(Long materia) {
        this.materia = materia;
    }
}
