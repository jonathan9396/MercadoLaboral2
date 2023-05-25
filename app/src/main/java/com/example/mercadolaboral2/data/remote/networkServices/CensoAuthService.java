package com.example.mercadolaboral2.data.remote.networkServices;


import com.example.mercadolaboral2.data.remote.networkEntities.AuthResponse;

import com.example.mercadolaboral2.data.remote.networkEntities.PostLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CensoAuthService {
    @POST("auth")
    Call<AuthResponse> doLogin(@Body PostLogin postLogin);
}
