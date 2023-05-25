package com.example.mercadolaboral2.data.remote.networkEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostCodigoAcceso {
    private String provincia;
    private String distrito;
    private String corregimiento;
    private String segmento;
    private String supervisor;
    private String div;
    private String numEmpadronador;
    private String empadronador;
    private String vivienda;
    private String hogar;

    @SerializedName("Region")
    @Expose
    private String regionID;

    @SerializedName("LugarPobladoCodigo")
    @Expose
    private String LugarPobladoID;

    @SerializedName("LugarPobladoNombre")
    @Expose
    private String LugarPobladoDescripcion;

    @SerializedName("BarriadaCodigo")
    @Expose
    private String barrioID;

    @SerializedName("BarriadaNombre")
    @Expose
    private String barrioDescripcion;

    private String area;

    @SerializedName("SubZona")
    @Expose
    private String subZonaID;

    public PostCodigoAcceso(String provincia, String distrito, String corregimiento, String segmento, String supervisor, String div, String numEmpadronador, String empadronador, String vivienda, String hogar, String regionID, String lugarPobladoID, String lugarPobladoDescripcion, String barrioID, String barrioDescripcion, String area, String subZonaID) {
        this.provincia = provincia;
        this.distrito = distrito;
        this.corregimiento = corregimiento;
        this.segmento = segmento;
        this.supervisor = supervisor;
        this.div = div;
        this.numEmpadronador = numEmpadronador;
        this.empadronador = empadronador;
        this.vivienda = vivienda;
        this.hogar = hogar;
        this.regionID = regionID;
        LugarPobladoID = lugarPobladoID;
        LugarPobladoDescripcion = lugarPobladoDescripcion;
        this.barrioID = barrioID;
        this.barrioDescripcion = barrioDescripcion;
        this.area = area;
        this.subZonaID = subZonaID;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getCorregimiento() {
        return corregimiento;
    }

    public void setCorregimiento(String corregimiento) {
        this.corregimiento = corregimiento;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getDiv() {
        return div;
    }

    public void setDiv(String div) {
        this.div = div;
    }

    public String getNumEmpadronador() {
        return numEmpadronador;
    }

    public void setNumEmpadronador(String numEmpadronador) {
        this.numEmpadronador = numEmpadronador;
    }

    public String getEmpadronador() {
        return empadronador;
    }

    public void setEmpadronador(String empadronador) {
        this.empadronador = empadronador;
    }

    public String getVivienda() {
        return vivienda;
    }

    public void setVivienda(String vivienda) {
        this.vivienda = vivienda;
    }

    public String getHogar() {
        return hogar;
    }

    public void setHogar(String hogar) {
        this.hogar = hogar;
    }

    public String getRegionID() {
        return regionID;
    }

    public void setRegionID(String regionID) {
        this.regionID = regionID;
    }

    public String getLugarPobladoID() {
        return LugarPobladoID;
    }

    public void setLugarPobladoID(String lugarPobladoID) {
        LugarPobladoID = lugarPobladoID;
    }

    public String getLugarPobladoDescripcion() {
        return LugarPobladoDescripcion;
    }

    public void setLugarPobladoDescripcion(String lugarPobladoDescripcion) {
        LugarPobladoDescripcion = lugarPobladoDescripcion;
    }

    public String getBarrioID() {
        return barrioID;
    }

    public void setBarrioID(String barrioID) {
        this.barrioID = barrioID;
    }

    public String getBarrioDescripcion() {
        return barrioDescripcion;
    }

    public void setBarrioDescripcion(String barrioDescripcion) {
        this.barrioDescripcion = barrioDescripcion;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSubZonaID() {
        return subZonaID;
    }

    public void setSubZonaID(String subZonaID) {
        this.subZonaID = subZonaID;
    }
}
