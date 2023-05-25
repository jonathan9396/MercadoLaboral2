package com.example.mercadolaboral2.data.local.dbEntities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "otrasEstructuras")
public class OtrasEstructuras {

    @SerializedName("llave")
    @ColumnInfo(name = "llave")
    @PrimaryKey
    @NonNull
    @Expose
    private String llave;

    @SerializedName("codigoSegmento")
    @ColumnInfo(name = "codigoSegmento")
    @Expose
    private String codigoSegmento;

    @SerializedName("provinciaId")
    @ColumnInfo(name = "provinciaID")
    @Expose
    private String provinciaID;

    @SerializedName("distritoId")
    @ColumnInfo(name = "distritoID")
    @Expose
    private String distritoID;

    @SerializedName("corregimientoId")
    @ColumnInfo(name = "corregimientoID")
    @Expose
    private String corregimientoID;

    @SerializedName("subZona")
    @ColumnInfo(name = "subzona")
    @Expose
    private String subzona;//Se añadió a la tabla SQL

    @SerializedName("segmento")
    @ColumnInfo(name = "segmento")
    @Expose
    private String segmento;

    @SerializedName("division")
    @ColumnInfo(name = "division")
    @Expose
    private String division;

    @SerializedName("numEstructura") //vivienda
    @ColumnInfo(name = "numEstructura")
    @Expose
    private String numEstructura;

    @SerializedName("tipoEstructura") //vivienda
    @ColumnInfo(name = "tipoEstructura")
    @Expose
    private int tipoEstructura;

    @SerializedName("observacion") //vivienda
    @ColumnInfo(name = "observacion")
    @Expose
    private String observacion;

    @SerializedName("empadronador")
    @ColumnInfo(name = "empadronador")
    @Expose
    private String empadronador;

    @SerializedName("numEmpadronador")
    @ColumnInfo(name = "numEmpadronador")
    @Expose
    private String numEmpadronador;

    @SerializedName("fechaCreacion")
    @ColumnInfo(name = "fechaCreacion")
    @Expose
    private String fechaCreacion;//Probar cambiar a dateTime

    @SerializedName("fechaModificacion")
    @ColumnInfo(name = "fechaModificacion")
    @Expose
    private String fechaModificacion;//Probar cambiar a dateTime

    @SerializedName("fechaRecepcion")
    @ColumnInfo(name = "fechaRecepcion")
    @Expose
    private String fechaRecepcion;//Probar cambiar a dateTime

    @SerializedName("fechaEntrada")
    @ColumnInfo(name = "fechaEntrada")
    @Expose
    private String fechaEntrada;//Probar cambiar a dateTime

    @SerializedName("localizacion")
    @ColumnInfo(name = "localizacion")
    @Expose
    private String localizacion;//Probar cambiar a dateTime

    @ColumnInfo(name = "flagEnvio")
    @Expose
    private boolean flagEnvio;//Probar cambiar a dateTime

    // @SerializedName("flagPrimerEnvio")
    @ColumnInfo(name = "flagPrimerEnvio")
    @Expose
    private boolean flagPrimerEnvio;//Probar cambiar a dateTime

    public OtrasEstructuras(@NonNull String llave, String codigoSegmento, String provinciaID, String distritoID, String corregimientoID, String subzona, String segmento, String division, String numEstructura, int tipoEstructura, String observacion, String empadronador, String numEmpadronador, String fechaCreacion, String fechaModificacion, String fechaRecepcion, String fechaEntrada, String localizacion, boolean flagEnvio, boolean flagPrimerEnvio) {
        this.llave = llave;
        this.codigoSegmento = codigoSegmento;
        this.provinciaID = provinciaID;
        this.distritoID = distritoID;
        this.corregimientoID = corregimientoID;
        this.subzona = subzona;
        this.segmento = segmento;
        this.division = division;
        this.numEstructura = numEstructura;
        this.tipoEstructura = tipoEstructura;
        this.observacion = observacion;
        this.empadronador = empadronador;
        this.numEmpadronador = numEmpadronador;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.fechaRecepcion = fechaRecepcion;
        this.fechaEntrada = fechaEntrada;
        this.localizacion = localizacion;
        this.flagEnvio = flagEnvio;
        this.flagPrimerEnvio = flagPrimerEnvio;
    }

    @NonNull
    public String getLlave() {
        return llave;
    }

    public void setLlave(@NonNull String llave) {
        this.llave = llave;
    }

    public String getCodigoSegmento() {
        return codigoSegmento;
    }

    public void setCodigoSegmento(String codigoSegmento) {
        this.codigoSegmento = codigoSegmento;
    }

    public String getProvinciaID() {
        return provinciaID;
    }

    public void setProvinciaID(String provinciaID) {
        this.provinciaID = provinciaID;
    }

    public String getDistritoID() {
        return distritoID;
    }

    public void setDistritoID(String distritoID) {
        this.distritoID = distritoID;
    }

    public String getCorregimientoID() {
        return corregimientoID;
    }

    public void setCorregimientoID(String corregimientoID) {
        this.corregimientoID = corregimientoID;
    }

    public String getSubzona() {
        return subzona;
    }

    public void setSubzona(String subzona) {
        this.subzona = subzona;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getNumEstructura() {
        return numEstructura;
    }

    public void setNumEstructura(String numEstructura) {
        this.numEstructura = numEstructura;
    }

    public int getTipoEstructura() {
        return tipoEstructura;
    }

    public void setTipoEstructura(int tipoEstructura) {
        this.tipoEstructura = tipoEstructura;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEmpadronador() {
        return empadronador;
    }

    public void setEmpadronador(String empadronador) {
        this.empadronador = empadronador;
    }

    public String getNumEmpadronador() {
        return numEmpadronador;
    }

    public void setNumEmpadronador(String numEmpadronador) {
        this.numEmpadronador = numEmpadronador;
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

    public String getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(String fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public boolean isFlagEnvio() {
        return flagEnvio;
    }

    public void setFlagEnvio(boolean flagEnvio) {
        this.flagEnvio = flagEnvio;
    }

    public boolean isFlagPrimerEnvio() {
        return flagPrimerEnvio;
    }

    public void setFlagPrimerEnvio(boolean flagPrimerEnvio) {
        this.flagPrimerEnvio = flagPrimerEnvio;
    }
}
