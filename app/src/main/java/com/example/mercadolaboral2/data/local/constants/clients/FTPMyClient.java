package com.example.mercadolaboral2.data.local.constants.clients;


import com.example.mercadolaboral2.data.local.constants.AppConstants;

import com.example.mercadolaboral2.data.local.constants.RequestInterceptor;

import com.example.mercadolaboral2.data.local.constants.TokenAuthenticator;

import com.example.mercadolaboral2.data.remote.LiveDataCallAdapterFactory;

import com.example.mercadolaboral2.data.remote.networkServices.FTPApiService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FTPMyClient {
    private static FTPMyClient instance = null;
    private final FTPApiService ftpApiService;

    public FTPMyClient() {
//         RequestInterceptor: incluir en la cabecera (URL) de la
//         petición el TOKEN o API_KEEY que autoriza al usuario
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.authenticator(new TokenAuthenticator());
        okHttpClientBuilder.addInterceptor(new RequestInterceptor("FTP"));
//        okHttpClientBuilder.addInterceptor(new RefreshInterceptor());
        OkHttpClient cliente = okHttpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL_FTP)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(cliente)
                .build();

        ftpApiService = retrofit.create(FTPApiService.class);
    }

    //Patron Singleton
    public static FTPMyClient getInstance() {
        if (instance == null) {
            instance = new FTPMyClient();
        }
        return instance;
    }

    public FTPApiService getFtpApiService() {
        return ftpApiService;
    }
}
