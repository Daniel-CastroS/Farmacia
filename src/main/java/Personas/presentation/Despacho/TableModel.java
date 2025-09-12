package Personas.presentation.Despacho;


import Personas.logic.Receta;
import Personas.presentation.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Receta> implements javax.swing.table.TableModel {
    public TableModel(int[] cols, List<Receta> rows) {
        super(cols, rows);
    }

    public static final int ID_PACIENTE = 0;
    public static final int NOMBRE_PACIENTE = 1;
    public static final int FECHA_ENTREGA = 2;
    public static final int ESTADO = 3;

    @Override
    protected Object getPropetyAt(Receta e, int col) {
        return switch (cols[col]) {
            case ID_PACIENTE -> e.getPaciente().getId();
            case NOMBRE_PACIENTE -> e.getPaciente().getName();
            case FECHA_ENTREGA -> e.getFechaRetiro();
            case ESTADO -> e.getEstado();
            default -> "";
        };
    }

    @Override
    protected void initColNames() {
        colNames = new String[4];
        colNames[ID_PACIENTE] = "Id";
        colNames[NOMBRE_PACIENTE] = "Nombre paciente";
        colNames[FECHA_ENTREGA] = "Fecha entrega";
        colNames[ESTADO] = "Estado";
    }
}
