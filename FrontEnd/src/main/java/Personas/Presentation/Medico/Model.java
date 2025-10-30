package Personas.Presentation.Medico;

import java.beans.PropertyChangeListener;
import Personas.Logic.Medico;
import Personas.Presentation.AbstractModel;

import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {

    private Medico current;
    private List<Medico> list;
    private Medico filter;  //ignorar por ahora
    int mode;

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST);
        firePropertyChange(CURRENT);
        firePropertyChange(FILTER);
    }

    public Model() {
        list = new ArrayList<>();
        current = new Medico();
        filter = new Medico();
    }

    // GETTERS Y SETTERS
    public List<Medico> getList() { return list; }

    public void setList(List<Medico> list) {
        this.list = list;
        firePropertyChange(LIST);
    }


    public Medico getCurrent() { return current; }


    public void setCurrent(Medico current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public Medico getFilter() { return filter; }

    public void setFilter(Medico filter) {
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
