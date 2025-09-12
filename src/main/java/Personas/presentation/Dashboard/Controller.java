package Personas.presentation.Dashboard;

import java.util.Map;

public class Controller {
    private Model model;
    private View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public void mostrarEstadisticas(String inicio, String fin) {
        Map<String, Integer> datosLine = model.getMedicamentosPorMes(inicio, fin);
        view.updateLineChart(datosLine);

        Map<String, Long> datosPie = model.getRecetasPorEstado();
        view.updatePieChart(datosPie);
    }
}
