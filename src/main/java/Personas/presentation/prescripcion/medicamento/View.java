package Personas.presentation.prescripcion.medicamento;

import Personas.logic.Medicamento;
import Personas.presentation.prescripcion.Controller;
import Personas.presentation.prescripcion.detalle.Detalle;
import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class View extends JDialog {
    private JPanel contentPane;
    private JButton buttonAgregar;
    private JButton buttonCancelar;
    private JTable tableMedicamentos;

    private Controller controller;
    private List<Medicamento> medicamentos;

    public View(Controller controller, List<Medicamento> medicamentos) {
        this.controller = controller;
        this.medicamentos = medicamentos;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonAgregar);

        // Inicializar tabla
        initTable();

        buttonAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAgregar();
            }
        });

        buttonCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void initTable() {
        // Aquí llenas la tabla con tus medicamentos
        // Por ejemplo, puedes usar un TableModel simple:
        String[] colNames = {"Código", "Nombre", "Presentación"};
        Object[][] data = new Object[medicamentos.size()][3];
        for (int i = 0; i < medicamentos.size(); i++) {
            Medicamento m = medicamentos.get(i);
            data[i][0] = m.getCodigo();
            data[i][1] = m.getNombre();
            data[i][2] = m.getPresentacion();
        }
        tableMedicamentos.setModel(new javax.swing.table.DefaultTableModel(data, colNames));
        tableMedicamentos.setRowHeight(25);
    }

    private void onAgregar() {
        int row = tableMedicamentos.getSelectedRow();
        if (row != -1) {
            Medicamento seleccionado = medicamentos.get(row);

            // Abrir el MiniView de Detalle
            Detalle detalleDialog = new Detalle(controller, controller.getModel().getCurrent(), seleccionado);
            detalleDialog.mostrar();

            // Una vez agregado el detalle, el medicamento recetado ya se incluye en la receta actual
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un medicamento");
        }
    }

    private void onCancel() {
        dispose();
    }

    public void mostrar() {
        pack();
        setVisible(true);
    }
}
