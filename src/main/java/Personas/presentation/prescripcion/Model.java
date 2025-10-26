package Personas.presentation.prescripcion;

import Personas.logic.Medicamento;
import Personas.logic.MedicamentoRecetado;
import Personas.logic.Receta;
import Personas.presentation.AbstractModel;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {

    private Receta current; // receta actual que se está creando o editando
    private MedicamentoRecetado currentMed;
    private List<Receta> list; // todas las recetas
    private List<MedicamentoRecetado> listMed; // Medicamentos de la receta actual
    private Receta filter; // filtro por paciente, opcional
    private Medicamento filterMed;
    private int mode;

    public static final String LIST = "list";
    public static final String CURRENT = "current";
    public static final String FILTER = "filter"; // filtro por paciente
    public static final String LIST_MED = "list_med";

    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST);
        firePropertyChange(CURRENT);
        firePropertyChange(FILTER);
        firePropertyChange(LIST_MED);
    }

    public Model() {
        this.list = new ArrayList<>();
        this.listMed = new ArrayList<>();
        this.currentMed = new MedicamentoRecetado();
        this.filterMed = new Medicamento();
        this.current = new Receta();
        this.filter = new Receta();
        this.mode = 0; // MODE_CREATE
    }

    public void initMed(List<MedicamentoRecetado> medicamentos) {
        this.listMed = medicamentos;
        firePropertyChange(LIST_MED);
    }

    // ===== Getters y Setters =====
    public Receta getCurrent() {
        return current;
    }

    public void setCurrent(Receta current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public MedicamentoRecetado getCurrentMed() {
        return currentMed;
    }

    public void setCurrentMed(MedicamentoRecetado currentMed) {
        this.currentMed = currentMed;
    }

    public Medicamento getFilterMed() {
        return filterMed;
    }

    public void setFilterMed(Medicamento filterMed) {
        this.filterMed = filterMed;
    }

    public List<MedicamentoRecetado> getListMed() {
        return listMed;
    }

    public void setListMed(List<MedicamentoRecetado> listMed) {
        this.listMed = listMed;
        firePropertyChange(LIST_MED);
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

    public void addMedicamento(MedicamentoRecetado mr) {
        if (current == null) return;
        this.current.getMedicamentos().add(mr);
        this.listMed.add(mr);
        firePropertyChange(CURRENT);
    }

    public void modiMed(MedicamentoRecetado mr, int pos) {
        current.getMedicamentos().set(pos, mr);
        firePropertyChange(CURRENT);
    }

    public void notifyCurrent() {
        firePropertyChange(CURRENT);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
