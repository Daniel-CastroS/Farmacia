package Personas.presentation.prescripcion;

import Personas.logic.Medicamento;
import Personas.logic.Receta;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import Personas.logic.MedicamentoRecetado;
import Personas.logic.Receta;


public class View implements PropertyChangeListener {

    private JPanel panelPrincipal;
    private JButton btnGuardar;
    private JButton btnSeleccionarPaciente;
    private JButton btnAgregarMedicamento;
    private JTable tableRecetas;
    private JTable tableMedicamentos;    // tabla de medicamentos

    private JTextField textFieldFecha;

    private Controller controller;
    private Model model;

    public View() {

        // Botón seleccionar paciente
        btnSeleccionarPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.seleccionarPaciente();
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

                // Setear fecha de retiro 3 días después de hoy y cambiar estado
                r.setFechaRetiro(java.time.LocalDate.now().plusDays(3));
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







        tableRecetas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableRecetas.getSelectedRow();
                if (row >= 0) {
                    model.setCurrent(model.getList().get(row));
                }
            }
        });








        tableRecetas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableRecetas.getSelectedRow();
                if (row >= 0) {
                    Receta seleccionada = model.getList().get(row);
                    model.setCurrent(seleccionada);  // Esto disparará el refresh de medicamentos
                }
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
        if (evt.getPropertyName().equals(Model.CURRENT)) {
            Receta recetaActual = model.getCurrent();
            if (recetaActual != null) {
                // Refrescar tabla de medicamentos
                int[] cols = {
                        TableModelMedicamentos.MEDICAMENTO,
                        TableModelMedicamentos.PRESENTACION,
                        TableModelMedicamentos.INDICACIONES,
                        TableModelMedicamentos.DURACION
                };
                tableMedicamentos.setModel(new TableModelMedicamentos(cols, recetaActual.getMedicamentos()));
                tableMedicamentos.setRowHeight(25);
            } else {
                // Limpiar tabla si no hay receta seleccionada
                tableMedicamentos.setModel(new TableModelMedicamentos(new int[]{0,1,2}, List.of()));
            }
        }

        if (evt.getPropertyName().equals(Model.LIST)) {
            // Refresca tabla de recetas
            int[] cols = {
                    TableModel.PACIENTE,
                    TableModel.FECHA_RETIRO,
                    TableModel.ESTADO,
            };
            tableRecetas.setModel(new TableModel(cols, model.getList()));
            tableRecetas.setRowHeight(30);
        }
    }




    public void agregarMedicamentoAlCurrent(MedicamentoRecetado mr) {
        Receta r = model.getCurrent();
        if (r != null) {
            r.getMedicamentos().add(mr);
            model.setCurrent(r);
        }
    }














































}
