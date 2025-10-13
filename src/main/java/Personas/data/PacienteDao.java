package Personas.data;

import Personas.logic.Paciente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao {
    Database db;

    public void PacienteDao(){
        db= Database.instance();
    }
    public void create(Paciente p) throws Exception{
        String sql="insert into Paciente (id, name, rol, telefono, fechaNac" +
                "values(?,?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getId());
        stm.setString(2, p.getName());
        stm.setString(3, p.getTelefono());
        stm.setString(4, p.getRol());
        stm.setString(5, p.getFechaNac());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Paciente ya existe");
        }
    }

    public Paciente read(String id) throws Exception{
        String sql="select * from Paciente p "+
                "where p.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs =  db.executeQuery(stm);
        Paciente p;
        if (rs.next()) {
            p= from(rs,"p");
            return p;
        }
        else{
            throw new Exception ("Paciente no Existe");
        }
    }

    public void update(Paciente p) throws Exception{
        String sql="update paciente set id=?,name=?,rol=?,telefono=?,fechaNac,"+
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getName());
        stm.setString(2, p.getId());
        stm.setString(3, p.getRol());
        stm.setString(4, p.getTelefono());
        stm.setString(5, p.getFechaNac());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Paciente ya existe");
        }
        if (count==0){
            throw new Exception("Paciente no existe");
        }
    }

    public void delete(Paciente o) throws Exception{
        String sql="delete from Paciente where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, o.getId());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Paciente no existe");
        }
    }

    public List<Paciente> findByNombre(Paciente filtro){
        List<Paciente> resultado = new ArrayList<Paciente>();
        try {
            String sql="select * from Paciente p "+
                    "where p.nombre like ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%"+filtro.getName()+"%");
            ResultSet rs =  db.executeQuery(stm);
            Paciente p;
            while (rs.next()) {
                p= from(rs,"p");
                resultado.add(p);
            }
        } catch (SQLException ex) {  }
        return resultado;
    }

    public Paciente from(ResultSet rs, String alias){
        try {
            Paciente p= new Paciente();
            p.setName(rs.getString(alias + ".name"));
            p.setId(rs.getString(alias + ".id"));
            p.setTelefono(rs.getString(alias + ".telefono"));
            p.setRol(rs.getString(alias + ".rol"));
            p.setFechaNac(rs.getString(alias + ".fechaNac"));
            return p;
        } catch (SQLException ex) {
            return null;
        }
    }
}
