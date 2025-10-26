package Personas;

import Personas.logic.Medico;
import Personas.logic.Farmaceuta;
import Personas.logic.Receta;
import Personas.logic.Service;
import Personas.presentation.Medico.Controller;
import Personas.presentation.Medico.Model;
import Personas.presentation.Medico.View;
import Personas.presentation.Sesion.Sesion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        doLogin();
        if (Sesion.isLoggedIn()) {
            doRun();
        }
    }

    private static void doLogin() {
        Personas.presentation.Sesion.View loginView = new Personas.presentation.Sesion.View();
        loginView.setIconImage((new ImageIcon(Application.class.getResource("/forms.png")).getImage()));
        loginView.setTitle("Login");
        loginView.setSize(600, 600);
        loginView.pack();
        loginView.setLocationRelativeTo(null);
        Personas.presentation.Sesion.Model loginModel = new Personas.presentation.Sesion.Model();
        Personas.presentation.Sesion.Controller loginController = new Personas.presentation.Sesion.Controller(loginView, loginModel);
        loginView.setVisible(true);
    }

    private static void doRun() {
        JFrame window = new JFrame();
        window.setSize(1200, 800);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        // comprobar rol Admin de forma robusta
        String role = null;
        if (Sesion.getUserLogged() != null) role = Sesion.getUserLogged().getRol();

        // DEBUG: imprimir info del usuario logeado
        if (Sesion.getUserLogged() != null) {
            System.out.println("[DEBUG] Usuario logueado id='" + Sesion.getUserLogged().getId() + "' rol='" + Sesion.getUserLogged().getRol() + "' clase='" + Sesion.getUserLogged().getClass().getSimpleName() + "'");
            String r = Sesion.getUserLogged().getRol();
            System.out.println("[DEBUG] Rol raw: '" + r + "' (len=" + (r == null ? 0 : r.length()) + ")");
        }
        if (role != null && role.trim().equalsIgnoreCase("admin")) {
            window.setTitle("Gestión");
            window.setIconImage((new ImageIcon(Application.class.getResource("/setting.png")).getImage()));

            View medicoView = new View();
            Model medicoModel = new Model();
            Controller medicoController = new Controller(medicoView, medicoModel);

            Personas.presentation.Farmaceuta.View farmView = new Personas.presentation.Farmaceuta.View();
            Personas.presentation.Farmaceuta.Model farmModel = new Personas.presentation.Farmaceuta.Model();
            Personas.presentation.Farmaceuta.Controller farmController = new Personas.presentation.Farmaceuta.Controller(farmView, farmModel);

            Personas.presentation.Paciente.View pacienteView = new Personas.presentation.Paciente.View();
            Personas.presentation.Paciente.Model pacienteModel = new Personas.presentation.Paciente.Model();
            Personas.presentation.Paciente.Controller pacienteController = new Personas.presentation.Paciente.Controller(pacienteView, pacienteModel);

            Personas.presentation.Medicamentos.View medicamentosView = new Personas.presentation.Medicamentos.View();
            Personas.presentation.Medicamentos.Model medicamentosModel = new Personas.presentation.Medicamentos.Model();
            Personas.presentation.Medicamentos.Controller medicamentosController = new Personas.presentation.Medicamentos.Controller(medicamentosView, medicamentosModel);

            Personas.presentation.ManejoRecetas.View manejoRecetasView = new Personas.presentation.ManejoRecetas.View();
            Personas.presentation.ManejoRecetas.Model manejoRecetasModel = new Personas.presentation.ManejoRecetas.Model();
            Personas.presentation.ManejoRecetas.Controller manejoRecetasController = new Personas.presentation.ManejoRecetas.Controller(manejoRecetasView, manejoRecetasModel);

            tabs.addTab("Médicos", new ImageIcon(Application.class.getResource("/doc.png")), medicoView.getPanel());
            tabs.addTab("Farmacéutas", new ImageIcon(Application.class.getResource("/farma.png")), farmView.getPanel());
            tabs.addTab("Pacientes", new ImageIcon(Application.class.getResource("/cough.png")), pacienteView.getPanel());
            tabs.addTab("Medicamentos", new ImageIcon(Application.class.getResource("/meds.png")), medicamentosView.getPanel());
            tabs.addTab("Manejo de Recetas", new ImageIcon(Application.class.getResource("/set.png")), manejoRecetasView.getPanel());
        } else if (Sesion.getUserLogged() instanceof Medico) {
            window.setTitle("Prescripciones");
            window.setIconImage((new ImageIcon(Application.class.getResource("/note.png")).getImage()));

            Personas.presentation.prescripcion.View prescripcionView = new Personas.presentation.prescripcion.View();
            Personas.presentation.prescripcion.Model prescripcionModel = new Personas.presentation.prescripcion.Model();
            Personas.presentation.prescripcion.Controller prescripcionController = new Personas.presentation.prescripcion.Controller(prescripcionView, prescripcionModel);

            tabs.addTab("Prescribir", new ImageIcon(Application.class.getResource("/pills.png")), prescripcionView.getPanel());
        } else if (Sesion.getUserLogged() instanceof Farmaceuta) {
            window.setTitle("Despacho");
            window.setIconImage((new ImageIcon(Application.class.getResource("/pack.png")).getImage()));

            Personas.presentation.Despacho.View despachoView = new Personas.presentation.Despacho.View();
            Personas.presentation.Despacho.Model despachoModel = new Personas.presentation.Despacho.Model();
            Personas.presentation.Despacho.Controller despachoController = new Personas.presentation.Despacho.Controller(despachoView, despachoModel);

            tabs.addTab("Despacho", new ImageIcon(Application.class.getResource("/send.png")), despachoView.getPanel());
        }

        // Dashboard (común para todos los roles)
        List<Receta> recetas = Personas.logic.Service.instance().readAllRecetas();
        Personas.presentation.Dashboard.Model dashboardModel = new Personas.presentation.Dashboard.Model(recetas);
        Personas.presentation.Dashboard.Controller dashboardController = new Personas.presentation.Dashboard.Controller(dashboardModel, null); // Controller temporal
        Personas.presentation.Dashboard.View dashboardView = new Personas.presentation.Dashboard.View(dashboardModel, dashboardController);
        dashboardController.setView(dashboardView); // Asigna el View al Controller
        tabs.addTab("Dashboard", new ImageIcon(Application.class.getResource("/stats.png")), dashboardView.getPanel());

        // Histórico
        Personas.presentation.Historico.View historicoView = new Personas.presentation.Historico.View();
        Personas.presentation.Historico.Model historicoModel = new Personas.presentation.Historico.Model();
        Personas.presentation.Historico.Controller historicoController = new Personas.presentation.Historico.Controller(historicoView, historicoModel);
        tabs.addTab("Histórico", new ImageIcon(Application.class.getResource("/med.png")), historicoView.getPanel());

        // Acerca de
        tabs.addTab("Acerca de...", new ImageIcon(Application.class.getResource("/pencil.png")), new JLabel(new ImageIcon(Application.class.getResource("/hospital.jpg"))), "View Image");

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Service.instance().stop();
            }
        });

        window.setContentPane(tabs);
        window.setVisible(true);
    }

    public final static int MODE_CREATE = 1;
    public final static int MODE_EDIT = 2;

    public static final Color BACKGROUND_ERROR = new Color(255, 102, 102, 255);
}