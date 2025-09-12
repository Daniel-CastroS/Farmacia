package Personas.data;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    private final DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDate unmarshal(String v) {
        return (v == null ? null : LocalDate.parse(v));
    }

    @Override
    public String marshal(LocalDate v) {
        return (v == null ? null : v.toString());
    }
}
