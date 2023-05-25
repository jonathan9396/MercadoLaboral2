package com.example.mercadolaboral2.data.local.constants.clients;


import com.example.mercadolaboral2.data.local.constants.AppConstants;

import com.example.mercadolaboral2.data.local.constants.RequestInterceptor;

import com.example.mercadolaboral2.data.local.constants.TokenAuthenticator;

import com.example.mercadolaboral2.data.remote.LiveDataCallAdapterFactory;

import com.example.mercadolaboral2.data.remote.networkServices.CensoApiService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CensoClient {
    private static CensoClient instance = null;
    private final CensoApiService censoApiService;

    public CensoClient() {
//         RequestInterceptor: incluir en la cabecera (URL) de la
//         petición el TOKEN o API_KEEY que autoriza al usuario
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.authenticator(new TokenAuthenticator());
        okHttpClientBuilder.addInterceptor(new RequestInterceptor("Censo"));
//        okHttpClientBuilder.addInterceptor(new RefreshInterceptor());
        OkHttpClient cliente = okHttpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(cliente)
                .build();

        censoApiService = retrofit.create(CensoApiService.class);
    }

    //Patron Singleton
    public static CensoClient getInstance() {
        if (instance == null) {
            instance = new CensoClient();
        }
        return instance;
    }

    public CensoApiService getCensoApiService() {
        return censoApiService;
    }
}
