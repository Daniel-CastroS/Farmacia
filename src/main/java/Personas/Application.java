package Personas;

import Personas.logic.Medico;
import Personas.logic.Farmaceuta;
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
        loginView.setIconImage((new ImageIcon(Application.class.getResource("/forms.png")).getImage()));
        loginView.setTitle("Login");
        loginView.setSize(600,600);
        loginView.pack();
        loginView.setLocationRelativeTo(null);
        Personas.presentation.Sesion.Model loginModel = new Personas.presentation.Sesion.Model();
        Personas.presentation.Sesion.Controller loginController = new Personas.presentation.Sesion.Controller(loginView, loginModel);
        loginView.setVisible(true);
    }

    private static void doRun(){
        JFrame window = new JFrame();
        window.setSize(1200, 800);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // ---PESTAÑAS
        JTabbedPane tabs = new JTabbedPane();


        if (Sesion.getUserLogged().getRol().equals("Admin")) {
            window.setTitle("Gestion");
            window.setIconImage((new ImageIcon(Application.class.getResource("/setting.png")).getImage()));

            //TODO ESTO ES PARA ADMINISTRADOR

            //ESTO ES DE MEDICO
            View medicoView = new View();
            Model medicoModel = new Model();
            Controller medicoController = new Controller(medicoView, medicoModel);

            //ESTO ES DE FARMACEUTA, sin embargo no puedo hacer la implementacion tan directa porque por algun no motivo no me deja hacer el puto import de Farmaceutas
            Personas.presentation.Farmaceuta.View farmView = new Personas.presentation.Farmaceuta.View();
            Personas.presentation.Farmaceuta.Model farmModel = new Personas.presentation.Farmaceuta.Model();
            Personas.presentation.Farmaceuta.Controller farmController = new Personas.presentation.Farmaceuta.Controller(farmView, farmModel);


            //ESTO ES DE PACIENTE
            Personas.presentation.Paciente.View pacienteView = new Personas.presentation.Paciente.View();
            Personas.presentation.Paciente.Model pacienteModel = new Personas.presentation.Paciente.Model();
            Personas.presentation.Paciente.Controller pacienteController = new Personas.presentation.Paciente.Controller(pacienteView, pacienteModel);

            //ESTO ES DE MEDICAMENTOS
            Personas.presentation.Medicamentos.View medicamentosView = new Personas.presentation.Medicamentos.View();
            Personas.presentation.Medicamentos.Model medicamentosModel = new Personas.presentation.Medicamentos.Model();
            Personas.presentation.Medicamentos.Controller medicamentosController = new Personas.presentation.Medicamentos.Controller(medicamentosView,medicamentosModel);

            // PESTAÑAS
            tabs.addTab("Médicos", new ImageIcon(Application.class.getResource("/doc.png")) ,medicoView.getPanel());
            tabs.addTab("Farmaceutas", new ImageIcon(Application.class.getResource("/farma.png")) ,farmView.getPanel());
            tabs.addTab("Pacientes", new ImageIcon(Application.class.getResource("/cough.png")) ,pacienteView.getPanel());
            tabs.addTab("Medicamentos", new ImageIcon(Application.class.getResource("/meds.png")) ,medicamentosView.getPanel());
        } else if(Sesion.getUserLogged() instanceof Medico){
            window.setTitle("Prescripciones");
            window.setIconImage((new ImageIcon(Application.class.getResource("/note.png")).getImage()));

            //ESTO ES DE PRESCRIPCION
            Personas.presentation.prescripcion.View prescripcionView = new Personas.presentation.prescripcion.View();
            Personas.presentation.prescripcion.Model prescripcionModel = new Personas.presentation.prescripcion.Model();
            Personas.presentation.prescripcion.Controller prescripcionController = new Personas.presentation.prescripcion.Controller(prescripcionView, prescripcionModel);

            // PESTAÑAS
            tabs.addTab("Prescribir", new ImageIcon(Application.class.getResource("/pills.png")), prescripcionView.getPanel());
        } else if(Sesion.getUserLogged() instanceof Farmaceuta){
            window.setTitle("Despacho");
            window.setIconImage((new ImageIcon(Application.class.getResource("/pack.png")).getImage()));

            //ESTO ES DESPACHO
            Personas.presentation.Despacho.View despachoView = new Personas.presentation.Despacho.View();
            Personas.presentation.Despacho.Model despachoModel = new Personas.presentation.Despacho.Model();
            Personas.presentation.Despacho.Controller despachoController = new Personas.presentation.Despacho.Controller(despachoView, despachoModel);

            // PESTAÑAS
            tabs.addTab("Despacho", new ImageIcon(Application.class.getResource("/send.png")), despachoView.getPanel());
        }

        //ESTO ES DE DASHBOARD
        Personas.presentation.Dashboard.Model dashboardModel = new Personas.presentation.Dashboard.Model();
        Personas.presentation.Dashboard.View dashboardView = new Personas.presentation.Dashboard.View(dashboardModel);
        Personas.presentation.Dashboard.Controller dashboardController = new Personas.presentation.Dashboard.Controller(dashboardModel,dashboardView);

        // PESTAÑAS GLOBALES
        tabs.addTab("Dashboard", new ImageIcon(Application.class.getResource("/stats.png")) ,dashboardView);
        tabs.addTab("Acerca de...", new ImageIcon(Application.class.getResource("/pencil.png")), new JLabel(new ImageIcon(Application.class.getResource("/hospital.jpg"))), "View Image");

        window.setContentPane(tabs);
        window.setVisible(true);
    }

    public final static int MODE_CREATE=1;
    public final static int MODE_EDIT=2;

    public static final Color BACKGROUND_ERROR = new Color(255, 102, 102, 255);
}
