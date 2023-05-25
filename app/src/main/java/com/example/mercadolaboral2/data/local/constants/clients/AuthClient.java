package com.example.mercadolaboral2.data.local.constants.clients;


import com.example.mercadolaboral2.data.local.constants.AppConstants;

import com.example.mercadolaboral2.data.remote.LiveDataCallAdapterFactory;

import com.example.mercadolaboral2.data.remote.networkServices.CensoAuthService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthClient {
    private static AuthClient instance = null;
    //    private DirectorioApiService directorioApiService;
    private final CensoAuthService censoAuthService;

    public AuthClient() {
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://84d1-200-46-58-8.ngrok.io/api/")
                .baseUrl(AppConstants.BASE_URL)
//                .baseUrl("http://localhost:5000/api/")
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        censoAuthService = retrofit.create(CensoAuthService.class);
    }

    //Patron Singleton
    public static AuthClient getInstance() {
        if (instance == null) {
            instance = new AuthClient();
        }
        return instance;
    }

    public CensoAuthService getCensoAuthService() {
        return censoAuthService;
    }
}
