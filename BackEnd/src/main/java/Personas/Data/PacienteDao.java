package Personas.Data;

import Personas.Logic.Medicamento;
import Personas.Logic.Paciente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao extends PersonaDao {

    public PacienteDao() {
        super();
    }

    // CREATE
    public void create(Paciente p) throws Exception {
        super.create(p); // Inserta en Persona
        String sql = "INSERT INTO Paciente (id, telefono, fechaNac) VALUES (?, ?, ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getId());
        stm.setString(2, p.getTelefono());
        if (p.getFechaNac() != null) {
            stm.setDate(3, Date.valueOf(p.getFechaNac()));
        } else {
            stm.setNull(3, java.sql.Types.DATE);
        }

        int count = db.executeUpdate(stm);
        if (count == 0) throw new Exception("No se pudo crear el paciente");
    }

    // READ
    public Paciente read(String id) throws Exception {
        String sql = "SELECT p.id, p.name, p.rol, pa.telefono, pa.fechaNac " +
                "FROM Persona p JOIN Paciente pa ON p.id = pa.id " +
                "WHERE p.id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            return from(rs);
        } else {
            throw new Exception("Paciente no existe");
        }
    }

    // UPDATE
    public void update(Paciente p) throws Exception {
        super.update(p); // Actualiza Persona
        String sql = "UPDATE Paciente SET telefono=?, fechaNac=? WHERE id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getTelefono());
        if (p.getFechaNac() != null) {
            stm.setDate(2, Date.valueOf(p.getFechaNac()));
        } else {
            stm.setNull(2, java.sql.Types.DATE);
        }
        stm.setString(3, p.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) throw new Exception("Paciente no existe");
    }

    // DELETE
    public void delete(Paciente p) throws Exception {
        String sql = "DELETE FROM Paciente WHERE id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) throw new Exception("Paciente no existe");
        super.delete(p); // elimina Persona al final
    }

    // FIND BY NAME
    public List<Paciente> findByNombre(Paciente filtro) {
        List<Paciente> resultado = new ArrayList<>();
        try {
            String sql = "SELECT p.id, p.name, p.rol, pa.telefono, pa.fechaNac " +
                    "FROM Persona p JOIN Paciente pa ON p.id = pa.id " +
                    "WHERE p.name LIKE ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%" + filtro.getName() + "%");
            ResultSet rs = db.executeQuery(stm);
            while (rs.next()) {
                resultado.add(from(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultado;
    }

    public List<Paciente> findAll(){
        List<Paciente> ds= new ArrayList<Paciente>();
        try {
            String sql = "SELECT p.id, p.name, p.rol, pa.telefono, pa.fechaNac " +
                    "FROM Persona p JOIN Paciente pa ON p.id = pa.id";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs =  db.executeQuery(stm);
            while (rs.next()) {
                ds.add(from(rs));
            }
        } catch (SQLException ex) { }
        return ds;
    }

    // MAPEO
    public Paciente from(ResultSet rs) {
        Paciente p = new Paciente();
        try {
            p.setId(rs.getString("id"));
            p.setName(rs.getString("name"));
            p.setRol(rs.getString("rol"));
            p.setTelefono(rs.getString("telefono"));
            Date sqlDate = rs.getDate("fechaNac");
            if (sqlDate != null) p.setFechaNac(sqlDate.toLocalDate());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return p;
    }
}
