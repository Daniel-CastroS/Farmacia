package Personas.logic;

public class Paciente extends Persona{
    private String telefono;
    private String fechaNac;

    public Paciente(String id, String name, String numero, String fechaNac) {
        super(id, name, "Paciente");
        this.telefono = numero;
        this.fechaNac = fechaNac;
    }
    public Paciente() {
        super();
        this.telefono = " ";
        this.fechaNac = " ";
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telephone) {
        this.telefono = telephone;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }
}
