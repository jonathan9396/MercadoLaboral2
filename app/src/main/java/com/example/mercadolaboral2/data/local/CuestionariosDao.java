package com.example.mercadolaboral2.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RewriteQueriesToDropUnusedColumns;
import androidx.room.Update;


import com.example.mercadolaboral2.data.local.dbEntities.Cuestionarios;
import com.example.mercadolaboral2.data.local.dbEntities.LogErrors;
import com.example.mercadolaboral2.data.local.dbEntities.Muestra;
import com.example.mercadolaboral2.data.local.dbEntities.TotCuestionarios;

import java.util.List;

@Dao
public interface CuestionariosDao {

    @Query("SELECT * FROM cuestionarios where muestraId2=:muestraId GROUP BY entrevistaNum ")
    LiveData<List<Cuestionarios>> getCuestionariosBySegmentoVivienda(String muestraId);


    @Query("SELECT * FROM cuestionarios where muestraId2=:muestraId " +
            "ORDER BY entrevistaId")
    LiveData<List<Cuestionarios>> getCuestionariosBySegmento(String muestraId);

    @Query("SELECT * FROM cuestionarios " +
            "where muestraId2=:muestraId AND flagEnvio = 0 AND flagPrimerEnvio= 0 AND estado_cuestionario > 0")
//true to 1 and false to 0.
    LiveData<List<Cuestionarios>> getCuestionariosBySegmentoNotSended(String  muestraId);


//    @Query("SELECT * FROM cuestionarios where vivienda=:viviendaID")
//    LiveData<List<Cuestionarios>> getCuestionariosByVivienda(String viviendaID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addVivienda(Cuestionarios nuevaVivienda);


    @Query("SELECT * FROM cuestionarios where estado_cuestionario > 1")
    LiveData<List<Cuestionarios>> getAllCuestionarios();

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT COUNT(*) AS totCuestionarios,  muestraId2 " +
            " FROM cuestionarios " +
            " GROUP BY muestraId2")
    LiveData<List<TotCuestionarios>> getAllCuestionariosCapture();

    @Query("SELECT * FROM cuestionarios where muestraId2 =:subzonaSelect " +
            "ORDER BY entrevistaId")
    LiveData<List<Cuestionarios>> getAllCuestionariosByZona(String subzonaSelect);

    @Query("SELECT * FROM cuestionarios where entrevistaId =:llave")
    LiveData<Cuestionarios> getCuestionarioECenso(String llave);

    @Query("UPDATE cuestionarios set erroresEstructura = erroresEstructura || :errorHogar WHERE entrevistaId =:llave ")
    void updateErrorHogar(String errorHogar, String llave);

    @Query("UPDATE cuestionarios set erroresEstructura = `replace`(erroresEstructura,:errorHogar,'') WHERE entrevistaId =:llave")
    void correctErrorHogar(String errorHogar, String llave);

//    @Query("SELECT COUNT(codigoSegmento)AS tot, llave, codigoSegmento,numEmpadronador,  estado, " +
//            "flagEnvio, flagPrimerEnvio FROM cuestionarios GROUP BY codigoSegmento")
//    LiveData<List<Cuestionarios>> getCuestionariosBySegmentoID();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void addCuestionarioDatosDat(Cuestionarios cuestionarioSelected);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCuestionario(Cuestionarios cuestionarioSelected);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addErrorLogs(LogErrors logErrors);

    @Query("DELETE FROM CUESTIONARIOS")
    void deleteCuestionario();

    @Query("DELETE FROM Muestra")
    void deleteSegmentos();

    @Query("DELETE FROM usuarios")
    void deleteUsuarios();

    @Delete
    void eliminarCuestionarioSeleccionado(Cuestionarios cuestionarioSelected);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void actualizarCodigoCenso(Cuestionarios cuestionarioSelected);


    @Query("SELECT * FROM logerrors")
    LiveData<List<LogErrors>> getLogErrors();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertCuestionarios(List<Cuestionarios> cuestionarios);

//    @Query("UPDATE segmentos " +
//            "SET estado = :estado " +
//            "WHERE id = :id ")
//    void updateSegmentosEstado(String estado, String id);

/*    @Query("UPDATE segmentos " +
            "SET estado = estado " +
            "IN (:segmentos)  WHERE id IN (:segmentos) ")
    void updateSegmentosEstado(List<Muestra> segmentos,int estado, String id);*/

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSegmentosEstado2(List<Muestra> segmentos);
}
