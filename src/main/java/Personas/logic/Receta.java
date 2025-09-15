package Personas.logic;

import Personas.data.LocalDateAdapter;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class Receta {
    private Paciente paciente;

    //@XmlJavaTypeAdapter(LocalDateAdapter.class)
    private String fechaConfeccion;

    //@XmlJavaTypeAdapter(LocalDateAdapter.class)
    private String fechaRetiro;

    @XmlElementWrapper(name = "medicamentos")
    @XmlElement(name = "medicamentoRecetado")
    private List<MedicamentoRecetado> medicamentos;

    private String estado; // "confeccionada", "proceso", "lista", "entregada"

    public Receta() {
        medicamentos = new ArrayList<>();
        estado = "confeccionada"; // por defecto
    }

    public Receta(Paciente paciente) {
        this.paciente = paciente;
        this.medicamentos = new ArrayList<>();
        this.estado = "confeccionada"; // por defecto
    }

    // --- Getters y Setters ---
    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getFechaConfeccion() {
        return fechaConfeccion;
    }

    public void setFechaConfeccion(String fechaConfeccion) {
        this.fechaConfeccion = fechaConfeccion;
    }

    public String getFechaRetiro() {
        return fechaRetiro;
    }

    public void setFechaRetiro(String fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }

    public List<MedicamentoRecetado> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<MedicamentoRecetado> medicamentos) {
        if (medicamentos == null) {
            this.medicamentos = new ArrayList<>();
        } else {
            this.medicamentos = medicamentos;
        }
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void addMedicamento(MedicamentoRecetado med) {
        if (this.medicamentos == null) {
            this.medicamentos = new ArrayList<>();
        }
        this.medicamentos.add(med);
    }

    public void removeMedicamento(MedicamentoRecetado med) {
        if (this.medicamentos != null) {
            this.medicamentos.remove(med);
        }
    }
}
