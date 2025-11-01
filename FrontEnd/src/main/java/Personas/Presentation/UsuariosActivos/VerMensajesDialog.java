package Personas.Presentation.UsuariosActivos;

import Personas.Logic.Mensaje;
import Personas.Logic.Service;
import Personas.Presentation.Sesion.Sesion;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VerMensajesDialog extends JDialog {
    private DefaultListModel<String> listModel;
    private JList<String> messageList;
    private JTextArea detailArea;
    private JButton btnRefrescar;
    private JButton btnCerrar;
    private List<Mensaje> mensajes;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public VerMensajesDialog(Frame parent) {
        super(parent, "Mis Mensajes", true);
        initComponents();
        setSize(600, 400);
        setLocationRelativeTo(parent);
        cargarMensajes();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel izquierdo: lista de mensajes
        listModel = new DefaultListModel<>();
        messageList = new JList<>(listModel);
        messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        messageList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalle();
            }
        });
        JScrollPane listScroll = new JScrollPane(messageList);
        listScroll.setPreferredSize(new Dimension(250, 0));

        // Panel derecho: detalle del mensaje
        detailArea = new JTextArea();
        detailArea.setEditable(false);
        detailArea.setLineWrap(true);
        detailArea.setWrapStyleWord(true);
        detailArea.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane detailScroll = new JScrollPane(detailArea);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setIcon(new ImageIcon(getClass().getResource("/search.png")));
        btnRefrescar.addActionListener(e -> cargarMensajes());

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setIcon(new ImageIcon(getClass().getResource("/close.png")));
        btnCerrar.addActionListener(e -> dispose());

        buttonPanel.add(btnRefrescar);
        buttonPanel.add(btnCerrar);

        // Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, detailScroll);
        splitPane.setDividerLocation(250);

        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void cargarMensajes() {
        try {
            String userId = Sesion.getUserLogged().getId();
            mensajes = Service.instance().getAllMessages(userId);

            listModel.clear();
            for (Mensaje m : mensajes) {
                String display = String.format("%s - %s",
                        m.getRemitenteNombre(),
                        m.getFecha().format(formatter));
                listModel.addElement(display);
            }

            if (!mensajes.isEmpty()) {
                messageList.setSelectedIndex(0);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar mensajes: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDetalle() {
        int index = messageList.getSelectedIndex();
        if (index >= 0 && index < mensajes.size()) {
            Mensaje m = mensajes.get(index);
            String detalle = String.format(
                    "De: %s (%s)\n" +
                            "Para: %s (%s)\n" +
                            "Fecha: %s\n" +
                            "Estado: %s\n\n" +
                            "Mensaje:\n%s",
                    m.getRemitenteNombre(), m.getRemitenteId(),
                    m.getDestinatarioNombre(), m.getDestinatarioId(),
                    m.getFecha().format(formatter),
                    m.isLeido() ? "Leído" : "No leído",
                    m.getContenido()
            );
            detailArea.setText(detalle);
        }
    }
}