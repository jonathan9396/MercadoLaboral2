package com.example.mercadolaboral2.data.remote.networkEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CodigoAccesoResponse {
    @SerializedName("exitoso")
    @Expose
    private boolean exitoso;

    @SerializedName("errores")
    @Expose
    private List<String> errores;

    @SerializedName("mensajeExito")
    @Expose
    private Object mensajeExito;

    @SerializedName("valorRetorno")
    @Expose
    private ValorRetorno valorRetorno;

    public CodigoAccesoResponse(boolean exitoso, List<String> errores, Object mensajeExito, ValorRetorno valorRetorno) {
        this.exitoso = exitoso;
        this.errores = errores;
        this.mensajeExito = mensajeExito;
        this.valorRetorno = valorRetorno;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }

    public List<String> getErrores() {
        return errores;
    }

    public void setErrores(List<String> errores) {
        this.errores = errores;
    }

    public Object getMensajeExito() {
        return mensajeExito;
    }

    public void setMensajeExito(Object mensajeExito) {
        this.mensajeExito = mensajeExito;
    }

    public ValorRetorno getValorRetorno() {
        return valorRetorno;
    }

    public void setValorRetorno(ValorRetorno valorRetorno) {
        this.valorRetorno = valorRetorno;
    }
}
