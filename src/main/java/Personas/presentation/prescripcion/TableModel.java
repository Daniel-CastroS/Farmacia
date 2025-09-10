package Personas.presentation.prescripcion;

import Personas.logic.Receta;
import Personas.presentation.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel<Receta> implements javax.swing.table.TableModel {

    public TableModel(int[] cols, List<Receta> rows) {
        super(cols, rows);
    }

    public static final int PACIENTE = 0;
    public static final int FECHA_RETIRO = 2;
    public static final int ESTADO = 3;
    public static final int CANT_MEDICAMENTOS = 4;

    @Override
    protected Object getPropetyAt(Receta r, int col) {
        switch (cols[col]) {
            case PACIENTE: return r.getPaciente() != null ? r.getPaciente().getName() : "";
            case FECHA_RETIRO: return r.getFechaRetiro();
            case ESTADO: return r.getEstado();
            case CANT_MEDICAMENTOS: return r.getMedicamentos() != null ? r.getMedicamentos().size() : 0;
            default: return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[5];

        colNames[PACIENTE] = "Paciente";

        colNames[FECHA_RETIRO] = "Fecha Retiro";
        colNames[ESTADO] = "Estado";
        colNames[CANT_MEDICAMENTOS] = "Cantidad Medicamentos";
    }
}
