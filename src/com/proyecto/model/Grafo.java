package com.proyecto.model;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Grafo {
    // Atributos
    protected Map<Long,Nodo> nodos = new HashMap<>();
    protected Map<Long, List<Long>> indiceCalle = new HashMap<>(); // Índice para poder buscar nombres de calles y obtener sus nodos asociados

    // Métodos
    public void anadirNodo(Nodo nodo) {
        nodos.put(nodo.nodoId, nodo);
    }
}
