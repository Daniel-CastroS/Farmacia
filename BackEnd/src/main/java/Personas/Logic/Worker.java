package Personas.Logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class Worker {
    Server srv;
    Socket s;
    ObjectOutputStream os;
    ObjectInputStream is;
    Service service;

    String sid; // Session Id
    Socket as; // Asynchronous Socket
    ObjectOutputStream aos;
    ObjectInputStream ais;

    public Worker(Server srv, Socket s, ObjectOutputStream os, ObjectInputStream is, String sid, Service service) {
        this.srv = srv;
        this.s = s;
        this.os = os;
        this.is = is;
        this.service = service;
        this.sid = sid;
    }

    public void setAs(Socket as, ObjectOutputStream aos, ObjectInputStream ais) {
        this.as = as;
        this.aos = aos;
        this.ais = ais;
    }

    boolean continuar;

    public void start() {
        try {
            System.out.println("Worker atendiendo peticiones...");
            Thread t = new Thread(new Runnable() {
                public void run() {
                    listen();
                }
            });
            continuar = true;
            t.start();
        } catch (Exception ex) {
        }
    }

    public void stop() {
        continuar = false;
        System.out.println("Conexion cerrada...");
    }

    public void listen() {
        int method;
        while (continuar) {
            try {
                method = is.readInt();
                switch (method) {
                    case Protocol.MEDICO_CREATE:
                        try {
                            Medico p = (Medico) is.readObject();
                            service.createMedico(p);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_medico(this,p);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICO_READ:
                        try {
                            Medico e = service.read((Medico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(e);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICO_UPDATE:
                        try {
                            service.update((Medico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICO_DELETE:
                        try {
                            service.delete((Medico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICO_SEARCH:
                        try {
                            List<Medico> le = service.search((Medico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(le);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.FARMACEUTA_CREATE:
                        try {
                            Farmaceuta p = (Farmaceuta) is.readObject();
                            service.createFarmaceuta(p);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_farmaceuta(this,p);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.FARMACEUTA_READ:
                        try {
                            Farmaceuta e = service.readFarmaceuta((Farmaceuta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(e);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.FARMACEUTA_UPDATE:
                        try {
                            service.updateFarmaceuta((Farmaceuta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.FARMACEUTA_DELETE:
                        try {
                            service.delete((Farmaceuta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.FARMACEUTA_SEARCH:
                        try {
                            List<Farmaceuta> le = service.search((Farmaceuta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(le);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.PACIENTE_CREATE:
                        try {
                            Paciente p = (Paciente) is.readObject();
                            service.createPaciente(p);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_paciente(this,p);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.PACIENTE_READ:
                        try {
                            Paciente e = service.readPaciente((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(e);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.PACIENTE_UPDATE:
                        try {
                            service.updatePaciente((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.PACIENTE_DELETE:
                        try {
                            service.deletePaciente((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.PACIENTE_SEARCH:
                        try {
                            List<Paciente> le = service.search((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(le);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICAMENTO_CREATE:
                        try {
                            Medicamento p = (Medicamento) is.readObject();
                            service.createMedicamento(p);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_medicamento(this,p);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICAMENTO_READ:
                        try {
                            Medicamento e = service.readMedicamento((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(e);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICAMENTO_UPDATE:
                        try {
                            service.updateMedicamento((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICAMENTO_DELETE:
                        try {
                            service.delete((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICAMENTO_SEARCH:
                        try {
                            List<Medicamento> le = service.search((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(le);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICAMENTORECETADO_CREATE:
                        try {
                            MedicamentoRecetado p = (MedicamentoRecetado) is.readObject();
                            service.createMedicamentoRecetado(p);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_Medicamento_recetado(this,p);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICAMENTORECETADO_READ:
                        try {
                            MedicamentoRecetado e = service.read((MedicamentoRecetado) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(e);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICAMENTORECETADO_UPDATE:
                        try {
                            service.updateMedicamentoRecetado((MedicamentoRecetado) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICAMENTORECETADO_DELETE:
                        try {
                            service.deleteMedicamentoRecetado((MedicamentoRecetado) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICAMENTORECETADO_SEARCH:
                        try {
                            List<MedicamentoRecetado> le = service.search((MedicamentoRecetado) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(le);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.RECETA_CREATE:
                        try {
                            Receta p = (Receta) is.readObject();
                            service.createReceta(p);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            srv.deliver_receta(this,p);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.RECETA_READ:
                        try {
                            Receta e = service.readReceta((Receta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(e);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.RECETA_UPDATE:
                        try {
                            service.updateReceta((Receta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.RECETA_DELETE:
                        try {
                            service.deleteReceta((Receta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.RECETA_SEARCH:
                        try {
                            List<Receta> le = service.searchReceta((Receta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(le);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.TRABAJADOR_READ:
                        try {
                            Trabajador filtro = (Trabajador) is.readObject();
                            Trabajador resultado = service.readTrabajador(filtro);
                            if (resultado != null) {
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(resultado);
                            } else {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;


                    case Protocol.USER_LOGIN:
                        try {
                            String userId = (String) is.readObject();
                            String userName = (String) is.readObject();
                            service.addActiveUser(userId, userName);
                            os.writeInt(Protocol.ERROR_NO_ERROR);

                            // Notificar a todos los demás clientes
                            srv.notifyUserLogin(this, userId, userName);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.USER_LOGOUT:
                        try {
                            String userId = (String) is.readObject();
                            service.removeActiveUser(userId);
                            os.writeInt(Protocol.ERROR_NO_ERROR);

                            // Notificar a todos los demás clientes
                            srv.notifyUserLogout(this, userId);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.USER_LIST:
                        try {
                            Map<String, String> users = service.getActiveUsers();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(users);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    //////
                    case Protocol.MESSAGE_SEND:
                        try {
                            Mensaje mensaje = (Mensaje) is.readObject();
                            service.createMensaje(mensaje);
                            os.writeInt(Protocol.ERROR_NO_ERROR);

                            // Notificar al destinatario
                            srv.notifyNewMessage(this, mensaje);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.MESSAGE_GET_PENDING:
                        try {
                            String userId = (String) is.readObject();
                            List<Mensaje> mensajes = service.getPendingMessages(userId);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(mensajes);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.MESSAGE_GET_ALL:
                        try {
                            String userId = (String) is.readObject();
                            List<Mensaje> mensajes = service.getAllMessages(userId);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(mensajes);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.DISCONNECT:
                        stop();
                        srv.remove(this);
                        break;
                }
                os.flush();
            } catch (IOException e) {
                stop();
            }
        }
    }

    // //////
    public synchronized void deliver_medico(Medico p) {
        if (as != null) {
            try {
                aos.writeInt(Protocol.DELIVER_MEDICO);
                aos.writeObject(p);
                aos.flush();
            } catch (Exception e) {
            }
        }
    }
/// /
    public synchronized void deliver_farmaceuta(Farmaceuta p) {
        if (as != null) {
            try {
                aos.writeInt(Protocol.DELIVER_FARMACEUTA);
                aos.writeObject(p);
                aos.flush();
            } catch (Exception e) {
            }
        }
    }
/// /
    public synchronized void deliver_paciente(Paciente p) {
        if (as != null) {
            try {
                aos.writeInt(Protocol.DELIVER_PACIENTE);
                aos.writeObject(p);
                aos.flush();
            } catch (Exception e) {
            }
        }
    }
///
    public synchronized void deliver_mediacamento(Medicamento p) {
        if (as != null) {
            try {
                aos.writeInt(Protocol.DELIVER_MEDICAMENTO);
                aos.writeObject(p);
                aos.flush();
            } catch (Exception e) {
            }
        }
    }
/// /
    public synchronized void deliver_receta(Receta p) {
        if (as != null) {
            try {
                aos.writeInt(Protocol.DELIVER_RECETA);
                aos.writeObject(p);
                aos.flush();
            } catch (Exception e) {
            }
        }
    }

    public synchronized void deliver_medicamento_recetado(MedicamentoRecetado p) {
        if (as != null) {
            try {
                aos.writeInt(Protocol.DELIVER_MEDICAMENTORECETADO);
                aos.writeObject(p);
                aos.flush();
            } catch (Exception e) {
            }
        }
    }

    // ////
    public synchronized void deliverUserLogin(String userId, String userName) {
        if (as != null) {
            try {
                aos.writeInt(Protocol.DELIVER_USER_LOGIN);
                aos.writeObject(userId);
                aos.writeObject(userName);
                aos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void deliverUserLogout(String userId) {
        if (as != null) {
            try {
                aos.writeInt(Protocol.DELIVER_USER_LOGOUT);
                aos.writeObject(userId);
                aos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void deliverMessage(Mensaje mensaje) {
        if (as != null) {
            try {
                aos.writeInt(Protocol.DELIVER_USER_MESSAGE);
                aos.writeObject(mensaje);
                aos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}