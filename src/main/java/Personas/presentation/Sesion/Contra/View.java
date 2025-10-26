package Personas.presentation.Sesion.Contra;

import Personas.logic.Service;
import Personas.presentation.Sesion.Controller;
import Personas.presentation.Sesion.Model;
import Personas.presentation.Sesion.Sesion;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;

public class View extends JDialog implements PropertyChangeListener {
    private JPasswordField passwordFieldAntigua;
    private JPasswordField passwordFieldNueva;
    private JPasswordField passwordFieldNueva2;
    private JPanel panel1;
    private JButton guardarButton;
    private JTextField textFieldID;

    Controller controller;
    Model model;

    public View() {
        setContentPane(panel1);
        setModal(true);
        getRootPane().setDefaultButton(guardarButton);

        guardarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(validateSpots()){
                    try {
                        cambio();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(View.this, "Por favor, rellene todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // call onCancel() on ESCAPE
        panel1.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    @Override
    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        // Aquí puedes manejar los cambios en el modelo si es necesario
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void cambio() throws Exception {
        // Lógica para guardar la nueva contraseña
        String id = textFieldID.getText();
        String antigua = new String(passwordFieldAntigua.getPassword());
        String nueva = new String(passwordFieldNueva.getPassword());
        String nueva2 = new String(passwordFieldNueva2.getPassword());
        Personas.logic.Trabajador trabajador = new Personas.logic.Trabajador();
        trabajador.setId(id);
        if(Service.instance().readTrabajador(trabajador) == null){
            throw new Exception("Trabajador no encontrado");
        } else {
            trabajador = Service.instance().readTrabajador(trabajador);
            if (!trabajador.getClave_sistema().equals(antigua)) {
                JOptionPane.showMessageDialog(View.this, "La contraseña antigua es incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
                resetField(passwordFieldAntigua);
                resetField(passwordFieldNueva);
                resetField(passwordFieldNueva2);
                return;
            }
            if (!nueva.equals(nueva2)) {
                JOptionPane.showMessageDialog(View.this, "Las nuevas contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
                resetField(passwordFieldAntigua);
                resetField(passwordFieldNueva);
                resetField(passwordFieldNueva2);
                return;
            }
            try {
                trabajador.setClave_sistema(nueva2);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(View.this, "Contraseña cambiada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    private void resetField(JTextField field) {
        field.setBackground(null);
        field.setToolTipText(null);
    }

    private boolean validateSpots(){
        boolean valido = true;
        if(passwordFieldAntigua.getText().isEmpty()){
            passwordFieldAntigua.setBackground(java.awt.Color.PINK);
            passwordFieldAntigua.setToolTipText("Este campo no puede estar vacío");
            valido = false;
        }
        if(passwordFieldNueva.getText().isEmpty()){
            passwordFieldNueva.setBackground(java.awt.Color.PINK);
            passwordFieldNueva.setToolTipText("Este campo no puede estar vacío");
            valido = false;
        }
        if(passwordFieldNueva2.getText().isEmpty()){
            passwordFieldNueva2.setBackground(java.awt.Color.PINK);
            passwordFieldNueva2.setToolTipText("Este campo no puede estar vacío");
            valido = false;
        }
        return valido;
    }
}
