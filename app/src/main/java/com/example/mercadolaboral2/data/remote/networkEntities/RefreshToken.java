package com.example.mercadolaboral2.data.remote.networkEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RefreshToken {
    @SerializedName("userName")
    @Expose
    private String username;
    @SerializedName("tokenString")
    @Expose
    private String tokenString;
    @SerializedName("expireAt")
    @Expose
    private String expireAt;

    public RefreshToken(String username, String tokenString, String expireAt) {
        this.username = username;
        this.tokenString = tokenString;
        this.expireAt = expireAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTokenString() {
        return tokenString;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    public String getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(String expireAt) {
        this.expireAt = expireAt;
    }
}