package Personas.Logic;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Receta implements Serializable {
    private Paciente paciente;

    private LocalDate fechaConfeccion;

    private LocalDate fechaRetiro;

    private final List<MedicamentoRecetado> prescripciones;

    private String estado; // "confeccionada", "proceso", "lista", "entregada"

    private int id; // Auto Increment

    public Receta() {
        prescripciones = new ArrayList<>();
    }

    public Receta(Paciente paciente) { // Constructor para crear
        this.paciente = paciente;
        this.prescripciones = new ArrayList<>();
        this.estado = "confeccionada"; // por defecto al crear
    }

    public Receta(Paciente paciente, LocalDate fechaConfeccion, LocalDate fechaRetiro, String estado, int id) { // Constructor para leer de BD
        this.id = id;
        this.paciente = paciente;
        this.fechaConfeccion = fechaConfeccion;
        this.fechaRetiro = fechaRetiro;
        this.prescripciones = new ArrayList<>();
        this.estado = estado;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public LocalDate getFechaConfeccion() {
        return fechaConfeccion;
    }

    public void setFechaConfeccion(LocalDate fechaConfeccion) {
        this.fechaConfeccion = fechaConfeccion;
    }

    public LocalDate getFechaRetiro() {
        return fechaRetiro;
    }

    public void setFechaRetiro(LocalDate fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }

    public List<MedicamentoRecetado> getMedicamentos() {
        return prescripciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
}

