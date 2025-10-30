package Personas.Data;

import Personas.Logic.Medicamento;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDao {
    Database db;

    public MedicamentoDao(){
        db= Database.instance();
    }

    public void create(Medicamento p) throws Exception{
        String sql="insert into Medicamento (codigo, nombre, presentacion) " +
                "values(?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getCodigo());
        stm.setString(2, p.getNombre());
        stm.setString(3, p.getPresentacion());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("No se pudo crear el Medicamento");
        }
    }

    public Medicamento read(String codigo) throws Exception{
        String sql="select * from Medicamento d where codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, codigo);
        ResultSet rs =  db.executeQuery(stm);
        if (rs.next()) {
            return from(rs);

        }
        else{
            throw new Exception ("Medicamento no Existe");
        }
    }

    public void update(Medicamento p) throws Exception{
        String sql="update medicamento set codigo=?,nombre=?,presentacion=? " +
                "where codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getCodigo());
        stm.setString(2, p.getNombre());
        stm.setString(3, p.getPresentacion());
        stm.setString(4, p.getCodigo());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medicamento no existe");
        }
    }

    public void delete(Medicamento o) throws Exception{
        String sql="delete from Medicamento where codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, o.getCodigo());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medicamento no existe");
        }
    }

    public List<Medicamento> findByNombre(Medicamento filtro){
        List<Medicamento> resultado = new ArrayList<>();
        try {
            String sql="select * from Medicamento p "+
                    "where p.nombre like ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%"+filtro.getNombre()+"%");
            ResultSet rs =  db.executeQuery(stm);
            Medicamento p;
            while (rs.next()) {
                p = from(rs);

                resultado.add(p);
            }
        } catch (SQLException ex) {  ex.printStackTrace(); }
        return resultado;
    }

    public List<Medicamento> findAll(){
        List<Medicamento> ds=new ArrayList<Medicamento>();
        try {
            String sql="select * from Medicamento d";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs =  db.executeQuery(stm);
            while (rs.next()) {
                ds.add(from(rs));
            }
        } catch (SQLException ex) { }
        return ds;
    }

    public Medicamento from(ResultSet rs) {
        try {
            Medicamento d = new Medicamento();
            d.setCodigo(rs.getString("codigo"));
            d.setNombre(rs.getString("nombre"));
            d.setPresentacion(rs.getString("presentacion"));
            return d;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}

