package com.example.mercadolaboral2.data.local.dbEntities;

import androidx.room.ColumnInfo;

public class CuestionariosPendientes {
    @ColumnInfo(name = "totPendientes")
    private String totPendientes;

    @ColumnInfo(name = "codigoSegmento")
    private String codigoSegmento;

    @ColumnInfo(name = "subzona")
    private String subzona;

    public CuestionariosPendientes(String totPendientes, String codigoSegmento, String subzona) {
        this.totPendientes = totPendientes;
        this.codigoSegmento = codigoSegmento;
        this.subzona = subzona;
    }

    public String getTotPendientes() {
        return totPendientes;
    }

    public void setTotPendientes(String totPendientes) {
        this.totPendientes = totPendientes;
    }

    public String getCodigoSegmento() {
        return codigoSegmento;
    }

    public void setCodigoSegmento(String codigoSegmento) {
        this.codigoSegmento = codigoSegmento;
    }

    public String getSubzona() {
        return subzona;
    }

    public void setSubzona(String subzona) {
        this.subzona = subzona;
    }
}
