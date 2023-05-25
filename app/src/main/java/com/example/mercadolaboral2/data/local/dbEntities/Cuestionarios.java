package com.example.mercadolaboral2.data.local.dbEntities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "cuestionarios",
        foreignKeys = {
                @ForeignKey(onDelete = CASCADE, entity = Muestra.class,
                        parentColumns = "muestraId", childColumns = "muestraId")},
        indices = {@Index("codigoSegmento")})
public class Cuestionarios {

    @SerializedName("entrevistaId")
    @ColumnInfo(name = "entrevistaId")
    @PrimaryKey
    @NonNull
    @Expose
    private String entrevistaId;

    @SerializedName("entrevistaBaseId")
    @ColumnInfo(name = "entrevistaBaseId")
    @PrimaryKey
    @NonNull
    @Expose
    private String entrevistaBaseId;

    @SerializedName("muestraId")
    @ColumnInfo(name = "muestraId")
    @Expose
    private String muestraId;

    @SerializedName("entrevistaNum")
    @ColumnInfo(name = "entrevistaNum")
    @Expose
    private int entrevistaNum;

    @SerializedName("hogar")
    @ColumnInfo(name = "hogar")
    @Expose
    private int hogar;

    @SerializedName("recorrido")
    @ColumnInfo(name = "recorrido")
    @Expose
    private String recorrido;

    @SerializedName("identificacion")
    @ColumnInfo(name = "identificacion")
    @Expose
    private String identificacion;//Se añadió a la tabla SQL

    @SerializedName("datos")
    @ColumnInfo(name = "datos")
    @Expose
    private String datos;

    @SerializedName("notas")
    @ColumnInfo(name = "notas")
    @Expose
    private String notas;

    @SerializedName("datosJson")
    @ColumnInfo(name = "datosJson")
    @Expose
    private String datosJson;

    @SerializedName("lugarPoblado")
    @ColumnInfo(name = "lugarPoblado")
    @Expose
    private String lugarPoblado;

    @SerializedName("calle")
    @ColumnInfo(name = "calle")
    @Expose
    private String calle;

    @SerializedName("casa")
    @ColumnInfo(name = "casa")
    @Expose
    private String casa;

    @SerializedName("piso")
    @ColumnInfo(name = "piso")
    @Expose
    private String piso;

    @SerializedName("apartamento")
    @ColumnInfo(name = "apartamento")
    @Expose
    private String apartamento;

    @SerializedName("jefe")
    @ColumnInfo(name = "jefe")
    @Expose
    private String jefe;

    @SerializedName("otraReferencia")
    @ColumnInfo(name = "otraReferencia")
    @Expose
    private String otraReferencia;

    @SerializedName("resultadoId")
    @ColumnInfo(name = "resultadoId")
    @Expose
    private int resultadoId;

    @SerializedName("fechaCreacion")
    @ColumnInfo(name = "fechaCreacion")
    @Expose
    private String fechaCreacion;

    @SerializedName("fechaModificacion")
    @ColumnInfo(name = "fechaModificacion")
    @Expose
    private String fechaModificacion;//Probar cambiar a dateTime

    @ColumnInfo(name = "flagEnvio")
    @Expose
    private boolean flagEnvio;

    @ColumnInfo(name = "flagPrimerEnvio")
    @Expose
    private boolean flagPrimerEnvio;

    @SerializedName("estado")
    @ColumnInfo(name = "estado_cuestionario")
    @Expose
    private int estado;

    @SerializedName("erroresEstructura")
    @ColumnInfo(name = "erroresEstructura")
    @Expose
    private String erroresEstructura;

    public Cuestionarios(@NonNull String entrevistaId, @NonNull String entrevistaBaseId, String muestraId, int entrevistaNum, int hogar,
                         String recorrido, String identificacion, String datos, String notas, String datosJson, String lugarPoblado,
                         String calle, String casa, String piso, String apartamento, String jefe, String otraReferencia, int resultadoId,
                         String fechaCreacion, String fechaModificacion, boolean flagEnvio, boolean flagPrimerEnvio, int estado, String erroresEstructura) {

        this.entrevistaId = entrevistaId;
        this.entrevistaBaseId = entrevistaBaseId;
        this.muestraId = muestraId;
        this.entrevistaNum = entrevistaNum;
        this.hogar = hogar;
        this.recorrido = recorrido;
        this.identificacion = identificacion;
        this.datos = datos;
        this.notas = notas;
        this.datosJson = datosJson;
        this.lugarPoblado = lugarPoblado;
        this.calle = calle;
        this.casa = casa;
        this.piso = piso;
        this.apartamento = apartamento;
        this.jefe = jefe;
        this.otraReferencia = otraReferencia;
        this.resultadoId = resultadoId;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.flagEnvio = flagEnvio;
        this.flagPrimerEnvio = flagPrimerEnvio;
        this.estado=estado;
        this.erroresEstructura=erroresEstructura;
    }

    @NonNull
    public String getEntrevistaId() {
        return entrevistaId;
    }

    public void setEntrevistaId(@NonNull String entrevistaId) {
        this.entrevistaId = entrevistaId;
    }

    @NonNull
    public String getEntrevistaBaseId() {
        return entrevistaBaseId;
    }

    public void setEntrevistaBaseId(@NonNull String entrevistaBaseId) {
        this.entrevistaBaseId = entrevistaBaseId;
    }

    public String getMuestraId() {
        return muestraId;
    }

    public void setMuestraId(String muestraId) {
        this.muestraId = muestraId;
    }

    public int getEntrevistaNum() {
        return entrevistaNum;
    }

    public void setEntrevistaNum(int entrevistaNum) {
        this.entrevistaNum = entrevistaNum;
    }

    public int getHogar() {
        return hogar;
    }

    public void setHogar(int hogar) {
        this.hogar = hogar;
    }

    public String getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(String recorrido) {
        this.recorrido = recorrido;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getDatosJson() {
        return datosJson;
    }

    public void setDatosJson(String datosJson) {
        this.datosJson = datosJson;
    }

    public String getLugarPoblado() {
        return lugarPoblado;
    }

    public void setLugarPoblado(String lugarPoblado) {
        this.lugarPoblado = lugarPoblado;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCasa() {
        return casa;
    }

    public void setCasa(String casa) {
        this.casa = casa;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getApartamento() {
        return apartamento;
    }

    public void setApartamento(String apartamento) {
        this.apartamento = apartamento;
    }

    public String getJefe() {
        return jefe;
    }

    public void setJefe(String jefe) {
        this.jefe = jefe;
    }

    public String getOtraReferencia() {
        return otraReferencia;
    }

    public void setOtraReferencia(String otraReferencia) {
        this.otraReferencia = otraReferencia;
    }

    public int getResultadoId() {
        return resultadoId;
    }

    public void setResultadoId(int resultadoId) {
        this.resultadoId = resultadoId;
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getErroresEstructura() {
        return erroresEstructura;
    }

    public void setErroresEstructura(String erroresEstructura) {
        this.erroresEstructura = erroresEstructura;
    }
}
