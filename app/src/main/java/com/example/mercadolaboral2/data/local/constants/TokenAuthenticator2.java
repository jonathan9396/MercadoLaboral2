package com.example.mercadolaboral2.data.local.constants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator2 implements Authenticator {

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NotNull Response response) {
        // Add new header to rejected request and retry it
        return response.request().newBuilder()
                .header("Authorization", "Bearer "
                        + "HD5qhELlDOHM3FmWy_QdDqWjhdbvVwQXzc4mlQ4VMFA")
                .build();
    }
}
