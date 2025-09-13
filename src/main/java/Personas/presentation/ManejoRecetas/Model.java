package Personas.presentation.ManejoRecetas;

import Personas.Application;
import Personas.logic.Receta;
import Personas.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.List;

public class Model extends AbstractModel {

    private Receta current;
    private List<Receta> list;
    private Receta filter;  // ignorar por ahora
    int mode;

    public static final String LIST = "list";
    public static final String CURRENT = "current";
    public static final String FILTER = "filter"; // ignorar por ahora

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST);
        firePropertyChange(CURRENT);
        firePropertyChange(FILTER);
    }

    public Model() { }

    public void init(List<Receta> list) {
        this.list = list;
        this.current = new Receta();
        this.filter = new Receta();
        this.mode = Application.MODE_CREATE;
    }

    // GETTERS Y SETTERS
    public List<Receta> getList() { return list; }

    public void setList(List<Receta> list) {
        this.list = list;
        firePropertyChange(LIST);
    }

    public Receta getCurrent() { return current; }

    public void setCurrent(Receta current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public Receta getFilter() { return filter; }

    public void setFilter(Receta filter) {
        this.filter = filter;
        firePropertyChange(FILTER);
    }

    public int getMode() { return mode; }

    public void setMode(int mode) { this.mode = mode; }
}
