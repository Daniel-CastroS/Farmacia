package Personas.logic;

public class Medico extends Trabajador{
    private String especialidad;
    private String clave_sistema;

    public Medico(String id, String name, String rol,String especialidad) {
        super(id, name, rol,id);
        setClave_sistema(id);
        this.clave_sistema = id;
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





