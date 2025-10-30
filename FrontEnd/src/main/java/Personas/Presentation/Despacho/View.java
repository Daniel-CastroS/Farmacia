package Personas.Presentation.Despacho;

import Personas.Application;
import Personas.Logic.Paciente;
import Personas.Logic.Receta;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class View implements PropertyChangeListener {
    private JTable tableRecetas;
    private JButton buscarButton;
    private JTextField textFieldID;
    private JPanel panel1;
    private JButton resetButton;

    Controller controller;
    Model model;

    public View() {
        $$$setupUI$$$();
        // BOTÓN BUSCAR
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
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.getAllRecetas(); // This loads all recetas
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
                resetField(textFieldID);
            }
        });
        tableRecetas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableRecetas.getSelectedRow();
                if (row != -1) {
                    try {
                        controller.edit(row);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
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
            case Model.LIST:
                int[] cols = {TableModel.ID_PACIENTE, TableModel.NOMBRE_PACIENTE, TableModel.FECHA_ENTREGA, TableModel.ESTADO};
                tableRecetas.setModel(new TableModel(cols, model.getList()));
                tableRecetas.setRowHeight(30);
                TableColumnModel columnModel = tableRecetas.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                columnModel.getColumn(1).setPreferredWidth(200);
                break;

            case Model.CURRENT:
                Receta current = model.getCurrent();
                if (current != null && current.getPaciente() != null && current.getPaciente().getId() != null) {
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

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(null, "Info", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("Busqueda por Id:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldID = new JTextField();
        panel2.add(textFieldID, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        buscarButton = new JButton();
        buscarButton.setIcon(new ImageIcon(getClass().getResource("/search.png")));
        buscarButton.setText("Buscar");
        panel2.add(buscarButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        resetButton = new JButton();
        resetButton.setIcon(new ImageIcon(getClass().getResource("/reset.png")));
        resetButton.setText("Reset");
        panel2.add(resetButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Listado", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        tableRecetas = new JTable();
        scrollPane1.setViewportView(tableRecetas);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
};