package com.example.mercadolaboral2.data.local.constants;


import com.example.mercadolaboral2.data.local.constants.clients.AuthClient;

import com.example.mercadolaboral2.data.local.constants.clients.RefreshClient;

import com.example.mercadolaboral2.data.remote.networkEntities.AuthResponse;

import com.example.mercadolaboral2.data.remote.networkEntities.AuthResponse2;

import com.example.mercadolaboral2.data.remote.networkEntities.PostLogin;

import com.example.mercadolaboral2.data.remote.networkEntities.RefreshTokenRequest;

import com.example.mercadolaboral2.data.remote.networkServices.CensoAuthService;

import com.example.mercadolaboral2.data.remote.networkServices.CensoRefreshToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;

public class TokenAuthenticator implements Authenticator {
    CensoRefreshToken censoRefreshToken;
    CensoAuthService censoAuthService;

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NotNull Response response) {
        RefreshClient refreshClient = RefreshClient.getInstance();
        censoRefreshToken = refreshClient.getCensoRefreshToken();

        if (refreshToken()) {
            // Add new header to rejected request and retry it
            return response.request().newBuilder()
                    .header("Authorization", "Bearer "
                            + SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_ACCESS_TOKEN))
                    .build();
        } else {
            if (SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME) != null
                    && !SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME).equals("")) {
                AuthClient authClient = AuthClient.getInstance();
                censoAuthService = authClient.getCensoAuthService();
                PostLogin postLogin = new PostLogin(SharedPreferencesManager.getSomeStringValue(
                        AppConstants.PREF_USERNAME),
                        SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_PASSWORD));

                Call<AuthResponse> call = censoAuthService.doLogin(postLogin);
                try {
                    retrofit2.Response<AuthResponse> authResponseResponse = call.execute();
                    if (authResponseResponse.isSuccessful()) {
                        // save new token to sharedpreferences, storage etc.
                        AuthResponse authResponse = authResponseResponse.body();
                        assert authResponse != null;
                        SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_ACCESS_TOKEN,
                                authResponse.getToken());
                        return response.request().newBuilder()
                                .header("Authorization", "Bearer "
                                        + SharedPreferencesManager.getSomeStringValue(
                                        AppConstants.PREF_ACCESS_TOKEN))
                                .build();
                    } else {
                        //cannot refresh
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        }
    }

    public boolean refreshToken() {
        // you can use RxJava with Retrofit and add blockingGet
        // it is up to you how to refresh your token
        Call<AuthResponse2> callRefresh = censoRefreshToken.doRefresh(new RefreshTokenRequest(
                SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_REFRESH_TOKEN)));
        try {
            retrofit2.Response<AuthResponse2> authResponseResponse = callRefresh.execute();
            if (authResponseResponse.isSuccessful()) {
                // save new token to sharedpreferences, storage etc.
                AuthResponse2 authResponse = authResponseResponse.body();
                assert authResponse != null;
                SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_ACCESS_TOKEN,
                        authResponse.getAccessToken());
                SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_REFRESH_TOKEN,
                        authResponse.getRefreshToken());
                return true;
            } else {
                //cannot refresh
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}