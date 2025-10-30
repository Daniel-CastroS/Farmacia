package Personas.Logic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Service{
    private static Service theInstance;
    public static Service instance(){
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }
    Socket s;
    ObjectOutputStream os;
    ObjectInputStream is;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    String pathMedicos = "medicos.pdf";
    String pathPacientes = "pacientes.pdf";
    String pathFarmaceutas = "farmaceutas.pdf";
    String pathMedicamentos = "medicamentos.pdf";

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public Service() {
        try {
            System.out.println("CLIENTE: Conectando al servidor...");
            s = new Socket(Protocol.SERVER, Protocol.PORT);

            System.out.println("CLIENTE: Creando streams...");
            os = new ObjectOutputStream(s.getOutputStream());
            os.flush();
            is = new ObjectInputStream(s.getInputStream());

            // ✅ ESTO FALTABA: Enviar SYNC al servidor
            System.out.println("CLIENTE: Enviando SYNC...");
            os.writeInt(Protocol.SYNC);
            os.flush();

            // ✅ Recibir el Session ID
            String sid = (String) is.readObject();
            System.out.println("CLIENTE: Session ID recibido: " + sid);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    // =============== FARMACEUTA ===============
    public void createFarmeceuta(Farmaceuta e) throws Exception {
        os.writeInt(Protocol.FARMACEUTA_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("Farmaceuta DUPLICADO");
    }

    public Farmaceuta readFarmaceuta(Farmaceuta e) throws Exception {
        os.writeInt(Protocol.FARMACEUTA_READ);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Farmaceuta) is.readObject();
        else throw new Exception("Farmaceuta NO EXISTE");
    }

    public void updateFarmaceuta(Farmaceuta e) throws Exception {
        os.writeInt(Protocol.FARMACEUTA_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("Farmaceuta NO EXISTE");
    }

    public void deleteFarmaceuta(Farmaceuta e) throws Exception {
        os.writeInt(Protocol.FARMACEUTA_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("Farmaceuta NO EXISTE");
    }

    public List<Farmaceuta> searchFarmaceuta(Farmaceuta e) {
        try {
            os.writeInt(Protocol.FARMACEUTA_SEARCH);
            os.writeObject(e);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Farmaceuta>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Farmaceuta> readAllFarmaceuta() {
        return searchFarmaceuta(new Farmaceuta());
    }

    // =============== MEDICO ===============
    public void createMedico(Medico e) throws Exception {
        os.writeInt(Protocol.MEDICO_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("Medico DUPLICADO");
    }

    public Medico readMedico(Medico e) throws Exception {
        os.writeInt(Protocol.MEDICO_READ);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Medico) is.readObject();
        else throw new Exception("Medico NO EXISTE");
    }

    public void updateMedico(Medico e) throws Exception {
        os.writeInt(Protocol.MEDICO_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("Medico NO EXISTE");
    }

    public void deleteMedico(Medico e) throws Exception {
        os.writeInt(Protocol.MEDICO_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("Medico NO EXISTE");
    }

    public List<Medico> searchMedico(Medico e) {
        try {
            os.writeInt(Protocol.MEDICO_SEARCH);
            os.writeObject(e);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Medico>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Medico> readAllMedico() {
        return searchMedico(new Medico());
    }

    // =============== PACIENTE ===============
    public void createPaciente(Paciente e) throws Exception {
        os.writeInt(Protocol.PACIENTE_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("Paciente DUPLICADO");
    }

    public Paciente readPaciente(Paciente e) throws Exception {
        os.writeInt(Protocol.PACIENTE_READ);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Paciente) is.readObject();
        else throw new Exception("Paciente NO EXISTE");
    }

    public void updatePaciente(Paciente e) throws Exception {
        os.writeInt(Protocol.PACIENTE_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("Paciente NO EXISTE");
    }

    public void deletePaciente(Paciente e) throws Exception {
        os.writeInt(Protocol.PACIENTE_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("Paciente NO EXISTE");
    }

    public List<Paciente> searchPaciente(Paciente e) {
        try {
            os.writeInt(Protocol.PACIENTE_SEARCH);
            os.writeObject(e);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Paciente>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Paciente> readAllPaciente() {
        return searchPaciente(new Paciente());
    }

    // =============== MEDICAMENTO ===============
    public void createMedicamento(Medicamento e) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("Medicamento DUPLICADO");
    }

    public Medicamento readMedicamento(Medicamento e) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_READ);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Medicamento) is.readObject();
        else throw new Exception("Medicamento NO EXISTE");
    }

    public void updateMedicamento(Medicamento e) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("Medicamento NO EXISTE");
    }

    public void deleteMedicamento(Medicamento e) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("Medicamento NO EXISTE");
    }

    public List<Medicamento> searchMedicamento(Medicamento e) {
        try {
            os.writeInt(Protocol.MEDICAMENTO_SEARCH);
            os.writeObject(e);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Medicamento>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Medicamento> readAllMedicamento() {
        return searchMedicamento(new Medicamento());
    }

    // =============== PRESCRIPCION ===============
    public void createMedicamentoRecetado(MedicamentoRecetado e) throws Exception {
        os.writeInt(Protocol.MEDICAMENTORECETADO_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("Medicamento Recetado DUPLICADO");
    }

    public MedicamentoRecetado readMedicamentoRecetado(MedicamentoRecetado e) throws Exception {
        os.writeInt(Protocol.MEDICAMENTORECETADO_READ);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (MedicamentoRecetado) is.readObject();
        else throw new Exception("Medicamento Recetado NO EXISTE");
    }

    public void updateMedicamentoRecetado(MedicamentoRecetado e) throws Exception {
        os.writeInt(Protocol.MEDICAMENTORECETADO_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("Medicamento Recetado NO EXISTE");
    }

    public void deleteMedicamentoRecetado(MedicamentoRecetado e) throws Exception {
        os.writeInt(Protocol.MEDICAMENTORECETADO_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("Medicamento Recetado NO EXISTE");
    }

    public List<MedicamentoRecetado> searchMedicamentoRecetado(MedicamentoRecetado e) {
        try {
            os.writeInt(Protocol.MEDICAMENTORECETADO_SEARCH);
            os.writeObject(e);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<MedicamentoRecetado>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<MedicamentoRecetado> readAllMedicamentoRecetado() {
        return searchMedicamentoRecetado(new MedicamentoRecetado());
    }

    // =============== RECETA ===============
    public void createReceta(Receta e) throws Exception {
        os.writeInt(Protocol.RECETA_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("Receta DUPLICADO");
    }

    public Receta readReceta(Receta e) throws Exception {
        os.writeInt(Protocol.RECETA_READ);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Receta) is.readObject();
        else throw new Exception("Receta NO EXISTE");
    }

    public void updateReceta(Receta e) throws Exception {
        os.writeInt(Protocol.RECETA_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("Receta NO EXISTE");
    }

    public void deleteReceta(Receta e) throws Exception {
        os.writeInt(Protocol.RECETA_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("Receta NO EXISTE");
    }

    public List<Receta> searchReceta(Receta e) {
        try {
            os.writeInt(Protocol.RECETA_SEARCH);
            os.writeObject(e);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Receta>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    public List<Receta> readAllRecetas() {
        return searchReceta(new Receta());
    }


    private void disconnect() throws Exception {
        os.writeInt(Protocol.DISCONNECT);
        os.flush();
        s.shutdownOutput();
        s.close();
    }

    public void stop() {
        try {
            disconnect();
        } catch (Exception e) {
            System.exit(-1);
        }
    }

    public Trabajador readTrabajador(Trabajador t) throws Exception {
        os.writeInt(Protocol.TRABAJADOR_READ);
        os.writeObject(t);
        os.flush();
        //Aca carga
        if (is.readInt() == Protocol.ERROR_NO_ERROR){
            //Aca se pega
        return (Trabajador) is.readObject();
        }else throw new Exception("Trabajador NO EXISTE");
    }

    public String getPathMedicos() { return pathMedicos; }
    public String getPathPacientes() { return pathPacientes; }
    public String getPathFarmaceutas() { return pathFarmaceutas; }
    public String getPathMedicamentos() { return pathMedicamentos; }
}
