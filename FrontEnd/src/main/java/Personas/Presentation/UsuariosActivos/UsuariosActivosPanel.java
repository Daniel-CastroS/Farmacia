package Personas.Presentation.UsuariosActivos;

import Personas.Logic.Mensaje;
import Personas.Logic.Service;
import Personas.Presentation.Sesion.Sesion;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Map;

public class UsuariosActivosPanel extends JPanel implements PropertyChangeListener {
    private DefaultListModel<String> listModel;
    private JList<String> userList;
    private JButton btnEnviarMensaje;
    private JButton btnVerMensajes;
    private JButton btnRefrescar;
    private Map<String, String> activeUsersMap; // userId -> userName

    public UsuariosActivosPanel() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Usuarios Activos"));
        setPreferredSize(new Dimension(250, 0));

        // Lista de usuarios
        listModel = new DefaultListModel<>();
        userList = new JList<>(listModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(userList);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));

        btnEnviarMensaje = new JButton("Enviar Mensaje");
        btnEnviarMensaje.setIcon(new ImageIcon(getClass().getResource("/send.png")));
        btnEnviarMensaje.addActionListener(e -> enviarMensaje());

        btnVerMensajes = new JButton("Ver Mensajes");
        btnVerMensajes.setIcon(new ImageIcon(getClass().getResource("/med.png")));
        btnVerMensajes.addActionListener(e -> verMensajes());

        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setIcon(new ImageIcon(getClass().getResource("/search.png")));
        btnRefrescar.addActionListener(e -> refreshUsers());

        buttonPanel.add(btnEnviarMensaje);
        buttonPanel.add(btnVerMensajes);
        buttonPanel.add(btnRefrescar);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);


        Service.instance().addPropertyChangeListener(this);


        refreshUsers();
    }

    private void refreshUsers() {
        try {
            activeUsersMap = Service.instance().getActiveUsers();
            listModel.clear();

            String currentUserId = Sesion.getUserLogged().getId();

            for (Map.Entry<String, String> entry : activeUsersMap.entrySet()) {

                if (!entry.getKey().equals(currentUserId)) {
                    listModel.addElement(entry.getValue() + " (" + entry.getKey() + ")");
                }
            }

            System.out.println("Lista de usuarios actualizada: " + activeUsersMap.size() + " usuarios");
        } catch (Exception ex) {
            System.err.println("Error al refrescar usuarios: " + ex.getMessage());
        }
    }

    private void enviarMensaje() {
        String selectedUser = userList.getSelectedValue();
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un usuario", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }


        String destinatarioId = selectedUser.substring(
                selectedUser.lastIndexOf("(") + 1,
                selectedUser.lastIndexOf(")")
        );
        String destinatarioNombre = selectedUser.substring(0, selectedUser.lastIndexOf("(")).trim();

        EnviarMensajeDialog dialog = new EnviarMensajeDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                destinatarioId,
                destinatarioNombre
        );
        dialog.setVisible(true);
    }

    private void verMensajes() {
        VerMensajesDialog dialog = new VerMensajesDialog(
                (Frame) SwingUtilities.getWindowAncestor(this)
        );
        dialog.setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "USER_LOGIN":
                String loginData = (String) evt.getNewValue();
                String[] parts = loginData.split("\\|");
                String userId = parts[0];
                String userName = parts[1];

                SwingUtilities.invokeLater(() -> {
                    System.out.println("Usuario conectado: " + userName);
                    refreshUsers();
                });
                break;

            case "USER_LOGOUT":
                String logoutUserId = (String) evt.getNewValue();

                SwingUtilities.invokeLater(() -> {
                    System.out.println("Usuario desconectado: " + logoutUserId);
                    refreshUsers();
                });
                break;

            case "NEW_MESSAGE":
                Mensaje mensaje = (Mensaje) evt.getNewValue();
                String currentUserId = Sesion.getUserLogged().getId();


                if (mensaje.getDestinatarioId().equals(currentUserId)) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this,
                                "Nuevo mensaje de: " + mensaje.getRemitenteNombre() + "\n\n" +
                                        mensaje.getContenido(),
                                "Mensaje Recibido",
                                JOptionPane.INFORMATION_MESSAGE);
                    });
                }
                break;
        }
    }
}