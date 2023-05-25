package com.example.mercadolaboral2.data.remote.networkEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//GET: api/Muestra/Asignados/{UsuarioId}
//[HttpGet("Asignados/{usuarioId}")]

////GET: api/Muestra/todos/{UsuarioId}
////[HttpGet("todos/{usuarioId}")]

//         //GET: api/Muestra/Adicionales/{UsuarioId}
//        [HttpGet("Adicionales/{usuarioId}")]
public class GetSegmentosResponse {
    @SerializedName("muestraId")
    @Expose
    private String muestraId;

    @SerializedName("paR01_ID")
    @Expose
    private String paR01_ID;

    @SerializedName("paR01_DESC")
    @Expose
    private String paR01_DESC;

    @SerializedName("paR02_ID")
    @Expose
    private String paR02_ID;

    @SerializedName("paR02_DESC")
    @Expose
    private String paR02_DESC;

    @SerializedName("paR03_ID")
    @Expose
    private String paR03_ID;

    @SerializedName("paR03_DESC")
    @Expose
    private String paR03_DESC;

    @SerializedName("paR04_ID")
    @Expose
    private String paR04_ID;

    @SerializedName("paR04_DESC")
    @Expose
    private String paR04_DESC;

    @SerializedName("paR05_ID")
    @Expose
    private String paR05_ID;

    @SerializedName("paR06_ID")
    @Expose
    private String paR06_ID;

    @SerializedName("llave")
    @Expose
    private String llave;

    @SerializedName("revisado")
    @Expose
    private String revisado;

    @SerializedName("estado")
    @Expose
    private String estado;

    @SerializedName("supervisorId")
    @Expose
    private String supervisorId;//FK

    @SerializedName("fechaCreacion")
    @Expose
    private String fechaCreacion;// CONFIRMAR

    @SerializedName("fechaDeModificacion")
    @Expose
    private String fechaDeModificacion;

    @SerializedName("fechaDeHabilitación")
    @Expose
    private String fechaDeHabilitacion;//DATE TIME

    @SerializedName("fechaCierre")
    @Expose
    private String fechaCierre;//DATE TIME

    @SerializedName("completamenteAsignado")
    @Expose
    private String completamenteAsignado;//DATE TIME

    @SerializedName("fechaUltimaCarga")
    @Expose
    private String fechaUltimaCarga;//DATE TIME

    @SerializedName("movil")
    @Expose
    private String movil;

    @SerializedName("supervisor")
    @Expose
    private String supervisor;

    @SerializedName("entrevistas")
    @Expose
    private String entrevistas;

    @SerializedName("entrevistasBase")
    @Expose
    private String entrevistasBase;


    public GetSegmentosResponse(String muestraId, String paR01_ID, String paR01_DESC, String paR02_ID, String paR02_DESC, String paR03_ID, String paR03_DESC, String paR04_ID, String paR04_DESC, String paR05_ID, String paR06_ID, String llave, String revisado, String estado, String supervisorId, String fechaCreacion, String fechaDeModificacion, String fechaDeHabilitacion, String fechaCierre, String completamenteAsignado, String fechaUltimaCarga, String movil, String supervisor, String entrevistas, String entrevistasBase) {
        this.muestraId = muestraId;
        this.paR01_ID = paR01_ID;
        this.paR01_DESC = paR01_DESC;
        this.paR02_ID = paR02_ID;
        this.paR02_DESC = paR02_DESC;
        this.paR03_ID = paR03_ID;
        this.paR03_DESC = paR03_DESC;
        this.paR04_ID = paR04_ID;
        this.paR04_DESC = paR04_DESC;
        this.paR05_ID = paR05_ID;
        this.paR06_ID = paR06_ID;
        this.llave = llave;
        this.revisado = revisado;
        this.estado = estado;
        this.supervisorId = supervisorId;
        this.fechaCreacion = fechaCreacion;
        this.fechaDeModificacion = fechaDeModificacion;
        this.fechaDeHabilitacion = fechaDeHabilitacion;
        this.fechaCierre = fechaCierre;
        this.completamenteAsignado = completamenteAsignado;
        this.fechaUltimaCarga = fechaUltimaCarga;
        this.movil = movil;
        this.supervisor = supervisor;
        this.entrevistas = entrevistas;
        this.entrevistasBase = entrevistasBase;

    }


    public String getMuestraId() {
        return muestraId;
    }

    public void setMuestraId(String muestraId) {
        this.muestraId = muestraId;
    }

    public String getPaR01_ID() {return paR01_ID;}
    public void setPaR01_ID(String paR01_ID) {
        this.paR01_ID = paR01_ID;
    }

    public String getPaR01_DESC() {return paR01_DESC;}
    public void setPaR01_DESC(String paR01_DESC) {
        this.paR01_DESC = paR01_DESC;
    }

    public String getPaR02_ID() {
        return paR02_ID;
    }
    public void setPaR02_ID(String paR02_ID) {
        this.paR02_ID = paR02_ID;
    }

    public String getPaR02_DESC() {
        return paR02_DESC;
    }
    public void setPaR02_DESC(String paR02_DESC) {
        this.paR02_DESC = paR02_DESC;
    }

    public String getPaR03_ID() {
        return paR03_ID;
    }
    public void setPaR03_ID(String paR03_ID) {
        this.paR03_ID = paR03_ID;
    }

    public String getPaR03_DESC() {
        return paR03_DESC;
    }
    public void setPaR03_DESC(String paR03_DESC) {
        this.paR03_DESC = paR03_DESC;
    }

    public String getPaR04_ID() {
        return paR04_ID;
    }
    public void setPaR04_ID(String paR04_ID) {
        this.paR04_ID = paR04_ID;
    }

    public String getPaR04_DESC() {
        return paR04_DESC;
    }
    public void setPaR04_DESC(String paR04_DESC) {
        this.paR04_DESC = paR04_DESC;
    }

    public String getPaR05_ID() {
        return paR05_ID;
    }
    public void setPaR05_ID(String paR05_ID) {
        this.paR05_ID = paR05_ID;
    }

    public String getPaR06_ID() {
        return paR06_ID;
    }
    public void setPaR06_ID(String paR06_ID) {
        this.paR06_ID = paR06_ID;
    }

    public String getLlave() {
        return llave;
    }
    public void setLlave(String llave) {
        this.llave = llave;
    }

    public String getRevisado() {
        return revisado;
    }
    public void setRevisado(String revisado) {
        this.revisado = revisado;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSupervisorId() {
        return supervisorId;
    }
    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getFechaCreacion() {return fechaCreacion;}
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaDeModificacion() {
        return fechaDeModificacion;
    }
    public void setFechaDeModificacion(String fechaDeModificacion) {this.fechaDeModificacion = fechaDeModificacion;}

    public String getFechaDeHabilitacion() {
        return fechaDeHabilitacion;
    }
    public void setFechaDeHabilitacion(String fechaDeHabilitacion) {this.fechaDeHabilitacion = fechaDeHabilitacion;}

    public String getFechaCierre() {
        return fechaCierre;
    }
    public void setFechaCierre(String fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getCompletamenteAsignado() {
        return completamenteAsignado;
    }
    public void setCompletamenteAsignado(String completamenteAsignado) {this.completamenteAsignado = completamenteAsignado;}

    public String getFechaUltimaCarga() {
        return fechaUltimaCarga;
    }
    public void setFechaUltimaCarga(String fechaUltimaCarga) {this.fechaUltimaCarga = fechaUltimaCarga;}

    public String getMovil() {
        return movil;
    }
    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getSupervisor() {
        return supervisor;
    }
    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getEntrevistas() {
        return entrevistas;
    }
    public void setEntrevistas(String entrevistas) {
        this.entrevistas = entrevistas;
    }

    public String getEntrevistasBase() {
        return entrevistasBase;
    }
    public void setEntrevistasBase(String entrevistasBase) {this.entrevistasBase = entrevistasBase;}
}
