package com.example.mercadolaboral2.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;



import com.example.mercadolaboral2.data.local.dbEntities.Cuestionarios;
import com.example.mercadolaboral2.data.local.dbEntities.CuestionariosPendientes;
import com.example.mercadolaboral2.data.local.dbEntities.Muestra;
import com.example.mercadolaboral2.data.local.dbEntities.OtrasEstructuras;

import java.util.List;

@Dao
public interface  SegmentosDao {
    //    If the return value is -1 or negative consider the operation is failed according to docs.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertSegmentosAdicionales(List<Muestra> segmentos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertSegmentos(List<Muestra> segmentos);

//    @Query("UPDATE Muestra " +
//            "set detalle = :detalle ," +
//            " lugarPobladoId= :lugarPobladoId, " +
//            " lugarPobladoDescripcion= :lugarPobladoDescripcion, " +
//            " barrioId= :barrioId, " +
//            " barrioDescripcion= :barrioDescripcion, " +
//            " asignacionAlterna = :idAsignacionAlterna " +
//            " WHERE id=:id")
//    void updateDetalleSegmento(String detalle, String id, String lugarPobladoId, String lugarPobladoDescripcion,
//                               String barrioId, String barrioDescripcion, int idAsignacionAlterna);

    @Delete
    void deleteSegmentos(List<Muestra> segmentos);

    @Query("DELETE FROM Muestra WHERE estado = 1 ")
    void deleteAllSegmentosEstado1();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void actualizarEstado(Muestra muestra);

    //----Cambio-----
    @Query("UPDATE Muestra set estado = 1 WHERE muestraId=:id")
    void updateEstadoSegmento(String id);

//    @Update
//    void updateSegmentos(Muestra segmentos);

    @Query("SELECT * FROM Muestra where estado > 0")
    LiveData<List<Muestra>> loadSegmentosFaltantesBackup();

    @Query("SELECT * FROM Muestra")
    LiveData<List<Muestra>> loadSegmentos();

    /* Query para semanas de trabajo */
    @Query("SELECT * FROM Muestra GROUP BY paR06_ID ORDER BY paR06_ID ASC")
    LiveData<List<Muestra>> getAllSubZonas();



    /* Todas las subzonas que tengan cuestionarios con estado >0 y flag envio =0 */
    @Query("SELECT * FROM Muestra S " +
            "INNER JOIN cuestionarios C " +
            "ON S.muestraId = C.codigoSegmento " +
            "WHERE C.flagPrimerEnvio  = 0 " +
            "AND C.flagEnvio = 0 AND C.estado_cuestionario > 0 " +
            " GROUP BY S.paR06_ID")
    LiveData<List<Muestra>> getAllSubZonasSend();

    @Query("SELECT * FROM Muestra where paR06_ID=:subZonaSelect " +
            "ORDER BY muestraId ASC ")
    LiveData<List<Muestra>> getSegmentosSelected(String subZonaSelect);

    @Query("SELECT COUNT(*) as totPendientes, codigoSegmento, subzona " +
            "FROM cuestionarios C " +
            "INNER JOIN Muestra S " +
            "ON C.codigoSegmento = S.muestraId " +
            "where S.paR06_ID= :subzona AND C.estado_cuestionario > 0 AND flagEnvio = 0 AND flagPrimerEnvio = 0 " +
            "GROUP BY codigoSegmento ")
    LiveData<List<CuestionariosPendientes>> getSegmentosCuestionariosNoEnviados(String subzona);

    @Query("SELECT COUNT(*) as totPendientes, codigoSegmento " +
            "FROM otrasEstructuras C " +
            "INNER JOIN Muestra S " +
            "ON C.codigoSegmento = S.muestraId " +
            "where S.paR06_ID= :subzona AND  flagEnvio = 0 AND flagPrimerEnvio = 0 " +
            "GROUP BY codigoSegmento ")
    LiveData<List<CuestionariosPendientes>> getOtrasEstructrurasSubZonaNoEnviados(String subzona);

    @Query("SELECT * " +
            "FROM cuestionarios C " +
            "INNER JOIN Muestra S " +
            "ON C.codigoSegmento = S.muestraId " +
            "where C.estado_cuestionario > 0 AND flagEnvio = 0 AND flagPrimerEnvio = 0 ")
    LiveData<List<Cuestionarios>> getSegmentosCuestionariosNoEnviados3();

    /**
     * Consulta en la BD si hay registros en la tabla de OtrasEstructuras con los campos de Flag Envio
     * y primer envio en 0, es decir, false.
     **/
    @Query("SELECT * " +
            "FROM otrasEstructuras  " +
            "where flagEnvio = 0 AND flagPrimerEnvio = 0 ")
    LiveData<List<OtrasEstructuras>> getOtrasEstructurasNoEnviados();

    @Query("SELECT * FROM Muestra S " +
            "INNER JOIN cuestionarios C " +
            "ON S.muestraId = C.codigoSegmento " +
            "WHERE C.flagEnvio  = 0 AND C.estado_cuestionario > 0 AND C.flagPrimerEnvio  = 0 " +
            "AND S.paR06_ID =:subzona " +
            "GROUP BY muestraId ")
    LiveData<List<Muestra>> getSegmentosCuestionariosNoEnviados2(String subzona);

    @Query("SELECT * FROM Muestra S " +
            "INNER JOIN otrasEstructuras C " +
            "ON S.muestraId = C.codigoSegmento " +
            "WHERE C.flagEnvio  = 0 AND C.flagPrimerEnvio  = 0 " +
            "AND S.paR06_ID =:subzona " +
            "GROUP BY muestraId ")
    LiveData<List<Muestra>> getSegmentosPendientesByZona(String subzona);

    @Query("SELECT * FROM Muestra where paR06_ID=:subZonaSelect")
    LiveData<List<Muestra>> getSegmentosSelectedGroup(String subZonaSelect);

//    @Query("SELECT * FROM Muestra GROUP BY subZonaId")
//    LiveData<List<Muestra>> getSegmentosMaps();

   /* @Query("SELECT * FROM segmentos")
    List<Muestra> getAllSegmentosEstados();*/
}
