package com.example.mercadolaboral2.data.remote.networkServices;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface FTPApiService {
    @Multipart
    @POST("upload/{username}")
    Call<Void> sendZipFile(@Part MultipartBody.Part file,
                           @Path("username") String username);
}
