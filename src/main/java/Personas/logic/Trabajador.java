package Personas.logic;

public class Trabajador extends Persona{
    private String clave_sistema;
    private String gafete;

    Trabajador(String id, String name, String rol, String clave_sistema, String gafete) {
        super(id, name, rol);
        this.clave_sistema = clave_sistema;
        this.gafete = gafete;
    }

    public Trabajador() {
        super();
        this.clave_sistema = "";
    }

    public String getGafete() {
        return gafete;
    }

    public void setGafete(String gafete) {
        this.gafete = gafete;
    }

    public String getClave_sistema() {
        return clave_sistema;
    }

    public void setClave_sistema(String clave_sistema) {
        this.clave_sistema = clave_sistema;
    }
}
