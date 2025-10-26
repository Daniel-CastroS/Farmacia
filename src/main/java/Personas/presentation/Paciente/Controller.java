package Personas.presentation.Paciente;

import Personas.Application;
import Personas.logic.Paciente;
import Personas.logic.Service;

import java.util.List;

public class Controller {
    View view;
    Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);

        try {
            search(new Paciente());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ====== MÉTODOS CRUD ======

    // Crear paciente


    public void search(Paciente filter) throws  Exception{
        model.setFilter(filter);
        model.setMode(Personas.Application.MODE_CREATE);
        model.setCurrent(new Paciente());
        model.setList(Service.instance().readAllPacientes());
    }


    public void save(Paciente m) throws  Exception{
        switch (model.getMode()) {
            case Personas.Application.MODE_CREATE:
                Service.instance().createPaciente(m);
                break;
            case Personas.Application.MODE_EDIT:
                Service.instance().updatePaciente(m);
                break;
        }
        model.setFilter(new Paciente());
        search(model.getFilter());
    }

    public void edit(int row){
        Paciente m = model.getList().get(row);
        try {
            model.setMode(Personas.Application.MODE_EDIT);
            model.setCurrent(Service.instance().readPaciente(m.getId()));
        } catch (Exception ex) {}
    }

    // Borrar Paciente
    public void deletePaciente() throws Exception {
        Service.instance().deletePaciente(model.getCurrent());
        search(model.getFilter());
    }

    // Limpiar formulario (reset current)
    public void clear() {
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Paciente());
    }
}
