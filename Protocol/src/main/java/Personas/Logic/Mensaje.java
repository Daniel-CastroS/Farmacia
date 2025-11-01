package Personas.Logic;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Mensaje implements Serializable {
    private int id;
    private String remitenteId;
    private String remitenteNombre;
    private String destinatarioId;
    private String destinatarioNombre;
    private String contenido;
    private LocalDateTime fecha;
    private boolean leido;

    public Mensaje() {
        this.fecha = LocalDateTime.now();
        this.leido = false;
    }

    public Mensaje(String remitenteId, String destinatarioId, String contenido) {
        this.remitenteId = remitenteId;
        this.destinatarioId = destinatarioId;
        this.contenido = contenido;
        this.fecha = LocalDateTime.now();
        this.leido = false;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRemitenteId() { return remitenteId; }
    public void setRemitenteId(String remitenteId) { this.remitenteId = remitenteId; }

    public String getRemitenteNombre() { return remitenteNombre; }
    public void setRemitenteNombre(String remitenteNombre) { this.remitenteNombre = remitenteNombre; }

    public String getDestinatarioId() { return destinatarioId; }
    public void setDestinatarioId(String destinatarioId) { this.destinatarioId = destinatarioId; }

    public String getDestinatarioNombre() { return destinatarioNombre; }
    public void setDestinatarioNombre(String destinatarioNombre) { this.destinatarioNombre = destinatarioNombre; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public boolean isLeido() { return leido; }
    public void setLeido(boolean leido) { this.leido = leido; }

    @Override
    public String toString() {
        return "De: " + remitenteNombre + " | " + contenido;
    }
}