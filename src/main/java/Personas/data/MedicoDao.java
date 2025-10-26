package Personas.data;

import Personas.logic.Medico;
import Personas.logic.Persona;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicoDao extends TrabajadorDao {


    public MedicoDao(){
        super();
    }


    public void create(Medico m) throws Exception {
        super.create(m); // Inserta en Persona + Trabajador
        String sql = "INSERT INTO Medico (gafete, especialidad) VALUES (?, ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getGafete());
        stm.setString(2, m.getEspecialidad());
        int count = db.executeUpdate(stm);
        if (count == 0) throw new Exception("No se pudo insertar Médico");
    }

    public Medico read(String gafete) throws Exception {
        String sql = "SELECT p.id, p.name, p.rol, t.gafete, t.claveSistema, m.especialidad " +
                "FROM Medico m " +
                "JOIN Trabajador t ON m.gafete = t.gafete " +
                "JOIN Persona p ON t.id = p.id " +
                "WHERE m.gafete = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, gafete);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            Medico p = from(rs); // quitar "p"
            return p;
        } else {
            throw new Exception("Medico no Existe");
        }
    }

    /*
    public void update(Medico p) throws Exception{
        String sqlMedico = "UPDATE Medico SET especialidad = ? WHERE gafete = ?";
        PreparedStatement stmMedico = db.prepareStatement(sqlMedico);
        stmMedico.setString(1, p.getEspecialidad());
        stmMedico.setString(2, p.getGafete());
        stmMedico.executeUpdate();
        int count=db.executeUpdate(stmMedico);
        if (count==0){
            throw new Exception("Medico ya existe");
        }
        if (count==0){
            throw new Exception("Medico no existe");
        }
    }

     */


    public void update(Medico p) throws Exception{

        String sqlMedico = "UPDATE Medico SET especialidad = ? WHERE gafete = ?";
        PreparedStatement stmMedico = db.prepareStatement(sqlMedico);
        stmMedico.setString(1, p.getEspecialidad());
        stmMedico.setString(2, p.getGafete());
        int count = db.executeUpdate(stmMedico);

        if (count == 0){
            throw new Exception("Medico no existe");
        }
    }

    public void delete(Medico o) throws Exception{
        String sql="delete from Medico where gafete=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, o.getGafete());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medico no existe");
        }
    }

    public List<Medico> findByNombre(Medico filtro){
        List<Medico> resultado = new ArrayList<Medico>();
        try {
            String sql = "SELECT p.id, p.name, p.rol, t.gafete, t.claveSistema, m.especialidad " +
                    "FROM Medico m " +
                    "JOIN Trabajador t ON m.gafete = t.gafete " +
                    "JOIN Persona p ON t.id = p.id " +
                    "WHERE p.name LIKE ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%"+filtro.getName()+"%");
            ResultSet rs =  db.executeQuery(stm);
            Medico p;
            while (rs.next()) {
                p= from(rs);
                resultado.add(p);
            }
        } catch (SQLException ex) {  }
        return resultado;
    }

    private Medico from(ResultSet rs) {
        Medico m = new Medico();
        try {
            m.setId(rs.getString("id"));                 // de Persona
            m.setName(rs.getString("name"));           // de Persona
            m.setGafete(rs.getString("gafete"));         // de Trabajador
            m.setRol(rs.getString("rol"));             // de Trabajador
            m.setClave_sistema(rs.getString("claveSistema")); // de Trabajador
            m.setEspecialidad(rs.getString("especialidad"));  // de Medico
            return m;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return m;
        }
    }
}

