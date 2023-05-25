package com.example.mercadolaboral2.data.local.dbEntities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;

@Entity(tableName = "usuarios")
public class Usuarios {
    @PrimaryKey
    private int idUsuarios;
    private String rol;
    private String region;
    private String zona;
    private String numeracion;
    private String codigo;
    private String nombreUsuario;
    private String nombre;
    private String contrasena;
    private Timestamp fechaCreacion;
    private Timestamp fechaModificacion;
    private Timestamp fechaUltimaConexion;

    public Usuarios(int idUsuarios, String rol, String region, String zona, String numeracion, String codigo, String nombreUsuario, String nombre, String contrasena, Timestamp fechaCreacion, Timestamp fechaModificacion, Timestamp fechaUltimaConexion) {
        this.idUsuarios = idUsuarios;
        this.rol = rol;
        this.region = region;
        this.zona = zona;
        this.numeracion = numeracion;
        this.codigo = codigo;
        this.nombreUsuario = nombreUsuario;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.fechaUltimaConexion = fechaUltimaConexion;
    }

    public int getIdUsuarios() {
        return idUsuarios;
    }

    public void setIdUsuarios(int idUsuarios) {
        this.idUsuarios = idUsuarios;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Timestamp fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Timestamp getFechaUltimaConexion() {
        return fechaUltimaConexion;
    }

    public void setFechaUltimaConexion(Timestamp fechaUltimaConexion) {
        this.fechaUltimaConexion = fechaUltimaConexion;
    }
}
