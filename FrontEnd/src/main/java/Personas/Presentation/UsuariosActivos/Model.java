package Personas.Presentation.UsuariosActivos;

import Personas.Logic.Mensaje;
import Personas.Presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model extends AbstractModel {

    private Map<String, String> activeUsers;  // userId -> userName
    private String selectedUserId;
    private String selectedUserName;
    private List<Mensaje> messages;
    private Mensaje currentMessage;

    public Model() {
        activeUsers = new HashMap<>();
        messages = new ArrayList<>();
        currentMessage = null;
        selectedUserId = null;
        selectedUserName = null;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(ACTIVE_USERS);
        firePropertyChange(MESSAGES);
        firePropertyChange(SELECTED_USER);
        firePropertyChange(CURRENT_MESSAGE);
    }

    // ========== GETTERS Y SETTERS ==========

    public Map<String, String> getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(Map<String, String> activeUsers) {
        this.activeUsers = activeUsers;
        firePropertyChange(ACTIVE_USERS);
    }

    public String getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(String selectedUserId) {
        this.selectedUserId = selectedUserId;
        firePropertyChange(SELECTED_USER);
    }

    public String getSelectedUserName() {
        return selectedUserName;
    }

    public void setSelectedUserName(String selectedUserName) {
        this.selectedUserName = selectedUserName;
        firePropertyChange(SELECTED_USER);
    }

    public List<Mensaje> getMessages() {
        return messages;
    }

    public void setMessages(List<Mensaje> messages) {
        this.messages = messages;
        firePropertyChange(MESSAGES);
    }

    public Mensaje getCurrentMessage() {
        return currentMessage;
    }

    public void setCurrentMessage(Mensaje currentMessage) {
        this.currentMessage = currentMessage;
        firePropertyChange(CURRENT_MESSAGE);
    }


    public static final String ACTIVE_USERS = "activeUsers";
    public static final String MESSAGES = "messages";
    public static final String SELECTED_USER = "selectedUser";
    public static final String CURRENT_MESSAGE = "currentMessage";
}