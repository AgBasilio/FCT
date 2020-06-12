package com.example.proyectoincremental.Utils;

public class Grupos {

    String nombreGrupo;
    String numeroGrupo;

    String id;

    public Grupos() {
        this.nombreGrupo = "";
        this.numeroGrupo = "";
        this.id = "";
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public String getNumeroGrupo() {
        return numeroGrupo;
    }

    public void setNumeroGrupo(String numeroGrupo) {
        this.numeroGrupo = numeroGrupo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        if(id.isEmpty())
            return "No seleccionar";
        else
            return "Nombre: "+nombreGrupo + " - Numero: " + numeroGrupo;
    }
}

