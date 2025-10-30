package Personas.Presentation.Dashboard;

import com.github.lgooddatepicker.components.DatePicker;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.text.NumberFormat;
import java.util.Optional;

public class View extends JPanel implements PropertyChangeListener {
    private Model model;
    private Controller controller;
    private JPanel lineChartPanel;
    private JPanel pieChartPanel;
    private DatePicker inicioPicker;
    private DatePicker finPicker;

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
        // No llamar a controller.mostrarEstadisticas aquí: el Controller aún puede no tener el view asignado.
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel de controles
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Filtros"));
        controlPanel.add(new JLabel("Fecha Inicio (YYYY-MM-DD):"));
        inicioPicker = new DatePicker();
        controlPanel.add(inicioPicker);
        controlPanel.add(new JLabel("Fecha Fin (YYYY-MM-DD):"));
        finPicker = new DatePicker();
        controlPanel.add(finPicker);
        JButton updateButton = new JButton("Actualizar Gráficos");
        updateButton.addActionListener(e -> {
            if (controller != null) {
                controller.mostrarEstadisticas(inicioPicker.getDate(), finPicker.getDate());
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
        // Establece valores por defecto en los DatePicker usando LocalDate.
        if (model.getRecetas() == null || model.getRecetas().isEmpty()) {
            inicioPicker.setDate(LocalDate.now().minusYears(1));
            finPicker.setDate(LocalDate.now());
            return;
        }

        try {
            Optional<LocalDate> minOpt = model.getRecetas().stream()
                    .map(r -> r.getFechaConfeccion() != null ? r.getFechaConfeccion() : r.getFechaRetiro())
                    .filter(d -> d != null)
                    .min(LocalDate::compareTo);

            Optional<LocalDate> maxOpt = model.getRecetas().stream()
                    .map(r -> r.getFechaConfeccion() != null ? r.getFechaConfeccion() : r.getFechaRetiro())
                    .filter(d -> d != null)
                    .max(LocalDate::compareTo);

            inicioPicker.setDate(minOpt.orElse(LocalDate.now().minusYears(1)));
            finPicker.setDate(maxOpt.orElse(LocalDate.now()));
        } catch (Exception e) {
            inicioPicker.setDate(LocalDate.now().minusYears(1));
            finPicker.setDate(LocalDate.now());
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
                controller.mostrarEstadisticas(inicioPicker.getDate(), finPicker.getDate());
            }
        }
    }

    public void updateLineChart(Map<String, Map<String, Integer>> datos, LocalDate inicio, LocalDate fin) {
        System.out.println("updateLineChart called. datos=" + datos + " inicio=" + inicio + " fin=" + fin);
        lineChartPanel.removeAll();
        if (datos == null || datos.isEmpty()) {
            lineChartPanel.add(new JLabel("Sin datos para mostrar", JLabel.CENTER), BorderLayout.CENTER);
            lineChartPanel.revalidate();
            lineChartPanel.repaint();
            return;
        }

        // Generar lista completa de meses entre inicio y fin (inclusive) en formato YYYY-MM
        LocalDate start = inicio.withDayOfMonth(1);
        LocalDate end = fin.withDayOfMonth(1);
        DateTimeFormatter monthFmt = DateTimeFormatter.ofPattern("yyyy-MM");
        java.util.List<String> months = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusMonths(1)) {
            months.add(d.format(monthFmt));
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int seriesIndex = 0;
        for (Map.Entry<String, Map<String, Integer>> entry : datos.entrySet()) {
            String medicamento = entry.getKey();
            Map<String, Integer> valores = entry.getValue();
            System.out.println("Serie: " + medicamento + " -> " + valores);
            // rellenar meses faltantes con 0 basados en el rango completo
            for (String m : months) {
                int v = valores.getOrDefault(m, 0);
                dataset.addValue(v, medicamento, m);
            }
            seriesIndex++;
        }

        System.out.println("Dataset rows=" + dataset.getRowCount() + " columns=" + dataset.getColumnCount());
        for (int r = 0; r < dataset.getRowCount(); r++) {
            for (int c = 0; c < dataset.getColumnCount(); c++) {
                System.out.println("Value[" + dataset.getRowKey(r) + "," + dataset.getColumnKey(c) + "] = " + dataset.getValue(r, c));
            }
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Medicamentos Recetados por Mes",
                "Mes",
                "Cantidad",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        chart.setAntiAlias(true);
        chart.setTextAntiAlias(true);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(240, 240, 240));
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        // Forzar uso de LineAndShapeRenderer para mostrar líneas y puntos
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setDefaultShapesVisible(true);
        renderer.setDefaultShapesFilled(true);
        renderer.setDefaultStroke(new BasicStroke(2.0f));
        // Generador de tooltips: muestra "Medicamento (Mes): cantidad"
        renderer.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator("{0} ({1}): {2}", NumberFormat.getIntegerInstance()));
        for (int i = 0; i < seriesIndex && i < LINE_COLORS.length; i++) {
            renderer.setSeriesPaint(i, LINE_COLORS[i]);
            renderer.setSeriesLinesVisible(i, true);
            renderer.setSeriesShapesVisible(i, true);
            renderer.setSeriesStroke(i, new BasicStroke(2.0f));
        }
        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(450, 400));
        chartPanel.setMouseWheelEnabled(true);
        // Habilitar tooltips en el ChartPanel para que se muestren al pasar el cursor
        chartPanel.setDisplayToolTips(true);
        lineChartPanel.add(chartPanel, BorderLayout.CENTER);
        lineChartPanel.revalidate();
        lineChartPanel.repaint();
    }

    @SuppressWarnings("unchecked")
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    }
}