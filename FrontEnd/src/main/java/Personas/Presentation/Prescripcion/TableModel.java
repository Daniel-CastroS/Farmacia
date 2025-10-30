package Personas.Presentation.Prescripcion;

import Personas.Logic.MedicamentoRecetado;
import Personas.Presentation.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel<MedicamentoRecetado> implements javax.swing.table.TableModel {

    public static final int MEDICAMENTO = 0;
    public static final int PRESENTACION = 1;
    public static final int CANTIDAD = 2;
    public static final int INDICACIONES = 3;
    public static final int DURACION = 4;

    public TableModel(int[] cols, List<MedicamentoRecetado> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropetyAt(MedicamentoRecetado mr, int col) {
        switch (cols[col]) {
            case MEDICAMENTO: return mr.getMedicamento().getNombre();
            case PRESENTACION: return mr.getPresentacion();
            case CANTIDAD: return mr.getCantidad();
            case INDICACIONES: return mr.getIndicaciones();
            case DURACION: return mr.getDuracionDias();
            default: return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[5];
        colNames[MEDICAMENTO] = "Medicamento";
        colNames[PRESENTACION] = "Presentación";
        colNames[CANTIDAD] = "Cantidad";
        colNames[INDICACIONES] = "Indicaciones";
        colNames[DURACION] = "Duración (días)";
    }
}
