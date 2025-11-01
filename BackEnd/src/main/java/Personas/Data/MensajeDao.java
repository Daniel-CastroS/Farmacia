package Personas.Data;

import Personas.Logic.Mensaje;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MensajeDao {
    Database db;

    public MensajeDao() {
        db = Database.instance();
    }


    public void create(Mensaje m) throws Exception {
        String sql = "INSERT INTO Mensaje (remitenteId, remitenteNombre, destinatarioId, destinatarioNombre, contenido, fecha, leido) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getRemitenteId());
        stm.setString(2, m.getRemitenteNombre());
        stm.setString(3, m.getDestinatarioId());
        stm.setString(4, m.getDestinatarioNombre());
        stm.setString(5, m.getContenido());
        stm.setTimestamp(6, Timestamp.valueOf(m.getFecha()));
        stm.setBoolean(7, m.isLeido());

        int count = db.executeUpdate(stm);
        if (count == 0) throw new Exception("No se pudo crear el mensaje");
    }


    public Mensaje read(int id) throws Exception {
        String sql = "SELECT * FROM Mensaje WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, id);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) return from(rs);
        else throw new Exception("Mensaje no existe");
    }

    public List<Mensaje> getPendingMessages(String destinatarioId) {
        List<Mensaje> mensajes = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Mensaje WHERE destinatarioId = ? AND leido = false ORDER BY fecha ASC";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, destinatarioId);
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                mensajes.add(from(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return mensajes;
    }


    public List<Mensaje> getAllMessages(String destinatarioId) {
        List<Mensaje> mensajes = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Mensaje WHERE destinatarioId = ? ORDER BY fecha DESC";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, destinatarioId);
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                mensajes.add(from(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return mensajes;
    }

    public void markAsRead(int messageId) throws Exception {
        String sql = "UPDATE Mensaje SET leido = true WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, messageId);
        int count = db.executeUpdate(stm);
        if (count == 0) throw new Exception("Mensaje no existe");
    }

    private Mensaje from(ResultSet rs) throws SQLException {
        Mensaje m = new Mensaje();
        m.setId(rs.getInt("id"));
        m.setRemitenteId(rs.getString("remitenteId"));
        m.setRemitenteNombre(rs.getString("remitenteNombre"));
        m.setDestinatarioId(rs.getString("destinatarioId"));
        m.setDestinatarioNombre(rs.getString("destinatarioNombre"));
        m.setContenido(rs.getString("contenido"));

        Timestamp ts = rs.getTimestamp("fecha");
        if (ts != null) m.setFecha(ts.toLocalDateTime());

        m.setLeido(rs.getBoolean("leido"));
        return m;
    }
}