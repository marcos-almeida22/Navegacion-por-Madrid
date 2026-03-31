package com.proyecto.nucleo;

import com.proyecto.model.Arista;
import com.proyecto.model.Nodo;

import java.util.ArrayList;
import java.util.HashMap; // Librería para crear pares de datos clave-valor, por ejemplo nombre de calle con su id
import java.util.Map;
import java.util.List;

public class Grafo {
    // Atributos
    protected Map<Long, Nodo> nodos = new HashMap<>();
    protected Map<String, List<Long>> indiceCalle = new HashMap<>(); // Índice para poder buscar nombres de calles y obtener sus nodos asociados

    private static final double METROS_LAT = 111000.0; // Constante de latitud en metros para los cálculos heurísticos
    private static final double METROS_LON = 85000.0; // Constante de longitud en metros para los cálculos heurísticos

    // Metodo para añadir un nodo al grafo en base a su id
    public void anadirNodo(Nodo nodo) {
        nodos.put(nodo.getNodoId(), nodo);
    }

    // Metodo getter para obtener todos los nodos del grafo
    public Map<Long, Nodo> getNodos() {
        return nodos;
    }

    // Metodo para añadir una arista al grafo
    public void anadirArista(long idOrigen, long idDestino, double distancia, String maxVelocidad, String nombre) {
        Nodo origen = nodos.get(idOrigen);
        Nodo destino = nodos.get(idDestino);

        // Comprobar que existen ambos nodos
        if (origen == null || destino == null) return;

        double velocidadKmh = extraerVelocidad(maxVelocidad);
        double velocidadMs = velocidadKmh / 3.6; // Convertir a metros/segundo

        // El peso de la arista será el tiempo en segundos, calculado con esta división
        double tiempoViaje = distancia / velocidadMs;

        Arista arista = new Arista(idOrigen, idDestino, distancia, nombre, velocidadKmh);
        arista.setTiempoViaje(tiempoViaje); // Asignamos el coste calculado

        origen.getAristasAdyacentes().add(arista); // Se añadirá a la lista de aristas adyacentes del nodo de origen
    }

    // Metodo para extraer la velocidad de cada una de las calles en un formato correcto para poder trabajar con ellas
    private double extraerVelocidad(String maxVelocidad) {
        if (maxVelocidad == null || maxVelocidad.trim().isEmpty() || maxVelocidad.equals("None")) {
            return 40.0; // Velocidad por defecto en Madrid si no hay dato o es inválido
        }

        // Limpieza: si viene como "[30, 50]", quitamos corchetes y nos quedamos con el mayor
        String limpiar = maxVelocidad.replaceAll("[\\[\\]\"']", "").trim();

        // Se limpiarán los datos de velocidades que contengan un intervalo de estas, obteniendo únicamente el valor máximo
        if (limpiar.contains(",")) {
            String[] partes = limpiar.split(",");
            double max = 0;
            for (String p : partes) {
                try {
                    double val = Double.parseDouble(p.trim());
                    if (val > max) {
                        max = val;
                    }
                } catch (NumberFormatException e) {}
            }
            return max > 0 ? max : 40.0;
        }

        try {
            return Double.parseDouble(limpiar); // Se intenta parsear como un número simple
        } catch (NumberFormatException e) {
            return 40.0; // Se usará la velocidad por defecto
        }
    }

    // Metodo para la heurística de A*, calculando la distancia en línea recta en metros entre dos nodos y dividiéndola por la velocidad máxima (120 km/h) para obtener un tiempo aproximado para recorrer esa vía
    public double calcularAproxHeuristica(Nodo a, Nodo b) {

        double distLat = (a.getLat() - b.getLat()) * METROS_LAT;
        double distLon = (a.getLon() - b.getLon()) * METROS_LON;
        double distanciaMetros = Math.sqrt((distLat * distLat) + (distLon * distLon));

        return distanciaMetros / (50.0 / 3.6);
    }

    // Metodo para indexar los nombres de las calles y sus nodos asociados
    public void indexarNombreCalle(String nombre, long idOrigen, long idDestino) {
        if (nombre == null || nombre.isEmpty() || nombre.equals("None")) { // Si no existe nombre, no lo indexamos
            return;
        }

        // Lo guardamos en minúsculas para que la búsqueda no falle si el usuario escribe en mayúsculas o en minúsculas
        String nombreLimpio = nombre.replaceAll("[\"\\[\\]']", "").replaceAll("\\s+", " ").toLowerCase().trim();

        // Si la calle no existe en el mapa, creamos una lista nueva. Luego añadimos los IDs
        indiceCalle.computeIfAbsent(nombreLimpio, k -> new ArrayList<>()).add(idOrigen);
        indiceCalle.computeIfAbsent(nombreLimpio, k -> new ArrayList<>()).add(idDestino);
    }

    // Metodo usado por el Main para obtener los nodos cuando el usuario escriba el nombre
    public List<Long> obtenerNodosPorCalle(String nombre) {
        if (nombre == null) {
            return null;
        } else {
            return indiceCalle.get(nombre.replace("\"", "").toLowerCase().trim());
        }
    }
}