package com.proyecto.ui;

import com.proyecto.algoritmos.BuscadorRutas;
import com.proyecto.model.Arista;
import com.proyecto.model.Nodo;
import com.proyecto.nucleo.CargadorCSV;
import com.proyecto.nucleo.Grafo;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grafo madrid = new Grafo();
        CargadorCSV  cargadorMadrid = new CargadorCSV();

        // Primero cargaremos los datos de los archivos .csv
        try {
            System.out.println("=== Cargando el mapa de Madrid ===");
            cargadorMadrid.cargarNodos("csv/nodes.csv", madrid);
            cargadorMadrid.cargarAristas("csv/edges.csv", madrid);
            System.out.println("Mapa cargado con éxito");
        } catch (Exception e) {
            System.err.println("Error al cargar los archivos: " + e.getMessage());
            return;
        }

        BuscadorRutas buscador = new BuscadorRutas(madrid); // Creamos un buscador para implementarlo en la búsqueda

        // Creamos la interfaz con la que va a interactuar el usuario
        List<Long> nodoOrigen = null;
        List<Long> nodoDestino = null;
        String dirOrigen;
        String dirDestino;

        while (nodoOrigen == null || nodoOrigen.isEmpty()) {
            System.out.print("\nIntroduce la calle de ORIGEN: ");
            dirOrigen = scanner.nextLine();
            nodoOrigen = madrid.obtenerNodosPorCalle(dirOrigen.toLowerCase()); // Obtenemos el ID de la calle (tomamos el primero de la lista para simplificar)

            if (nodoOrigen == null || nodoOrigen.isEmpty()) {
                System.out.println("Error: La dirección '" + dirOrigen + "' no ha sido encontrada. Inténtelo de nuevo");
            }
        }
        while (nodoDestino == null || nodoDestino.isEmpty()) {
            System.out.print("Introduce la calle de DESTINO: ");
            dirDestino = scanner.nextLine();
            nodoDestino = madrid.obtenerNodosPorCalle(dirDestino.toLowerCase());

            if (nodoDestino == null || nodoDestino.isEmpty()) {
                System.out.println("Error: La dirección '" + dirDestino + "' no ha sido encontrada. Inténtelo de nuevo");
            }
        }

        // Se cogerá el primero de la lista de nodos,
        long idO = nodoOrigen.get(0);
        long idD = nodoDestino.get(0);

        // Ejecución y comparativa de modelos
        System.out.println("\n=== RESULTADOS DE LA ALGORITMIA ===");

        // Ejecución Dijkstra
        long inicioD = System.currentTimeMillis();
        List<Long> rutaDijkstra = buscador.buscarRuta(idO, idD, false);
        long finD = System.currentTimeMillis();
        int nodosD = buscador.getNodosExplorados();

        // Ejecución A*
        long inicioA = System.currentTimeMillis();
        List<Long> rutaAEstrella = buscador.buscarRuta(idO, idD, true);
        long finA = System.currentTimeMillis();
        int nodosA = buscador.getNodosExplorados();


        if (rutaAEstrella == null || rutaDijkstra == null) {
            System.out.println("No se ha encontrado ninguna ruta válida");
        } else {
            imprimirComparativa("Dijkstra", rutaDijkstra, finD - inicioD, nodosD);
            imprimirComparativa("A*", rutaAEstrella, finA - inicioA, nodosA);

            System.out.println("\n=== ALGORITMO DE A* ===:");
            System.out.println("Número de paradas: " + rutaAEstrella.size());
            nodosRecorridos(rutaAEstrella, madrid);


            System.out.println("\n=== ALGORITMO DE DIJKSTRA ===:");
            System.out.println("Número de paradas: " + rutaDijkstra.size());
            nodosRecorridos(rutaDijkstra, madrid);
        }
    }

    private static void imprimirComparativa(String tipoAlgoritmo, List<Long> ruta, long tiempo, int explorados) {
        System.out.printf("[%s] -> Tiempo ejecución: %d ms | Nodos explorados: %d\n",
                tipoAlgoritmo, tiempo, explorados);
    }

    // Metodo para poder mostrar la ruta que se ha seguido en los algoritmos
    private static void nodosRecorridos(List<Long> ruta, Grafo grafo) {
        System.out.println("\n=== RUTA A SEGUIR ===");
        double tiempoTotal = 0;
        double distanciaTotal = 0;

        // Recorreremos la ruta yendo de nodo en nodo
        for (int i = 0; i < ruta.size() - 1; i++) {
            long idNodoActual = ruta.get(i);
            long idNodoSiguiente = ruta.get(i + 1);

            Nodo nodoActual = grafo.getNodos().get(idNodoActual);


            // Se buscará la arista que conecta los dos nodos, el actual con el siguiente
            Arista aristaEncontrada = null;
            for (Arista arista : nodoActual.aristasAdyacentes) {
                if (arista.getDestino() == idNodoSiguiente) { // Si la siguiente arista ya es el destino seleccionado, se cortará el bucle
                    aristaEncontrada = arista;
                    break;
                }
            }

            if (aristaEncontrada != null) {
                double tiempoSegundos = aristaEncontrada.getTiempoViaje();
                double distanciaMetros = aristaEncontrada.getDistancia();
                double velocidadKmh = aristaEncontrada.getMaxVelocidad();
                String nombreVia = aristaEncontrada.getNombreVia();

                tiempoTotal += tiempoSegundos;
                distanciaTotal += distanciaMetros;

                // Convertiremos el tiempo de segundos a minutos y segundos
                double minutos = (int) tiempoSegundos / 60;
                double segundos = (int) tiempoSegundos % 60;

                System.out.println("ID del nodo actual: " + idNodoActual +
                        " || Nombre de la vía: " + nombreVia +
                        " || Tiempo: " + minutos + " min " + segundos + "seg ");
            }
        }

        // Mostrar la información final
        int minutosTotales = (int) tiempoTotal / 60;
        int segundosTotales = (int) tiempoTotal % 60;

        System.out.println("RESUMEN");
        System.out.println("Distancia total: " + distanciaTotal + " metros (" + distanciaTotal / 1000 + " km)");
        System.out.println("Tiempo total: " + minutosTotales + " minutos y " + segundosTotales + " segundos");
        System.out.println("Número de nodos recorridos: " + ruta.size());
    }
}