package Personas.Presentation.Paciente;

import Personas.Logic.Paciente;
import Personas.Presentation.AbstractTableModel;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class TableModel extends AbstractTableModel<Paciente> implements javax.swing.table.TableModel {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TableModel(int[] cols, List<Paciente> rows) {
        super(cols, rows);
    }

    public static final int ID = 0;
    public static final int NOMBRE = 1;
    public static final int TELEFONO = 2;
    public static final int FECHANACIMIENTO = 3;

    @Override
    protected Object getPropetyAt(Paciente e, int col) {
        switch (cols[col]) {
            case ID: return e.getId();
            case NOMBRE: return e.getName();
            case TELEFONO: return e.getTelefono();
            case FECHANACIMIENTO:
                LocalDate fecha = e.getFechaNac();
                if (fecha == null) return "";
                return fecha.format(FORMATTER);
            default: return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[4];
        colNames[ID] = "Id";
        colNames[NOMBRE] = "Nombre";
        colNames[TELEFONO] = "Telefono";
        colNames[FECHANACIMIENTO] = "Fecha Nacimiento";
    }
}
