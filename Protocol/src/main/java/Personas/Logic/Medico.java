package Personas.Logic;

import java.io.Serializable;

public class Medico extends Trabajador implements Serializable {
    private String especialidad;

    public Medico(String id, String name, String rol, String especialidad) {
        super(id, name, rol, id, id);
        this.especialidad = especialidad;
    }
    public Medico() {
        super();
        this.especialidad = "";
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}