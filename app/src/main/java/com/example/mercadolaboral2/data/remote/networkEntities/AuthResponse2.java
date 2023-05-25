package com.example.mercadolaboral2.data.remote.networkEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthResponse2 {
    @SerializedName("userName")
    @Expose
    private String userName;

    @SerializedName("codigo")
    @Expose
    private String codigo;

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("region")
    @Expose
    private String region;

    @SerializedName("accessToken")
    @Expose
    private String accessToken;

    @SerializedName("refreshToken")
    @Expose
    private String refreshToken;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public AuthResponse2(String userName, String codigo, String role, String region, String accessToken, String refreshToken) {
        this.userName = userName;
        this.codigo = codigo;
        this.role = role;
        this.region = region;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;


    }
}