package Personas.presentation.Dashboard;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class View extends JPanel {
    private Model model;

    private ChartPanel chartLinePanel;
    private ChartPanel chartPiePanel;

    public View(Model model) {
        this.model = model;
        this.setLayout(new GridLayout(1, 2)); // dos gráficos lado a lado
        initComponents();
    }

    private void initComponents() {
        chartLinePanel = new ChartPanel(null);
        chartPiePanel = new ChartPanel(null);

        this.add(chartLinePanel);
        this.add(chartPiePanel);
    }

    public void updateLineChart(Map<String, Integer> datos) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            dataset.addValue(entry.getValue(), "Medicamentos", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Medicamentos",
                "Mes",
                "Cantidad",
                dataset
        );
        chartLinePanel.setChart(chart);
    }

    public void updatePieChart(Map<String, Integer> datos) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Recetas",
                dataset
        );
        chartPiePanel.setChart(chart);
    }
}

