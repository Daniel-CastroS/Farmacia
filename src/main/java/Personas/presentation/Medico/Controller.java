package Personas.presentation.Medico;

//import Personas.data.XmlPersister;
import Personas.data.XmlPersister;
import Personas.logic.Persona;
import Personas.logic.Service;
import Personas.Application;


import Personas.logic.Medico;
import Personas.logic.Service;
import java.util.List;

public class Controller {
    View view;
    Model model;

    public Controller(View view, Model model) {
        model.init(Service.instance().search(new Medico()));
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    // ====== MÉTODOS CRUD ======

    // Crear médico


    public void search(Medico filter) throws  Exception{
        model.setFilter(filter);
        model.setMode(Personas.Application.MODE_CREATE);
        model.setCurrent(new Medico());
        model.setList(Service.instance().search(model.getFilter()));
    }


    public void save(Medico m) throws  Exception{
        switch (model.getMode()) {
            case Personas.Application.MODE_CREATE:
                Service.instance().createMedico(m);
                break;
            case Personas.Application.MODE_EDIT:
                Service.instance().updateMedico(m);
                break;
        }
        XmlPersister.instance().store(Service.instance().getData());

        model.setFilter(new Medico());
        search(model.getFilter());
    }
    public void edit(int row){

        Medico m = model.getList().get(row);
        try {
            model.setMode(Personas.Application.MODE_EDIT);
            model.setCurrent(Service.instance().readMedico(m));
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

    // Borrar médico
    public void deleteMedico() throws Exception {
        Service.instance().deleteMedico(model.getCurrent());
        search(model.getFilter());
    }

    // Limpiar formulario (reset current)
    public void clear() {
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Medico());
    }

    // Obtener listado completo
    public List<Medico> getAll() {
        return Service.instance().findAll();
    }

    public void shown(){
        model.setList(Service.instance().search(new Medico()));
    }
}
