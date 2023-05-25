package com.example.mercadolaboral2.data.local.dbEntities;

import androidx.room.ColumnInfo;

public class TotCuestionarios {

    @ColumnInfo(name = "totCuestionarios")
    private int totCuestionarios;
    @ColumnInfo(name = "codigoSegmento")
    private String codigoSegmento;

    public TotCuestionarios(int totCuestionarios, String codigoSegmento) {
        this.totCuestionarios = totCuestionarios;
        this.codigoSegmento = codigoSegmento;
    }

    public int getTotCuestionarios() {
        return totCuestionarios;
    }

    public void setTotCuestionarios(int totCuestionarios) {
        this.totCuestionarios = totCuestionarios;
    }

    public String getCodigoSegmento() {
        return codigoSegmento;
    }

    public void setCodigoSegmento(String codigoSegmento) {
        this.codigoSegmento = codigoSegmento;
    }
}
