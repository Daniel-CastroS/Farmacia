package Personas.Logic;


import java.io.Serializable;

public class Persona implements Serializable {
    private String id;
    private String name;
    private String rol;
    // En el enunciado solo sale estos en comun entre los tipos persona.
    public  Persona(String id, String name, String rol) {
        this.id = id;
        this.name = name;
        this.rol = rol;
    }
    public Persona() {
        this.id = "";
        this.name = "";
        this.rol = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
