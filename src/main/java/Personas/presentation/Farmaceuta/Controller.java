package Personas.presentation.Farmaceuta;

import Personas.data.XmlPersister;
import Personas.logic.Farmaceuta;
import Personas.logic.Service;
import Personas.Application;
import java.util.List;

public class Controller {
    View view;
    Model model;

    public Controller(View view, Model model) {
        model.init(Service.instance().search(new Farmaceuta()));
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    // ====== MÉTODOS CRUD ======

    // Buscar (por filtro)
    public void search(Farmaceuta filter) throws Exception {
        model.setFilter(filter);
        model.setMode(Personas.Application.MODE_CREATE);
        model.setCurrent(new Farmaceuta());
        model.setList(Service.instance().search(model.getFilter()));
    }

    // Guardar (crear o actualizar)
    public void save(Farmaceuta f) throws Exception {
        switch (model.getMode()) {
            case Personas.Application.MODE_CREATE:
                Service.instance().createFarmaceuta(f);
                break;
            case Personas.Application.MODE_EDIT:
                Service.instance().updateFarmaceuta(f);
                break;
        }
        XmlPersister.instance().store(Service.instance().getData());

        model.setFilter(new Farmaceuta());
        search(model.getFilter());
    }

    // Editar (cargar un farmaceuta desde la lista)
    public void edit(int row) {
        Farmaceuta f = model.getList().get(row);
        try {
            model.setMode(Personas.Application.MODE_EDIT);
            model.setCurrent(Service.instance().readFarmaceuta(f));
        } catch (Exception ex) {
        }
    }

    // Borrar farmaceuta
    public void deleteFarmaceuta() throws Exception {
        Service.instance().deleteFarmaceuta(model.getCurrent());
        search(model.getFilter());
    }

    // Limpiar formulario (reset current)
    public void clear() {
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Farmaceuta());
    }

    // Obtener listado completo
    public List<Farmaceuta> getAll() {
        return Service.instance().findAllFarmaceutas();
    }

    // Mostrar datos iniciales
    public void shown() {
        model.setList(Service.instance().search(new Farmaceuta()));
    }
}
