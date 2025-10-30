package Personas.Data;

import Personas.Logic.Medicamento;
import Personas.Logic.MedicamentoRecetado;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoRecetadoDao {
    Database db;

    public MedicamentoRecetadoDao(){
        db= Database.instance();
    }

    public void create(MedicamentoRecetado p) throws Exception{
        String sql="insert into MedicamentoRecetado (medicamento, cantidad, indicaciones, duracionDias, prescripcion) " +
                "values(?,?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getMedicamento().getCodigo());
        stm.setInt(2, p.getCantidad());
        stm.setString(3, p.getIndicaciones());
        stm.setInt(4, p.getDuracionDias());
        stm.setInt(5, p.getPrescripcion());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("No se pudo crear la Prescripcion");
        }
    }

    public MedicamentoRecetado read(int codigo) throws Exception{
        String sql="select * from MedicamentoRecetado d"+
                "inner join Medicamento m on m.codigo=d.medicamento "+
                "where codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, codigo);
        ResultSet rs =  db.executeQuery(stm);
        if (rs.next()) {
            return from(rs,"d");
        }
        else{
            throw new Exception ("Prescripcion no Existe");
        }
    }

    public void update(MedicamentoRecetado p) throws Exception{
        String sql="update medicamentoRecetado set medicamento=?,cantidad=?,indicaciones=?, duracionDias=?, prescripcion=? " +
                "where prescripcion=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getMedicamento().getCodigo());
        stm.setInt(2, p.getCantidad());
        stm.setString(3, p.getIndicaciones());
        stm.setInt(4, p.getDuracionDias());
        stm.setInt(5, p.getPrescripcion());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Prescripcion no existe");
        }
    }

    public void delete(MedicamentoRecetado o) throws Exception{
        String sql="delete from MedicamentoRecetado where prescripcion=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, o.getMedicamento().getCodigo());
        int count=db.executeUpdate(stm);
       /* if (count==0){
            throw new Exception("Prescripcion no existe");
        }
        */

    }

    public List<MedicamentoRecetado> findAll(){
        List<MedicamentoRecetado> ds=new ArrayList<MedicamentoRecetado>();
        try {
            String sql="select * from MedicamentoRecetado d";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs =  db.executeQuery(stm);
            while (rs.next()) {
                ds.add(from(rs,"d"));
            }
        } catch (SQLException ex) { }
        return ds;
    }

    public List<MedicamentoRecetado> findByReceta(int id){
        List<MedicamentoRecetado> ds=new ArrayList<>();
        try {
            String sql="select mr.*, m.* from MedicamentoRecetado mr "+
                    "inner join Medicamento m on m.codigo=mr.medicamento "+
                    "where mr.prescripcion=?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs =  db.executeQuery(stm);
            while (rs.next()) {
                ds.add(from(rs,"mr"));
            }
        } catch (SQLException ex) {  ex.printStackTrace(); }
        return ds;
    }

    /*public MedicamentoRecetado from(ResultSet rs, String alias){
        MedicamentoDao md= new MedicamentoDao();
        try {
            MedicamentoRecetado d= new MedicamentoRecetado();
            try{
                d.setMedicamento(md.from(rs));

            } catch(Exception e){
                //ignorar
            }
            d.setCantidad(rs.getInt(".cantidad"));
            d.setIndicaciones(rs.getString(".indicaciones"));
            d.setDuracionDias(rs.getInt(".duracionDias"));
            d.setPrescripcion(rs.getInt(".prescripcion"));
            return d;
        } catch (SQLException ex) {
            return null;
        }
    }

     */
    public MedicamentoRecetado from(ResultSet rs, String alias){
        MedicamentoDao md = new MedicamentoDao();
        try {
            MedicamentoRecetado d = new MedicamentoRecetado();

            // Leer el medicamento (columnas del JOIN con Medicamento)
            try {
                Medicamento med = new Medicamento();
                med.setCodigo(rs.getString("codigo"));      // m.codigo
                med.setNombre(rs.getString("nombre"));      // m.nombre
                med.setPresentacion(rs.getString("presentacion")); // m.presentacion
                d.setMedicamento(med);
            } catch(Exception e) {
                System.err.println("Error leyendo medicamento: " + e.getMessage());
            }

            // Leer campos de MedicamentoRecetado
            d.setCantidad(rs.getInt("cantidad"));           // mr.cantidad
            d.setIndicaciones(rs.getString("indicaciones"));    // mr.indicaciones
            d.setDuracionDias(rs.getInt("duracionDias"));       // mr.duracionDias
            d.setPrescripcion(rs.getInt("prescripcion"));       // mr.prescripcion

            return d;
        } catch (SQLException ex) {

            ex.printStackTrace();
            return null;
        }
    }

    // Borrar TODOS los medicamentos de una receta
    public void deleteByReceta(int recetaId) throws Exception {
        String sql = "DELETE FROM MedicamentoRecetado WHERE prescripcion = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, recetaId);
        db.executeUpdate(stm);

    }
}
