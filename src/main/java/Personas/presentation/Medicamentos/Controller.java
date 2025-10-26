package Personas.presentation.Medicamentos;

import Personas.logic.Medicamento;
import Personas.logic.Service;
import Personas.Application;
import java.util.List;

public class Controller {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);

        try {
            search(new Medicamento());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ====== MÉTODOS CRUD ======

    // Buscar medicamentos según un filtro
    public void search(Medicamento filter) throws Exception {
        model.setFilter(filter);
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Medicamento());
        model.setList(Service.instance().readAllMedicamentos());
    }

    // Guardar medicamento (crear o actualizar)
    public void save(Medicamento m) throws Exception {
        switch (model.getMode()) {
            case Application.MODE_CREATE:
                Service.instance().createMedicamento(m);
                break;
            case Application.MODE_EDIT:
                Service.instance().updateMedicamento(m);
                break;
        }
        model.setFilter(new Medicamento());
        search(model.getFilter());
    }

    // Editar medicamento
    public void edit(int row) {
        Medicamento m = model.getList().get(row);
        try {
            model.setMode(Application.MODE_EDIT);
            model.setCurrent(Service.instance().readMedicamento(m.getCodigo()));
        } catch (Exception ex) {
            // podrías loguear el error si quieres
        }
    }

    // Borrar medicamento
    public void deleteMedicamento() throws Exception {
        Service.instance().deleteMedicamento(model.getCurrent());
        search(model.getFilter());
    }

    // Limpiar formulario (reset current)
    public void clear() {
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Medicamento());
    }

}
