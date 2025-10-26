package Personas.presentation.Medico;

import Personas.logic.Service;
import Personas.Application;
import Personas.logic.Medico;

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
            search(new Medico());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ====== MÉTODOS CRUD ======

    public void search(Medico filter) throws Exception{
        model.setFilter(filter);
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Medico());
        model.setList(Service.instance().readAllMedicos());
    }

    public void save(Medico m) throws Exception {
        switch (model.getMode()) {
            case Application.MODE_CREATE:
                Service.instance().createMedico(m);
                break;
            case Application.MODE_EDIT:
                Service.instance().updateMedico(m);
                break;
        }

        // REFRESCO DIRECTO: leer desde DB y actualizar el modelo
        List<Medico> nuevos = Service.instance().readAllMedicos();
        model.setList(nuevos);
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Medico());
    }

    public void edit(int row){
        Medico m = model.getList().get(row);
        try {
            model.setMode(Application.MODE_EDIT);
            model.setCurrent(Service.instance().readMedico(m.getGafete()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteMedico() throws Exception {
        Service.instance().deleteMedico(model.getCurrent());
        search(model.getFilter());
    }

    public void clear() {
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Medico());
    }
}