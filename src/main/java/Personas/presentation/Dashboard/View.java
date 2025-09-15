package Personas.presentation.Dashboard;

import Personas.logic.Receta;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class View extends JPanel implements PropertyChangeListener {
    private Model model;
    private Controller controller;
    private JPanel lineChartPanel;
    private JPanel pieChartPanel;
    private JTextField inicioField;
    private JTextField finField;

    private static final Color[] LINE_COLORS = {
            new Color(50, 205, 50), // Verde
            new Color(255, 99, 71), // Rojo
            new Color(30, 144, 255), // Azul
            Color.MAGENTA,
            Color.ORANGE
    };

    public View(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;
        model.addPropertyChangeListener(this);
        initComponents();
        initializeDateFields();
        if (controller != null) {
            controller.mostrarEstadisticas(inicioField.getText(), finField.getText());
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel de controles
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Filtros"));
        controlPanel.add(new JLabel("Fecha Inicio (YYYY-MM-DD):"));
        inicioField = new JTextField(10);
        controlPanel.add(inicioField);
        controlPanel.add(new JLabel("Fecha Fin (YYYY-MM-DD):"));
        finField = new JTextField(10);
        controlPanel.add(finField);
        JButton updateButton = new JButton("Actualizar Gráficos");
        updateButton.addActionListener(e -> {
            if (controller != null) {
                controller.mostrarEstadisticas(inicioField.getText(), finField.getText());
            } else {
                JOptionPane.showMessageDialog(this, "Error: Controlador no inicializado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        controlPanel.add(updateButton);
        add(controlPanel, BorderLayout.NORTH);

        // Panel de gráficos
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2));
        lineChartPanel = new JPanel(new BorderLayout());
        lineChartPanel.setBorder(BorderFactory.createTitledBorder("Medicamentos por Mes"));
        lineChartPanel.add(new JLabel("Cargando datos...", JLabel.CENTER));
        chartsPanel.add(lineChartPanel);

        pieChartPanel = new JPanel(new BorderLayout());
        pieChartPanel.setBorder(BorderFactory.createTitledBorder("Recetas por Estado"));
        pieChartPanel.add(new JLabel("Cargando datos...", JLabel.CENTER));
        chartsPanel.add(pieChartPanel);

        add(chartsPanel, BorderLayout.CENTER);
    }

    private void initializeDateFields() {
        if (model.getRecetas() == null || model.getRecetas().isEmpty()) {
            inicioField.setText(LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            finField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } else {
            try {
                String minDate = model.getRecetas().stream()
                        .filter(r -> r.getFechaConfeccion() != null && !r.getFechaConfeccion().isEmpty())
                        .map(Receta::getFechaConfeccion)
                        .min(String::compareTo)
                        .orElse(LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                String maxDate = model.getRecetas().stream()
                        .filter(r -> r.getFechaConfeccion() != null && !r.getFechaConfeccion().isEmpty())
                        .map(Receta::getFechaConfeccion)
                        .max(String::compareTo)
                        .orElse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                inicioField.setText(minDate);
                finField.setText(maxDate);
            } catch (Exception e) {
                inicioField.setText(LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                finField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        }
    }

    public JPanel getPanel() {
        return this;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Model.RECETAS.equals(evt.getPropertyName())) {
            initializeDateFields();
            if (controller != null) {
                controller.mostrarEstadisticas(inicioField.getText(), finField.getText());
            }
        }
    }

    public void updateLineChart(Map<String, Map<String, Integer>> datos) {
        lineChartPanel.removeAll();
        if (datos == null || datos.isEmpty()) {
            lineChartPanel.add(new JLabel("Sin datos para mostrar", JLabel.CENTER), BorderLayout.CENTER);
            lineChartPanel.revalidate();
            lineChartPanel.repaint();
            return;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int seriesIndex = 0;
        for (Map.Entry<String, Map<String, Integer>> entry : datos.entrySet()) {
            String medicamento = entry.getKey();
            Map<String, Integer> valores = entry.getValue();
            for (Map.Entry<String, Integer> mesData : valores.entrySet()) {
                dataset.addValue(mesData.getValue(), medicamento, mesData.getKey());
            }
            seriesIndex++;
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Medicamentos Recetados por Mes",
                "Mes",
                "Cantidad",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(240, 240, 240));
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);
        for (int i = 0; i < seriesIndex && i < LINE_COLORS.length; i++) {
            plot.getRenderer().setSeriesPaint(i, LINE_COLORS[i]);
        }

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(450, 400));
        lineChartPanel.add(chartPanel, BorderLayout.CENTER);
        lineChartPanel.revalidate();
        lineChartPanel.repaint();
    }

    public void updatePieChart(Map<String, Long> datos) {
        pieChartPanel.removeAll();
        if (datos == null || datos.isEmpty()) {
            pieChartPanel.add(new JLabel("Sin datos para mostrar", JLabel.CENTER), BorderLayout.CENTER);
            pieChartPanel.revalidate();
            pieChartPanel.repaint();
            return;
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Long> entry : datos.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Distribución de Recetas por Estado",
                dataset,
                true, true, false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setLabelBackgroundPaint(new Color(220, 220, 220));
        plot.setSectionPaint("confeccionada", new Color(100, 149, 237));
        plot.setSectionPaint("proceso", new Color(255, 165, 0));
        plot.setSectionPaint("lista", new Color(50, 205, 50));
        plot.setSectionPaint("entregada", new Color(255, 99, 71));
        plot.setSectionPaint("Desconocido", Color.GRAY);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(450, 400));
        pieChartPanel.add(chartPanel, BorderLayout.CENTER);
        pieChartPanel.revalidate();
        pieChartPanel.repaint();
    }
}