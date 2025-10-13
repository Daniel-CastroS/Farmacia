package Personas.data;

import Personas.logic.MedicamentoRecetado;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoRecetadoDao {
    Database db;

    public void MedicamentoRecetadoDao(){
        db= Database.instance();
    }
    public MedicamentoRecetado read(String codigo) throws Exception{
        String sql="select * from MedicamentoRecetado d"+
                "inner join Medicamento m on m.codigo=d.medicamento "+
                "where codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, codigo);
        ResultSet rs =  db.executeQuery(stm);
        if (rs.next()) {
            return from(rs,"d");
        }
        else{
            throw new Exception ("Medicamento no Existe");
        }
    }

    public List<MedicamentoRecetado> findAll(){
        List<MedicamentoRecetado> ds=new ArrayList<MedicamentoRecetado>();
        try {
            String sql="select * from Medicamento d";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs =  db.executeQuery(stm);
            while (rs.next()) {
                ds.add(from(rs,"d"));
            }
        } catch (SQLException ex) { }
        return ds;
    }

    public MedicamentoRecetado from(ResultSet rs, String alias){
        MedicamentoDao md= new MedicamentoDao();
        try {
            MedicamentoRecetado d= new MedicamentoRecetado();
            try{
                d.setMedicamento(md.from(rs,".medicamento"));
            } catch(Exception e){
                //ignorar
            }
            d.setCantidad(rs.getInt(".cantidad"));
            d.setIndicaciones(rs.getString(".indicaciones"));
            d.setDuracionDias(rs.getInt(".duracionDias"));
            return d;
        } catch (SQLException ex) {
            return null;
        }
    }
}
