package Personas.Logic;

import java.io.Serializable;

public class Farmaceuta extends Trabajador implements Serializable {
    public Farmaceuta(String id, String name, String rol) {
        super(id, name, "Farmaceuta",id, id);
    }
    public Farmaceuta() {
        super();
    }
}
