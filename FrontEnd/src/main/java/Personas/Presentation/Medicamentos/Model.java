package Personas.Presentation.Medicamentos;

import Personas.Logic.Medicamento;
import Personas.Presentation.AbstractModel;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private Medicamento current;
    private List<Medicamento> list;
    private Medicamento filter;
    int mode;

    public static final String LIST = "list";
    public static final String CURRENT = "current";
    public static final String FILTER = "filter";

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST);
        firePropertyChange(FILTER);
        firePropertyChange(CURRENT);


    }
    public Model(){
        current = new Medicamento();
        filter = new Medicamento();
        list = new ArrayList<>();
    }

    public List<Medicamento> getList(){
        return this.list;
    }

    public void setList(List<Medicamento> list){
        this.list = list;
        firePropertyChange(LIST);
    }

    public Medicamento getCurrent(){
        return this.current;
    }

    public void setCurrent(Medicamento current){
        this.current = current;
        firePropertyChange(CURRENT);
    }

/// //
public Medicamento getFilter() { return filter; }

    public void setFilter(Medicamento filter) {
        this.filter = filter;
        firePropertyChange(FILTER);
    }

    public int getMode() { return mode; }

    public void setMode(int mode) { this.mode = mode; }

}
