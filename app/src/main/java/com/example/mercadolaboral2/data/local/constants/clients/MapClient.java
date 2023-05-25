package com.example.mercadolaboral2.data.local.constants.clients;


import com.example.mercadolaboral2.data.local.constants.AppConstants;

import com.example.mercadolaboral2.data.local.constants.RequestInterceptor;

import com.example.mercadolaboral2.data.local.constants.TokenAuthenticator;

import com.example.mercadolaboral2.data.remote.LiveDataCallAdapterFactory;

import com.example.mercadolaboral2.data.remote.networkServices.CensoMapService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapClient {
    private static MapClient instance = null;
    private final CensoMapService censoMapService;

    public MapClient() {
//         RequestInterceptor: incluir en la cabecera (URL) de la
//         petición el TOKEN o API_KEY que autoriza al usuario
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.authenticator(new TokenAuthenticator());
        okHttpClientBuilder.addInterceptor(new RequestInterceptor("Map"));
//        okHttpClientBuilder.addInterceptor(new RefreshInterceptor());
        OkHttpClient cliente = okHttpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL_MAPS)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(cliente)
                .build();

        censoMapService = retrofit.create(CensoMapService.class);
    }

    //Patron Singleton
    public static MapClient getInstance() {
        if (instance == null) {
            instance = new MapClient();
        }
        return instance;
    }

    public CensoMapService getCensoMapService() {
        return censoMapService;
    }
}