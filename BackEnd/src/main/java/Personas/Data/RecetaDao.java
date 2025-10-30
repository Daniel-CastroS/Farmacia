package Personas.Data;

import Personas.Logic.MedicamentoRecetado;
import Personas.Logic.Receta;
import Personas.Logic.Paciente;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecetaDao {
    Database db;

    public RecetaDao(){
        db= Database.instance();
    }

    public void create(Receta p) throws Exception{
        if (p.getFechaConfeccion() == null) {
            p.setFechaConfeccion(LocalDate.now());
        }

        String sql="insert into Receta (paciente, fechaConfeccion, fechaRetiro, estado) " +
                "values(?,?,?,?)";

        PreparedStatement stm = db.cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stm.setString(1, p.getPaciente().getId());
        stm.setDate(2, Date.valueOf(p.getFechaConfeccion()));

        if (p.getFechaRetiro() != null) {
            stm.setDate(3, Date.valueOf(p.getFechaRetiro()));
        } else {
            stm.setNull(3, java.sql.Types.DATE);
        }

        stm.setString(4, p.getEstado());

        int count = stm.executeUpdate();

        if (count == 0){
            throw new Exception("No se pudo crear la Receta");
        }


        ResultSet generatedKeys = stm.getGeneratedKeys();
        if (generatedKeys.next()) {
            int generatedId = generatedKeys.getInt(1);
            p.setId(generatedId);

        } else {
            throw new Exception("No se pudo obtener el ID generado");
        }
    }

    public Receta read(int id) throws Exception{
        String sql="select r.*, pa.telefono, pa.fechaNac, per.name, per.rol " +
                "from Receta r " +
                "left join Paciente pa on pa.id = r.paciente " +
                "left join Persona per on per.id = pa.id " +
                "where r.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, id);
        ResultSet rs =  db.executeQuery(stm);
        if (rs.next()) {
            return from(rs,"r");
        }
        else{
            throw new Exception ("Receta no Existe");
        }
    }

    public void update(Receta p) throws Exception{
        String sql="update receta set id=?,paciente=?,fechaConfeccion=?, fechaRetiro=?, estado=? " +
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, p.getId());
        stm.setString(2, p.getPaciente().getId());
        stm.setDate(3, Date.valueOf(p.getFechaConfeccion()));

        if (p.getFechaRetiro() != null) {
            stm.setDate(4, Date.valueOf(p.getFechaRetiro()));
        } else {
            stm.setNull(4, java.sql.Types.DATE);
        }

        stm.setString(5, p.getEstado());
        stm.setInt(6, p.getId());

        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Receta no existe");
        }
    }

    public void delete(Receta o) throws Exception{
        String sql="delete from Receta where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, o.getId());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Receta no existe");
        }
    }

    public List<Receta> findAll(){
        List<Receta> ds=new ArrayList<>();
        try {
            String sql="select r.*, pa.telefono, pa.fechaNac, per.name, per.rol " +
                    "from Receta r " +
                    "left join Paciente pa on pa.id = r.paciente " +
                    "left join Persona per on per.id = pa.id";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs =  db.executeQuery(stm);
            while (rs.next()) {
                ds.add(from(rs,"r"));
            }
        } catch (SQLException ex) {
            System.err.println("RecetaDao.findAll error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return ds;
    }

    public List<Receta> findByPaciente(String pacienteId){
        List<Receta> resultado = new ArrayList<>();
        try {
            String sql="select r.*, pa.telefono, pa.fechaNac, per.name, per.rol " +
                    "from Receta r " +
                    "left join Paciente pa on pa.id = r.paciente " +
                    "left join Persona per on per.id = pa.id " +
                    "where r.paciente = ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, pacienteId);
            ResultSet rs =  db.executeQuery(stm);
            Receta d;
            while (rs.next()) {
                d= from(rs,"r");
                resultado.add(d);
            }
        } catch (SQLException ex) {
            System.err.println("RecetaDao.findByPaciente error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return resultado;
    }

    public Receta from(ResultSet rs, String alias){
        MedicamentoRecetadoDao mr= new MedicamentoRecetadoDao();
        try {
            Receta d= new Receta();

            // Leer campos de la receta
            d.setId(rs.getInt("id"));
            d.setEstado(rs.getString("estado"));

            // Leer fechas
            java.sql.Date sqlFechaConfeccion = rs.getDate("fechaConfeccion");
            if (sqlFechaConfeccion != null) {
                d.setFechaConfeccion(sqlFechaConfeccion.toLocalDate());
            }

            java.sql.Date sqlFechaRetiro = rs.getDate("fechaRetiro");
            if (sqlFechaRetiro != null) {
                d.setFechaRetiro(sqlFechaRetiro.toLocalDate());
            }

            // Leer paciente del JOIN (ahora con Persona incluida)
            try{
                String pacienteId = rs.getString("paciente");
                if (pacienteId != null && !pacienteId.isEmpty()) {
                    Paciente pac = new Paciente();
                    pac.setId(pacienteId);

                    // Leer datos de Persona (name y rol)
                    try {
                        pac.setName(rs.getString("name"));
                    } catch (Exception e) {
                        System.err.println("Error leyendo name: " + e.getMessage());
                    }

                    try {
                        pac.setRol(rs.getString("rol"));
                    } catch (Exception e) {
                        System.err.println("Error leyendo rol: " + e.getMessage());
                    }

                    // Leer datos específicos de Paciente
                    try {
                        pac.setTelefono(rs.getString("telefono"));
                    } catch (Exception e) {
                        System.err.println("Error leyendo telefono: " + e.getMessage());
                    }

                    try {
                        java.sql.Date sqlFechaNac = rs.getDate("fechaNac");
                        if (sqlFechaNac != null) {
                            pac.setFechaNac(sqlFechaNac.toLocalDate());
                        }
                    } catch (Exception e) {
                        System.err.println("Error leyendo fechaNac: " + e.getMessage());
                    }

                    d.setPaciente(pac);
                }
            } catch(Exception e){
                System.err.println("Error cargando paciente: " + e.getMessage());
                e.printStackTrace();
            }

            // Cargar medicamentos recetados
            try{
                List<MedicamentoRecetado> prescripcionesArray = mr.findByReceta(d.getId());
                for (MedicamentoRecetado prescripcion : prescripcionesArray) {
                    d.getMedicamentos().add(prescripcion);
                }
            } catch(Exception e){
                System.err.println("Error cargando medicamentos: " + e.getMessage());
            }

            return d;
        } catch (SQLException ex) {
            System.err.println("RecetaDao.from error: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
}





/*
package Personas.Data;

import Personas.Logic.MedicamentoRecetado;
import Personas.Logic.Receta;
import Personas.Logic.Paciente;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecetaDao {
    Database db;

    public RecetaDao(){
        db= Database.instance();
    }

    public void create(Receta p) throws Exception{
        if (p.getFechaConfeccion() == null) {
            p.setFechaConfeccion(LocalDate.now());
        }

        String sql="insert into Receta (paciente, fechaConfeccion, fechaRetiro, estado) " +
                "values(?,?,?,?)";

        PreparedStatement stm = db.cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stm.setString(1, p.getPaciente().getId());
        stm.setDate(2, Date.valueOf(p.getFechaConfeccion()));

        if (p.getFechaRetiro() != null) {
            stm.setDate(3, Date.valueOf(p.getFechaRetiro()));
        } else {
            stm.setNull(3, java.sql.Types.DATE);
        }

        stm.setString(4, p.getEstado());

        int count = stm.executeUpdate();

        if (count == 0){
            throw new Exception("No se pudo crear la Receta");
        }


        ResultSet generatedKeys = stm.getGeneratedKeys();
        if (generatedKeys.next()) {
            int generatedId = generatedKeys.getInt(1);
            p.setId(generatedId);

        } else {
            throw new Exception("No se pudo obtener el ID generado");
        }
    }

    public Receta read(int id) throws Exception{
        String sql="select d.*, p.* from Receta d " +
                "left join Paciente p on p.id = d.paciente " +
                "where d.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, id);
        ResultSet rs =  db.executeQuery(stm);
        if (rs.next()) {
            return from(rs,"d");
        }
        else{
            throw new Exception ("Receta no Existe");
        }
    }

    public void update(Receta p) throws Exception{
        String sql="update receta set id=?,paciente=?,fechaConfeccion=?, fechaRetiro=?, estado=? " +
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, p.getId());
        stm.setString(2, p.getPaciente().getId());
        stm.setDate(3, Date.valueOf(p.getFechaConfeccion()));

        if (p.getFechaRetiro() != null) {
            stm.setDate(4, Date.valueOf(p.getFechaRetiro()));
        } else {
            stm.setNull(4, java.sql.Types.DATE);
        }

        stm.setString(5, p.getEstado());
        stm.setInt(6, p.getId());

        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Receta no existe");
        }
    }

    public void delete(Receta o) throws Exception{
        String sql="delete from Receta where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, o.getId());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Receta no existe");
        }
    }

    public List<Receta> findAll(){
        List<Receta> ds=new ArrayList<>();
        try {
            String sql="select d.*, p.* from Receta d left join Paciente p on p.id = d.paciente";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs =  db.executeQuery(stm);
            while (rs.next()) {
                ds.add(from(rs,"d"));
            }
        } catch (SQLException ex) { System.err.println("RecetaDao.findAll error: " + ex.getMessage()); }
        return ds;
    }

    public List<Receta> findByPaciente(String pacienteId){
        List<Receta> resultado = new ArrayList<>();
        try {
            String sql="select d.*, p.* from Receta d "+
                    "left join Paciente p on p.id = d.paciente "+
                    "where d.paciente = ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, pacienteId);
            ResultSet rs =  db.executeQuery(stm);
            Receta d;
            while (rs.next()) {
                d= from(rs,"d");
                resultado.add(d);
            }
        } catch (SQLException ex) {  System.err.println("RecetaDao.findByPaciente error: " + ex.getMessage()); }
        return resultado;
    }

    public Receta from(ResultSet rs, String alias){
        MedicamentoRecetadoDao mr= new MedicamentoRecetadoDao();
        try {
            Receta d= new Receta();

            // Leer campos de la receta usando nombres de columna
            d.setId(rs.getInt("id"));
            d.setEstado(rs.getString("estado"));

            // Leer fechas
            java.sql.Date sqlFechaConfeccion = rs.getDate("fechaConfeccion");
            if (sqlFechaConfeccion != null) {
                d.setFechaConfeccion(sqlFechaConfeccion.toLocalDate());
            }

            java.sql.Date sqlFechaRetiro = rs.getDate("fechaRetiro");
            if (sqlFechaRetiro != null) {
                d.setFechaRetiro(sqlFechaRetiro.toLocalDate());
            }

            // Leer paciente del JOIN - usar try-catch para detectar el problema
            try{
                // Intentar leer usando el ID del paciente de la receta
                String pacienteIdRef = rs.getString("paciente");
                if (pacienteIdRef != null && !pacienteIdRef.isEmpty()) {
                    // Intentar leer los datos del paciente del JOIN
                    String pacienteId = null;
                    try {
                        pacienteId = rs.getString("p.id");
                    } catch (SQLException e) {
                        // Si falla con alias, intentar sin alias
                        try {
                            // Buscar por índice después de las columnas de Receta
                            ResultSetMetaData metaData = rs.getMetaData();
                            int columnCount = metaData.getColumnCount();

                            // Buscar la columna que corresponde al paciente
                            for (int i = 1; i <= columnCount; i++) {
                                String columnName = metaData.getColumnName(i);
                                if (columnName.equalsIgnoreCase("id") && i > 5) {
                                    pacienteId = rs.getString(i);
                                    break;
                                }
                            }
                        } catch (Exception ex2) {
                            System.err.println("Error buscando columna paciente: " + ex2.getMessage());
                        }
                    }

                    if (pacienteId != null && !pacienteId.isEmpty()) {
                        Paciente pac = new Paciente();
                        pac.setId(pacienteId);

                        // Intentar leer el resto de campos del paciente
                        try { pac.setName(rs.getString("name")); } catch (Exception e) {}
                        try { pac.setRol(rs.getString("rol")); } catch (Exception e) {}
                        try { pac.setTelefono(rs.getString("telefono")); } catch (Exception e) {}
                        try {
                            java.sql.Date sqlFechaNac = rs.getDate("fechaNac");
                            if (sqlFechaNac != null) {
                                pac.setFechaNac(sqlFechaNac.toLocalDate());
                            }
                        } catch (Exception e) {}

                        d.setPaciente(pac);
                    } else {
                        // Si no se pudo cargar del JOIN, cargar directamente de la BD
                        PacienteDao pd = new PacienteDao();
                        try {
                            Paciente paciente = pd.read(pacienteIdRef);
                            d.setPaciente(paciente);
                        } catch (Exception ex) {
                            System.err.println("No se pudo cargar paciente con ID: " + pacienteIdRef);
                        }
                    }
                }
            } catch(Exception e){
                System.err.println("Error cargando paciente: " + e.getMessage());
                e.printStackTrace();
            }

            // Cargar medicamentos recetados
            try{
                List<MedicamentoRecetado> prescripcionesArray = mr.findByReceta(d.getId());
                for (MedicamentoRecetado prescripcion : prescripcionesArray) {
                    d.getMedicamentos().add(prescripcion);
                }
            } catch(Exception e){
                System.err.println("Error cargando medicamentos: " + e.getMessage());
            }

            return d;
        } catch (SQLException ex) {
            System.err.println("RecetaDao.from error: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
}

*/