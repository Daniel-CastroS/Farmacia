package Personas.presentation.prescripcion;

import Personas.logic.Medicamento;
import Personas.logic.Receta;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import Personas.logic.MedicamentoRecetado;
import com.github.lgooddatepicker.components.DatePicker;


public class View implements PropertyChangeListener {

    private JPanel panelPrincipal;
    private JButton btnGuardar;
    private JButton btnSeleccionarPaciente;
    private JButton btnAgregarMedicamento;
    private JTable tableMedicamentos;    // tabla de medicamentos

    private DatePicker fechaRetiroPicker;
    private JButton limpiarButton;
    private JTextField textFieldNombrePaciente;

    private Controller controller;
    private Model model;

    public View() {
        textFieldNombrePaciente.setEnabled(false);
        // Botón seleccionar paciente
        btnSeleccionarPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.seleccionarPaciente();
                if(controller.getModel().getCurrent().getPaciente() != null)
                    setNombrePaciente(controller.getModel().getCurrent().getPaciente().getName());
            }
        });

        // Botón agregar medicamento
        btnAgregarMedicamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrent().getPaciente() != null) {
                    Personas.presentation.prescripcion.medicamento.View mini =
                            new Personas.presentation.prescripcion.medicamento.View(controller, obtenerListaMedicamentos());
                    mini.mostrar();
                } else {
                    JOptionPane.showMessageDialog(panelPrincipal, "Primero selecciona un paciente");
                }
            }
        });
        // Botón guardar receta
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Receta r = model.getCurrent();

                // Validaciones
                if (r.getPaciente() == null) {
                    JOptionPane.showMessageDialog(panelPrincipal, "Debe seleccionar un paciente");
                    return;
                }
                if (r.getMedicamentos().isEmpty()) {
                    JOptionPane.showMessageDialog(panelPrincipal, "Debe agregar al menos un medicamento");
                    return;
                }
                if (fechaRetiroPicker.getDate() == null) {
                    JOptionPane.showMessageDialog(panelPrincipal, "Debe ingresar la fecha de retiro");
                    return;
                }

                r.setFechaRetiro(fechaRetiroPicker.getDate());
                r.setEstado("Confeccionada");

                // Guardar la receta
                try {
                    controller.save(r);
                    JOptionPane.showMessageDialog(panelPrincipal, "Receta guardada correctamente");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panelPrincipal, ex.getMessage());
                }
            }
        });

        tableMedicamentos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableMedicamentos.getSelectedRow();
                if (row >= 0) {
                    model.setCurrent(model.getList().get(row));
                }
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model = new Model();
                model.notifyCurrent();
                textFieldNombrePaciente.setText("");
                fechaRetiroPicker.setDate(null);
                tableMedicamentos.setModel(new TableModel(
                        new int[]{TableModel.MEDICAMENTO, TableModel.PRESENTACION, TableModel.INDICACIONES, TableModel.DURACION},
                        java.util.List.of()
                ));
                tableMedicamentos.setRowHeight(25);
            }
        });

    }

    public JPanel getPanel() {
        return panelPrincipal;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private List<Medicamento> obtenerListaMedicamentos() {
        // Esto obtiene todos los medicamentos desde el Service
        return Personas.logic.Service.instance().findAllMedicamentos();
    }


    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        int[] cols = {
                TableModel.MEDICAMENTO,
                TableModel.PRESENTACION,
                TableModel.CANTIDAD,
                TableModel.INDICACIONES,
                TableModel.DURACION
        };
       switch (evt.getPropertyName()) {
           case Model.CURRENT:
               if (model.getCurrent() != null) {
               Receta recetaActual = model.getCurrent();
               tableMedicamentos.setModel(new TableModel(cols, recetaActual.getMedicamentos()));
               tableMedicamentos.setRowHeight(25);
               // sincronizar fecha y paciente en la UI
               fechaRetiroPicker.setDate(recetaActual.getFechaRetiro());
               if (recetaActual.getPaciente() != null) setNombrePaciente(recetaActual.getPaciente().getName());
               } else {
                   // Limpiar tabla si no hay receta seleccionada
                   tableMedicamentos.setModel(new TableModel(cols, List.of()));
                   fechaRetiroPicker.setDate(null);
                   textFieldNombrePaciente.setText("");
               }
               break;
           case Model.LIST:
               if (model.getCurrent() != null){
                   tableMedicamentos.setModel(new TableModel(cols, model.getCurrent().getMedicamentos()));
                   tableMedicamentos.setRowHeight(25);
                   // mantener sincronía con current
                   fechaRetiroPicker.setDate(model.getCurrent().getFechaRetiro());
                   if (model.getCurrent().getPaciente() != null) setNombrePaciente(model.getCurrent().getPaciente().getName());
               } else {
                   tableMedicamentos.setModel(new TableModel(cols, List.of()));
                   fechaRetiroPicker.setDate(null);
                   textFieldNombrePaciente.setText("");
               }
               break;
           case Model.FILTER:
                tableMedicamentos.setModel(new TableModel(cols, model.getCurrent().getMedicamentos()));
                tableMedicamentos.setRowHeight(25);
                break;
       }
    }

    public void setNombrePaciente(String nombre) {
        textFieldNombrePaciente.setText(nombre);
        textFieldNombrePaciente.setEnabled(false);
    }

}
