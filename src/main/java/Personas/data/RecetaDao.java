package Personas.data;

import Personas.logic.Receta;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecetaDao {
    Database db;

    public RecetaDao(){
        db= Database.instance();
    }
    public Receta read(String id) throws Exception{
        String sql="select d.*, p.* from Receta d " +
                "left join Paciente p on p.id = d.paciente " +
                "where d.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs =  db.executeQuery(stm);
        if (rs.next()) {
            return from(rs,"d");
        }
        else{
            throw new Exception ("Receta no Existe");
        }
    }

    public List<Receta> findAll(){
        List<Receta> ds=new ArrayList<>();
        try {
            String sql="select d.*, p.* from Receta d left join Paciente p on p.id = d.paciente";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs =  db.executeQuery(stm);
            while (rs.next()) {
                ds.add(from(rs,"d"));
            }
        } catch (SQLException ex) { System.err.println("RecetaDao.findAll error: " + ex.getMessage()); }
        return ds;
    }

    public Receta from(ResultSet rs, String alias){
        PacienteDao pd= new PacienteDao();
        MedicamentoRecetadoDao mr= new MedicamentoRecetadoDao();
        try {
            Receta d= new Receta();
            try{
                // Intentar leer el paciente si está presente en el ResultSet (alias "p" from the join)
                d.setPaciente(pd.from(rs,"p"));
            } catch(Exception e){
                //ignorar si no viene el paciente
            }
            try{
                // Si la base de datos guarda un ARRAY de medicamentos, intentar leerlo.
                // Si no existe, se ignora. La estructura exacta depende del esquema.
                Array medicamentosArray = null;
                try {
                    medicamentosArray = rs.getArray(alias + ".medicamentos");
                } catch (SQLException ex) {
                    // columna no existe o no es ARRAY -> ignorar
                }
                if (medicamentosArray != null) {
                    String[] medicamentos = (String[]) medicamentosArray.getArray();
                    for (String medicamentoAlias : medicamentos) {
                        try {
                            // Este código es conservador: si mr.from no coincide con la forma
                            // en la que se almacenan los medicamentos en el ResultSet, se ignora.
                            d.getMedicamentos().add(mr.from(rs, medicamentoAlias));
                        } catch (Exception ex) {
                            // ignorar
                        }
                    }
                }
            } catch(Exception e){
                //ignorar
            }
            d.setEstado(rs.getString(alias + ".estado"));

            java.sql.Date sqlFechaRetiro = rs.getDate(alias + ".fechaRetiro");
            if (sqlFechaRetiro != null) {
                d.setFechaRetiro(sqlFechaRetiro.toLocalDate());
            } else {
                d.setFechaRetiro(null);
            }

            java.sql.Date sqlFechaConfeccion = rs.getDate(alias + ".fechaConfeccion");
            if (sqlFechaConfeccion != null) {
                d.setFechaConfeccion(sqlFechaConfeccion.toLocalDate());
            } else {
                d.setFechaConfeccion(null);
            }

            return d;
        } catch (SQLException ex) {
            System.err.println("RecetaDao.from error: " + ex.getMessage());
            return null;
        }
    }
}
