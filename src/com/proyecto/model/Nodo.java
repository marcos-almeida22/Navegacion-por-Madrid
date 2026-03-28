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
}
