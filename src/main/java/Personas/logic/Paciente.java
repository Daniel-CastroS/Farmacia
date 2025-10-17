package Personas.logic;

import java.time.LocalDate;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import Personas.data.LocalDateAdapter;

public class Paciente extends Persona{
    private String telefono;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fechaNac;

    public Paciente(String id, String name, String numero, LocalDate fechaNac) {
        super(id, name, "Paciente");
        this.telefono = numero;
        this.fechaNac = fechaNac;
    }
    public Paciente() {
        super();
        this.telefono = " ";
        this.fechaNac = null;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telephone) {
        this.telefono = telephone;
    }

    public LocalDate getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(LocalDate fechaNac) {
        this.fechaNac = fechaNac;
    }
}
