package com.proyecto.ui;

import com.proyecto.algoritmos.BuscadorRutas;
import com.proyecto.model.Arista;
import com.proyecto.model.Nodo;
import com.proyecto.nucleo.CargadorCSV;
import com.proyecto.nucleo.Grafo;

import java.util.List;
import java.util.Scanner; // Librería para poder preguntar al usuario por ciertos datos, por ejemplo, de dónde a dónde va a ir

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Objeto para poder preguntar al usuario por las direcciones
        Grafo madrid = new Grafo(); // Objeto para crear el grafo de Madrid basándonos en los datos
        CargadorCSV  cargadorMadrid = new CargadorCSV(); // Objeto para cargar los datos de los .csv

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

        // Bucle que preguntará al usuario por la dirección de origen y comprobará que es correcta, si no es así volverá a preguntar
        while (nodoOrigen == null || nodoOrigen.isEmpty()) {
            System.out.print("\n1. ¿Desde dónde vas a ir?: ");
            dirOrigen = scanner.nextLine();
            nodoOrigen = madrid.obtenerNodosPorCalle(dirOrigen.toLowerCase()); // Obtenemos el ID de la calle (tomamos el primero de la lista para simplificar)

            if (nodoOrigen == null || nodoOrigen.isEmpty()) {
                System.out.println("No se encontró: '" + dirOrigen + "'");
                System.out.println("* Comprueba la ortografía del nombre de la calle (tildes, guiones, comillas)");
                System.out.println("* Verifica si añadiste la palabra 'de' después del tipo de vía");
                System.out.println("* Ejemplo: Escribe 'Calle de Alcalá' en lugar de 'Calle Alcala'");

            }
        }
        // Bucle que preguntará al usuario por la dirección de destino y comprobará que es correcta, si no es así volverá a preguntar
        while (nodoDestino == null || nodoDestino.isEmpty()) {
            System.out.print("2. ¿A dónde vas a ir?: ");
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

        // Se mostrará los resultados de cada uno de los algoritmos, si no hay resultado se informará por pantalla
        if (rutaAEstrella == null || rutaDijkstra == null) {
            System.out.println("No se ha encontrado ninguna ruta válida");
        } else { // Se llama al metodo para imprimir los datos pertinentes
            imprimirComparativa("Dijkstra", finD - inicioD, nodosD, rutaDijkstra.size());
            imprimirComparativa("A*", finA - inicioA, nodosA, rutaAEstrella.size());

            // Se llama a la función que imprime todos los nodos recorridos en los algoritmos, con su ID, el tiempo tardado en recorrer esa vía y el nombre de la arista entre los nodos
            nodosRecorridos(rutaAEstrella, madrid);
        }
    }

    // Metodo para mostrar la comparativa de los algoritmos de A* y Dijkstra
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
            for (Arista arista : nodoActual.getAristasAdyacentes()) {
                if (arista.getDestino() == idNodoSiguiente) { // Si la siguiente arista ya es el destino seleccionado, se cortará el bucle
                    aristaEncontrada = arista;
                    break;
                }
            }

            // Si se encuentra la arista se obtendrán los datos y se transformarán a unidades óptimas para imprimirlas por pantalla
            if (aristaEncontrada != null) {
                double tiempoSegundos = aristaEncontrada.getTiempoViaje(); // Tiempo de cada trayecto
                double distanciaMetros = aristaEncontrada.getDistancia(); // Distancia de cada trayecto
                String nombreVia = aristaEncontrada.getNombreVia();

                tiempoTotal += tiempoSegundos;
                distanciaTotal += (int) distanciaMetros;

                // Convertiremos el tiempo de segundos a minutos y segundos
                int minutos = (int) (tiempoSegundos / 60);
                int segundos = (int) tiempoSegundos % 60;

                // Imprimimos la información
                System.out.println((i + 1) +
                        ". Vía: " + nombreVia +
                        " (" + idNodoActual + ") " +
                        " || Tiempo: " + minutos + " min " + segundos + "seg ");
            }
        }

        // Mostrar un resumen de la información
        int minutosTotales = (int) tiempoTotal / 60;
        int segundosTotales = (int) tiempoTotal % 60;

        System.out.println("\n=== RESUMEN ===");
        System.out.println("Distancia total: " + distanciaTotal + " metros (" + distanciaTotal / 1000 + " km)");
        System.out.println("Tiempo total: " + minutosTotales + " minutos y " + segundosTotales + " segundos");
        System.out.println("Número de nodos recorridos: " + ruta.size());
    }
}