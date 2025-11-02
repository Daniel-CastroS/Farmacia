package Personas.Presentation.UsuariosActivos;

import Personas.Logic.Mensaje;
import Personas.Logic.Service;
import Personas.Presentation.Sesion.Sesion;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

public class View extends JPanel implements PropertyChangeListener {
    private DefaultListModel<String> listModel;
    private JList<String> userList;
    private JButton btnEnviarMensaje;
    private JButton btnVerMensajes;
    private JButton btnRefrescar;

    private Controller controller;
    private Model model;

    public View() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Usuarios Activos"));
        setPreferredSize(new Dimension(250, 0));


        listModel = new DefaultListModel<>();
        userList = new JList<>(listModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedUser = userList.getSelectedValue();
                if (selectedUser != null) {
                    String userId = extractUserId(selectedUser);
                    String userName = extractUserName(selectedUser);
                    controller.selectUser(userId, userName);
                } else {
                    controller.clearSelection();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(userList);


        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));

        btnEnviarMensaje = new JButton("Enviar Mensaje");
        btnEnviarMensaje.setIcon(new ImageIcon(getClass().getResource("/send.png")));
        btnEnviarMensaje.addActionListener(e -> enviarMensaje());

        btnVerMensajes = new JButton("Ver Mensajes");
        btnVerMensajes.setIcon(new ImageIcon(getClass().getResource("/med.png")));
        btnVerMensajes.addActionListener(e -> verMensajes());

        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setIcon(new ImageIcon(getClass().getResource("/search.png")));
        btnRefrescar.addActionListener(e -> {
            try {
                controller.refreshUsers();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al refrescar: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(btnEnviarMensaje);
        buttonPanel.add(btnVerMensajes);
        buttonPanel.add(btnRefrescar);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);


        Service.instance().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                switch (evt.getPropertyName()) {
                    case "USER_LOGIN":
                        String loginData = (String) evt.getNewValue();
                        String[] parts = loginData.split("\\|");
                        String userId = parts[0];
                        String userName = parts[1];
                        SwingUtilities.invokeLater(() -> controller.handleUserLogin(userId, userName));
                        break;

                    case "USER_LOGOUT":
                        String logoutUserId = (String) evt.getNewValue();
                        SwingUtilities.invokeLater(() -> controller.handleUserLogout(logoutUserId));
                        break;

                    case "NEW_MESSAGE":
                        Mensaje mensaje = (Mensaje) evt.getNewValue();
                        SwingUtilities.invokeLater(() -> controller.handleNewMessage(mensaje));
                        break;
                }
            }
        });
    }

    // ========== SETTERS ==========

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    // ========== PROPERTY CHANGE LISTENER ==========

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.ACTIVE_USERS:
                updateUserList();
                break;

            case Model.SELECTED_USER:

                boolean hasSelection = model.getSelectedUserId() != null;
                btnEnviarMensaje.setEnabled(hasSelection);
                break;

            case Model.MESSAGES:
                //
                break;

            case Model.CURRENT_MESSAGE:
          //
                break;
        }
    }



    private void updateUserList() {
        listModel.clear();
        Map<String, String> users = model.getActiveUsers();
        String currentUserId = Sesion.getUserLogged().getId();

        for (Map.Entry<String, String> entry : users.entrySet()) {
            // No mostrar el usuario actual
            if (!entry.getKey().equals(currentUserId)) {
                String display = entry.getValue() + " (" + entry.getKey() + ")";
                listModel.addElement(display);
            }
        }

        System.out.println("Lista de usuarios actualizada: " + users.size() + " usuarios");
    }

    private void enviarMensaje() {
        if (model.getSelectedUserId() == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un usuario",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        EnviarMensajeDialog dialog = new EnviarMensajeDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                model.getSelectedUserId(),
                model.getSelectedUserName(),
                controller
        );
        dialog.setVisible(true);
    }

    private void verMensajes() {
        VerMensajesDialog dialog = new VerMensajesDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                controller
        );
        dialog.setVisible(true);
    }


    public void showNewMessageNotification(Mensaje mensaje) {
        JOptionPane.showMessageDialog(this,
                "Nuevo mensaje de: " + mensaje.getRemitenteNombre() + "\n\n" +
                        mensaje.getContenido(),
                "Mensaje Recibido",
                JOptionPane.INFORMATION_MESSAGE);
    }


    private String extractUserId(String listItem) {
        return listItem.substring(
                listItem.lastIndexOf("(") + 1,
                listItem.lastIndexOf(")")
        );
    }


    private String extractUserName(String listItem) {
        return listItem.substring(0, listItem.lastIndexOf("(")).trim();
    }


    public JPanel getPanel() {
        return this;
    }


}
