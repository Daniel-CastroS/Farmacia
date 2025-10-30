package Personas.Presentation.Despacho.Detalle;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.Objects;

import Personas.Logic.Receta;
import Personas.Presentation.Despacho.Controller;
import com.github.lgooddatepicker.components.DatePicker;

public class Editar extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBox1;
    private DatePicker nuevaFecha;
    private Controller controller;
    private Receta receta;
    private boolean entregada = false;

    public Editar(Controller controller, Receta receta) {
        this.controller = controller;
        this.receta = receta;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        System.out.println("Estado actual: " + receta.getEstado());

        if (Objects.equals(receta.getEstado(), "Entregada")) {
            entregada = true;
        }

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        try {
            if(entregada) {
                JOptionPane.showMessageDialog(this, "La receta ya ha sido entregada y no puede ser modificada", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String estado = comboBox1.getSelectedItem().toString();
            LocalDate fechaRetiroNueva = nuevaFecha.getDate();

            receta.setEstado(estado);
            receta.setFechaRetiro(fechaRetiroNueva);
            controller.modificarReceta(receta);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad y duración deben ser números", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
