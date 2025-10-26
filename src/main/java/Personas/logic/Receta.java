package Personas.logic;

import Personas.data.LocalDateAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Receta {
    @XmlElement
    private Paciente paciente;

    @XmlElement
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fechaConfeccion;

    @XmlElement
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fechaRetiro;

    @XmlElementWrapper(name = "medicamentos")
    @XmlElement(name = "medicamentoRecetado")
    private final List<MedicamentoRecetado> prescripciones;

    @XmlElement
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

