package com.example.mercadolaboral2.data.remote.networkEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetRefreshEstado {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("estado")
    @Expose
    private int estado;

    @SerializedName("revision")
    @Expose
    private int revision;

    @SerializedName("asignacionPrincipal")
    @Expose
    private String asignacionPrincipal;

    @SerializedName("asignacionAlterna")
    @Expose
    private int asignacionAlterna;

    @SerializedName("asignacionAlternaCodigo")
    @Expose
    private String asignacionAlternaCodigo;

    @SerializedName("asignacionAlternaUsername")
    @Expose
    private String asignacionAlternaUsername;

    public GetRefreshEstado(String id, int estado, int revision, String asignacionPrincipal, int asignacionAlterna, String asignacionAlternaCodigo, String asignacionAlternaUsername) {
        this.id = id;
        this.estado = estado;
        this.revision = revision;
        this.asignacionPrincipal = asignacionPrincipal;
        this.asignacionAlterna = asignacionAlterna;
        this.asignacionAlternaCodigo = asignacionAlternaCodigo;
        this.asignacionAlternaUsername = asignacionAlternaUsername;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public String getAsignacionPrincipal() {
        return asignacionPrincipal;
    }

    public void setAsignacionPrincipal(String asignacionPrincipal) {
        this.asignacionPrincipal = asignacionPrincipal;
    }

    public int getAsignacionAlterna() {
        return asignacionAlterna;
    }

    public void setAsignacionAlterna(int asignacionAlterna) {
        this.asignacionAlterna = asignacionAlterna;
    }

    public String getAsignacionAlternaCodigo() {
        return asignacionAlternaCodigo;
    }

    public void setAsignacionAlternaCodigo(String asignacionAlternaCodigo) {
        this.asignacionAlternaCodigo = asignacionAlternaCodigo;
    }

    public String getAsignacionAlternaUsername() {
        return asignacionAlternaUsername;
    }

    public void setAsignacionAlternaUsername(String asignacionAlternaUsername) {
        this.asignacionAlternaUsername = asignacionAlternaUsername;
    }
}
