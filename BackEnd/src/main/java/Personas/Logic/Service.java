package Personas.Logic;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


import Personas.Data.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;




//



public class Service {
    private static Service theInstance;

    public static Service instance(){
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }

    private MedicoDao medicoDao;
    private FarmaceutaDao farmaceutaDao;
    private PacienteDao pacienteDao;
    private MedicamentoDao medicamentoDao;
    private MedicamentoRecetadoDao medicamentoRecetadoDao;
    private RecetaDao recetaDao;



    private MensajeDao mensajeDao;
    private Map<String, String> activeUsers;

    public Service() {
        try{
            recetaDao = new RecetaDao();
            medicamentoRecetadoDao = new MedicamentoRecetadoDao();
            medicamentoDao = new MedicamentoDao();
            pacienteDao = new PacienteDao();
            farmaceutaDao = new FarmaceutaDao();
            medicoDao = new MedicoDao();


            mensajeDao = new MensajeDao();  /////
            activeUsers = new ConcurrentHashMap<>();///
        }
        catch(Exception e){
        }
    }

    public void stop(){
    }

    //================= MEDICO ============
    public void createMedico(Medico e) throws Exception {
        medicoDao.create(e);
    }

    public Medico read(Medico e) throws Exception {
        return medicoDao.read(e.getGafete());
    }

    public void update(Medico e) throws Exception {
        medicoDao.update(e);
    }

    public void delete(Medico e) throws Exception {
        medicoDao.delete(e);
    }

    public List<Medico> search(Medico e) {
        try {
            if (Objects.equals(e.getGafete(), "")) {
                // Filtro vacío = devolver TODAS
                return medicoDao.findAll();
            } else {
                // Filtro con datos = buscar por criterios
                return medicoDao.findByNombre(e);
            }        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //================= FARMACEUTA ============
    public void createFarmaceuta(Farmaceuta e) throws Exception {
        farmaceutaDao.create(e);
    }

    public Farmaceuta readFarmaceuta(Farmaceuta e) throws Exception {
        return farmaceutaDao.read(e.getGafete());
    }

    public void updateFarmaceuta(Farmaceuta e) throws Exception {
        farmaceutaDao.update(e);
    }

    public void delete(Farmaceuta e) throws Exception {
        farmaceutaDao.delete(e);
    }

    public List<Farmaceuta> search(Farmaceuta e) {
        try {
            if (Objects.equals(e.getId(), "")) {
                // Filtro vacío = devolver TODAS
                return farmaceutaDao.findAll();
            } else {
                // Filtro con datos = buscar por criterios
                return farmaceutaDao.findByNombre(e);
            }        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //================= PACIENTE ============
    public void createPaciente(Paciente e) throws Exception {
        pacienteDao.create(e);
    }

    public Paciente readPaciente(Paciente e) throws Exception {
        return pacienteDao.read(e.getId());
    }

    public void updatePaciente(Paciente e) throws Exception {
        pacienteDao.update(e);
    }

    public void deletePaciente(Paciente e) throws Exception {
        pacienteDao.delete(e);
    }

    public List<Paciente> search(Paciente e) {
        try {
            if (Objects.equals(e.getId(), "")) {
                // Filtro vacío = devolver TODAS
                return pacienteDao.findAll();
            } else {
                // Filtro con datos = buscar por criterios
                return pacienteDao.findByNombre(e);
            }        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //================= MEDICAMENTO ============
    public void createMedicamento(Medicamento e) throws Exception {
        medicamentoDao.create(e);
    }

    public Medicamento readMedicamento(Medicamento e) throws Exception {
        return medicamentoDao.read(e.getCodigo());
    }

    public void updateMedicamento(Medicamento e) throws Exception {
        medicamentoDao.update(e);
    }

    public void delete(Medicamento e) throws Exception {
        medicamentoDao.delete(e);
    }

    public List<Medicamento> search(Medicamento e) {
        try {
            if (Objects.equals(e.getCodigo(), "")) {
                // Filtro vacío = devolver TODAS
                return medicamentoDao.findAll();
            } else {
                // Filtro con datos = buscar por criterios
                return medicamentoDao.findByNombre(e);
            }        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //================= MEDICAMENTORECETADO ============
    public void createMedicamentoRecetado(MedicamentoRecetado e) throws Exception {
        medicamentoRecetadoDao.create(e);
    }

    public MedicamentoRecetado read(MedicamentoRecetado e) throws Exception {
        return medicamentoRecetadoDao.read(e.getPrescripcion());
    }

    public void updateMedicamentoRecetado(MedicamentoRecetado e) throws Exception {
        medicamentoRecetadoDao.update(e);
    }

    public void deleteMedicamentoRecetado(MedicamentoRecetado e) throws Exception {
        medicamentoRecetadoDao.delete(e);
    }

    public List<MedicamentoRecetado> search(MedicamentoRecetado e) {
        try {
            if (e.getPrescripcion() == 0 && e.getMedicamento() == null) {
                // Filtro vacío = devolver TODAS
                return medicamentoRecetadoDao.findAll();
            } else {
                // Filtro con datos = buscar por criterios
                return medicamentoRecetadoDao.findByReceta(e.getPrescripcion());
            }        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //================= RECETA   ============
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

    public Receta readReceta(Receta e) throws Exception {
        return recetaDao.read(e.getId());
    }

    public void updateReceta(Receta e) throws Exception {
        recetaDao.update(e);
    }

    public void deleteReceta(Receta r) throws Exception {
        try{
            medicamentoRecetadoDao.deleteByReceta(r.getId());
        }
        catch(Exception ex){
            throw ex;
        }
        recetaDao.delete(r);
    }

    public List<Receta> searchReceta(Receta e) {
        try {
            if (e.getId() == 0 && e.getPaciente() == null && e.getEstado() == null) {
                // Filtro vacío = devolver TODAS
                return recetaDao.findAll();
            } else {
                // Filtro con datos = buscar por criterios
                return recetaDao.findByPaciente(e.getPaciente().getId());
            }        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // ================= TRABAJADOR ============
    public Trabajador readTrabajador(Trabajador e) throws Exception {
        try {
            return medicoDao.read(e.getId());
        } catch (Exception ex) {
            try {
                return farmaceutaDao.read(e.getId());
            } catch (Exception ex2) {
                throw new Exception("Trabajador NO EXISTE");
            }
        }
    }
    public void addActiveUser(String userId, String userName) {
        activeUsers.put(userId, userName);
        System.out.println("Usuario conectado: " + userName + " (" + userId + ")");
    }

    public void removeActiveUser(String userId) {
        String userName = activeUsers.remove(userId);
        System.out.println("Usuario desconectado: " + userName + " (" + userId + ")");
    }

    public Map<String, String> getActiveUsers() {
        return new HashMap<>(activeUsers);
    }

    // ////
    public void createMensaje(Mensaje m) throws Exception {
        mensajeDao.create(m);
    }

    public Mensaje readMensaje(int id) throws Exception {
        return mensajeDao.read(id);
    }

    public List<Mensaje> getPendingMessages(String destinatarioId) {
        return mensajeDao.getPendingMessages(destinatarioId);
    }

    public List<Mensaje> getAllMessages(String destinatarioId) {
        return mensajeDao.getAllMessages(destinatarioId);
    }

    public void markMessageAsRead(int messageId) throws Exception {
        mensajeDao.markAsRead(messageId);
    }



}
