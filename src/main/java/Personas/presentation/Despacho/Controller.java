package Personas.presentation.Despacho;

import Personas.logic.MedicamentoRecetado;
import Personas.logic.Receta;
import Personas.logic.Service;
import Personas.Application;
import java.util.List;

public class Controller {
    View view;
    Model model;

    public Controller(View view, Model model) {
        model.init(Service.instance().search(new Receta()));
        this.view = view;
        this.model = model;
        model.init(Service.instance().findAllRecetas());
        view.setController(this);
        view.setModel(model);
    }

    // ====== MÉTODOS CRUD ======

    // Buscar (por filtro)
    public void search(Receta filter) throws Exception {
        model.setFilter(filter);
        model.setMode(Personas.Application.MODE_CREATE);
        model.setCurrent(new Receta());
        model.setList(Service.instance().search(model.getFilter()));
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

    // Editar (cargar una Receta desde la lista)
    public void edit(int row) throws Exception {
        Receta f = model.getList().get(row);
        try {
            model.setCurrent(f);
            model.setMode(Application.MODE_EDIT);
            Personas.presentation.Despacho.Detalle.Editar viewD = new Personas.presentation.Despacho.Detalle.Editar(this, f);
            viewD.setModal(true);
            viewD.pack();
            viewD.setLocationRelativeTo(view.getPanel());
            viewD.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace(); // O muestra el error si lo deseas
        }
        Service.instance().saveAllDataToXML();
    }

    public void modificarReceta(Receta receta) throws Exception {
        model.modificarReceta(receta);
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

    public void getAllRecetas() {
        List<Receta> recetas = Service.instance().findAllRecetas();
        model.setList(recetas); // This should fire the LIST property change
    }

    // Obtener listado completo
    public List<Receta> getAll() {
        return Service.instance().findAllRecetas();
    }

    // Mostrar datos iniciales
    public void shown() {
        model.setList(Service.instance().search(new Receta()));
    }
}
