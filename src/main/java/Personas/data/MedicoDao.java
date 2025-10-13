package Personas.data;

import Personas.logic.Medico;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicoDao {
    Database db;

    public void MedicoDao(){
        db= Database.instance();
    }
    public void create(Medico p) throws Exception{
        String sql="insert into Medico (id, name, rol, especialidad, claveSistema" +
                "values(?,?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getId());
        stm.setString(2, p.getName());
        stm.setString(3, p.getEspecialidad());
        stm.setString(4, p.getRol());
        stm.setString(5, p.getClave_sistema());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medico ya existe");
        }
    }

    public Medico read(String id) throws Exception{
        String sql="select * from Medico p "+
                "where p.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs =  db.executeQuery(stm);
        Medico p;
        if (rs.next()) {
            p= from(rs,"p");
            return p;
        }
        else{
            throw new Exception ("Medico no Existe");
        }
    }

    public void update(Medico p) throws Exception{
        String sql="update medico set id=?,name=?,rol=?,especialidad=?,claveSistema,"+
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getName());
        stm.setString(2, p.getId());
        stm.setString(3, p.getRol());
        stm.setString(4, p.getEspecialidad());
        stm.setString(5, p.getClave_sistema());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medico ya existe");
        }
        if (count==0){
            throw new Exception("Medico no existe");
        }
    }

    public void delete(Medico o) throws Exception{
        String sql="delete from Medico where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, o.getId());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medico no existe");
        }
    }

    public List<Medico> findByNombre(Medico filtro){
        List<Medico> resultado = new ArrayList<Medico>();
        try {
            String sql="select * from Medico p "+
                    "where p.nombre like ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%"+filtro.getName()+"%");
            ResultSet rs =  db.executeQuery(stm);
            Medico p;
            while (rs.next()) {
                p= from(rs,"p");
                resultado.add(p);
            }
        } catch (SQLException ex) {  }
        return resultado;
    }

    private Medico from(ResultSet rs, String alias){
        try {
            Medico p= new Medico();
            p.setName(rs.getString(alias + ".name"));
            p.setId(rs.getString(alias + ".id"));
            p.setEspecialidad(rs.getString(alias + ".especialidad"));
            p.setRol(rs.getString(alias + ".rol"));
            p.setClave_sistema(rs.getString(alias + ".claveSistema"));
            return p;
        } catch (SQLException ex) {
            return null;
        }
    }
}

