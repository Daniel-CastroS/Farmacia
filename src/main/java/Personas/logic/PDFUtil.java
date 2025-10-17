package Personas.logic;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PDFUtil {
    public static void farmaceutaToPDF(Farmaceuta f, String outputPdfPath) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(outputPdfPath));
        document.open();
        if(f==null) return;

        document.add(new Paragraph("Farmaceuta: " + f.getName()));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("ID: " + f.getId()));
        document.add(new Paragraph("Rol: " + f.getRol()));
        document.add(new Paragraph(" ")); // Blank line between entries

        document.close();
    }
    public static void PacientetoPDF(Paciente p, String outputPdfPath) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(outputPdfPath));
        document.open();
        if(p==null) return;

        document.add(new Paragraph("Paciente: " + p.getName()));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("ID: " + p.getId()));
        document.add(new Paragraph("Rol: " + p.getRol()));
        document.add(new Paragraph("Teléfono: " + p.getTelefono()));
        String fechaStr = "";
        if (p.getFechaNac() != null) {
            fechaStr = p.getFechaNac().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        document.add(new Paragraph("Fecha de Nacimiento: " + fechaStr));
        document.add(new Paragraph(" ")); // Blank line between entries

        document.close();
    }

    public static void MedicotoPDF(Medico m, String outputPdfPath) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(outputPdfPath));
        document.open();
        if(m==null) return;

        document.add(new Paragraph("Médico: " + m.getName()));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("ID: " + m.getId()));
        document.add(new Paragraph("Rol: " + m.getRol()));
        document.add(new Paragraph("Especialidad: " + m.getEspecialidad()));
        document.add(new Paragraph(" ")); // Blank line between entries

        document.close();
    }

    public static void MedicamentotoPDF(Medicamento m, String outputPdfPath) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(outputPdfPath));
        document.open();
        if(m==null) return;

        document.add(new Paragraph("Medicamento: " + m.getNombre()));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("ID: " + m.getCodigo()));
        document.add(new Paragraph("Descripción: " + m.getPresentacion()));
        document.add(new Paragraph(" ")); // Blank line between entries

        document.close();
    }
}