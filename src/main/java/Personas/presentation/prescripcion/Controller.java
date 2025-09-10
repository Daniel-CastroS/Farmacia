package Personas.presentation.prescripcion;

import Personas.logic.MedicamentoRecetado;
import Personas.logic.Receta;
import Personas.logic.Paciente;
import Personas.logic.Service;
import Personas.Application;
import java.util.List;
import javax.swing.JOptionPane;


public class Controller {

    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;

        // Inicializamos el modelo con todas las recetas
        model.init(Service.instance().findAllRecetas());

        view.setController(this);
        view.setModel(model);
    }

    // ====== CRUD RECETA ======

    public void search(Receta filter) throws Exception {
        model.setFilter(filter);
        model.setMode(Application.MODE_CREATE);

        if (filter.getPaciente() != null) {
            model.setList(Service.instance().searchRecetaPorPaciente(filter.getPaciente()));
        } else {
            model.setList(Service.instance().findAllRecetas());
        }
    }
    public void save(Receta r) throws Exception {
        // asegurar valores por defecto
        if (r.getFechaRetiro() == null) r.setFechaRetiro(java.time.LocalDate.now().plusDays(3));
        if (r.getEstado() == null) r.setEstado("Confeccionada");

        // crear copia para guardar y evitar aliasing
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

        // guardar en el service
        Service.instance().createReceta(toSave);

        // limpiar el current para que la próxima receta empiece en blanco
        model.setCurrent(new Receta());

        // refrescar listado
        model.setFilter(new Receta());
        search(model.getFilter());
    }



    public void seleccionarPaciente() {
        // Obtener todos los pacientes desde el Service
        List<Paciente> lista = Service.instance().findAllPacientes();

        // Crear el mini view con la lista
        Personas.presentation.prescripcion.paciente.View mini = new Personas.presentation.prescripcion.paciente.View(lista);
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
        Service.instance().findAllRecetas().remove(r);
        search(new Receta());
    }

    public void clear() {
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Receta());
    }

    // ====== MÉTODOS ESPECÍFICOS ======
    public void setPacienteActual(Paciente p) {
        model.getCurrent().setPaciente(p);
    }

    public void agregarMedicamento(Receta r, Personas.logic.MedicamentoRecetado mr) {
        r.getMedicamentos().add(mr);
    }

    public List<Receta> getAll() {
        return Service.instance().findAllRecetas();
    }

    public Model getModel() {
        return model;
    }
}
