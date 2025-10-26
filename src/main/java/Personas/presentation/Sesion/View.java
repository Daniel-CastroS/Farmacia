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

    private void onLoggin()  {
        Trabajador trabajador = new Trabajador();
        trabajador.setId(textFieldId.getText());

        try {
            // Buscamos al trabajador en la base de datos
            trabajador = Service.instance().readTrabajador(trabajador);

            if(trabajador == null){
                JOptionPane.showMessageDialog(this, "Usuario no existe");
            } else if(trabajador.getClave_sistema().equals(textFieldClave.getText())){
                // Si la contraseña es correcta, se hace login
                controller.login(trabajador);
                dispose(); // cerramos ventana de login
            } else {
                JOptionPane.showMessageDialog(this, "Clave incorrecta");
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(this, "Usuario no existe");
        }
    }




    /*
private void onLoggin() {
    String idIngresado = textFieldId.getText().trim();
    String claveIngresada = textFieldClave.getText().trim();

    try {
        if ("1".equals(idIngresado)) {  // ID especial para admin
            Trabajador admin = new Trabajador();
            admin.setId("1");
            admin.setName("Administrador");
            admin.setRol("admin");
            admin.setGafete("1");
            admin.setClave_sistema("1");

            Sesion.setUserLogged(admin);
            dispose(); // cerrar ventana de login
        } else {
            Trabajador trabajador = new Trabajador();
            trabajador.setId(idIngresado);

            Trabajador usuarioBD = Service.instance().readTrabajador(trabajador);
            if (usuarioBD == null) {
                JOptionPane.showMessageDialog(this, "Usuario no existe");
                return;
            }

            if (usuarioBD.getClave_sistema().equals(claveIngresada)) {
                controller.login(usuarioBD);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Clave incorrecta");
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Usuario no existe");
    }
}


     */
    private void onX() {
        dispose();
    }

    private void resetField(JTextField field) {
        field.setBackground(null);
        field.setToolTipText(null);
    }

    private void onExtra() {
        Personas.presentation.Sesion.Contra.View contraView = new Personas.presentation.Sesion.Contra.View();
        contraView.setIconImage((new ImageIcon(Application.class.getResource("/forms.png")).getImage()));
        contraView.setTitle("Cambiar Contraseña");
        contraView.setSize(400,300);
        contraView.pack();
        contraView.setVisible(true);
    }
}
