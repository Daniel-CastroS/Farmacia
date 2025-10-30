package Personas.Presentation.Sesion;

import Personas.Application;
import Personas.Logic.Paciente;
import Personas.Logic.Persona;
import Personas.Logic.Trabajador;
import Personas.Logic.Service;

public class Controller {
    View view;
    Model model;

    public Controller(View view, Model model) {
        model.setCurrent(new Trabajador());
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void login(Trabajador t1) throws Exception {
        Trabajador user = Service.instance().readTrabajador(t1);
        if (!user.getClave_sistema().equals(t1.getClave_sistema())) {
            throw new Exception("Clave incorrecta");
        }
        Sesion.setUserLogged(user);
    }

    public void clear() {
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Trabajador());
    }
}
