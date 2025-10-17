package Personas.data;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDate unmarshal(String v) throws Exception {
        if (v == null) return null;
        String s = v.trim();
        if (s.isEmpty()) return null;
        // try multiple formats
        try {
            return LocalDate.parse(s, DMY);
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(s, ISO);
            } catch (DateTimeParseException ex) {
                // try flexible parse (e.g., d/M/yyyy)
                String[] parts = s.split("/");
                if (parts.length==3) {
                    String dd = parts[0].length()==1?"0"+parts[0]:parts[0];
                    String mm = parts[1].length()==1?"0"+parts[1]:parts[1];
                    String yyyy = parts[2].trim();
                    try {
                        return LocalDate.parse(yyyy+"-"+mm+"-"+dd, ISO);
                    } catch (DateTimeParseException exc) {
                        throw new DateTimeParseException("Unsupported date format: " + s, s, 0);
                    }
                }
                throw new DateTimeParseException("Unsupported date format: " + s, s, 0);
            }
        }
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
        if (v == null) return "";
        return v.format(DMY);
    }
}
