package com.example.mercadolaboral2.data.remote.networkServices;

import androidx.lifecycle.LiveData;


import com.example.mercadolaboral2.data.local.dbEntities.Cuestionarios;

import com.example.mercadolaboral2.data.local.dbEntities.OtrasEstructuras;

import com.example.mercadolaboral2.data.remote.ApiResponse;

import com.example.mercadolaboral2.data.remote.networkEntities.CodigoAccesoResponse;

import com.example.mercadolaboral2.data.remote.networkEntities.GetInconsistenciasResponse;

import com.example.mercadolaboral2.data.remote.networkEntities.GetRefreshEstado;

import com.example.mercadolaboral2.data.remote.networkEntities.GetSegmentosResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CensoApiService {
    // The Endpoint is defined in the @GET annotation.

//    @GET("Muestra/todos/{empadronadorID}")
//    LiveData<ApiResponse<List<GetSegmentosResponse>>> getSegmentosAsignados(@Path("empadronadorID") String empadronadorID);

    @GET("Muestras/empadronador/EPM002")
    LiveData<ApiResponse<List<GetSegmentosResponse>>> getSegmentosAsignados();

//    @GET("Muestra/todos/{empadronadorID}")
//    LiveData<ApiResponse<List<Muestra>>> getSegmentosAsignadosNuevos(
//            @Path("empadronadorID") String empadronadorID,
//            @Query("filtrados") String tipo);

    @GET("Muestra/todos/{empadronadorID}")
    LiveData<ApiResponse<List<GetSegmentosResponse>>> getSegmentosAsignadosNuevosFiltro(
            @Path("empadronadorID") String empadronadorID,//
            @Query("filtrados") String tipo);

/*
    @GET("Muestra/todos/{empadronadorID}")
    Call<List<GetSegmentosResponse>> getSegmentosAsignadosNuevos3(
            @Path("empadronadorID") String empadronadorID,
            @Query("filtrados") String tipo);
*/

    @GET("Muestra/estados/{empadronadorID}/{fechaUltimoSync}")
    Call<List<GetRefreshEstado>> actualizarEstados(
            @Path("empadronadorID") String usuario,
            @Path("fechaUltimoSync") String fechaUltimoSync);

    @GET("Cuestionarios/propios")
    LiveData<ApiResponse<List<Cuestionarios>>> getCuestionarios();

    @POST("Cuestionarios/")
    Call<Void> sendCuestionario(@Body Cuestionarios cuestionarioSelected);

    @PUT("Cuestionarios/{llave}")
    Call<Void> sendCuestionarioUpdate(@Body Cuestionarios cuestionarioSelected,
                                      @Path("llave") String llave);

    @DELETE("Cuestionarios/{llave}")
    Call<Void> eliminarCuestionarioServer(
            @Path("llave") String llave);

    @DELETE("OtrasEstructuras/{llave}")
    Call<Void> eliminarOtraEstructuraServer(
            @Path("llave") String llave);

    @GET("OtrasEstructuras/{empadronador}")
    LiveData<ApiResponse<List<OtrasEstructuras>>> cargarOtraEstructuraRecuperacion(
            @Path("empadronador") String empadronador);

    @POST("OtrasEstructuras/")
    Call<Void> sendOtraEstructura(@Body OtrasEstructuras otrasEstructuras);

    @PUT("OtraEstructura/{llave}")
    Call<Void> sendCuestionarioUpdate(@Body OtrasEstructuras cuestionarioSelected,
                                      @Path("llave") String llave);

    @GET("Inconsistencias/empadronador/{empadronadorID}")
    Call<List<GetInconsistenciasResponse>> getInconsistencias(
            @Path("empadronadorID") String empadronadorID);

    @PUT("Cuestionarios/ecenso/agregar/{llave}")
    LiveData<ApiResponse<CodigoAccesoResponse>> sendCodigoECenso(@Path("llave") String llave);
}
