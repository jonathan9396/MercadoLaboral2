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

@Entity(tableName = "EntrevistaB",foreignKeys= {
        @ForeignKey(onDelete = CASCADE, entity = Muestra.class,parentColumns = "muestraId", childColumns = "muestraId")},
indices = {@Index("muestraId")})

public class EntrevistaBase {
    @SerializedName("entrevistaId")
    @ColumnInfo(name = "entrevistaId")
    @PrimaryKey
    @NonNull
    @Expose
    private String entrevistaId;

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

    @SerializedName("empadronadorId")
    @ColumnInfo(name = "empadronadorId")
    @Expose
    private String empadronadorId;

    @SerializedName("fechaActualizacion")
    @ColumnInfo(name = "fechaActualizacion")
    @Expose
    private String fechaActualizacion;

    @SerializedName("fechaAsignacion")
    @ColumnInfo(name = "fechaAsignacion")
    @Expose
    private int fechaAsignacion;

    @SerializedName("resultado")
    @ColumnInfo(name = "resultado")
    @Expose
    private String resultado;//Probar cambiar a dateTime


    @SerializedName("muestra")
    @ColumnInfo(name = "muestra")
    @Expose
    private String muestra;

    @SerializedName("empadronador")
    @ColumnInfo(name = "empadronador")
    @Expose
    private String empadronador;

    @SerializedName("estado")
    @ColumnInfo(name = "estado_cuestionario")
    @Expose
    private int estado;

    public EntrevistaBase(@NonNull String entrevistaId, String muestraId, int entrevistaNum, int hogar, String recorrido,
                         String identificacion, String lugarPoblado, String calle, String casa, String piso, String apartamento,
                         String jefe, String otraReferencia, int resultadoId, String empadronadorId, String fechaActualizacion,
                         int fechaAsignacion, String resultado, String muestra, String empadronador, int estado) {
        this.entrevistaId = entrevistaId;
        this.muestraId = muestraId;
        this.entrevistaNum = entrevistaNum;
        this.hogar=hogar;
        this.recorrido = recorrido;
        this.identificacion = identificacion;
        this.lugarPoblado = lugarPoblado;
        this.calle = calle;
        this.casa = casa;
        this.piso = piso;
        this.apartamento = apartamento;
        this.jefe = jefe;
        this.otraReferencia = otraReferencia;
        this.resultadoId = resultadoId;
        this.empadronadorId = empadronadorId;
        this.fechaActualizacion = fechaActualizacion;
        this.fechaAsignacion = fechaAsignacion;
        this.resultado = resultado;
        this.muestra = muestra;
        this.empadronador = empadronador;
        this.estado=estado;
    }
    @NonNull
    public String getEntrevistaId() {
        return entrevistaId;
    }

    public void setEntrevistaId(@NonNull String entrevistaId) {
        this.entrevistaId = entrevistaId;
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

    public String getEmpadronadorId() {
        return empadronadorId;
    }

    public void setEmpadronadorId(String empadronadorId) {
        this.empadronadorId = empadronadorId;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public int getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(int fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getMuestra() {
        return muestra;
    }

    public void setMuestra(String muestra) {
        this.muestra = muestra;
    }

    public String getEmpadronador() {
        return empadronador;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void setEmpadronador(String empadronador) {
        this.empadronador = empadronador;


    }
}
