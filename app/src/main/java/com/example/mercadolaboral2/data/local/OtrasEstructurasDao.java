package com.example.mercadolaboral2.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mercadolaboral2.data.local.dbEntities.OtrasEstructuras;

import java.util.List;

@Dao
public interface OtrasEstructurasDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOtraEstructura(OtrasEstructuras otrasEstructuras);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOtraEstructuraList(List<OtrasEstructuras> otrasEstructuras);

    @Query("UPDATE otrasEstructuras " +
            "set observacion = :observacion, fechaModificacion =:fechaUpdate " +
            " WHERE llave =:llave ")
    void saveObservacion(String observacion, String llave, String fechaUpdate);

    @Query("UPDATE otrasEstructuras " +
            "set localizacion = :gps, fechaModificacion =:fechaUpdate " +
            "WHERE llave =:llave ")
    void saveGps(String gps, String llave, String fechaUpdate);

    @Query("SELECT * FROM otrasEstructuras " +
            "where codigoSegmento=:id AND flagEnvio = 0 AND flagPrimerEnvio= 0 ")
//true to 1 and false to 0.
    LiveData<List<OtrasEstructuras>> getOtrasEstructurasBySegmentoNotSended(String id);

    @Query("SELECT * FROM otrasEstructuras")
    LiveData<List<OtrasEstructuras>> cargarOtrasEstructuras();

    @Query("SELECT * FROM otrasEstructuras where codigoSegmento=:id")
    LiveData<List<OtrasEstructuras>> getOtrasEstructurasBySegmento(String id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateOtrasEstructuras(OtrasEstructuras otrasEstructuras);

    @Delete
    void eliminarOtraEstructura(OtrasEstructuras otrasEstructuras);
}
