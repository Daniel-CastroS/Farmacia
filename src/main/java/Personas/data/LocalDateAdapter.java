package Personas.data;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    private final DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDate unmarshal(String v) throws Exception {
        if (v == null || v.isEmpty()) return null;
        return LocalDate.parse(v, fmt);
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
        if (v == null) return null;
        return v.format(fmt);
    }
}
