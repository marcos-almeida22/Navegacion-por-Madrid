package com.proyecto.ui;

import com.proyecto.algoritmos.BuscadorRutas;
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
            System.out.println("=== Cargando el mapa de Madrid ==");
            cargadorMadrid.cargarNodos("csv/nodes.csv", madrid);
            cargadorMadrid.cargarAristas("csv/edges.csv", madrid);
            System.out.println("Mapa cargado con éxito.");
        } catch (Exception e) {
            System.err.println("Error al cargar los archivos: " + e.getMessage());
            return;
        }

        BuscadorRutas buscador = new BuscadorRutas(madrid); // Creamos un buscador para implementarlo en la búsqueda

        // Creamos la interfaz con la que va a interactuar el usuario
        System.out.print("\nIntroduce la calle de ORIGEN: ");
        String dirOrigen = scanner.nextLine();
        System.out.print("Introduce la calle de DESTINO: ");
        String dirDestino = scanner.nextLine();

        // Obtener IDs de las calles (tomamos el primero de la lista para simplificar)
        List<Long> nodosOrigen = madrid.obtenerNodosPorCalle(dirOrigen.toLowerCase());
        List<Long> nodosDestino = madrid.obtenerNodosPorCalle(dirDestino.toLowerCase());

        if (nodosOrigen == null || nodosDestino == null) {
            System.out.println("Error: Una de las calles no se ha encontrado en el mapa.");
            return;
        }

        long idO = nodosOrigen.getFirst();
        long idD = nodosDestino.getFirst();

        // Ejecución y comparativa de modelos
        System.out.println("\n=== RESULTADOS DE NAVEGACIÓN ===");

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

        //
        if (rutaAEstrella == null) {
            System.out.println("No se ha encontrado ninguna ruta válida.");
        } else {
            imprimirComparativa("Dijkstra", rutaDijkstra, finD - inicioD, nodosD);
            imprimirComparativa("A*", rutaAEstrella, finA - inicioA, nodosA);

            System.out.println("\nDetalle de la ruta con el algoritmo A*:");
            System.out.println("Número de paradas: " + rutaAEstrella.size());
            // Aquí podrías iterar los nodos para mostrar nombres de calles si quisieras

            System.out.println("\nDetalle de la ruta con el algoritmo de Dijkstra:");
            System.out.println("Número de paradas: " + rutaDijkstra.size());
        }
    }

    private static void imprimirComparativa(String tipoAlgoritmo, List<Long> ruta, long tiempo, int explorados) {
        System.out.printf("[%s] -> Tiempo ejecución: %d ms | Nodos explorados: %d\n",
                tipoAlgoritmo, tiempo, (ruta != null ? ruta.size() : 0));
    }
}