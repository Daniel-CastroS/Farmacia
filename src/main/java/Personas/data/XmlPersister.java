package Personas.data;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.JAXBElement;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class XmlPersister {
    private String path;
    private static XmlPersister theInstance;
    public static XmlPersister instance(){
        if (theInstance==null) theInstance=new XmlPersister("pos.xml");
        return theInstance;
    }
    public XmlPersister(String p) {
        path=p;
    }
    public Data load() throws Exception{
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("[XmlPersister] file '"+path+"' does not exist, returning empty Data");
            return new Data();
        }

        if (f.length() == 0) {
            System.out.println("[XmlPersister] file '"+path+"' is empty, returning empty Data");
            return new Data();
        }

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Data.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            // primary attempt: unmarshal directly from the file (most robust)
            try {
                Object o = unmarshaller.unmarshal(f);
                if (o instanceof Data) return (Data) o;
                if (o instanceof JAXBElement) {
                    JAXBElement<?> je = (JAXBElement<?>) o;
                    Object value = je.getValue();
                    if (value instanceof Data) return (Data) value;
                }
                System.err.println("[XmlPersister] Unmarshal returned unexpected type: " + (o==null?"null":o.getClass()));
            } catch (Exception ex) {
                System.err.println("[XmlPersister] Unmarshal from File failed: " + ex.getMessage());
                // fall through to reader approach
            }

            // second attempt: read via Reader (with explicit UTF-8)
            try (FileInputStream fis = new FileInputStream(f);
                 InputStreamReader isr = new InputStreamReader(fis, java.nio.charset.StandardCharsets.UTF_8)) {
                JAXBContext jaxbContext2 = JAXBContext.newInstance(Data.class);
                Unmarshaller unmarshaller2 = jaxbContext2.createUnmarshaller();
                try {
                    Object o = unmarshaller2.unmarshal(isr);
                    if (o instanceof Data) return (Data) o;
                    if (o instanceof JAXBElement) {
                        JAXBElement<?> je = (JAXBElement<?>) o;
                        Object value = je.getValue();
                        if (value instanceof Data) return (Data) value;
                    }
                    System.err.println("[XmlPersister] Reader unmarshal returned unexpected type: " + (o==null?"null":o.getClass()));
                } catch (Exception ex) {
                    System.err.println("[XmlPersister] Reader unmarshal failed: " + ex.getMessage());
                }
            } catch (Exception e) {
                System.err.println("[XmlPersister] Error reading file via reader '"+path+"': " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("[XmlPersister] JAXB init error: " + e.getMessage());
        }

        // fallback: read content as string, remove BOM and retry
        try {
            String content = readFileContent(path);
            // remove UTF-8 BOM if present
            if (content.startsWith("\uFEFF")) content = content.substring(1);
            JAXBContext jaxbContext = JAXBContext.newInstance(Data.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Object o = unmarshaller.unmarshal(new StringReader(content));
            if (o instanceof Data) return (Data) o;
            if (o instanceof JAXBElement) {
                JAXBElement<?> je = (JAXBElement<?>) o;
                Object value = je.getValue();
                if (value instanceof Data) return (Data) value;
            }
            System.err.println("[XmlPersister] Fallback unmarshal returned unexpected type: " + (o==null?"null":o.getClass()));
            return new Data();
        } catch (Exception ex) {
            System.err.println("[XmlPersister] Fallback unmarshal failed: " + ex.getMessage());
            ex.printStackTrace();
            // devolver Data vacía para que la aplicación siga funcionando
            return new Data();
        }
    }

    private String readFileContent(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), java.nio.charset.StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        }
    }

    public void store(Data d)throws Exception{
        JAXBContext jaxbContext = JAXBContext.newInstance(Data.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        File target = new File(path);
        File parent = target.getAbsoluteFile().getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        File tmp = new File(path + ".tmp");
        try (FileOutputStream fos = new FileOutputStream(tmp);
             OutputStreamWriter osw = new OutputStreamWriter(fos, java.nio.charset.StandardCharsets.UTF_8)) {
            marshaller.marshal(d, osw);
            osw.flush();
        }
        // move temp into place (replace existing)
        try {
            Files.move(tmp.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            // if atomic move not supported, try a simple rename
            if (tmp.exists()) {
                if (target.exists()) target.delete();
                boolean ok = tmp.renameTo(target);
                if (!ok) throw new Exception("Could not move temp file to target location: " + tmp.getAbsolutePath());
            }
        }
    }
}


///

