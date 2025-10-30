package Personas.Presentation.Farmaceuta;

import Personas.Application;
import Personas.Logic.Farmaceuta;
import Personas.Logic.PDFUtil;
import Personas.Logic.Service;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
    private JTextField textField1;  // campo de búsqueda
    private JButton buscarButton;
    private JTable table1;
    private JButton reporteButton;

    Controller controller;
    Model model;

    public View() {
        // BOTÓN BUSCAR
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Farmaceuta filter = new Farmaceuta();
                    filter.setId(textField1.getText());
                    filter.setName(textField1.getText());
                    controller.search(filter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // BOTÓN GUARDAR
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    Farmaceuta f = take();
                    try {
                        controller.save(f);
                        JOptionPane.showMessageDialog(panel1, "Farmacéutico registrado", "Info", JOptionPane.INFORMATION_MESSAGE);
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
                if (row != -1) {
                    try {
                        controller.edit(row);  // aquí atrapamos la excepción
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


        // BOTÓN BORRAR
        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.deleteFarmaceuta();
                    JOptionPane.showMessageDialog(panel1, "Farmacéutico eliminado", "Info", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        reporteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PDFUtil.farmaceutaToPDF(model.getCurrent(), Service.instance().getPathFarmaceutas());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                int[] cols = {TableModel.ID, TableModel.NOMBRE};
                table1.setModel(new TableModel(cols, model.getList()));
                table1.setRowHeight(30);
                TableColumnModel columnModel = table1.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                columnModel.getColumn(1).setPreferredWidth(200);
                break;

            case Model.CURRENT:
                Farmaceuta current = model.getCurrent();
                textFieldId.setText(current.getId());
                textFieldNombre.setText(current.getName());

                // habilitar/deshabilitar según modo
                if (model.getMode() == Application.MODE_EDIT) {
                    textFieldId.setEnabled(false);
                    borrarButton.setEnabled(true);
                } else {
                    textFieldId.setEnabled(true);
                    borrarButton.setEnabled(false);
                }

                // reset visual
                resetField(textFieldId);
                resetField(textFieldNombre);
                break;

            case Model.FILTER:
                textField1.setText(model.getFilter().getId());
                // textField1.setText(model.getFilter().getName());
                break;
        }
        if (this.panel1 != null) {
            this.panel1.revalidate();
        }
    }

    private Farmaceuta take() {
        Farmaceuta f = new Farmaceuta();
        f.setId(textFieldId.getText());
        f.setGafete(textFieldId.getText());         // igual que id
        f.setClave_sistema(textFieldId.getText());  // necesario para Trabajador
        f.setRol("Farmaceuta");                     // obligatorio para Persona
        f.setName(textFieldNombre.getText());
        return f;
    }


    private boolean validateFields() {
        boolean valid = true;
        if (textFieldId.getText().isEmpty()) {
            valid = false;
            textFieldId.setBackground(Application.BACKGROUND_ERROR);
            textFieldId.setToolTipText("ID requerido");
        } else resetField(textFieldId);

        if (textFieldNombre.getText().isEmpty()) {
            valid = false;
            textFieldNombre.setBackground(Application.BACKGROUND_ERROR);
            textFieldNombre.setToolTipText("Nombre requerido");
        } else resetField(textFieldNombre);

        return valid;
    }

    private void resetField(JTextField field) {
        field.setBackground(null);
        field.setToolTipText(null);
    }
}
