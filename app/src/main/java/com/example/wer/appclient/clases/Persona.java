package com.example.wer.appclient.clases;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class Persona {
    String dpi;
    String nombre;
    String coordenadas;
    String direccion;
    String emergencia;


    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getDireccion() {
        return direccion;
    }

    public String getEmergencia() {
        return emergencia;
    }

    public void setEmergencia(String emergencia) {
        this.emergencia = emergencia;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public static ArrayList<Persona> obtenerPersonas(String json){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Persona>>(){}.getType();
        return gson.fromJson(json, type);
    }
}
