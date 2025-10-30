package Personas.Presentation.Historico;

import Personas.Logic.Receta;
import Personas.Presentation.AbstractTableModel;

import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class TableModel extends AbstractTableModel<Receta> implements javax.swing.table.TableModel{
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TableModel(int[] cols, List<Receta> rows) {
        super(cols, rows);
    }

    public static final int ID_PACIENTE = 0;
    public static final int NOMBRE_PACIENTE = 1;
    public static final int FECHA_RETIRO = 2;
    public static final int CANTIDAD_MEDICINAS = 3;
    public static final int ESTADO = 4;

    @Override
    protected Object getPropetyAt(Receta e, int col) {
        switch (cols[col]) {
            case ID_PACIENTE: return e.getPaciente().getId();
            case NOMBRE_PACIENTE: return e.getPaciente().getName();
            case FECHA_RETIRO:
                LocalDate d = e.getFechaRetiro();
                if (d == null) return "";
                return d.format(FORMATTER);
            case CANTIDAD_MEDICINAS: return e.getMedicamentos().size();
            case ESTADO: return e.getEstado();
            default: return "";
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "ID Paciente";
            case 1: return "Nombre Paciente";
            case 2: return "Fecha Retiro";
            case 3: return "Cantidad Medicinas";
            case 4: return "Estado";
            default: return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[5];
        colNames[ID_PACIENTE] = "ID Paciente";
        colNames[NOMBRE_PACIENTE] = "Nombre Paciente";
        colNames[FECHA_RETIRO] = "Fecha Retiro";
        colNames[CANTIDAD_MEDICINAS] = "Cantidad Medicinas";
        colNames[ESTADO] = "Estado";
    }
}
