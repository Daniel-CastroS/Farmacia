package Personas.presentation.Medicamentos;

import Personas.Application;
import Personas.logic.Medicamento;
import Personas.logic.PDFUtil;
import Personas.logic.Service;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View implements PropertyChangeListener {
    private JPanel panel;
    private JTextField textFieldCodigo;
    private JTextField textFieldNombre;
    private JTextField textFieldDescripcion;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JButton borrarButton;
    private JTextField buscarTextField;
    private JButton buscarButton;
    private JButton reporteButton;
    private JTable table1;

    Controller controller;
    Model model;

    public View() {
        // BOTÓN BUSCAR
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Medicamento filter = new Medicamento();
                    filter.setCodigo(buscarTextField.getText());
                    filter.setNombre(buscarTextField.getText());
                    controller.search(filter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // BOTÓN GUARDAR
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    Medicamento m = take();
                    try {
                        controller.save(m);
                        JOptionPane.showMessageDialog(panel, "Medicamento registrado", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // CLICK EN TABLA
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table1.getSelectedRow();
                if (row != -1) {
                    controller.edit(row);
                }
            }
        });

        // BOTÓN BORRAR
        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.deleteMedicamento();
                    JOptionPane.showMessageDialog(panel, "Medicamento eliminado", "Info", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        reporteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PDFUtil.MedicamentotoPDF(model.getCurrent(), Service.instance().getData().getPathMedicamentos());
                    JOptionPane.showMessageDialog(panel, "Reporte generado", "Info", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // BOTÓN LIMPIAR
        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clear();
            }
        });
    }

    // ==== GETTERS & SETTERS ====
    public JPanel getPanel() {
        return panel;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    // ==== BINDING ENTRE VISTA Y MODELO ====
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {TableModel.CODIGO, TableModel.NOMBRE, TableModel.DESCRIPCION};
                table1.setModel(new TableModel(cols, model.getList()));
                table1.setRowHeight(30);
                TableColumnModel columnModel = table1.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                columnModel.getColumn(1).setPreferredWidth(150);
                columnModel.getColumn(2).setPreferredWidth(200);
                break;

            case Model.CURRENT:
                Medicamento current = model.getCurrent();
                textFieldCodigo.setText(current.getCodigo());
                textFieldNombre.setText(current.getNombre());
                textFieldDescripcion.setText(current.getPresentacion());

                if (model.getMode() == Application.MODE_EDIT) {
                    textFieldCodigo.setEnabled(false);
                    borrarButton.setEnabled(true);
                } else {
                    textFieldCodigo.setEnabled(true);
                    borrarButton.setEnabled(false);
                }

                resetField(textFieldCodigo);
                resetField(textFieldNombre);
                resetField(textFieldDescripcion);
                break;

            case Model.FILTER:
                buscarTextField.setText(model.getFilter().getCodigo());
                break;
        }

        this.panel.revalidate();
    }

    // ==== HELPERS ====
    private Medicamento take() {
        Medicamento m = new Medicamento();
        m.setCodigo(textFieldCodigo.getText());
        m.setNombre(textFieldNombre.getText());
        m.setPresentacion(textFieldDescripcion.getText());
        return m;
    }

    private boolean validateFields() {
        boolean valid = true;

        if (textFieldCodigo.getText().isEmpty()) {
            valid = false;
            textFieldCodigo.setBackground(Application.BACKGROUND_ERROR);
            textFieldCodigo.setToolTipText("Código requerido");
        }
        if (textFieldNombre.getText().isEmpty()) {
            valid = false;
            textFieldNombre.setBackground(Application.BACKGROUND_ERROR);
            textFieldNombre.setToolTipText("Nombre requerido");
        }
        if (textFieldDescripcion.getText().isEmpty()) {
            valid = false;
            textFieldDescripcion.setBackground(Application.BACKGROUND_ERROR);
            textFieldDescripcion.setToolTipText("Presentación requerida");
        }
        return valid;
    }

    private void resetField(JTextField field) {
        field.setBackground(null);
        field.setToolTipText(null);
    }
}
