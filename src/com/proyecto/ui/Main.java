package com.proyecto.ui;

import com.proyecto.algoritmos.BuscadorRutas;
import com.proyecto.model.Arista;
import com.proyecto.model.Nodo;
import com.proyecto.nucleo.CargadorCSV;
import com.proyecto.nucleo.Grafo;

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
            System.out.println("¡Mapa cargado con éxito!");
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

        System.out.print("\n=== NAVEGACIÓN POR MADRID ===");
        while (nodoOrigen == null || nodoOrigen.isEmpty()) {
            System.out.print("\n1. ¿A dónde quieres ir?: ");
            dirOrigen = scanner.nextLine();
            nodoOrigen = madrid.obtenerNodosPorCalle(dirOrigen.toLowerCase()); // Obtenemos el ID de la calle (tomamos el primero de la lista para simplificar)

            if (nodoOrigen == null || nodoOrigen.isEmpty()) {
                System.out.println("No se encontró: '" + dirOrigen + "'");
                System.out.println("* Comprueba la ortografía del nombre de la calle (tildes, guiones, comillas)");
                System.out.println("* Verifica si añadiste la palabra 'de' después del tipo de vía");
                System.out.println("* Ejemplo: Escribe 'Calle de Alcalá' en lugar de 'Calle Alcala'");

            }
        }
        while (nodoDestino == null || nodoDestino.isEmpty()) {
            System.out.print("2. ¿Desde dónde vas a ir?: ");
            dirDestino = scanner.nextLine();
            nodoDestino = madrid.obtenerNodosPorCalle(dirDestino.toLowerCase());

            if (nodoDestino == null || nodoDestino.isEmpty()) {
                System.out.println("No se encontró: '" + dirDestino + "'");
                System.out.println("* Comprueba la ortografía del nombre de la calle (tildes, guiones, comillas)");
                System.out.println("* Verifica si añadiste la palabra 'de' después del tipo de vía");
                System.out.println("* Ejemplo: Escribe 'Calle de Alcalá' en lugar de 'Calle Alcala'\n");
            }
        }

        // Se cogerá el primero de la lista de nodos,
        long idOrigen = nodoOrigen.getFirst();
        long idDestino = nodoDestino.getFirst();

        // Ejecución y comparativa de modelos
        System.out.println("\n=== RESULTADOS DE LA ALGORITMIA ===");

        // Ejecución Dijkstra
        long inicioD = System.currentTimeMillis();
        List<Long> rutaDijkstra = buscador.buscarRuta(idOrigen, idDestino, false);
        long finD = System.currentTimeMillis();
        int nodosD = buscador.getNodosExplorados();

        // Ejecución A*
        long inicioA = System.currentTimeMillis();
        List<Long> rutaAEstrella = buscador.buscarRuta(idOrigen, idDestino, true);
        long finA = System.currentTimeMillis();
        int nodosA = buscador.getNodosExplorados();


        if (rutaAEstrella == null || rutaDijkstra == null) {
            System.out.println("No se ha encontrado ninguna ruta válida");
        } else {
            imprimirComparativa("Dijkstra", finD - inicioD, nodosD, rutaDijkstra.size());
            imprimirComparativa("A*", finA - inicioA, nodosA, rutaAEstrella.size());

            nodosRecorridos(rutaAEstrella, madrid);
        }
    }

    private static void imprimirComparativa(String tipoAlgoritmo, long tiempo, int explorados, int ruta) {
        System.out.printf(tipoAlgoritmo + " -> Tiempo ejecución: " + tiempo + " ms | " + "Nº de nodos explorados: " + explorados + " | Número de paradas: " + ruta + "\n");
    }

    // Metodo para poder mostrar la ruta que se ha seguido en los algoritmos
    private static void nodosRecorridos(List<Long> ruta, Grafo grafo) {
        System.out.println("\n=== RUTA SEGUIDA ===");
        double tiempoTotal = 0;
        int distanciaTotal = 0;

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
                String nombreVia = aristaEncontrada.getNombreVia();

                tiempoTotal += tiempoSegundos;
                distanciaTotal += (int) distanciaMetros;

                // Convertiremos el tiempo de segundos a minutos y segundos
                int minutos = (int) (tiempoSegundos / 60);
                int segundos = (int) tiempoSegundos % 60;

                System.out.println(i + 1 + ". ID del nodo actual: " + idNodoActual +
                        " || Nombre de la vía: " + nombreVia +
                        " || Tiempo: " + minutos + " min " + segundos + "seg ");
            }
        }

        // Mostrar la información final
        int minutosTotales = (int) tiempoTotal / 60;
        int segundosTotales = (int) tiempoTotal % 60;

        System.out.println("\n=== RESUMEN ===");
        System.out.println("Distancia total: " + distanciaTotal + " metros (" + distanciaTotal / 1000 + " km)");
        System.out.println("Tiempo total: " + minutosTotales + " minutos y " + segundosTotales + " segundos");
        System.out.println("Número de nodos recorridos: " + ruta.size());
    }
}