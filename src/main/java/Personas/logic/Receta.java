package Personas.logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Receta {
    private Paciente paciente;
    private LocalDate fechaConfeccion;
    private LocalDate fechaRetiro;
    private List<MedicamentoRecetado> medicamentos;
    private String estado; // "confeccionada", "proceso", "lista", "entregada"

    public Receta() {
        medicamentos = new ArrayList<>();
    }
    public Receta(Paciente paciente) {
        this.paciente = paciente;
        this.fechaConfeccion = LocalDate.now();
        this.medicamentos = new ArrayList<>();
        this.estado = "confeccionada"; // por defecto al crear
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

    public LocalDate getFechaRetiro() {
        return fechaRetiro;
    }

    public void setFechaRetiro(LocalDate fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }

    public List<MedicamentoRecetado> getMedicamentos() {
        return medicamentos;
    }
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
