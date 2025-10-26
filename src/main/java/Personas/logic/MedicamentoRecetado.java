package Personas.logic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)

public class MedicamentoRecetado {
    @XmlElement
    private Medicamento medicamento;
    @XmlElement
    private int cantidad;
    @XmlElement
    private String indicaciones;
    @XmlElement
    private int duracionDias;

    private int prescripcion;// ID Auto Increment

    public MedicamentoRecetado(){
        this.medicamento = new Medicamento();
        this.cantidad = 0;
        this.indicaciones = "";
        this.duracionDias = 0;
    }

    public MedicamentoRecetado(Medicamento medicamento, int cantidad, String indicaciones, int duracionDias) {
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
        this.duracionDias = duracionDias;
    }

    public MedicamentoRecetado(Medicamento medicamento, int cantidad, String indicaciones, int duracionDias, int prescripcion) { // Constructor para leer de BD
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
        this.duracionDias = duracionDias;
        this.prescripcion = prescripcion;
    }
    public String getPresentacion() {
        return medicamento.getPresentacion();
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }
    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getIndicaciones() {
        return indicaciones;
    }
    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public int getDuracionDias() {
        return duracionDias;
    }
    public void setDuracionDias(int duracionDias) {
        this.duracionDias = duracionDias;
    }

    public int getPrescripcion() { return prescripcion; }
    public void setPrescripcion(int prescripcion) { this.prescripcion = prescripcion; }
}
