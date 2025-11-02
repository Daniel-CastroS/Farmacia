package Personas.Presentation.Despacho;

import Personas.Logic.Receta;
import Personas.Logic.Service;
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


        getAllRecetas();
    }

    // ====== MÉTODOS CRUD ======

    // Buscar (por filtro)
    public void search(Receta filter) throws Exception {
        model.setFilter(filter);
        model.setMode(Personas.Application.MODE_CREATE);
        model.setCurrent(new Receta());
        model.setList(Service.instance().readAllRecetas());
       // model.setList(Service.instance().searchReceta(filter));

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
            Personas.Presentation.Despacho.Detalle.Editar viewD = new Personas.Presentation.Despacho.Detalle.Editar(this, f);
            viewD.setModal(true);
            viewD.pack();
            viewD.setLocationRelativeTo(view.getPanel());
            viewD.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace(); // O muestra el error si lo deseas
        }
    }
/*
    public void modificarReceta(Receta receta) throws Exception {
        model.modificarReceta(receta);
    }
*/
public void modificarReceta(Receta receta) throws Exception {
    // 1. Guardar en la base de datos
    Service.instance().updateReceta(receta);

    // 2. Actualizar el modelo
    model.modificarReceta(receta);

    // 3. Refrescar la lista para ver los cambios
    search(model.getFilter());
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
        List<Receta> recetas = Service.instance().readAllRecetas();
        model.setList(recetas); // This should fire the LIST property change
    }
}
