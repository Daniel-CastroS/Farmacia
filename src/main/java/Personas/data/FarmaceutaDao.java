package Personas.data;

import Personas.logic.Farmaceuta;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FarmaceutaDao {
    Database db;

    public void FarmaceutaDao(){
        db= Database.instance();
    }
    public void create(Farmaceuta p) throws Exception{
        String sql="insert into Farmaceuta (id, name, rol, claveSistema" +
                "values(?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getId());
        stm.setString(2, p.getName());
        stm.setString(3, p.getRol());
        stm.setString(4, p.getClave_sistema());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Farmaceuta ya existe");
        }
    }

    public Farmaceuta read(String id) throws Exception{
        String sql="select * from Farmaceuta p "+
                "where p.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs =  db.executeQuery(stm);
        Farmaceuta p;
        if (rs.next()) {
            p= from(rs,"p");
            return p;
        }
        else{
            throw new Exception ("Farmaceuta no Existe");
        }
    }

    public void update(Farmaceuta p) throws Exception{
        String sql="update farmaceuta set id=?,name=?,rol=?,claveSistema,"+
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getName());
        stm.setString(2, p.getId());
        stm.setString(3, p.getRol());
        stm.setString(4, p.getClave_sistema());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Farmaceuta ya existe");
        }
        if (count==0){
            throw new Exception("Farmaceuta no existe");
        }
    }

    public void delete(Farmaceuta o) throws Exception{
        String sql="delete from Farmaceuta where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, o.getId());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Farmaceuta no existe");
        }
    }

    public List<Farmaceuta> findByNombre(Farmaceuta filtro){
        List<Farmaceuta> resultado = new ArrayList<Farmaceuta>();
        try {
            String sql="select * from Farmaceuta p "+
                    "where p.nombre like ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%"+filtro.getName()+"%");
            ResultSet rs =  db.executeQuery(stm);
            Farmaceuta p;
            while (rs.next()) {
                p= from(rs,"p");
                resultado.add(p);
            }
        } catch (SQLException ex) {  }
        return resultado;
    }

    private Farmaceuta from(ResultSet rs, String alias){
        try {
            Farmaceuta p= new Farmaceuta();
            p.setName(rs.getString(alias + ".name"));
            p.setId(rs.getString(alias + ".id"));
            p.setRol(rs.getString(alias + ".rol"));
            p.setClave_sistema(rs.getString(alias + ".claveSistema"));
            return p;
        } catch (SQLException ex) {
            return null;
        }
    }
}
