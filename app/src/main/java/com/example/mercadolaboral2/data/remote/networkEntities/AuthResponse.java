package com.example.mercadolaboral2.data.remote.networkEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("codigo")
    @Expose
    private String codigo;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("rol")
    @Expose
    private String rol;

    @SerializedName("expireAt")
    @Expose
    private String expireAt;

    public AuthResponse(String nombre, String codigo, String token, String rol, String expireAt){
        this.nombre = nombre;
        this.codigo = codigo;
        this.token = token;
        this.rol = rol;
        this.expireAt = expireAt;
    }

    //----Nombre
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre=nombre;
    }

    //----Codigo
    public String getCodigo(){
        return  codigo;
    }
    public void setCodigo(String codigo){
        this.codigo=codigo;
    }

    //----Rol
    public String getRol(){
        return rol;
    }
    public void setRol(String rol){
        this.rol= rol;
    }

    //----Token
    public String getToken(){
        return token;
    }
    public void setToken(String token){
        this.token=token;
    }

    //----Expire
    public String getExpireAt(){
        return expireAt;
    }
    public void setExpireAt(String expireAt){
        this.expireAt=expireAt;
    }

}
