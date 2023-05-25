package com.example.mercadolaboral2.data.remote.networkEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AsignacionAlterna {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("rol")
    @Expose
    public String rol;

    @SerializedName("region")
    @Expose
    public String region;

    @SerializedName("zona")
    @Expose
    public String zona;

    @SerializedName("numeracion")
    @Expose
    public String numeracion;

    @SerializedName("codigo")
    @Expose
    public String codigo;

    @SerializedName("userName")
    @Expose
    public String userName;

    @SerializedName("superior")
    @Expose
    public int superior;

    @SerializedName("nombre")
    @Expose
    public String nombre;

    @SerializedName("fechaCreacion")
    @Expose
    public String fechaCreacion;

    @SerializedName("fechaModificacion")
    @Expose
    public String fechaModificacion;

    public AsignacionAlterna(int id, String rol, String region, String zona, String numeracion, String codigo, String userName, int superior, String nombre, String fechaCreacion, String fechaModificacion) {
        this.id = id;
        this.rol = rol;
        this.region = region;
        this.zona = zona;
        this.numeracion = numeracion;
        this.codigo = codigo;
        this.userName = userName;
        this.superior = superior;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSuperior() {
        return superior;
    }

    public void setSuperior(int superior) {
        this.superior = superior;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
}
