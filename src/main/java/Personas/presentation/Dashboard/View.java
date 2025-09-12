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

    private JPanel lineChartPanel;
    private JPanel pieChartPanel;

    public View(Model model) {
        this.model = model;

        setLayout(new GridLayout(1, 2)); // dos gráficos lado a lado

        lineChartPanel = new JPanel(new BorderLayout());
        add(lineChartPanel);

        pieChartPanel = new JPanel(new BorderLayout());
        add(pieChartPanel);
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

        lineChartPanel.removeAll();
        lineChartPanel.add(new ChartPanel(chart), BorderLayout.CENTER);
        lineChartPanel.revalidate();
        lineChartPanel.repaint();
    }

    public void updatePieChart(Map<String, Long> datos) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map.Entry<String, Long> entry : datos.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Recetas",
                dataset,
                true, true, false
        );

        pieChartPanel.removeAll();
        pieChartPanel.add(new ChartPanel(chart), BorderLayout.CENTER);
        pieChartPanel.revalidate();
        pieChartPanel.repaint();
    }
}