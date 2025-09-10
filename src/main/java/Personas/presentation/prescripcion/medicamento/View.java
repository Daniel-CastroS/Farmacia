package Personas.presentation.prescripcion.medicamento;

import Personas.logic.Medicamento;
import Personas.presentation.prescripcion.Controller;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class View extends JDialog {
    private JPanel contentPane;
    private JButton buttonAgregar;
    private JButton buttonCancelar;
    private JTable tableMedicamentos;
    private JTextField textFieldFiltro;
    private JButton buttonFiltrar;

    private Controller controller;
    private List<Medicamento> medicamentos;
    private List<Medicamento> medicamentosFiltrados;

    public View(Controller controller, List<Medicamento> medicamentos) {
        this.controller = controller;
        this.medicamentos = medicamentos;
        this.medicamentosFiltrados = medicamentos; // inicialmente todos

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonAgregar);

        initTable();

        // Botón agregar
        buttonAgregar.addActionListener(e -> onAgregar());

        // Botón cancelar
        buttonCancelar.addActionListener(e -> dispose());

        // Botón filtrar
        buttonFiltrar.addActionListener(e -> filtrarPorCodigo());

        // cerrar con X
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // cerrar con ESC
        contentPane.registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void initTable() {
        int[] cols = {TableModelMedicamentos.CODIGO, TableModelMedicamentos.NOMBRE, TableModelMedicamentos.PRESENTACION};
        tableMedicamentos.setModel(new TableModelMedicamentos(cols, medicamentosFiltrados));
        tableMedicamentos.setRowHeight(25);
    }

    private void filtrarPorCodigo() {
        String filtro = textFieldFiltro.getText().trim();
        if (filtro.isEmpty()) {
            medicamentosFiltrados = medicamentos; // mostrar todos
        } else {
            medicamentosFiltrados = medicamentos.stream()
                    .filter(m -> m.getCodigo().equals(filtro))
                    .collect(Collectors.toList());
        }
        int[] cols = {TableModelMedicamentos.CODIGO, TableModelMedicamentos.NOMBRE, TableModelMedicamentos.PRESENTACION};
        tableMedicamentos.setModel(new TableModelMedicamentos(cols, medicamentosFiltrados));
    }

    private void onAgregar() {
        int row = tableMedicamentos.getSelectedRow();
        if (row != -1) {
            Medicamento seleccionado = medicamentosFiltrados.get(row);

            // Abrir MiniView de detalle
            Personas.presentation.prescripcion.detalle.Detalle detalleDialog =
                    new Personas.presentation.prescripcion.detalle.Detalle(controller, controller.getModel().getCurrent(), seleccionado);
            detalleDialog.mostrar();

            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un medicamento");
        }
    }

    public void mostrar() {
        pack();
        setVisible(true);
    }
}
