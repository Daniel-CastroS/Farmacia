package Personas.presentation.Historico;

import Personas.Application;
import Personas.logic.Receta;
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
            search(new Receta());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ====== MÉTODOS CRUD ======

    // Buscar (por filtro)
    public void search(Receta filter) throws Exception {
        model.setFilter(filter);
        model.setMode(Personas.Application.MODE_CREATE);
        model.setCurrent(new Receta());
        model.setList(Service.instance().readAllRecetas());
    }

    // Guardar (crear o actualizar)
    public void save(Receta f) throws Exception {
        switch (model.getMode()) {
            case Personas.Application.MODE_CREATE:
                Service.instance().createReceta(f);
                break;
            case Personas.Application.MODE_EDIT:
                Service.instance().updateReceta(f);
                break;
        }

        model.setFilter(new Receta());
        search(model.getFilter());
    }

    // Editar (cargar un farmaceuta desde la lista)
    public void edit(int row) {
        Receta f = model.getList().get(row);
        try {
            model.setMode(Personas.Application.MODE_EDIT);
            model.setCurrent(Service.instance().readReceta(f.getId()));
        } catch (Exception ex) {
        }
    }

    // Borrar farmaceuta
    public void deleteFarmaceuta() throws Exception {
        Service.instance().deleteReceta(model.getCurrent());
        search(model.getFilter());
    }

    // Limpiar formulario (reset current)
    public void clear() {
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Receta());
    }
}
