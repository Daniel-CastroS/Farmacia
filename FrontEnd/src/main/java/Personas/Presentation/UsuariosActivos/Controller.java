package Personas.Presentation.UsuariosActivos;

import Personas.Logic.Mensaje;
import Personas.Logic.Service;
import Personas.Presentation.Sesion.Sesion;

import java.util.Map;
import java.util.List;

public class Controller {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);

        // Cargar usuarios activos al inicio
        try {
            refreshUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void refreshUsers() throws Exception {
        Map<String, String> users = Service.instance().getActiveUsers();
        model.setActiveUsers(users);
    }


    public void selectUser(String userId, String userName) {
        model.setSelectedUserId(userId);
        model.setSelectedUserName(userName);
    }


    public void clearSelection() {
        model.setSelectedUserId(null);
        model.setSelectedUserName(null);
    }




    public void sendMessage(String destinatarioId, String destinatarioNombre, String contenido) throws Exception {
        Mensaje mensaje = new Mensaje();
        mensaje.setRemitenteId(Sesion.getUserLogged().getId());
        mensaje.setRemitenteNombre(Sesion.getUserLogged().getName());
        mensaje.setDestinatarioId(destinatarioId);
        mensaje.setDestinatarioNombre(destinatarioNombre);
        mensaje.setContenido(contenido);

        Service.instance().sendMessage(mensaje);
    }


    public void loadMessages() throws Exception {
        String userId = Sesion.getUserLogged().getId();
        List<Mensaje> messages = Service.instance().getAllMessages(userId);
        model.setMessages(messages);
    }

    /**
     * Carga solo mensajes pendientes (no leídos)
     */
    public void loadPendingMessages() throws Exception {
        String userId = Sesion.getUserLogged().getId();
        List<Mensaje> messages = Service.instance().getPendingMessages(userId);
        model.setMessages(messages);
    }


    public void selectMessage(int index) {
        if (index >= 0 && index < model.getMessages().size()) {
            Mensaje mensaje = model.getMessages().get(index);
            model.setCurrentMessage(mensaje);


        }
    }



    public void handleUserLogin(String userId, String userName) {
        try {
            refreshUsers();
            System.out.println("Usuario conectado: " + userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void handleUserLogout(String userId) {
        try {
            refreshUsers();
            System.out.println("Usuario desconectado: " + userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void handleNewMessage(Mensaje mensaje) {
        String currentUserId = Sesion.getUserLogged().getId();

        // Solo procesar si es para el usuario actual
        if (mensaje.getDestinatarioId().equals(currentUserId)) {
            // Notificar a la vista para que muestre un diálogo
            view.showNewMessageNotification(mensaje);
        }
    }

    // ========== GETTERS ==========


    public Model getModel() {
        return model;
    }
}