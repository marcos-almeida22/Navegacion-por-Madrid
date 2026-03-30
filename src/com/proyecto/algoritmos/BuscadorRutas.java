package com.proyecto.algoritmos;

import com.proyecto.model.Arista;
import com.proyecto.model.Nodo;
import com.proyecto.nucleo.Grafo;

import java.util.*;

public class BuscadorRutas {
    // Atributos
    protected Grafo grafo;
    protected int nodosExplorados;

    // Constructor
    public BuscadorRutas(Grafo grafo) {
        this.grafo = grafo;
    }

    public List<Long> buscarRuta(long idOrigen, long idDestino, boolean usarAEstrella) {
        this.nodosExplorados = 0;

        // Estructuras necesarias para desarrollar el algoritmo
        PriorityQueue<EstadoNodo> colaPrioridad = new PriorityQueue<>();
        Map<Long, Double> tiemposMinimos = new HashMap<>();
        Map<Long, Long> padres = new HashMap<>(); // Para reconstruir el camino

        // Obtener los nodos de origen y destino desde el grafo
        Nodo nodoOrigen = grafo.getNodos().get(idOrigen);
        Nodo nodoDestino = grafo.getNodos().get(idDestino);

        // Comprobación que los nodos de origen y destino no son nulos
        if (nodoOrigen == null || nodoDestino == null) {
            return null;
        }

        // Inicialización de la cola de prioridad y los mapas
        colaPrioridad.add(new EstadoNodo(idOrigen, 0, 0));
        tiemposMinimos.put(idOrigen, 0.0);

        // Si la cola no está vacía iniciamos el bucle
        while (!colaPrioridad.isEmpty()) {
            EstadoNodo actual = colaPrioridad.poll();
            nodosExplorados++;

            // Si llegamos al destino, reconstruimos la ruta y devolvemos el camino
            if (actual.nodoId == idDestino) {
                return reconstruirCaminos(padres, idDestino);
            }

            // Si el tiempo actual es peor que uno ya encontrado, ignoramos
            if (actual.coste > tiemposMinimos.getOrDefault(actual.nodoId, Double.MAX_VALUE)) {
                continue;
            }

            Nodo nodoActual = grafo.getNodos().get(actual.nodoId);

            for (Arista arista : nodoActual.aristasAdyacentes) {
                double  nuevoTiempo = actual.coste + arista.getTiempoViaje();

                if (nuevoTiempo < tiemposMinimos.getOrDefault(arista.getDestino(), Double.MAX_VALUE)) {
                    tiemposMinimos.put(arista.getDestino(), nuevoTiempo);
                    padres.put(arista.getDestino(), actual.nodoId);

                    double prioridad = nuevoTiempo;

                    if (usarAEstrella) {
                        // Aplicaremos la heurística anteriormente definida en la clase Grafo (Distancia euclidiana / Velocidad máx (120 km/h))
                        Nodo vecino = grafo.getNodos().get(arista.getDestino());
                        prioridad += grafo.calcularAproxHeuristica(vecino, nodoDestino);
                    }

                    colaPrioridad.add(new EstadoNodo(arista.getDestino(), nuevoTiempo, prioridad));
                }
            }
        }
        return null; // Si no se encuentra ruta, devolvemos el valor nulo
    }

    // Metodo auxiliar para reconstruir la ruta de deestino a origen
    private List<Long> reconstruirCaminos(Map<Long, Long> padres, long idDestino) {
        LinkedList<Long> camino = new LinkedList<>();
        Long actual = idDestino;

        while (actual != null) {
            camino.addFirst(actual);
            actual = padres.get(actual);
        }
        return camino;
    }

    // Metodo obtención de nodos explorados por si es necesario para la interfaz de usuario (ui)
    public int getNodosExplorados() {
        return nodosExplorados;
    }
}
