package Personas.Presentation.Dashboard;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.time.LocalDate;
import java.util.Map;

public class Controller {
    private Model model;
    private View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        model.reloadRecetas();
    }


    public void setView(View view) {
        this.view = view;
        // Forzar inicialización y actualización ahora que view está asignado
        if (this.view != null) {
            try {
                this.view.propertyChange(new PropertyChangeEvent(model, Model.RECETAS, null, null));
            } catch (Exception e) {
                // ignorar
            }
        }
    }

    public void mostrarEstadisticas(LocalDate inicio, LocalDate fin) {
        try {
            System.out.println("mostrarEstadisticas called. inicio=" + inicio + " fin=" + fin);
            if (inicio == null || fin == null) {
                JOptionPane.showMessageDialog(null, "Fechas nulas.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (inicio.isAfter(fin)) {
                JOptionPane.showMessageDialog(null, "La fecha de inicio debe ser anterior a la fecha de fin.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Map<String, Map<String, Integer>> datosLine = model.getMedicamentosPorMes(inicio, fin);
            System.out.println("datosLine=" + datosLine);
            Map<String, Long> datosPie = model.getRecetasPorEstado();
            System.out.println("datosPie=" + datosPie);
            if (view != null) {
                view.updateLineChart(datosLine, inicio, fin);
                view.updatePieChart(datosPie);
            } else {
                System.out.println("view es null, no se actualizan gráficos");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar estadísticas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

