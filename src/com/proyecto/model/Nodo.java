package com.proyecto.model;

import java.util.ArrayList;
import java.util.List;

public class Nodo {
    // Atributos
    protected long nodoId;
    protected double lon; // longitud
    protected double lat; // latitud
    protected List<Arista> aristasAdyacentes;

    // Constructor
    public Nodo(long nodoId, double lon, double lat) {
        this.nodoId = nodoId;
        this.lon = lon;
        this.lat = lat;
        this.aristasAdyacentes = new ArrayList<Arista>();
    }

    // Getters y setters
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
}
