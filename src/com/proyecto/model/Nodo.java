package com.proyecto.model;

import java.util.ArrayList;
import java.util.List;

public class Nodo {
    // Atributos
    protected long nodoId;
    protected double lon; // Longitud
    protected double lat; // Latitud
    protected List<Arista> aristasAdyacentes; // Lista de aristas que saldrán de este nodo

    // Constructor
    public Nodo(long nodoId, double lon, double lat) {
        this.nodoId = nodoId;
        this.lon = lon;
        this.lat = lat;
        this.aristasAdyacentes = new ArrayList<Arista>();
    }

    // Getters y setters, creamos todos por si es necesario usarlos más tarde
    public long getNodoId() {
        return nodoId;
    }
    public void setNodoId(long nodoId) {
        this.nodoId = nodoId;
    }

    public double getLon() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }

    public List<Arista> getAristasAdyacentes() {
        return aristasAdyacentes;
    }
    public void setAristasAdyacentes(List<Arista> aristasAdyacentes) {
        this.aristasAdyacentes = aristasAdyacentes;
    }
}
