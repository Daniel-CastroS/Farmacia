package Personas.Presentation.Medico;

import Personas.Logic.Medico;
import Personas.Presentation.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel<Medico> implements javax.swing.table.TableModel {
    public TableModel(int[] cols, List<Medico> rows) {
        super(cols, rows);
    }

    public static final int ID = 0;
    public static final int NOMBRE = 1;
    public static final int ESPECIALIDAD = 2;

    @Override
    protected Object getPropetyAt(Medico e, int col) {
        switch (cols[col]) {
            case ID: return e.getId();
            case NOMBRE: return e.getName();
            case ESPECIALIDAD: return e.getEspecialidad();
            default: return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[3];
        colNames[ID] = "Id";
        colNames[NOMBRE] = "Nombre";
        colNames[ESPECIALIDAD] = "Especialidad";
    }
}
