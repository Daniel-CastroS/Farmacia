package Personas.presentation.Dashboard;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Controller {
    private Model model;
    private View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    // Nuevo método para asignar el View después de la creación
    public void setView(View view) {
        this.view = view;
    }

    public void mostrarEstadisticas(String inicio, String fin) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date inicioDate = sdf.parse(inicio);
            Date finDate = sdf.parse(fin);

            if (inicioDate.after(finDate)) {
                JOptionPane.showMessageDialog(null, "La fecha de inicio debe ser anterior a la fecha de fin.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Map<String, Map<String, Integer>> datosLine = model.getMedicamentosPorMes(inicio, fin);
            if (view != null) {
                view.updateLineChart(datosLine);
                Map<String, Long> datosPie = model.getRecetasPorEstado();
                view.updatePieChart(datosPie);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Use YYYY-MM-DD (ej. 2023-01-01).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}