package Personas.Logic;

import java.io.Serializable;

public class Medicamento implements Serializable {
    private String codigo;
    private String presentacion;
    private String nombre;

    public Medicamento(String codigo, String presentacion, String nombre) {
        this.codigo = codigo;
        this.presentacion = presentacion;
        this.nombre = nombre;
    }
    public Medicamento() {
        codigo="";
        presentacion="";
        nombre="";
    }

    public String getCodigo(){
        return codigo;
    }
    public  String getPresentacion(){
        return presentacion;
    }
    public String getNombre(){
        return nombre;
    }
    public void setCodigo(String codigo){
        this.codigo = codigo;
    }
    public void setPresentacion(String presentacion){
        this.presentacion = presentacion;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }


}
