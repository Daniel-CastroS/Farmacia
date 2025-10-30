package Personas.Data;

import Personas.Logic.Farmaceuta;
import Personas.Logic.Paciente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FarmaceutaDao extends TrabajadorDao {

    public FarmaceutaDao() {
        super();
    }

    // Crear farmaceuta: primero Persona+Trabajador, luego Farmaceuta
    public void create(Farmaceuta f) throws Exception {
        super.create(f); // Inserta en Persona + Trabajador
        String sql = "INSERT INTO Farmaceuta (gafete) VALUES (?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, f.getGafete());
        int count = db.executeUpdate(stm);
        if (count == 0) throw new Exception("No se pudo insertar Farmaceuta");
    }

    // Leer un farmaceuta
    public Farmaceuta read(String gafete) throws Exception {
        String sql = "SELECT p.id, p.name, p.rol, t.gafete, t.claveSistema " +
                "FROM Farmaceuta f " +
                "JOIN Trabajador t ON f.gafete = t.gafete " +
                "JOIN Persona p ON t.id = p.id " +
                "WHERE f.gafete = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, gafete);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) return from(rs);
        else throw new Exception("Farmaceuta no existe");
    }

    // Update de farmaceuta
    public void update(Farmaceuta f) throws Exception {
        // Solo se actualizan campos de Persona y Trabajador
        super.update(f); // update en Persona + Trabajador
    }

    // Borrar farmaceuta
    public void delete(Farmaceuta f) throws Exception {
        String sql = "DELETE FROM Farmaceuta WHERE gafete=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, f.getGafete());
        int count = db.executeUpdate(stm);
        if (count == 0) throw new Exception("Farmaceuta no existe");
        super.delete(f); // borrar Trabajador + Persona
    }

    // Buscar por nombre
    public List<Farmaceuta> findByNombre(Farmaceuta filtro){
        List<Farmaceuta> resultado = new ArrayList<>();
        try {
            String sql = "SELECT p.id, p.name, p.rol, t.gafete, t.claveSistema " +
                    "FROM Farmaceuta f " +
                    "JOIN Trabajador t ON f.gafete = t.gafete " +
                    "JOIN Persona p ON t.id = p.id " +
                    "WHERE p.name LIKE ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%"+filtro.getName()+"%");
            ResultSet rs = db.executeQuery(stm);
            while (rs.next()) resultado.add(from(rs));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultado;
    }
/*
    public List<Farmaceuta> findAll(){
        List<Farmaceuta> ds= new ArrayList<Farmaceuta>();
        try {
            String sql="select * from Farmaceuta d";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs =  db.executeQuery(stm);
            while (rs.next()) {
                ds.add(from(rs));
            }
        } catch (SQLException ex) { }
        return ds;
    }

 */
public List<Farmaceuta> findAll(){
    List<Farmaceuta> ds = new ArrayList<>();
    try {
        String sql = "SELECT p.id, p.name, p.rol, t.gafete, t.claveSistema " +
                "FROM Farmaceuta f " +
                "JOIN Trabajador t ON f.gafete = t.gafete " +
                "JOIN Persona p ON t.id = p.id";
        PreparedStatement stm = db.prepareStatement(sql);
        ResultSet rs = db.executeQuery(stm);
        while (rs.next()) {
            ds.add(from(rs));
        }
    } catch (SQLException ex) {
        ex.printStackTrace(); // Para ver el error si ocurre
    }
    return ds;
}

    private Farmaceuta from(ResultSet rs) throws SQLException {
        Farmaceuta f = new Farmaceuta();
        f.setId(rs.getString("id"));
        f.setName(rs.getString("name"));
        f.setRol(rs.getString("rol"));
        f.setGafete(rs.getString("gafete"));
        f.setClave_sistema(rs.getString("claveSistema"));
        return f;
    }
}
