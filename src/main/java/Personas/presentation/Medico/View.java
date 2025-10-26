package Personas.presentation.Medico;
import Personas.Application;
import Personas.logic.Medico;
import Personas.logic.PDFUtil;
import Personas.logic.Service;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import javax.swing.table.TableColumnModel;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class View implements PropertyChangeListener {
    private JPanel panel1;
    private JButton guardarButton;
    private JButton borrarButton;
    private JButton limpiarButton;
    private JTextField textFieldId;
    private JTextField textFieldNombre;
    private JTextField textFieldEspecialidad;
    private JTextField buscarTextField;
    private JButton buscarButton;
    private JTable table1;
    private JButton reporteButton;

    Controller controller;
    Model model;

    public View() {
        model = new Model();
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Medico filter = new Medico();
                    filter.setId(buscarTextField.getText());
                    filter.setName(buscarTextField.getText());
                    controller.search(filter);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // BOTONES
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    Medico m = take();
                    try {
                        controller.save(m);
                        JOptionPane.showMessageDialog(panel1, "Médico registrado", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table1.getSelectedRow();
                if (row != -1) { // Si se seleccionó una fila
                    controller.edit(row);
                }
            }
        });

        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.deleteMedico();
                    JOptionPane.showMessageDialog(panel1, "Médico eliminado", "Info", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        reporteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PDFUtil.MedicotoPDF(model.getCurrent(), Service.instance().getPathMedicamentos());
                    JOptionPane.showMessageDialog(panel1, "Reporte generado", "Info", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clear();
            }
        });


    }

    public JPanel getPanel() {
        return panel1;
    }


    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {TableModel.ID, TableModel.NOMBRE, TableModel.ESPECIALIDAD};
                table1.setModel(new TableModel(cols, model.getList()));
                table1.setRowHeight(30);
                TableColumnModel columnModel = table1.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                columnModel.getColumn(1).setPreferredWidth(150);
                columnModel.getColumn(2).setPreferredWidth(200);
                break;

            case Model.CURRENT:
                Medico current = model.getCurrent();
                textFieldId.setText(current.getId());
                textFieldNombre.setText(current.getName());
                textFieldEspecialidad.setText(current.getEspecialidad());

                // habilitar/deshabilitar según modo
                if (model.getMode() == Personas.Application.MODE_EDIT) {
                    textFieldId.setEnabled(false);
                    borrarButton.setEnabled(true);

                } else {
                    textFieldId.setEnabled(true);
                    borrarButton.setEnabled(false);
                }

                // reset visual
                resetField(textFieldId);
                resetField(textFieldNombre);
                resetField(textFieldEspecialidad);
                break;

            case Model.FILTER:
                buscarTextField.setText(model.getFilter().getId());
                buscarTextField.setText(model.getFilter().getName());

                break;
        }

        this.panel1.revalidate();
    }


    private Medico take() {
        Medico m = new Medico();
        m.setId(textFieldId.getText());
        m.setGafete(textFieldId.getText());
        m.setClave_sistema(textFieldId.getText());
        m.setName(textFieldNombre.getText());
        m.setRol("Medico");
        m.setEspecialidad(textFieldEspecialidad.getText());
        return m;
    }

    private boolean validateFields() {
        boolean valid = true;
        if (textFieldId.getText().isEmpty()) {
            valid = false;
            textFieldId.setBackground(Application.BACKGROUND_ERROR);
            textFieldId.setToolTipText("ID requerido");
        } else {
            textFieldId.setBorder(null);
            textFieldId.setToolTipText(null);
        }

        if (textFieldNombre.getText().isEmpty()) {
            valid = false;
            textFieldNombre.setBackground(Application.BACKGROUND_ERROR);
            textFieldNombre.setToolTipText("Nombre requerido");
        } //else resetField(textFieldNombre);
        else {
            textFieldId.setBorder(null);
            textFieldId.setToolTipText(null);
        }
        if (textFieldEspecialidad.getText().isEmpty()) {
            valid = false;
            textFieldEspecialidad.setBackground(Application.BACKGROUND_ERROR);
            textFieldEspecialidad.setToolTipText("Especialidad requerida");
        } //else resetField(textFieldEspecialidad);
        else {
            textFieldId.setBorder(null);
            textFieldId.setToolTipText(null);

        }

        return valid;
    }

    private void resetField(JTextField field) {
        field.setBackground(null);
        field.setToolTipText(null);
    }


}
