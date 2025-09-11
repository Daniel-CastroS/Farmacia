package Personas.presentation.Despacho;

import Personas.Application;
import Personas.logic.Paciente;
import Personas.logic.Receta;
import Personas.logic.Service;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

public class View implements PropertyChangeListener {
    private JTable tableRecetas;
    private JButton buscarButton;
    private JButton entregarButton;
    private JTextField textFieldID;
    private JPanel panel1;

    Controller controller;
    Model model;

    public View() {
        // BOTÓN BUSCAR
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Receta filter = new Receta();
                    Paciente aux = new Paciente();
                    aux.setId(textFieldID.getText());
                    filter.setPaciente(aux);
                    controller.search(filter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        entregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    entregarReceta();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
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
                int[] cols = {TableModel.ID_PACIENTE, TableModel.FECHA_ENTREGA};
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

            case Model.FILTER:
                if (model.getFilter() == null || model.getFilter().getPaciente() == null || model.getFilter().getPaciente().getId() == null)
                    textFieldID.setText("");
                else
                    textFieldID.setText(model.getFilter().getPaciente().getId());
                break;
        }

        this.panel1.revalidate();
    }

    private Receta take() {
        Receta f = new Receta();
        Paciente p = new Paciente();
        p.setId(textFieldID.getText());
        f.setPaciente(p);
        return f;
    }

    private void entregarReceta(){
        LocalDate fecha = LocalDate.now();
        if(model.getCurrent() != null && !model.getCurrent().getEstado().equals("entregada")){
            if (model.getCurrent().getFechaRetiro().equals(fecha)) {
                model.getCurrent().setEstado("entregada");
            }
        }
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