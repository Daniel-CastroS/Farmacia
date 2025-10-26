package Personas.data;

import Personas.logic.Persona;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonaDao {
    Database db;

    public PersonaDao() {
        db = Database.instance();
    }

    public void create(Persona p) throws Exception {
        String sql = "INSERT INTO Persona (id, name, rol) VALUES (?, ?, ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getId());
        stm.setString(2, p.getName());
        stm.setString(3, p.getRol());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo insertar Persona");
        }
    }

    public Persona read(String id) throws Exception {
        String sql = "SELECT * FROM Persona WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            Persona p = new Persona();
            p.setId(rs.getString("id"));
            p.setName(rs.getString("name"));
            p.setRol(rs.getString("rol"));
            return p;
        } else {
            throw new Exception("Persona no existe");
        }
    }

    public void update(Persona p) throws Exception {
        String sql = "UPDATE Persona SET name=?, rol=? WHERE id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getName());
        stm.setString(2, p.getRol());
        stm.setString(3, p.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) throw new Exception("Persona no existe");
    }

    public void delete(Persona p) throws Exception {
        String sql = "DELETE FROM Persona WHERE id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) throw new Exception("Persona no existe");
    }

    public List<Persona> findByName(String name) {
        List<Persona> result = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Persona WHERE name LIKE ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%" + name + "%");
            ResultSet rs = db.executeQuery(stm);
            while (rs.next()) {
                Persona p = new Persona();
                p.setId(rs.getString("id"));
                p.setName(rs.getString("name"));
                p.setRol(rs.getString("rol"));
                result.add(p);
            }
        } catch (SQLException ex) { }
        return result;
    }
}
