package Personas.data;

import Personas.logic.Medicamento;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDao {
    Database db;

    public void MedicamentoDao(){
        db= Database.instance();
    }
    public Medicamento read(String codigo) throws Exception{
        String sql="select * from Medicamento d where codigo=?";
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

    public List<Medicamento> findAll(){
        List<Medicamento> ds=new ArrayList<Medicamento>();
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

    public Medicamento from(ResultSet rs, String alias){
        try {
            Medicamento d= new Medicamento();
            d.setCodigo(rs.getString(alias + ".codigo"));
            d.setNombre(rs.getString(alias + ".nombre"));

            d.setPresentacion(rs.getString(alias) + ".presentacion");
            return d;
        } catch (SQLException ex) {
            return null;
        }
    }
}
