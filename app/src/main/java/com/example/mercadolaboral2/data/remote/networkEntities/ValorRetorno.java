package com.example.mercadolaboral2.data.remote.networkEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValorRetorno {
    @SerializedName("token")
    @Expose
    private String token;

    public ValorRetorno(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
