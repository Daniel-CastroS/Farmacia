package Personas.logic;

import Personas.data.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.PreparedStatement;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
    private static Service theInstance;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    String pathMedicos = "medicos.pdf";
    String pathPacientes = "pacientes.pdf";
    String pathFarmaceutas = "farmaceutas.pdf";
    String pathMedicamentos = "medicamentos.pdf";
    private MedicoDao medicoDao;
    private FarmaceutaDao farmaceutaDao;
    private PacienteDao pacienteDao;
    private MedicamentoDao medicamentoDao;
    private MedicamentoRecetadoDao medicamentoRecetadoDao;
    private RecetaDao recetaDao;

    public static Service instance() {
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    private Service() {
        try{
            medicoDao = new MedicoDao();
            farmaceutaDao = new FarmaceutaDao();
            pacienteDao = new PacienteDao();
            medicamentoDao = new MedicamentoDao();
            medicamentoRecetadoDao = new MedicamentoRecetadoDao();
            recetaDao = new RecetaDao();
        }
        catch(Exception e) {
            System.exit(-1);
        }
    }
    public void stop() {
        try {
            Database.instance().close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // =============== MEDICO ===============
    public Medico readMedico(String codigo) throws Exception {
        return medicoDao.read(codigo);
    }
    public void createMedico(Medico m) throws Exception {
        medicoDao.create(m);
    }
    public void updateMedico(Medico m) throws Exception {
        medicoDao.update(m);
    }
    public void deleteMedico(Medico m)  throws Exception {
        medicoDao.delete(m);
    }
    public List<Medico> readAllMedicos() {
        Medico filtro = new Medico();
        return medicoDao.findByNombre(filtro);
    }

    // =============== FARMACEUTA ===============
    public Farmaceuta readFarmaceuta(String codigo) throws Exception {
        return farmaceutaDao.read(codigo);
    }
    public void createFarmaceuta(Farmaceuta f) throws Exception {
        farmaceutaDao.create(f);
    }
    public void updateFarmaceuta(Farmaceuta f) throws Exception {
        farmaceutaDao.update(f);
    }
    public void deleteFarmaceuta(Farmaceuta f)  throws Exception {
        farmaceutaDao.delete(f);
    }
    public List<Farmaceuta> readAllFarmaceutas() {
        Farmaceuta filtro = new Farmaceuta();
        return farmaceutaDao.findByNombre(filtro);
    }

    // =============== PACIENTE ===============
    public Paciente readPaciente(String codigo) throws Exception {
        return pacienteDao.read(codigo);
    }
    public void createPaciente(Paciente p) throws Exception {
        pacienteDao.create(p);
    }
    public void updatePaciente(Paciente p) throws Exception {
        pacienteDao.update(p);
    }
    public void deletePaciente(Paciente p)  throws Exception {
        pacienteDao.delete(p);
    }
    public List<Paciente> readAllPacientes() {
        Paciente filtro = new Paciente();
        return pacienteDao.findByNombre(filtro);
    }

    // =============== MEDICAMENTO ===============
    public Medicamento readMedicamento(String codigo) throws Exception {
        return medicamentoDao.read(codigo);
    }
    public void createMedicamento(Medicamento m) throws Exception {
        medicamentoDao.create(m);
    }
    public void updateMedicamento(Medicamento m) throws Exception {
        medicamentoDao.update(m);
    }
    public void deleteMedicamento(Medicamento m)  throws Exception {
        medicamentoDao.delete(m);
    }
    public List<Medicamento> readAllMedicamentos() {
        Medicamento filtro = new Medicamento();
        return medicamentoDao.findByNombre(filtro);
    }

    // =============== PRESCRIPCION ===============
    public MedicamentoRecetado readMedicamentoRecetado(String codigo) throws Exception {
        return medicamentoRecetadoDao.read(codigo);
    }
    public void createMedicamentoRecetado(MedicamentoRecetado m) throws Exception {
        medicamentoRecetadoDao.create(m);
    }
    public  void updateMedicamentoRecetado(MedicamentoRecetado m) throws Exception {
        medicamentoRecetadoDao.update(m);
    }
    public  void deleteMedicamentoRecetado(MedicamentoRecetado m)  throws Exception {
        medicamentoRecetadoDao.delete(m);
    }
    public List<MedicamentoRecetado> readAllMedicamentoRecetados() {
        return medicamentoRecetadoDao.findAll();
    }

    // =============== RECETA ===============
    public Receta readReceta(int codigo) throws Exception {
        return recetaDao.read(codigo);
    }

   /* public void createReceta(Receta r) throws Exception {
        recetaDao.create(r);
    }
    */
   public void createReceta(Receta r) throws Exception {

       recetaDao.create(r);

       if (r.getMedicamentos() != null && !r.getMedicamentos().isEmpty()) {

           for (MedicamentoRecetado mr : r.getMedicamentos()) {
               try {
                   mr.setPrescripcion(r.getId());
                   medicamentoRecetadoDao.create(mr);
               } catch (Exception e) {
                   e.printStackTrace();
                   throw e;
               }
           }

       }
   }

    public void updateReceta(Receta r) throws Exception {
        recetaDao.update(r);
    }
    /*
    public void deleteReceta(Receta r)  throws Exception {
        recetaDao.delete(r);
    }

     */
    public void deleteReceta(Receta r) throws Exception {

        try {
            medicamentoRecetadoDao.deleteByReceta(r.getId());
        } catch (Exception e) {
            System.err.println("Error borrando medicamentos: " + e.getMessage());

        }

        recetaDao.delete(r);
    }

    public List<Receta> readAllRecetas() {
        return recetaDao.findAll();
    }
    public List<Receta> readByPaciente(Paciente p) {
        return recetaDao.findAll();
    }

    // ================== TRABAJADORES ==================
    public Trabajador readTrabajador(Trabajador t) throws Exception {
        try {
            return readMedico(t.getId());
        } catch (Exception e) {
            return readFarmaceuta(t.getId());
        }
    }

    public String getPathMedicos() { return pathMedicos; }
    public String getPathPacientes() { return pathPacientes; }
    public String getPathFarmaceutas() { return pathFarmaceutas; }
    public String getPathMedicamentos() { return pathMedicamentos; }
}
