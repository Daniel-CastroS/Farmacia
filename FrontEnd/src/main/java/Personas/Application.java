package Personas;

import Personas.Logic.Medico;
import Personas.Logic.Farmaceuta;
import Personas.Logic.Receta;
import Personas.Logic.Service;
import Personas.Presentation.Medico.Controller;
import Personas.Presentation.Medico.Model;
import Personas.Presentation.Medico.View;
import Personas.Presentation.Sesion.Sesion;

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
        Personas.Presentation.Sesion.Model loginModel = new Personas.Presentation.Sesion.Model();
        Personas.Presentation.Sesion.View loginView = new Personas.Presentation.Sesion.View();
        Personas.Presentation.Sesion.Controller loginController = new Personas.Presentation.Sesion.Controller(loginView, loginModel);
        loginView.setIconImage((new ImageIcon(Application.class.getResource("/forms.png")).getImage()));
        loginView.setTitle("Login");
        loginView.setSize(600, 600);
        loginView.pack();
        loginView.setLocationRelativeTo(null);
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

            Personas.Presentation.Farmaceuta.View farmView = new Personas.Presentation.Farmaceuta.View();
            Personas.Presentation.Farmaceuta.Model farmModel = new Personas.Presentation.Farmaceuta.Model();
            Personas.Presentation.Farmaceuta.Controller farmController = new Personas.Presentation.Farmaceuta.Controller(farmView, farmModel);

            Personas.Presentation.Paciente.View pacienteView = new Personas.Presentation.Paciente.View();
            Personas.Presentation.Paciente.Model pacienteModel = new Personas.Presentation.Paciente.Model();
            Personas.Presentation.Paciente.Controller pacienteController = new Personas.Presentation.Paciente.Controller(pacienteView, pacienteModel);

            Personas.Presentation.Medicamentos.View medicamentosView = new Personas.Presentation.Medicamentos.View();
            Personas.Presentation.Medicamentos.Model medicamentosModel = new Personas.Presentation.Medicamentos.Model();
            Personas.Presentation.Medicamentos.Controller medicamentosController = new Personas.Presentation.Medicamentos.Controller(medicamentosView, medicamentosModel);

            Personas.Presentation.ManejoRecetas.View manejoRecetasView = new Personas.Presentation.ManejoRecetas.View();
            Personas.Presentation.ManejoRecetas.Model manejoRecetasModel = new Personas.Presentation.ManejoRecetas.Model();
            Personas.Presentation.ManejoRecetas.Controller manejoRecetasController = new Personas.Presentation.ManejoRecetas.Controller(manejoRecetasView, manejoRecetasModel);

            tabs.addTab("Médicos", new ImageIcon(Application.class.getResource("/doc.png")), medicoView.getPanel());
            tabs.addTab("Farmacéutas", new ImageIcon(Application.class.getResource("/farma.png")), farmView.getPanel());
            tabs.addTab("Pacientes", new ImageIcon(Application.class.getResource("/cough.png")), pacienteView.getPanel());
            tabs.addTab("Medicamentos", new ImageIcon(Application.class.getResource("/meds.png")), medicamentosView.getPanel());
            tabs.addTab("Manejo de Recetas", new ImageIcon(Application.class.getResource("/set.png")), manejoRecetasView.getPanel());
        } else if (Sesion.getUserLogged() instanceof Medico) {
            window.setTitle("Prescripciones");
            window.setIconImage((new ImageIcon(Application.class.getResource("/note.png")).getImage()));

            Personas.Presentation.Prescripcion.View prescripcionView = new Personas.Presentation.Prescripcion.View();
            Personas.Presentation.Prescripcion.Model prescripcionModel = new Personas.Presentation.Prescripcion.Model();
            Personas.Presentation.Prescripcion.Controller prescripcionController = new Personas.Presentation.Prescripcion.Controller(prescripcionView, prescripcionModel);

            tabs.addTab("Prescribir", new ImageIcon(Application.class.getResource("/pills.png")), prescripcionView.getPanel());
        } else if (Sesion.getUserLogged() instanceof Farmaceuta) {
            window.setTitle("Despacho");
            window.setIconImage((new ImageIcon(Application.class.getResource("/pack.png")).getImage()));

            Personas.Presentation.Despacho.View despachoView = new Personas.Presentation.Despacho.View();
            Personas.Presentation.Despacho.Model despachoModel = new Personas.Presentation.Despacho.Model();
            Personas.Presentation.Despacho.Controller despachoController = new Personas.Presentation.Despacho.Controller(despachoView, despachoModel);

            tabs.addTab("Despacho", new ImageIcon(Application.class.getResource("/send.png")), despachoView.getPanel());
        }

        // Dashboard (común para todos los roles)
        List<Receta> recetas = Personas.Logic.Service.instance().readAllRecetas();
        Personas.Presentation.Dashboard.Model dashboardModel = new Personas.Presentation.Dashboard.Model(recetas);
        Personas.Presentation.Dashboard.Controller dashboardController = new Personas.Presentation.Dashboard.Controller(dashboardModel, null); // Controller temporal
        Personas.Presentation.Dashboard.View dashboardView = new Personas.Presentation.Dashboard.View(dashboardModel, dashboardController);
        dashboardController.setView(dashboardView); // Asigna el View al Controller
        tabs.addTab("Dashboard", new ImageIcon(Application.class.getResource("/stats.png")), dashboardView.getPanel());

        // Histórico
        Personas.Presentation.Historico.View historicoView = new Personas.Presentation.Historico.View();
        Personas.Presentation.Historico.Model historicoModel = new Personas.Presentation.Historico.Model();
        Personas.Presentation.Historico.Controller historicoController = new Personas.Presentation.Historico.Controller(historicoView, historicoModel);
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