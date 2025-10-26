package Personas.presentation.ManejoRecetas;

import Personas.Application;
import Personas.logic.Paciente;
import Personas.logic.Receta;
import Personas.logic.Service;
import Personas.presentation.Despacho.TableModel;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class View implements PropertyChangeListener {
    private JTextField textFieldID;
    private JButton buscarButton;
    private JButton borrarButton;
    private JTable tableRecetas;
    private JPanel panel1;

    Controller controller;
    Model model;

    public View() {
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!validateFields()) {
                    JOptionPane.showMessageDialog(panel1, "Debe rellenar los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    Receta filter = new Receta();
                    Paciente aux = new Paciente();
                    aux.setId(textFieldID.getText());
                    filter.setPaciente(aux);
                    controller.search(filter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
                resetField(textFieldID);
            }
        });

        borrarButton.addActionListener(e -> {
            try{
                controller.deleteReceta();
                JOptionPane.showMessageDialog(panel1, "Receta eliminada", "Info", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        tableRecetas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableRecetas.getSelectedRow();
                if (row != -1) {
                    controller.edit(row);  // Llama al método edit del controller
                }
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
            case Personas.presentation.Despacho.Model.LIST:
                int[] cols = {Personas.presentation.Despacho.TableModel.ID_PACIENTE, Personas.presentation.Despacho.TableModel.NOMBRE_PACIENTE, Personas.presentation.Despacho.TableModel.FECHA_ENTREGA, Personas.presentation.Despacho.TableModel.ESTADO};
                tableRecetas.setModel(new TableModel(cols, model.getList()));
                tableRecetas.setRowHeight(30);
                TableColumnModel columnModel = tableRecetas.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                columnModel.getColumn(1).setPreferredWidth(200);
                break;

            case Personas.presentation.Despacho.Model.CURRENT:
                Receta current = model.getCurrent();
                if(current != null && current.getPaciente() != null && current.getPaciente().getId() != null){
                    textFieldID.setText(current.getPaciente().getId());
                }

                // habilitar/deshabilitar según modo
                textFieldID.setEnabled(model.getMode() != Application.MODE_EDIT);

                resetField(textFieldID);
                break;

            case Personas.presentation.Despacho.Model.FILTER:
                if (model.getFilter() == null || model.getFilter().getPaciente() == null || model.getFilter().getPaciente().getId() == null)
                    textFieldID.setText("");
                else
                    textFieldID.setText(model.getFilter().getPaciente().getId());
                break;
        }

        this.panel1.revalidate();
    }


    private boolean validateFields() {
        boolean valid = true;
        if (textFieldID.getText().isEmpty()) {
            valid = false;
            textFieldID.setBackground(Application.BACKGROUND_ERROR);
            textFieldID.setToolTipText("ID requerido");
        } else resetField(textFieldID);

        return valid;
    }

    private void resetField(JTextField field) {
        field.setBackground(null);
        field.setToolTipText(null);
    }
}
