package com.proyecto.model;

public class Arista {
    // Atributos
    protected long origen;
    protected long destino;
    protected double distancia;
    protected double tiempoViaje; // coste "real" (distancia / velocidad)
    protected String nombreVia;
    protected double maxVelocidad;

    // Constructor
    public Arista(long origen, long destino, double distancia, String nombreVia, double maxVelocidad) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.nombreVia = nombreVia;
        this.maxVelocidad = maxVelocidad;
        this.tiempoViaje = distancia / (maxVelocidad / 3.6); // Tiempo calculado mediante con la distancia y la velocidad en metros/segundo
    }
}