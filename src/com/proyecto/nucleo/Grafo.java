package com.proyecto.nucleo;

import com.proyecto.model.Arista;
import com.proyecto.model.Nodo;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Grafo {
    // Atributos
    protected Map<Long, Nodo> nodos = new HashMap<>();
    protected Map<Long, List<Long>> indiceCalle = new HashMap<>(); // Índice para poder buscar nombres de calles y obtener sus nodos asociados

    private static final double METROS_LAT = 111000.0;
    private static final double METROS_LON = 85000.0;

    // Métodos
    public void anadirNodo(Nodo nodo) {
        nodos.put(nodo.getNodoId(), nodo);
    }

    // Metodo getter para obtener todos los nodos del grafo
    public Map<Long, Nodo> getNodos() {
        return nodos;
    }

    public void anadirArista(long idOrigen, long idDestino, double distancia, String maxVelocidad, String nombre) {
        Nodo origen = nodos.get(idOrigen);
        Nodo destino = nodos.get(idDestino);

        // Comprobar que existen ambos nodos
        if (origen == null || destino == null) return;

        double velocidadKmh = extraerVelocidad(maxVelocidad);
        double velocidadMs = velocidadKmh / 3.6; // Convertir a metros/segundo

        // El "peso" de la arista será el tiempo en segundos
        double tiempoViaje = distancia / velocidadMs;

        Arista arista = new Arista(idOrigen, idDestino, distancia, nombre, velocidadKmh);
        arista.setTiempoViaje(tiempoViaje); // Asignamos el coste calculado

        origen.aristasAdyacentes.add(arista);
    }

    // Metodo para extraer la velocidad de cada una de las calles en un formato correcto para poder trabajar con ellas
    private double extraerVelocidad(String maxVelocidad) {
        if (maxVelocidad == null || maxVelocidad.isEmpty() || maxVelocidad.equals("None")) {
            return 40.0; // Velocidad por defecto en Madrid si no hay dato o es inválido
        }

        // Limpieza: si viene como "[30, 50]", quitamos corchetes y nos quedamos con el mayor
        String limpiar = maxVelocidad.replaceAll("[\\[\\]\"']", "");

        if (limpiar.contains(",")) {
            String[] partes = limpiar.split(",");
            double max = 0;
            for (String p : partes) {
                try {
                    double val = Double.parseDouble(p.trim());
                    if (val > max) max = val;
                } catch (NumberFormatException e) { /* ignorar */ }
            }
            return max > 0 ? max : 40.0;
        }

        try {
            return Double.parseDouble(limpiar.trim());
        } catch (NumberFormatException e) {
            return 40.0;
        }
    }

    // Metodo para la heurística de A* (Distancia en línea recta en metros)
    public double calcularAproxHeuristica(Nodo a, Nodo b) {

        double distLat = (a.getLat() - b.getLat()) * METROS_LAT;
        double distLon = (a.getLon() - b.getLon()) * METROS_LON;
        double distanciaMetros = Math.sqrt((distLat * distLat) + (distLon * distLon));

        return distanciaMetros / (120.0 / 3.6);
    }

    // Metodo para obtener los datos de los nodos

}