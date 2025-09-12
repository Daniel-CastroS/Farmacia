package Personas.presentation.Dashboard;

import Personas.logic.Receta;
import Personas.presentation.AbstractModel;

import java.util.*;
import java.util.stream.Collectors;

public class Model extends AbstractModel {

    private List<Receta> recetas;

    public static final String RECETAS = "recetas";

    public Model() {
        this.recetas = new ArrayList<>();
    }

    public Model(List<Receta> recetas) {
        this.recetas = recetas;
    }

    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
        firePropertyChange(RECETAS);
    }

    // --- Estadísticas ---

    // 1. Cantidad de medicamentos prescritos por mes (String fechas "yyyy-MM-dd")
    public Map<String, Integer> getMedicamentosPorMes(String inicio, String fin) {
        Map<String, Integer> conteo = new TreeMap<>();

        try {
            // convertimos a enteros comparables YYYYMM
            int inicioInt = Integer.parseInt(inicio.replace("-", "").substring(0, 6));
            int finInt = Integer.parseInt(fin.replace("-", "").substring(0, 6));

            for (Receta r : recetas) {
                try {
                    if (r.getFechaConfeccion() == null) continue;
                    String fecha = r.getFechaConfeccion(); // formato "yyyy-MM-dd"
                    String ym = fecha.substring(0, 7);     // "yyyy-MM"
                    int ymInt = Integer.parseInt(fecha.replace("-", "").substring(0, 6));

                    if (ymInt >= inicioInt && ymInt <= finInt) {
                        int cantidad = (r.getMedicamentos() != null) ? r.getMedicamentos().size() : 0;
                        conteo.put(ym, conteo.getOrDefault(ym, 0) + cantidad);
                    }
                } catch (Exception e) {
                    // ignorar recetas mal formateadas
                }
            }
        } catch (Exception e) {
            System.err.println("Error en rango de fechas: " + e.getMessage());
        }

        return conteo;
    }

    // 2. Cantidad de recetas por estado
    public Map<String, Long> getRecetasPorEstado() {
        return recetas.stream()
                .collect(Collectors.groupingBy(
                        Receta::getEstado,
                        Collectors.counting()
                ));
    }
}
