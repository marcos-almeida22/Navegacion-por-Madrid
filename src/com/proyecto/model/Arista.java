package com.proyecto.model;

public class Arista {
    // Atributos
    protected long origen;
    protected long destino;
    protected double distancia;
    protected double tiempoViaje; // coste real (distancia / velocidad)
    protected String nombreVia;
    protected double maxVelocidad;

    // Constructor
    public Arista(long origen, long destino, double distancia, String nombreVia, double maxVelocidad) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.nombreVia = nombreVia;
        this.maxVelocidad = maxVelocidad;
        this.tiempoViaje = 0; // Tiempo calculado mediante con la distancia y la velocidad en metros/segundo
    }

    // Getters y setters, crearemos todos por si es necesario usarlos más tarde
    public long getOrigen() {
        return origen;
    }
    public void setOrigen(long origen) {
        this.origen = origen;
    }

    public long getDestino() {
        return destino;
    }
    public void setDestino(long destino) {
        this.destino = destino;
    }

    public double getDistancia() {
        return distancia;
    }
    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getTiempoViaje() {
        return tiempoViaje;
    }
    public void setTiempoViaje(double tiempoViaje) {
        this.tiempoViaje = tiempoViaje;
    }

    public String getNombreVia() {
        return nombreVia;
    }
    public void setNombreVia(String nombreVia) {
        this.nombreVia = nombreVia;
    }

    public double getMaxVelocidad() {
        return maxVelocidad;
    }
    public void setMaxVelocidad(double maxVelocidad) {
        this.maxVelocidad = maxVelocidad;
    }
}