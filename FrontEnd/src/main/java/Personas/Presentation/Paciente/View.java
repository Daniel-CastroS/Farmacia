package Personas.Presentation.Paciente;

import Personas.Application;
import Personas.Logic.PDFUtil;
import Personas.Logic.Paciente;
import Personas.Logic.Service;
import Personas.Presentation.Paciente.Controller;
import Personas.Presentation.Paciente.Model;
import Personas.Presentation.Paciente.TableModel;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import com.github.lgooddatepicker.components.DatePicker;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

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
    private JPanel panel1;
    private JPanel panelPrincipal;

    Controller controller;
    Model model;

    public View() {
        $$$setupUI$$$();
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Paciente filter = new Paciente();
                    filter.setId(searchTextfield.getText());
                    filter.setName(searchTextfield.getText());
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
                    Paciente m = take();
                    try {
                        controller.save(m);
                        resetField(idTextField);
                        resetField(nameTextField);
                        resetField(telephonTextField);
                        birthDatePicker.setDate(null);
                        JOptionPane.showMessageDialog(panel1, "Paciente registrado", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(panel1, "Por favor, rellene todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(panel1, "Paciente eliminado", "Info", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        reporteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PDFUtil.PacientetoPDF(model.getCurrent(), Service.instance().getPathPacientes());
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
                int[] cols = {TableModel.ID, TableModel.NOMBRE, TableModel.TELEFONO, TableModel.FECHANACIMIENTO};
                table1.setModel(new TableModel(cols, model.getList()));
                table1.setRowHeight(30);
                TableColumnModel columnModel = table1.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                columnModel.getColumn(1).setPreferredWidth(150);
                columnModel.getColumn(2).setPreferredWidth(200);
                columnModel.getColumn(3).setPreferredWidth(150);
                break;

            case Model.CURRENT:
                Paciente current = model.getCurrent();
                idTextField.setText(current.getId());
                nameTextField.setText(current.getName());
                telephonTextField.setText(current.getTelefono());
                birthDatePicker.setDate(current.getFechaNac());

                // habilitar/deshabilitar según modo
                if (model.getMode() == Application.MODE_EDIT) {
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
        if (this.panel1 != null) {
            this.panel1.revalidate();
        }
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

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(null, "Paciente", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("Id:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Telefono:");
        panel2.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        idTextField = new JTextField();
        panel2.add(idTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        telephonTextField = new JTextField();
        panel2.add(telephonTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Nombre:");
        panel2.add(label3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nameTextField = new JTextField();
        panel2.add(nameTextField, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Fecha de Nacimiento:");
        panel2.add(label4, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        guardarButton = new JButton();
        guardarButton.setIcon(new ImageIcon(getClass().getResource("/save.png")));
        guardarButton.setText("Guardar");
        panel2.add(guardarButton, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        limpiarButton = new JButton();
        limpiarButton.setIcon(new ImageIcon(getClass().getResource("/clean.png")));
        limpiarButton.setText("Limpiar");
        panel2.add(limpiarButton, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        borrarButton = new JButton();
        borrarButton.setIcon(new ImageIcon(getClass().getResource("/bin.png")));
        borrarButton.setText("Borrar");
        panel2.add(borrarButton, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        birthDatePicker = new DatePicker();
        panel2.add(birthDatePicker, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1, true, true));
        panel1.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(null, "Búsqueda", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label5 = new JLabel();
        label5.setText("Nombre/ID: ");
        panel3.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        searchTextfield = new JTextField();
        panel3.add(searchTextfield, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        buscarButton = new JButton();
        buscarButton.setIcon(new ImageIcon(getClass().getResource("/search.png")));
        buscarButton.setText("Buscar");
        panel3.add(buscarButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reporteButton = new JButton();
        reporteButton.setIcon(new ImageIcon(getClass().getResource("/pdf.png")));
        reporteButton.setText("Reporte");
        panel3.add(reporteButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Listado", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        table1 = new JTable();
        scrollPane1.setViewportView(table1);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}