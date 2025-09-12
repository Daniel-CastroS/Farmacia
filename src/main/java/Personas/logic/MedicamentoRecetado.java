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

    public MedicamentoRecetado(){

    }

    public MedicamentoRecetado(Medicamento medicamento, int cantidad, String indicaciones, int duracionDias) {
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
        this.duracionDias = duracionDias;
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
}
