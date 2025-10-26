package Personas.presentation.Paciente;

import Personas.Application;
import Personas.logic.PDFUtil;
import Personas.logic.Paciente;
import java.awt.BorderLayout;

import Personas.logic.Service;
import Personas.presentation.Paciente.Controller;
import Personas.presentation.Paciente.Model;
import Personas.presentation.Paciente.TableModel;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import com.github.lgooddatepicker.components.DatePicker;

public class View implements PropertyChangeListener {
    private JTextField idTextField;
    private JTextField telephonTextField;
    private JTextField nameTextField;
    private DatePicker birthDatePicker;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JButton borrarButton;
    private JTextField searchTextfield;
    private JButton buscarButton;
    private JButton reporteButton;
    private JTable table1;
    private JPanel panelPrincipal;

    Controller controller;
    Model model;

    public View() {

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Paciente filter = new Paciente();
                    filter.setId(searchTextfield.getText());
                    filter.setName(searchTextfield.getText());
                    controller.search(filter);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panelPrincipal, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // BOTONES
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    Paciente m = take();
                    try {
                        controller.save(m);
                        resetField(idTextField);
                        resetField(nameTextField);
                        resetField(telephonTextField);
                        birthDatePicker.setDate(null);
                        JOptionPane.showMessageDialog(panelPrincipal, "Paciente registrado", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panelPrincipal, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(panelPrincipal, "Por favor, rellene todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
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
                    controller.deletePaciente();
                    JOptionPane.showMessageDialog(panelPrincipal, "Paciente eliminado", "Info", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panelPrincipal, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        reporteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PDFUtil.PacientetoPDF(model.getCurrent(), Service.instance().getPathPacientes());
                    JOptionPane.showMessageDialog(panelPrincipal, "Reporte generado", "Info", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panelPrincipal, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clear();
                resetField(idTextField);
                resetField(nameTextField);
                resetField(telephonTextField);
                birthDatePicker.setDate(null);

            }
        });

        table1.setModel(new TableModel(
                new int[]{TableModel.ID, TableModel.NOMBRE, TableModel.TELEFONO, TableModel.FECHANACIMIENTO},
                new ArrayList<>()
        ));
    }

    public JPanel getPanel() {
        return panelPrincipal;
    }


    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Personas.presentation.Paciente.Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Personas.presentation.Paciente.Model.LIST:
                int[] cols = {TableModel.ID, TableModel.NOMBRE, TableModel.TELEFONO, TableModel.FECHANACIMIENTO};
                table1.setModel(new TableModel(cols, model.getList()));
                table1.setRowHeight(30);
                TableColumnModel columnModel = table1.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                columnModel.getColumn(1).setPreferredWidth(150);
                columnModel.getColumn(2).setPreferredWidth(200);
                columnModel.getColumn(3).setPreferredWidth(150);
                break;

            case Personas.presentation.Paciente.Model.CURRENT:
                Paciente current = model.getCurrent();
                idTextField.setText(current.getId());
                nameTextField.setText(current.getName());
                telephonTextField.setText(current.getTelefono());
                birthDatePicker.setDate(current.getFechaNac());

                // habilitar/deshabilitar según modo
                if (model.getMode() == Personas.Application.MODE_EDIT) {
                    idTextField.setEnabled(false);
                    borrarButton.setEnabled(true);

                } else {
                    idTextField.setEnabled(true);
                    borrarButton.setEnabled(false);
                }

                break;

            case Model.FILTER:
                idTextField.setText(model.getFilter().getId());
                idTextField.setText(model.getFilter().getName());

                break;
        }

        this.panelPrincipal.revalidate();
    }


    private Paciente take() {
        Paciente p = new Paciente();
        p.setId(idTextField.getText());
        p.setName(nameTextField.getText());
        p.setTelefono(telephonTextField.getText());
        p.setFechaNac(birthDatePicker.getDate());
        p.setRol("Paciente");


        return p;
    }

    private boolean validateFields() {
        boolean valid = true;
        if (idTextField.getText().trim().isEmpty()) {
            valid = false;
            idTextField.setBackground(Application.BACKGROUND_ERROR);
            idTextField.setToolTipText("ID requerido");
        } else {
            idTextField.setBackground(null);
            idTextField.setToolTipText(null);
        }
        if (nameTextField.getText().trim().isEmpty()) {
            valid = false;
            nameTextField.setBackground(Application.BACKGROUND_ERROR);
            nameTextField.setToolTipText("Nombre requerido");
        } else {
            nameTextField.setBackground(null);
            nameTextField.setToolTipText(null);
        }
        if (telephonTextField.getText().trim().isEmpty()) {
            valid = false;
            telephonTextField.setBackground(Application.BACKGROUND_ERROR);
            telephonTextField.setToolTipText("Telefono requerido");
        } else {
            telephonTextField.setBackground(null);
            telephonTextField.setToolTipText(null);
        }
        if (birthDatePicker.getDate() == null) {
            valid = false;
            birthDatePicker.setBackground(Application.BACKGROUND_ERROR);
            birthDatePicker.setToolTipText("Fecha de nacimiento requerida");
        } else {
            birthDatePicker.setBackground(null);
            birthDatePicker.setToolTipText(null);
        }
        return valid;
    }

    private void resetField(JTextField field) {
        field.setBackground(null);
        field.setToolTipText(null);
    }
}
