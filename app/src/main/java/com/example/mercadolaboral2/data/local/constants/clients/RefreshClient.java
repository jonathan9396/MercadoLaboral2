package com.example.mercadolaboral2.data.local.constants.clients;


import com.example.mercadolaboral2.data.local.constants.AppConstants;

import com.example.mercadolaboral2.data.local.constants.RequestInterceptor;

import com.example.mercadolaboral2.data.remote.LiveDataCallAdapterFactory;

import com.example.mercadolaboral2.data.remote.networkServices.CensoRefreshToken;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RefreshClient {
    private static RefreshClient instance = null;
    private final CensoRefreshToken censoRefreshToken;

    public RefreshClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new RequestInterceptor("Refresh"));
        OkHttpClient cliente = okHttpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(cliente)
                .build();

        censoRefreshToken = retrofit.create(CensoRefreshToken.class);
    }

    //Patron Singleton
    public static RefreshClient getInstance() {
        if (instance == null) {
            instance = new RefreshClient();
        }
        return instance;
    }

    public CensoRefreshToken getCensoRefreshToken() {
        return censoRefreshToken;
    }
}
