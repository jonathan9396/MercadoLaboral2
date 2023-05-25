package com.example.mercadolaboral2.data.remote.networkServices;

import androidx.lifecycle.LiveData;


import com.example.mercadolaboral2.data.remote.ApiResponse;

import com.example.mercadolaboral2.data.remote.networkEntities.CodigoAccesoResponse;

import com.example.mercadolaboral2.data.remote.networkEntities.PostCodigoAcceso;

import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IDEATIApiService {
    @POST("Administracion/GenerarCodigoAcceso/")
    LiveData<ApiResponse<CodigoAccesoResponse>> getCodigoECensoCall(@Body PostCodigoAcceso postCodigoAcceso);
}
