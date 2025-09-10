package Personas.presentation.prescripcion.paciente;

import Personas.logic.Paciente;
import Personas.presentation.AbstractTableModel;
import java.util.List;

public class TableModelPacientes extends AbstractTableModel<Paciente> implements javax.swing.table.TableModel {

    public static final int ID = 0;
    public static final int NOMBRE = 1;

    public TableModelPacientes(int[] cols, List<Paciente> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropetyAt(Paciente p, int col) {
        switch (cols[col]) {
            case ID: return p.getId();
            case NOMBRE: return p.getName();
            default: return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[2];
        colNames[ID] = "ID";
        colNames[NOMBRE] = "Nombre";
    }
}
