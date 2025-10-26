package Personas.presentation.Sesion;

import Personas.Application;
import Personas.logic.Paciente;
import Personas.logic.Persona;
import Personas.logic.Trabajador;
import Personas.logic.Service;

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
