package com.proyecto.algoritmos;

import com.proyecto.model.Arista;
import com.proyecto.model.Nodo;
import com.proyecto.nucleo.Grafo;

import java.util.List;
import java.util.Map; // Librería usada para crear pares de clave-valor como el de 'tiemposMinimos' o el de 'padres'
import java.util.HashMap;
import java.util.PriorityQueue; // Librería usada para organizar y procesar los datos según el orden de inserción de estos
import java.util.LinkedList; // Librería para usar listas enlazadas como la de camino, enlazando el ID de un nodo con el siguiente

public class BuscadorRutas {
    // Atributos
    protected Grafo grafo;
    protected int nodosExplorados;

    // Constructor
    public BuscadorRutas(Grafo grafo) {
        this.grafo = grafo;
    }

    // Metodo
    public List<Long> buscarRuta(long idOrigen, long idDestino, boolean usarAEstrella) {
        this.nodosExplorados = 0;

        // Estructuras necesarias para desarrollar el algoritmo
        PriorityQueue<EstadoNodo> colaPrioridad = new PriorityQueue<>();
        Map<Long, Double> tiemposMinimos = new HashMap<>();
        Map<Long, Long> padres = new HashMap<>(); // Para reconstruir el camino posteriormente

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

            // Si el tiempo actual es peor que uno ya encontrado, ignoraremos el dato
            if (actual.coste > tiemposMinimos.getOrDefault(actual.nodoId, Double.MAX_VALUE)) {
                continue;
            }

            Nodo nodoActual = grafo.getNodos().get(actual.nodoId); // Para obtener el id del nodo en el que nos encontramos

            // Bucle for que recorrerá todas las aristas para encontrar la arista adyacente al nodo actual más cercana al nodo de destino
            for (Arista arista : nodoActual.getAristasAdyacentes()) {
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

                    colaPrioridad.add(new EstadoNodo(arista.getDestino(), nuevoTiempo, prioridad)); // Se añadirá a la cola de prioridad el estado del nodo
                }
            }
        }
        return null; // Si no se encuentra ruta, devolvemos el valor nulo
    }

    // Metodo auxiliar para reconstruir la ruta de destino a origen
    private List<Long> reconstruirCaminos(Map<Long, Long> padres, long idDestino) {
        LinkedList<Long> camino = new LinkedList<>();
        Long actual = idDestino;

        // Bucle para añadir a la lista enlazada el ID del nodo de destino
        while (actual != null) {
            camino.addFirst(actual);
            actual = padres.get(actual);
        }
        return camino; // Devolverá el camino entero seguido
    }

    // Metodo obtención de nodos explorados por si es necesario para la interfaz de usuario
    public int getNodosExplorados() {
        return nodosExplorados;
    }
}