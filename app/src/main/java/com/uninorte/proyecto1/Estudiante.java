package com.uninorte.proyecto1;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by daniel on 16/04/17.
 */

public class Estudiante extends SugarRecord {
    private String name ;
    private String materia;
    int state;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Estudiante(String namestud, int statestud, Materia mat) {
        name = namestud;
        state = statestud;
        materia=mat.getKey();

    }

    public Estudiante() {    }

    public String getName() {
        return name;
    }

    public void setName(String namestud) {
        name = namestud;
    }

    public int getState() {
        return state;
    }

    public void setState(int statestud) {
        state = statestud;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia.getKey();
    }

    public List<NotaEstudElemento> getNotas(Long evaluacion){
        return NotaEstudElemento.find(NotaEstudElemento.class,"estudiante = ? and evaluacion = ?",new String[] {String.valueOf(this.getId()),String.valueOf(evaluacion)},null,"elemento",null);
    }

    public NotaEstudElemento findRegister (Long evaluacion, Long elemento){
        NotaEstudElemento notaEstudElemento;
        List<NotaEstudElemento> notaEstudElementolist;
        notaEstudElementolist =NotaEstudElemento.find(NotaEstudElemento.class,"estudiante = ? and elemento = ? and evaluacion = ?",String.valueOf(this.getId()),
                String.valueOf(elemento),String.valueOf(evaluacion));
        if(!notaEstudElementolist.isEmpty()) {
            notaEstudElemento = notaEstudElementolist.get(0);
            if (notaEstudElemento == null) {
                return null;
            } else {
                return notaEstudElemento;
            }
        }else{
            return null;
        }
    }
}
