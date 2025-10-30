package Personas.Data;

import Personas.Logic.Trabajador;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Personas.Data.PersonaDao;

import java.sql.SQLException;

public class TrabajadorDao extends PersonaDao {


    public TrabajadorDao() {
        super();
    }


    public void create(Trabajador t) throws Exception {
        // Crear Persona primero
        super.create(t);

        // Luego insertar en Trabajador
        String sql = "INSERT INTO Trabajador (gafete, claveSistema, id) VALUES (?, ?, ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, t.getGafete());
        stm.setString(2, t.getClave_sistema());
        stm.setString(3, t.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo insertar Trabajador");
        }
    }

    public Trabajador read(String gafete) throws Exception {
        String sql = "SELECT p.id, p.name, p.rol, t.gafete, t.claveSistema " +
                "FROM Trabajador t JOIN Persona p ON t.id = p.id " +
                "WHERE t.gafete = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, gafete);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            Trabajador t = new Trabajador();
            t.setId(rs.getString("id"));
            t.setName(rs.getString("name"));
            t.setRol(rs.getString("rol"));
            t.setGafete(rs.getString("gafete"));
            t.setClave_sistema(rs.getString("claveSistema"));
            return t;
        } else {
            throw new Exception("Trabajador no existe");
        }
    }

    public void update(Trabajador t) throws Exception {
        super.update(t);
        String sql = "UPDATE Trabajador SET claveSistema=? WHERE gafete=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, t.getClave_sistema());
        stm.setString(2, t.getGafete());
        int count = db.executeUpdate(stm);
        if (count == 0) throw new Exception("Trabajador no existe");
    }

    public void delete(Trabajador t) throws Exception {
        String sql = "DELETE FROM Trabajador WHERE gafete=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, t.getGafete());
        int count = db.executeUpdate(stm);
        if (count == 0) throw new Exception("Trabajador no existe");

        // Finalmente, eliminar también la Persona
        super.delete(t);
    }
}
