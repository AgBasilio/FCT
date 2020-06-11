package com.example.proyectoincremental.Utils;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Asignatura {
    String nombre;
    String curso;
    String descricion;
    String imgAsignatura;
    String id;

    public Asignatura() {
    }

    public Asignatura(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getDescricion() {
        return descricion;
    }

    public void setDescricion(String descricion) {
        this.descricion = descricion;
    }

    public String getImgAsignatura() {
        return imgAsignatura;
    }

    public void setImgAsignatura(String imgAsignatura) {
        this.imgAsignatura = imgAsignatura;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asignatura that = (Asignatura) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
