package Personas.data;

import Personas.logic.MedicamentoRecetado;
import Personas.logic.Receta;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecetaDao {
    Database db;

    public void RecetaDao(){
        db= Database.instance();
    }
    public Receta read(String codigo) throws Exception{
        String sql="select * from Receta d"+
                "inner join Paciente p on p.codigo=d.paciente "+
                "where codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, codigo);
        ResultSet rs =  db.executeQuery(stm);
        if (rs.next()) {
            return from(rs,"d");
        }
        else{
            throw new Exception ("Receta no Existe");
        }
    }

    public List<Receta> findAll(){
        List<Receta> ds=new ArrayList<Receta>();
        try {
            String sql="select * from Receta d";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs =  db.executeQuery(stm);
            while (rs.next()) {
                ds.add(from(rs,"d"));
            }
        } catch (SQLException ex) { }
        return ds;
    }

    public Receta from(ResultSet rs, String alias){
        PacienteDao pd= new PacienteDao();
        MedicamentoRecetadoDao mr= new MedicamentoRecetadoDao();
        try {
            Receta d= new Receta();
            try{
                d.setPaciente(pd.from(rs,".paciente"));
            } catch(Exception e){
                //ignorar
            }
            try{
                Array medicamentosArray = rs.getArray(".medicamentos");
                if (medicamentosArray != null) {
                    String[] medicamentos = (String[]) medicamentosArray.getArray();
                    for (String medicamento : medicamentos) {
                        d.getMedicamentos().add(mr.from(rs, medicamento));
                    }
                }
            } catch(Exception e){
                //ignorar
            }
            d.setEstado(rs.getString(".estado"));
            d.setFechaRetiro(rs.getString(".fechaRetiro"));
            d.setFechaConfeccion(rs.getString(".fechaConfeccion"));
            return d;
        } catch (SQLException ex) {
            return null;
        }
    }
}
