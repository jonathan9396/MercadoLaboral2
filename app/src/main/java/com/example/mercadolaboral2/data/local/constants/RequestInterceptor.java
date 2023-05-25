package com.example.mercadolaboral2.data.local.constants;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {
    //    private static final String TAG = "RequestInterceptor";
    private final String tipo;

    public RequestInterceptor(String tipo) {
        this.tipo = tipo;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request;
        Request originalRequest = chain.request();
        HttpUrl originalHttpUrl = originalRequest.url();
        String key;

        if (tipo.equals("Map")) {
            key = "9050e2053e607e81d0efb261512a497f";
            request = originalRequest.newBuilder()
                    .addHeader("x-api-key", key)
                    .url(originalHttpUrl)
                    .build();
        } else if (tipo.equals("FTP")) {
            key = "ChangeThisSecretInProdPlease";
            request = originalRequest.newBuilder()
                    .addHeader("x-api-key", key)
                    .url(originalHttpUrl)
                    .build();
        } else {
            key = SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_ACCESS_TOKEN);
            request = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer " + key)
                    .url(originalHttpUrl)
                    .build();
        }

        return chain.proceed(request);
    }
}
