package com.proyecto.nucleo;


import com.proyecto.model.Nodo;

import java.io.BufferedReader; // Librería que permite la lectura de grandes cantidades de datos de archivos de texto de manera eficiente
import java.io.FileReader; // Librería que permite la lectura de archivos de texto, en este caso para leer los archivos con extensión .csv
import java.io.IOException; // Librería para manejar excepciones relacionadas con la entrada/salida, por ejemplo con problemas al leer los archivos

public class CargadorCSV {

    // Metodo para poder cargar los datos del archivo 'nodes.csv'
    public void cargarNodos(String camino, Grafo grafo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(camino))) {
            String linea = br.readLine(); // Saltar cabecera con la información inicial
            while ((linea = br.readLine()) != null) { // Bucle para recorrer todos los datos de los nodos
                String[] datos = linea.split(",");

                // Estructura de los datos: node_id, lon, lat [ejemplo: 171946, -3.6844432, 40.4212466]
                long id = Long.parseLong(datos[0]); // Primera posición del CSV, el ID del nodo
                double lon = Double.parseDouble(datos[1]); // Segunda posición del CSV, la longitud del nodo
                double lat = Double.parseDouble(datos[2]); // Tercera posición del CSV, a latitud del nodo

                grafo.anadirNodo(new Nodo(id, lon, lat)); // Se crea un nuevo nodo después de extraer los datos y se añade al grafo
            }
        }
    }

    // Metodo para poder cargar los datos del archivo 'edges.csv'
    public void cargarAristas(String camino, Grafo grafo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(camino))) {
            String linea = br.readLine(); // Saltar cabecera con la información inicial
            while ((linea = br.readLine()) != null) {
                // Se sustituirán las comas que estén dentro de las comillas por un carácter temporal para evitar problemas al extraer los datos
                String[] datos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                // Estructura de los datos: source, target, length, name, oneway, highway, maxspeed [ejemplo: 171946, 171947, 10.0, "Calle de Alcalá", false, "residential", "30"]
                long idOrigen = Long.parseLong(datos[0]);
                long idDestino = Long.parseLong(datos[1]);
                double distancia = Double.parseDouble(datos[2]);

                String nombre = ""; // Dato name
                if (datos.length > 3) {
                    nombre = datos[3].trim();
                    nombre = nombre.replace("\"", "").replaceAll("[\\[\\]]", "");
                }

                if (nombre.isEmpty()) {
                    nombre = "Calle desconocida";
                }

                boolean unidireccional = false; // Dato de unidireccionalidad
                if (datos.length > 4) {
                    unidireccional = Boolean.parseBoolean(datos[4].trim());
                }

                String maxVelocidad = ""; // Dato de velocidad máxima
                if (datos.length > 6) {
                    maxVelocidad = datos[6].trim(); // Eliminará los espacios
                }

                if (maxVelocidad.isEmpty()) {
                    maxVelocidad = "40"; // Velocidad máxima por defecto si no hay dato
                }

                // Se creará una arista de ida con sus correspondientes atributos
                grafo.anadirArista(idOrigen, idDestino, distancia, maxVelocidad, nombre);

                // Si la vía no es de sentido único, se creará una arista de vuelta para esa misma vía
                if (!unidireccional) {
                    grafo.anadirArista(idDestino, idOrigen, distancia, maxVelocidad, nombre);
                }

                // Mediante esta función se indexará el nombre de la vía en función de su id
                grafo.indexarNombreCalle(nombre, idOrigen, idDestino);
            }
        }
    }
}