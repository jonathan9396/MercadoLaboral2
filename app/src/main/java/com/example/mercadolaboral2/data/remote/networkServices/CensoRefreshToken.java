package com.example.mercadolaboral2.data.remote.networkServices;


import com.example.mercadolaboral2.data.remote.networkEntities.AuthResponse2;

import com.example.mercadolaboral2.data.remote.networkEntities.RefreshTokenRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CensoRefreshToken {

    @POST("auth/refresh")
    Call<AuthResponse2> doRefresh(@Body RefreshTokenRequest refreshTokenRequest);
}
