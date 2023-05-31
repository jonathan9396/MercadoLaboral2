package com.example.mercadolaboral2.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;



import com.example.mercadolaboral2.data.local.dbEntities.EntrevistaBase;

import java.util.List;
@Dao
public interface EntrevistaBaseDao {
    @Query("SELECT * FROM EntrevistaB where muestraId=:muestraId GROUP BY entrevistaNum ")
    LiveData<List<EntrevistaBase>> getCuestionariosBySegmentoVivienda(String muestraId);


    @Query("SELECT * FROM EntrevistaB where muestraId=:muestraId " +
            "ORDER BY entrevistaId")
    LiveData<List<EntrevistaBase>> getCuestionariosBySegmento(String muestraId);

    @Query("SELECT * FROM EntrevistaB where estado_cuestionario > 1")
    LiveData<List<EntrevistaBase>> getAllCuestionarios();

    @Query("SELECT * FROM EntrevistaB where muestraId =:subzonaSelect " +
            "ORDER BY entrevistaId")
    LiveData<List<EntrevistaBase>> getAllCuestionariosByZona(String subzonaSelect);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
       void addVivienda(EntrevistaBase nuevaVivienda);

}
