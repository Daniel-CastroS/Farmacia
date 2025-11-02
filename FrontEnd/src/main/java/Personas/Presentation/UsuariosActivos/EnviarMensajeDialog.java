package Personas.Presentation.UsuariosActivos;

import Personas.Logic.Mensaje;

import javax.swing.*;
import java.awt.*;

public class EnviarMensajeDialog extends JDialog {
    private JTextArea textArea;
    private JButton btnEnviar;
    private JButton btnCancelar;
    private String destinatarioId;
    private String destinatarioNombre;
    private Controller controller;

    public EnviarMensajeDialog(Frame parent, String destinatarioId, String destinatarioNombre, Controller controller) {
        super(parent, "Enviar Mensaje a " + destinatarioNombre, true);
        this.destinatarioId = destinatarioId;
        this.destinatarioNombre = destinatarioNombre;
        this.controller = controller;

        initComponents();
        setSize(400, 300);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Área de texto
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnEnviar = new JButton("Enviar");
        btnEnviar.setIcon(new ImageIcon(getClass().getResource("/send.png")));
        btnEnviar.addActionListener(e -> enviarMensaje());

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setIcon(new ImageIcon(getClass().getResource("/close.png")));
        btnCancelar.addActionListener(e -> dispose());

        buttonPanel.add(btnEnviar);
        buttonPanel.add(btnCancelar);

        add(new JLabel("Mensaje:"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void enviarMensaje() {
        String contenido = textArea.getText().trim();

        if (contenido.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El mensaje no puede estar vacío",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {

            controller.sendMessage(destinatarioId, destinatarioNombre, contenido);

            JOptionPane.showMessageDialog(this,
                    "Mensaje enviado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al enviar mensaje: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}