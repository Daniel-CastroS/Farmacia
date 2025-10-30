package Personas.Presentation.Prescripcion;

import Personas.Logic.Medicamento;
import Personas.Logic.Receta;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Personas.Logic.Service;
import com.github.lgooddatepicker.components.DatePicker;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;


public class View implements PropertyChangeListener {

    private JPanel panelPrincipal;
    private JButton btnGuardar;
    private JButton btnSeleccionarPaciente;
    private JButton btnAgregarMedicamento;
    private JTable tableMedicamentos;    // tabla de medicamentos

    private DatePicker fechaRetiroPicker;
    private JButton limpiarButton;
    private JTextField textFieldNombrePaciente;
    private JPanel panel1;

    private Controller controller;
    private Model model;

    public View() {
        $$$setupUI$$$();
        textFieldNombrePaciente.setEnabled(false);
        // Botón seleccionar paciente
        btnSeleccionarPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.seleccionarPaciente();
                if (controller.getModel().getCurrent().getPaciente() != null)
                    setNombrePaciente(controller.getModel().getCurrent().getPaciente().getName());
            }
        });

        // Botón agregar medicamento
        btnAgregarMedicamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrent().getPaciente() != null) {
                    Personas.Presentation.Prescripcion.Medicamento.View mini =
                            new Personas.Presentation.Prescripcion.Medicamento.View(controller, obtenerListaMedicamentos());
                    mini.mostrar();
                } else {
                    JOptionPane.showMessageDialog(panel1, "Primero selecciona un paciente");
                }
            }
        });
        // Botón guardar receta
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Receta r = model.getCurrent();

                // Validaciones
                if (r.getPaciente() == null) {
                    JOptionPane.showMessageDialog(panel1, "Debe seleccionar un paciente");
                    return;
                }
                if (r.getMedicamentos().isEmpty()) {
                    JOptionPane.showMessageDialog(panel1, "Debe agregar al menos un medicamento");
                    return;
                }
                if (fechaRetiroPicker.getDate() == null) {
                    JOptionPane.showMessageDialog(panel1, "Debe ingresar la fecha de retiro");
                    return;
                }
                if (fechaRetiroPicker.getDate().isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(panel1, "La fecha de retiro no puede ser anterior a la fecha actual");
                    return;
                }

                r.setFechaRetiro(fechaRetiroPicker.getDate());
                r.setEstado("Confeccionada");

                // Guardar la receta
                try {
                    controller.save(r);
                    JOptionPane.showMessageDialog(panel1, "Receta guardada correctamente");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel1, ex.getMessage());
                }
            }
        });

        tableMedicamentos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableMedicamentos.getSelectedRow();
                if (row != -1) {
                    try {
                        controller.editMed(row);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setCurrent(new Receta());
                model.setListMed(new ArrayList<>());
                textFieldNombrePaciente.setText("");
                fechaRetiroPicker.setDate(null);
                tableMedicamentos.setModel(new TableModel(
                        new int[]{TableModel.MEDICAMENTO, TableModel.PRESENTACION, TableModel.INDICACIONES, TableModel.DURACION},
                        List.of()
                ));
                tableMedicamentos.setRowHeight(25);
            }
        });

    }

    public JPanel getPanel() {
        return panel1;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private List<Medicamento> obtenerListaMedicamentos() {
        // Esto obtiene todos los medicamentos desde el Service
        return Service.instance().readAllMedicamento();
    }


    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        int[] cols = {
                TableModel.MEDICAMENTO,
                TableModel.PRESENTACION,
                TableModel.CANTIDAD,
                TableModel.INDICACIONES,
                TableModel.DURACION
        };
        switch (evt.getPropertyName()) {
            case Model.CURRENT:
                if (model.getCurrent() != null) {
                    Receta recetaActual = model.getCurrent();
                    tableMedicamentos.setModel(new TableModel(cols, recetaActual.getMedicamentos()));
                    tableMedicamentos.setRowHeight(25);
                    // sincronizar fecha y paciente en la UI
                    fechaRetiroPicker.setDate(recetaActual.getFechaRetiro());
                    if (recetaActual.getPaciente() != null) setNombrePaciente(recetaActual.getPaciente().getName());
                } else {
                    // Limpiar tabla si no hay receta seleccionada
                    tableMedicamentos.setModel(new TableModel(cols, List.of()));
                    fechaRetiroPicker.setDate(null);
                    textFieldNombrePaciente.setText("");
                }
                break;
            case Model.LIST:
                if (model.getCurrent() != null) {
                    tableMedicamentos.setModel(new TableModel(cols, model.getCurrent().getMedicamentos()));
                    tableMedicamentos.setRowHeight(25);
                    // mantener sincronía con current
                    fechaRetiroPicker.setDate(model.getCurrent().getFechaRetiro());
                    if (model.getCurrent().getPaciente() != null)
                        setNombrePaciente(model.getCurrent().getPaciente().getName());
                } else {
                    tableMedicamentos.setModel(new TableModel(cols, List.of()));
                    fechaRetiroPicker.setDate(null);
                    textFieldNombrePaciente.setText("");
                }
                break;
            case Model.FILTER:
                tableMedicamentos.setModel(new TableModel(cols, model.getCurrent().getMedicamentos()));
                tableMedicamentos.setRowHeight(25);
                break;
        }
    }

    public void setNombrePaciente(String nombre) {
        textFieldNombrePaciente.setText(nombre);
        textFieldNombrePaciente.setEnabled(false);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(null, "Control\n", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        btnSeleccionarPaciente = new JButton();
        btnSeleccionarPaciente.setIcon(new ImageIcon(getClass().getResource("/search.png")));
        btnSeleccionarPaciente.setText("Seleccionar Paciente");
        panel2.add(btnSeleccionarPaciente, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnAgregarMedicamento = new JButton();
        btnAgregarMedicamento.setIcon(new ImageIcon(getClass().getResource("/meds.png")));
        btnAgregarMedicamento.setText("Agregar Medicamento");
        panel2.add(btnAgregarMedicamento, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(null, "Receta Médica", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("Fecha de retiro");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldNombrePaciente = new JTextField();
        panel3.add(textFieldNombrePaciente, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setIcon(new ImageIcon(getClass().getResource("/cough.png")));
        label2.setText("");
        panel3.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fechaRetiroPicker = new DatePicker();
        panel4.add(fechaRetiroPicker, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel4.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Info", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        tableMedicamentos = new JTable();
        scrollPane1.setViewportView(tableMedicamentos);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnGuardar = new JButton();
        btnGuardar.setIcon(new ImageIcon(getClass().getResource("/save.png")));
        btnGuardar.setText("Guardar");
        panel5.add(btnGuardar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        limpiarButton = new JButton();
        limpiarButton.setIcon(new ImageIcon(getClass().getResource("/clean.png")));
        limpiarButton.setText("Limpiar");
        panel5.add(limpiarButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}