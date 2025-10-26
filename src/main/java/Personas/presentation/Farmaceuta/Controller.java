package Personas.presentation.Farmaceuta;

import Personas.logic.Farmaceuta;
import Personas.logic.Service;
import Personas.Application;
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
            search(new Farmaceuta());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ====== MÉTODOS CRUD ======

    // Buscar (por filtro)
    public void search(Farmaceuta filter) throws Exception {
        model.setFilter(filter);
        model.setMode(Personas.Application.MODE_CREATE);
        model.setCurrent(new Farmaceuta());
        model.setList(Service.instance().readAllFarmaceutas());
    }

    // Guardar (crear o actualizar)
    public void save(Farmaceuta f) throws Exception {
        switch(model.getMode()) {
            case Application.MODE_CREATE:
                Service.instance().createFarmaceuta(f);
                break;
            case Application.MODE_EDIT:
                Service.instance().updateFarmaceuta(f);
                break;
        }

        // REFRESCAR DESDE DB
        List<Farmaceuta> nuevos = Service.instance().readAllFarmaceutas();
        model.setList(nuevos);
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Farmaceuta());
    }

    // Editar (cargar un farmaceuta desde la lista)
    public void edit(int row) {
        Farmaceuta f = model.getList().get(row);
        try {
            model.setMode(Personas.Application.MODE_EDIT);
            //model.setCurrent(Service.instance().readFarmaceuta(f.getId()));
            model.setCurrent(Service.instance().readFarmaceuta(f.getGafete()));
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
}
