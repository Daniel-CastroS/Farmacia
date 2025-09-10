package Personas.presentation.Paciente;

import Personas.Application;
import Personas.data.XmlPersister;
import Personas.logic.Paciente;
import Personas.logic.Service;

import java.util.List;

public class Controller {
    View view;
    Model model;

    public Controller(View view, Model model) {
        model.init(Service.instance().search(new Paciente()));
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    // ====== MÉTODOS CRUD ======

    // Crear paciente


    public void search(Paciente filter) throws  Exception{
        model.setFilter(filter);
        model.setMode(Personas.Application.MODE_CREATE);
        model.setCurrent(new Paciente());
        model.setList(Service.instance().search(model.getFilter()));
    }


    public void save(Paciente m) throws  Exception{
        switch (model.getMode()) {
            case Personas.Application.MODE_CREATE:
                Service.instance().createPatiente(m);
                break;
            case Personas.Application.MODE_EDIT:
                Service.instance().updatePaciente(m);
                break;
        }
        XmlPersister.instance().store(Service.instance().getData());

        model.setFilter(new Paciente());
        search(model.getFilter());
    }
    public void edit(int row){

        Paciente m = model.getList().get(row);
        try {
            model.setMode(Personas.Application.MODE_EDIT);
            model.setCurrent(Service.instance().readPaciente(m));
        } catch (Exception ex) {}
    }




    /*
    public void createMedico(Medico m) throws Exception {
        Service.instance().createMedico(m);
        model.setCurrent(new Medico());
        model.setList(Service.instance().findAll());
    }
    */
    /*
    public void readMedico(String id) throws Exception {
        Medico temp = new Medico();
        temp.setId(id);
        try {
            Medico result = Service.instance().readMedico(temp);
            model.setCurrent(result);
        } catch (Exception ex) {
            model.setCurrent(new Medico());
            throw ex;
        }
    }*/

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

    // Obtener listado completo
    public List<Paciente> getAll() {
        return Service.instance().findAllPacientes();
    }

    public void shown(){
        model.setList(Service.instance().search(new Paciente()));
    }
}
