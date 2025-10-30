package Personas.Presentation.Prescripcion.Detalle;

import Personas.Logic.Medicamento;
import Personas.Logic.MedicamentoRecetado;
import Personas.Logic.Receta;
import Personas.Presentation.Prescripcion.Controller;

import javax.naming.ldap.Control;
import javax.swing.*;
import java.awt.event.*;

public class Detalle extends JDialog {
    private JPanel contentPane;
    private JButton buttonAgregar;
    private JButton buttonCancelar;
    private JTextField textCantidad;
    private JTextField textDuracion;
    private JTextField textIndicaciones;

    private Controller controller;
    private Receta receta;
    private Medicamento medicamento;

    public Detalle(Controller controller, Receta receta, Medicamento medicamento, int pos) {
        this.controller = controller;
        this.receta = receta;
        this.medicamento = medicamento;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonAgregar);

        buttonAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(pos != -1){
                    modi(pos);
                }else {
                    onAgregar();
                }
            }
        });

        buttonCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onAgregar() {
        try {
            int cantidad = Integer.parseInt(textCantidad.getText());
            int duracion = Integer.parseInt(textDuracion.getText());
            String indicaciones = textIndicaciones.getText();

            MedicamentoRecetado mr = new MedicamentoRecetado(medicamento, cantidad, indicaciones, duracion);
            controller.agregarMedicamento(mr);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad y duración deben ser números", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modi(int pos){
        try {
            int cantidad = Integer.parseInt(textCantidad.getText());
            int duracion = Integer.parseInt(textDuracion.getText());
            String indicaciones = textIndicaciones.getText();

            MedicamentoRecetado mr = new MedicamentoRecetado(medicamento, cantidad, indicaciones, duracion);
            controller.modificarMedicamento(mr, pos);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad y duración deben ser números", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }

    public void mostrar() {
        pack();
        setVisible(true);
    }
}