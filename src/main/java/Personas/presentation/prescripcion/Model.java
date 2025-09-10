package Personas.presentation.prescripcion;

import Personas.logic.Receta;
import Personas.logic.Paciente;
import Personas.presentation.AbstractModel;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {

    private Receta current; // receta actual que se está creando o editando
    private List<Receta> list; // todas las recetas
    private Receta filter; // filtro por paciente, opcional
    private int mode;

    public static final String LIST = "list";
    public static final String CURRENT = "current";
    public static final String FILTER = "filter"; // filtro por paciente

    public Model() {
        this.list = new ArrayList<>();
        this.current = new Receta();
        this.filter = new Receta();
        this.mode = 0; // MODE_CREATE
    }

    public void init(List<Receta> recetas) {
        this.list = recetas;
        this.current = new Receta();
        this.filter = new Receta();
        this.mode = 0; // MODE_CREATE
    }

    // ===== Getters y Setters =====
    public Receta getCurrent() {
        return current;
    }

    public void setCurrent(Receta current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public List<Receta> getList() {
        return list;
    }

    public void setList(List<Receta> list) {
        this.list = list;
        firePropertyChange(LIST);
    }

    public Receta getFilter() {
        return filter;
    }

    public void setFilter(Receta filter) {
        this.filter = filter;
        firePropertyChange(FILTER);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
