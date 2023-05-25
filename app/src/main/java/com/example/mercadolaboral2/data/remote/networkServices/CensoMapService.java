package com.example.mercadolaboral2.data.remote.networkServices;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface CensoMapService {
    @GET("subzonazip/{username}/{region}/{zona}/{subzona}")
    @Streaming
    Call<ResponseBody> getMapsByRegionZonaSubzonaZIP(@Path("region")String region,
                                                     @Path("username")String username,
                                                     @Path("zona")String zona,
                                                     @Path("subzona")String subzona);
}//subzonazip/{username}/{region}/{zona}/{subzona}")]
