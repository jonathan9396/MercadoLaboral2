package com.example.mercadolaboral2.data.remote.networkEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetInconsistenciasResponse {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("llave")
    @Expose
    private String llave;

    @SerializedName("mensajes")
    @Expose
    private String mensajes;

    @SerializedName("fechaCreacion")
    @Expose
    private String fechaCreacion;

    @SerializedName("fechaModificacion")
    @Expose
    private String fechaModificacion;

    public GetInconsistenciasResponse(int id, String llave, String mensajes, String fechaCreacion, String fechaModificacion) {
        this.id = id;
        this.llave = llave;
        this.mensajes = mensajes;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public String getMensajes() {
        return mensajes;
    }

    public void setMensajes(String mensajes) {
        this.mensajes = mensajes;
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
