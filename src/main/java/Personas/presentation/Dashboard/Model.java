package Personas.presentation.Dashboard;

import Personas.presentation.AbstractModel;
import java.util.HashMap;
import java.util.Map;

public class Model extends AbstractModel {
    // Datos de ejemplo (Medicamentos por mes, Recetas por estado)
    private Map<String, Integer> medicamentosPorMes;
    private Map<String, Integer> recetasPorEstado;

    public Model() {
        medicamentosPorMes = new HashMap<>();
        recetasPorEstado = new HashMap<>();
    }

    public Map<String, Integer> getMedicamentosPorMes() {
        return medicamentosPorMes;
    }

    public Map<String, Integer> getRecetasPorEstado() {
        return recetasPorEstado;
    }

    public void setMedicamentosPorMes(Map<String, Integer> datos) {
        this.medicamentosPorMes = datos;
        firePropertyChange("medicamentosPorMes");
    }

    public void setRecetasPorEstado(Map<String, Integer> datos) {
        this.recetasPorEstado = datos;
        firePropertyChange("recetasPorEstado");
    }
}
