package Personas.Logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    ServerSocket ss;
    List<Worker> workers;

    public Server() {
        try {
            ss = new ServerSocket(Protocol.PORT);
            workers = Collections.synchronizedList(new ArrayList<Worker>());
            System.out.println("Servidor iniciado...");
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void run() {
        Service service = new Service();
        boolean continuar = true;
        Socket s;
        Worker worker;
        String sid;  // Session Id
        while (continuar) {
            try {
                s = ss.accept();
                System.out.println("Conexion Establecida...");
                ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(s.getInputStream());
                int type = is.readInt();
                switch (type) {
                    case Protocol.SYNC:
                        sid = s.getRemoteSocketAddress().toString();
                        System.out.println("SYNCH: " + sid);
                        worker = new Worker(this, s, os, is, sid, Service.instance());
                        workers.add(worker);
                        System.out.println("Quedan: " + workers.size());
                        worker.start();
                        os.writeObject(sid); // send Session Id back
                        break;
                    case Protocol.ASYNC:
                        sid = (String) is.readObject(); // recieves Session Id
                        System.out.println("ASYNCH: " + sid);
                        join(s, os, is, sid);
                        break;
                }
                os.flush();
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println(ex);
            }
        }
    }

    public void remove(Worker w) {
        workers.remove(w);
        System.out.println("Quedan: " + workers.size());
    }

    public void join(Socket as, ObjectOutputStream aos, ObjectInputStream ais, String sid) {
        for (Worker w : workers) {
            if (w.sid.equals(sid)) {
                w.setAs(as, aos, ais);
                break;
            }
        }
    }

    public void deliver_medico(Worker from, Medico p){
        for(Worker w:workers){
            if (w!=from) w.deliver_medico(p);
        }
    }

    public void deliver_farmaceuta(Worker from, Farmaceuta p){
        for(Worker w:workers){
            if (w!=from) w.deliver_farmaceuta(p);
        }
    }

    public void deliver_paciente(Worker from, Paciente p){
        for(Worker w:workers){
            if (w!=from) w.deliver_paciente(p);
        }
    }

    public void deliver_medicamento(Worker from, Medicamento p){
        for(Worker w:workers){
            if (w!=from) w.deliver_mediacamento(p);
        }
    }

    public void deliver_Medicamento_recetado(Worker from, MedicamentoRecetado p){
        for(Worker w:workers){
            if (w!=from) w.deliver_medicamento_recetado(p);
        }
    }

    public void deliver_receta(Worker from, Receta p){
        for(Worker w:workers){
            if (w!=from) w.deliver_receta(p);
        }
    }
}
