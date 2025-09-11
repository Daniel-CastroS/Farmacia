package Personas.presentation.Sesion;

import Personas.Application;
import Personas.logic.Service;
import Personas.logic.Trabajador;
import Personas.presentation.Sesion.Controller;
import Personas.presentation.Sesion.Model;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;

public class View extends JDialog implements PropertyChangeListener {
    private JPanel contentPane;
    private JButton xButton;
    private JButton loginButton;
    private JTextField textFieldId;
    private JTextField textFieldClave;
    private JLabel clave;
    private JLabel id;
    private JButton extraButton;

    Controller controller;
    Model model;

    public View() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(loginButton);

        xButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onX();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onLoggin();
            }
        });

        extraButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {onExtra();}
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onX();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onX();
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

    private void onLoggin() {
        Trabajador trabajador = new Trabajador();
        trabajador.setId(textFieldId.getText());
        try{
            if(Service.instance().read(trabajador) == null){
                JOptionPane.showMessageDialog(this, "Usuario no existe");
            } else {
                trabajador = Service.instance().read(trabajador);
                if(trabajador.getClave_sistema().equals(textFieldClave.getText())){
                    controller.login(trabajador);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Clave incorrecta");
                }
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(this, "Usuario no existe");
        }
    }

    private void onX() {
        dispose();
    }

    private void resetField(JTextField field) {
        field.setBackground(null);
        field.setToolTipText(null);
    }

    private void onExtra() {
        // Acción para cambiar la contraseña, si la contraseña es igual al ID se procede al cambio de una, sino se pide la clave actual
    }

    public static void main(String[] args) {
        View dialog = new View();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
