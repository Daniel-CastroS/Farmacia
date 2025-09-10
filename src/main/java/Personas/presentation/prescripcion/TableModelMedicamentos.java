package Personas.presentation.prescripcion;

import Personas.logic.MedicamentoRecetado;
import Personas.presentation.AbstractTableModel;
import java.util.List;

public class TableModelMedicamentos extends AbstractTableModel<MedicamentoRecetado> implements javax.swing.table.TableModel {

    public static final int MEDICAMENTO = 0;
    public static final int PRESENTACION = 1;
    public static final int INDICACIONES = 2;
    public static final int DURACION = 3;

    public TableModelMedicamentos(int[] cols, List<MedicamentoRecetado> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropetyAt(MedicamentoRecetado mr, int col) {
        switch (cols[col]) {
            case MEDICAMENTO: return mr.getMedicamento().getNombre();
            case PRESENTACION: return mr.getPresentacion();
            case INDICACIONES: return mr.getIndicaciones();
            case DURACION: return mr.getDuracionDias();
            default: return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[4];
        colNames[MEDICAMENTO] = "Medicamento";
        colNames[PRESENTACION] = "Presentación";
        colNames[INDICACIONES] = "Indicaciones";
        colNames[DURACION] = "Duración (días)";
    }
}
