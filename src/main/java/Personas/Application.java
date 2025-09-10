package Personas;

import Personas.logic.Persona;
import Personas.presentation.Medico.Controller;
import Personas.presentation.Medico.Model;
import Personas.presentation.Medico.View;
import Personas.presentation.Sesion.Sesion;


import javax.swing.*;
import java.awt.*;

public class Application {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        doLogin();
        if(Sesion.isLoggedIn()){
            doRun();
        }

    }

    private static void doLogin(){
        Personas.presentation.Sesion.View loginView = new Personas.presentation.Sesion.View();
        //loginView.setIconImage((new ImageIcon(Application.class.getResource("/images/login.png"))).getImage());
        loginView.setTitle("Login");
        loginView.pack();
        loginView.setLocationRelativeTo(null);
        Personas.presentation.Sesion.Model loginModel = new Personas.presentation.Sesion.Model();
        Personas.presentation.Sesion.Controller loginController = new Personas.presentation.Sesion.Controller(loginView, loginModel);
        loginView.setVisible(true);
    }

    private static void doRun(){
        //ESTO ES DE MEDICO
        View medicoView = new View();
        Model medicoModel = new Model();
        Controller medicoController = new Controller(medicoView, medicoModel);

        //ESTO ES DE FARMACEUTA, sin embargo no puedo hacer la implementacion tan directa porque por algun no motivo no me deja hacer el puto import de Farmaceutas
        Personas.presentation.Farmaceuta.View farmView = new Personas.presentation.Farmaceuta.View();
        Personas.presentation.Farmaceuta.Model farmModel = new Personas.presentation.Farmaceuta.Model();
        Personas.presentation.Farmaceuta.Controller farmController = new Personas.presentation.Farmaceuta.Controller(farmView, farmModel);


        JFrame window = new JFrame("Gestión de Personal");
        window.setSize(800, 600);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //ESTO ES DE PACIENTE
        Personas.presentation.Paciente.View pacienteView = new Personas.presentation.Paciente.View();
        Personas.presentation.Paciente.Model pacienteModel = new Personas.presentation.Paciente.Model();
        Personas.presentation.Paciente.Controller pacienteController = new Personas.presentation.Paciente.Controller(pacienteView, pacienteModel);


        //
        Personas.presentation.Medicamentos.View medicamentosView = new Personas.presentation.Medicamentos.View();
        Personas.presentation.Medicamentos.Model medicamentosModel = new Personas.presentation.Medicamentos.Model();
        Personas.presentation.Medicamentos.Controller medicamentosController = new Personas.presentation.Medicamentos.Controller(medicamentosView,medicamentosModel);

/// ///////////
        Personas.presentation.prescripcion.View prescripcionView = new Personas.presentation.prescripcion.View();
        Personas.presentation.prescripcion.Model prescripcionModel = new Personas.presentation.prescripcion.Model();
        Personas.presentation.prescripcion.Controller prescripcionController = new Personas.presentation.prescripcion.Controller(prescripcionView, prescripcionModel);

        //
        JFrame windowPaciente = new JFrame("Gestión de Pacientes");
        windowPaciente.setSize(800, 600);
        windowPaciente.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);



        // ---PESTANAS
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Médicos", medicoView.getPanel());
        tabs.addTab("Farmaceutas", farmView.getPanel());
        tabs.addTab("Pacientes", pacienteView.getPanel());
        tabs.addTab("Medicamentos", medicamentosView.getPanel());
        tabs.addTab("Prescribir", prescripcionView.getPanel());
        window.setContentPane(tabs);
        window.setVisible(true);
    }

    public final static int MODE_CREATE=1;
    public final static int MODE_EDIT=2;

    public static final Color BACKGROUND_ERROR = new Color(255, 102, 102, 255);
}
