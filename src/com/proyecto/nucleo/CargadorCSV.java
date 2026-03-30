package com.proyecto.nucleo;


import com.proyecto.model.Nodo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CargadorCSV {

    // Metodo para poder cargar los datos del archivo 'nodes.csv'
    public void cargarNodos(String camino, Grafo grafo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(camino))) {
            String linea = br.readLine(); // Saltar cabecera con la información inicial
            while ((linea = br.readLine()) != null) { // Bucle para recorrer todos los nodos
                String[] datos = linea.split(",");

                // Estructura de los datos: node_id, lon, lat [ejemplo: 171946, -3.6844432, 40.4212466]
                long id = Long.parseLong(datos[0]);
                double lon = Double.parseDouble(datos[1]);
                double lat = Double.parseDouble(datos[2]);

                grafo.anadirNodo(new Nodo(id, lon, lat));
            }
        }
    }

    // Metodo para poder cargar los datos del archivo 'edges.csv'
    public void cargarAristas(String camino, Grafo grafo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(camino))) {
            String linea = br.readLine(); // Saltar cabecera con la información inicial
            while ((linea = br.readLine()) != null) {
                // El split puede variar según cómo se guardó el CSV (comas en nombres)
                String[] datos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                long idOrigen = Long.parseLong(datos[0]); // u [cite: 198, 203]
                long idDestino = Long.parseLong(datos[1]); // v [cite: 198, 204]
                double distancia = Double.parseDouble(datos[2]); // length [cite: 198, 201]

                // Atributos opcionales [cite: 73, 81]
                String nombre = "";
                if (datos.length > 3) {
                    nombre = datos[3].trim();
                    nombre = nombre.replace("\"", "").replaceAll("[\\[\\]]", "");
                }

                if (nombre.isEmpty()) {
                    nombre = "Calle desconocida";
                }

                boolean unidireccional = false;
                if (datos.length > 4) {
                    unidireccional = Boolean.parseBoolean(datos[4].trim());
                }

                String maxVelocidad = "";
                if (datos.length > 6) {
                    maxVelocidad = datos[6].trim(); // Eliminará los espacios
                }

                if (maxVelocidad.isEmpty()) {
                    maxVelocidad = "40";
                }

                // 1. Crear arista de ida
                grafo.anadirArista(idOrigen, idDestino, distancia, maxVelocidad, nombre);

                // 2. Si no es sentido único, crear arista de vuelta
                if (!unidireccional) {
                    grafo.anadirArista(idDestino, idOrigen, distancia, maxVelocidad, nombre);
                }

                // 3. Indexar nombre para la Fase 4
                grafo.indexarNombreCalle(nombre, idOrigen, idDestino);
            }
        }

    }
}
