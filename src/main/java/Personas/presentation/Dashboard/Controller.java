package Personas.presentation.Dashboard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Map;

public class Controller implements PropertyChangeListener {
    private Model model;
    private View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        model.addPropertyChangeListener(this);

        // Datos iniciales (ejemplo)
        cargarDatosEjemplo();
    }

    private void cargarDatosEjemplo() {
        model.setMedicamentosPorMes(Map.of(
                "Enero", 50,
                "Febrero", 30,
                "Marzo", 70
        ));

        model.setRecetasPorEstado(Map.of(
                "Pendiente", 40,
                "Entregada", 60,
                "Cancelada", 10
        ));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "medicamentosPorMes":
                view.updateLineChart(model.getMedicamentosPorMes());
                break;
            case "recetasPorEstado":
                view.updatePieChart(model.getRecetasPorEstado());
                break;
        }
    }
}
