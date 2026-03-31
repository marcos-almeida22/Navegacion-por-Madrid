package com.proyecto.algoritmos;

// Clase para representar un nodo en la cola de prioridad durante la búsqueda de cualquiera de los algoritmos: Dijkstra o A*
public class EstadoNodo implements Comparable<EstadoNodo>{
    // Atributos
    protected long nodoId;
    protected double coste; // Tiempo acumulado desde el origen
    protected double prioridad; // Prioridad total, solo se usará para el algoritmo A*

    public EstadoNodo(long nodoId, double coste, double prioridad) {
        this.nodoId = nodoId;
        this.coste = coste;
        this.prioridad = prioridad;
    }

    // Metodo para comparar la prioridad de un nodo próximo con otro
    @Override
    public int compareTo(EstadoNodo otro) {
        return Double.compare(this.prioridad, otro.prioridad);
    }
}
