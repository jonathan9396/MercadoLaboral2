package com.example.mercadolaboral2.data.local.constants.clients;


import com.example.mercadolaboral2.data.local.constants.TokenAuthenticator2;

import com.example.mercadolaboral2.data.remote.LiveDataCallAdapterFactory;

import com.example.mercadolaboral2.data.remote.networkServices.IDEATIApiService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ECensoClient {
    private static ECensoClient instance = null;
    private final IDEATIApiService ideatiApiService;

    public ECensoClient() {
//         RequestInterceptor: incluir en la cabecera (URL) de la
//         petición el TOKEN o API_KEEY que autoriza al usuario
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.authenticator(new TokenAuthenticator2());
//        okHttpClientBuilder.addInterceptor(new RefreshInterceptor());
        OkHttpClient cliente = okHttpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.censospanama.pa/ec-test-seguridad/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(cliente)
                .build();

        ideatiApiService = retrofit.create(IDEATIApiService.class);
    }

    //Patron Singleton
    public static ECensoClient getInstance() {
        if (instance == null) {
            instance = new ECensoClient();
        }
        return instance;
    }

    public IDEATIApiService getIDEATIApiService() {
        return ideatiApiService;
    }
}
