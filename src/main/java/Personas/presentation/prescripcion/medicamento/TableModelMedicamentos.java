package Personas.presentation.prescripcion.medicamento;

import Personas.logic.Medicamento;
import Personas.presentation.AbstractTableModel;

import java.util.List;

public class TableModelMedicamentos extends AbstractTableModel<Medicamento> implements javax.swing.table.TableModel {

    public static final int CODIGO = 0;
    public static final int NOMBRE = 1;
    public static final int PRESENTACION = 2;

    public TableModelMedicamentos(int[] cols, List<Medicamento> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropetyAt(Medicamento m, int col) {
        switch (cols[col]) {
            case CODIGO: return m.getCodigo();
            case NOMBRE: return m.getNombre();
            case PRESENTACION: return m.getPresentacion();
            default: return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[3];
        colNames[CODIGO] = "Código";
        colNames[NOMBRE] = "Nombre";
        colNames[PRESENTACION] = "Presentación";
    }
}
