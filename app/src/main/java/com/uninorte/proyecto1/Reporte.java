package com.uninorte.proyecto1;

/**
 * Created by daniel on 1/05/17.
 */

public class Reporte {
    private String Nombre;
    private Double Nota;
    private Long id;

    public Reporte(String nombre, Double nota, Long id) {
        Nombre = nombre;
        Nota = nota;
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public Double getNota() {
        return Nota;
    }

    public void setNota(Double nota) {
        Nota = nota;
    }

    public void addNota(Double nota){
        this.Nota=this.Nota+nota;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
