package Personas.Presentation.Sesion;

import Personas.Logic.Trabajador;
import Personas.Presentation.AbstractModel;

import java.beans.PropertyChangeListener;

public class Model extends AbstractModel{
    Trabajador current;
    int mode;

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
    }

    public Model() {

    }

    public Trabajador getCurrent() {
        return current;
    }

    public void setCurrent(Trabajador current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public static final String CURRENT = "current";
}
