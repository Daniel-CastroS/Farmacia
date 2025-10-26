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
            this.recetas = Service.instance().readAllRecetas();
            firePropertyChange(RECETAS);
        } catch (Exception e) {
            System.err.println("Error al recargar recetas: " + e.getMessage());
        }
    }

    public Map<String, Map<String, Integer>> getMedicamentosPorMes(java.time.LocalDate inicio, java.time.LocalDate fin) {
        Map<String, Map<String, Integer>> conteo = new TreeMap<>();
        if (inicio == null || fin == null) return conteo;
        try {
            for (Receta r : recetas) {
                java.time.LocalDate fecha = r.getFechaConfeccion() != null ? r.getFechaConfeccion() : r.getFechaRetiro();
                if (fecha == null) continue;

                if (fecha.isBefore(inicio) || fecha.isAfter(fin)) continue;

                if (r.getMedicamentos() != null) {
                    String ym = String.format("%04d-%02d", fecha.getYear(), fecha.getMonthValue());
                    for (MedicamentoRecetado med : r.getMedicamentos()) {
                        String nombre = med.getMedicamento().getNombre();
                        if (nombre == null || nombre.isEmpty()) nombre = "Desconocido";

                        conteo.putIfAbsent(nombre, new TreeMap<>());
                        Map<String, Integer> porMes = conteo.get(nombre);
                        porMes.put(ym, porMes.getOrDefault(ym, 0) + 1);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error en getMedicamentosPorMes: " + e.getMessage());
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