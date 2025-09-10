package Personas.presentation.prescripcion.paciente;

import Personas.logic.Paciente;
import Personas.logic.Service;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class View extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tablePacientes;
    private JTextField textFieldFiltro;
    private JButton buttonFiltrar;



    private Paciente seleccionado; // Paciente elegido
    private List<Paciente> pacientes; // Lista de pacientes a mostrar
    private List<Paciente> pacientesFiltrados; // Lista que se muestra en la tabla


    public View(List<Paciente> pacientes) {
        this.pacientes = pacientes;
        this.pacientesFiltrados = pacientes; // inicialmente toda la lista


        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        initTable(); // Inicializa la tabla con los pacientes

        // Botón OK
        buttonOK.addActionListener(e -> {
            int row = tablePacientes.getSelectedRow();
            if (row != -1) {
                seleccionado = pacientesFiltrados.get(row); // usar la lista filtrada
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un paciente");
            }
        });


        // Botón Cancel
        buttonCancel.addActionListener(e -> dispose());

        buttonFiltrar.addActionListener(e -> filtrarPorId());

        // Cerrar con X
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dispose(); }
        });

        // Cerrar con ESC
        contentPane.registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    // Inicializa la tabla con la lista de pacientes
    private void initTable() {
        int[] cols = {TableModelPacientes.ID, TableModelPacientes.NOMBRE};
        tablePacientes.setModel(new TableModelPacientes(cols, pacientes));
        tablePacientes.setRowHeight(25);
    }


    // Devuelve el paciente seleccionado
    public Paciente getSeleccionado() {
        return seleccionado;
    }



    private void filtrarPorId() {
        String filtro = textFieldFiltro.getText().trim();
        if (filtro.isEmpty()) {
            pacientesFiltrados = pacientes; // mostrar todos
        } else {
            pacientesFiltrados = pacientes.stream()
                    .filter(p -> p.getId().equals(filtro))
                    .collect(Collectors.toList());
        }
        int[] cols = {TableModelPacientes.ID, TableModelPacientes.NOMBRE};
        tablePacientes.setModel(new TableModelPacientes(cols, pacientesFiltrados));
    }









}
