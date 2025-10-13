package Personas.presentation.Dashboard;

import Personas.logic.MedicamentoRecetado;
import Personas.logic.Receta;
import Personas.logic.Service;
import Personas.presentation.AbstractModel;

import java.util.*;
import java.util.stream.Collectors;

public class Model extends AbstractModel {
    private List<Receta> recetas;

    public static final String RECETAS = "recetas";

    public Model() {
        this.recetas = new ArrayList<>();
        Service.instance().addPropertyChangeListener(evt -> {
            if ("recetas".equals(evt.getPropertyName())) {
                reloadRecetas();
            }
        });
    }

    public Model(List<Receta> recetas) {
        this.recetas = recetas;
        Service.instance().addPropertyChangeListener(evt -> {
            if ("recetas".equals(evt.getPropertyName())) {
                reloadRecetas();
            }
        });
    }

    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
        firePropertyChange(RECETAS);
    }

    public void reloadRecetas() {
        try {
            this.recetas = Service.instance().findAllRecetas();
            firePropertyChange(RECETAS);
        } catch (Exception e) {
            System.err.println("Error al recargar recetas: " + e.getMessage());
        }
    }

    public Map<String, Map<String, Integer>> getMedicamentosPorMes(String inicio, String fin) {
        Map<String, Map<String, Integer>> conteo = new TreeMap<>();
        try {
            int inicioInt = Integer.parseInt(inicio.replace("-", "").substring(0, 6));
            int finInt = Integer.parseInt(fin.replace("-", "").substring(0, 6));

            for (Receta r : recetas) {
                if (r.getFechaConfeccion() == null || r.getFechaConfeccion().isEmpty()) continue;

                String fecha = r.getFechaConfeccion();
                String ym = fecha.substring(0, 7);
                int ymInt = Integer.parseInt(fecha.replace("-", "").substring(0, 6));

                if (ymInt >= inicioInt && ymInt <= finInt) {
                    if (r.getMedicamentos() != null) {
                        for (MedicamentoRecetado med : r.getMedicamentos()) {
                            String nombre = med.getMedicamento().getNombre();
                            if (nombre == null || nombre.isEmpty()) nombre = "Desconocido";

                            conteo.putIfAbsent(nombre, new TreeMap<>());
                            Map<String, Integer> porMes = conteo.get(nombre);
                            porMes.put(ym, porMes.getOrDefault(ym, 0) + 1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error en rango de fechas: " + e.getMessage());
        }
        return conteo;
    }

    public Map<String, Long> getRecetasPorEstado() {
        return recetas.stream()
                .collect(Collectors.groupingBy(
                        Receta::getEstado,
                        Collectors.counting()
                ));
    }
}