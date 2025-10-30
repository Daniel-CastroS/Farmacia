package Personas.Presentation.Farmaceuta;


import Personas.Logic.Farmaceuta;
import Personas.Presentation.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Farmaceuta> implements javax.swing.table.TableModel {
    public TableModel(int[] cols, List<Farmaceuta> rows) {
        super(cols, rows);
    }

    public static final int ID = 0;
    public static final int NOMBRE = 1;

    @Override
    protected Object getPropetyAt(Farmaceuta e, int col) {
        switch (cols[col]) {
            case ID: return e.getId();
            case NOMBRE: return e.getName();
            default: return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[2];
        colNames[ID] = "Id";
        colNames[NOMBRE] = "Nombre";
    }
}
