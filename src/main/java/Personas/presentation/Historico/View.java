package Personas.presentation.Historico;

import Personas.Application;
import Personas.presentation.Historico.Controller;
import Personas.presentation.Historico.Model;
import Personas.presentation.Historico.TableModel;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View implements PropertyChangeListener {
    private JTable table1;
    private JPanel panel1;

    Controller controller;
    Model model;


    public View() {
        panel1 = new JPanel(new BorderLayout());
        table1 = new JTable();
        panel1.add(new JScrollPane(table1), BorderLayout.CENTER);
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
            case Personas.presentation.Historico.Model.LIST:
                int[] cols = {TableModel.ID_PACIENTE, TableModel.NOMBRE_PACIENTE, TableModel.FECHA_RETIRO, TableModel.CANTIDAD_MEDICINAS, TableModel.ESTADO};
                table1.setModel(new TableModel(cols, model.getList()));
                table1.setRowHeight(30);
                TableColumnModel columnModel = table1.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                columnModel.getColumn(1).setPreferredWidth(200);
                break;

            case Personas.presentation.Farmaceuta.Model.CURRENT, Model.FILTER:
                break;
        }

        this.panel1.revalidate();
    }
}
