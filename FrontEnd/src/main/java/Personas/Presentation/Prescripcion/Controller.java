package Personas.Presentation.Prescripcion;

import Personas.Logic.*;
import Personas.Application;
import java.util.List;
import javax.swing.JOptionPane;
import java.time.LocalDate;

public class Controller {

    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    // ====== CRUD RECETA ======

    public void search(Receta filter) throws Exception {
        model.setFilter(filter);
        model.setMode(Application.MODE_CREATE);

        if (filter.getPaciente() != null) {
            model.setList(Service.instance().searchReceta(filter));
        } else {
            model.setList(Service.instance().readAllRecetas());
        }
    }
    public void save(Receta r) throws Exception {
        // asegurar valores por defecto
        if (r.getFechaRetiro() == null) r.setFechaRetiro(LocalDate.now().plusDays(3));
        if (r.getEstado() == null) r.setEstado("Confeccionada");

        Receta toSave = new Receta(r.getPaciente());
        toSave.setFechaRetiro(r.getFechaRetiro());
        toSave.setEstado(r.getEstado());

        for (MedicamentoRecetado mr : r.getMedicamentos()) {
            MedicamentoRecetado copy = new MedicamentoRecetado(
                    mr.getMedicamento(),
                    mr.getCantidad(),
                    mr.getIndicaciones(),
                    mr.getDuracionDias()
            );
            toSave.getMedicamentos().add(copy);
        }
        Service.instance().createReceta(toSave);
        model.setCurrent(new Receta());
        model.setFilter(new Receta());
        search(model.getFilter());
    }



    public void seleccionarPaciente() {
        // Obtener todos los pacientes desde el Service
        List<Paciente> lista = Service.instance().readAllPaciente();

        // Crear el mini view con la lista
        Personas.Presentation.Prescripcion.Paciente.View mini = new Personas.Presentation.Prescripcion.Paciente.View(lista);
        mini.setModal(true);
        mini.pack();
        mini.setLocationRelativeTo(view.getPanel());
        mini.setVisible(true);

        // Recuperar el paciente seleccionado del mini view
        Paciente seleccionado = mini.getSeleccionado();
        if (seleccionado != null) {
            setPacienteActual(seleccionado);
            JOptionPane.showMessageDialog(view.getPanel(), "Paciente seleccionado: " + seleccionado.getName());
        }
    }



    public void edit(int row) {
        Receta r = model.getList().get(row);
        model.setMode(Application.MODE_EDIT);
        model.setCurrent(r);
    }

    public void delete() throws Exception {
        Receta r = model.getCurrent();
        Service.instance().deleteReceta(r);

    }

    public void clear() {
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Receta());
    }

    // ====== MÉTODOS ESPECÍFICOS ======
    public void setPacienteActual(Paciente p) {
        model.getCurrent().setPaciente(p);
    }

    public void agregarMedicamento(Personas.Logic.MedicamentoRecetado mr) {
        model.addMedicamento(mr);
    }

    public void editMed(int row) throws Exception {
        MedicamentoRecetado m = model.getListMed().get(row);
        try {
            model.setCurrentMed(m);
            model.setMode(Application.MODE_EDIT);
            Personas.Presentation.Prescripcion.Detalle.Detalle viewD = new Personas.Presentation.Prescripcion.Detalle.Detalle(this, this.model.getCurrent(), m.getMedicamento(), row);
            viewD.setModal(true);
            viewD.pack();
            viewD.setLocationRelativeTo(view.getPanel());
            viewD.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace(); // O muestra el error si lo deseas
        }
    }

    public List<Receta> getAll() {
        return Service.instance().readAllRecetas();
    }

    public Model getModel() {
        return model;
    }

    public void modificarMedicamento(MedicamentoRecetado mr, int pos) {
        model.modiMed(mr, pos);
    }
}
