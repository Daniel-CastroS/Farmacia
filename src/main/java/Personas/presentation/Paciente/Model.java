package Personas.presentation.Paciente;

import Personas.Application;
import Personas.logic.Paciente;
import Personas.presentation.AbstractModel;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel{
    private Paciente current;
    private List<Paciente> list;
    private Paciente filter;  //ignorar por ahora
    int mode;

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST);
        firePropertyChange(CURRENT);
        firePropertyChange(FILTER);
    }

    public Model() {

    }

    public void init(List<Paciente> list) {
        this.list = list;
        this.current = new Paciente();
        this.filter = new Paciente();
        this.mode = Application.MODE_CREATE;
    }

    // GETTERS Y SETTERS
    public List<Paciente> getList() { return list; }

    public void setList(List<Paciente> list) {
        this.list = list;
        firePropertyChange(LIST);
    }


    public Paciente getCurrent() { return current; }


    public void setCurrent(Paciente current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public Paciente getFilter() { return filter; }

    public void setFilter(Paciente filter) {
        this.filter = filter;
        firePropertyChange(FILTER);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }



    public static final String LIST = "list";
    public static final String CURRENT = "current";
    public static final String FILTER = "filter";//ignorar por ahora
}
