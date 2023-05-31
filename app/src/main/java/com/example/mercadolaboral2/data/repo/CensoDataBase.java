package com.example.mercadolaboral2.data.repo;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mercadolaboral2.data.local.CuestionariosDao;
import com.example.mercadolaboral2.data.local.EntrevistaBaseDao;
import com.example.mercadolaboral2.data.local.OtrasEstructurasDao;
import com.example.mercadolaboral2.data.local.SegmentosDao;
import com.example.mercadolaboral2.data.local.constants.TypeConverterCens;
import com.example.mercadolaboral2.data.local.dbEntities.Cuestionarios;
import com.example.mercadolaboral2.data.local.dbEntities.EntrevistaBase;
import com.example.mercadolaboral2.data.local.dbEntities.LogErrors;
import com.example.mercadolaboral2.data.local.dbEntities.Muestra;
import com.example.mercadolaboral2.data.local.dbEntities.OtrasEstructuras;
import com.example.mercadolaboral2.data.local.dbEntities.Usuarios;

@Database(entities = {
        Usuarios.class,
        Muestra.class,
        LogErrors.class,
        Cuestionarios.class,
        OtrasEstructuras.class,
        EntrevistaBase.class},
        version = 1
        )
@TypeConverters({TypeConverterCens.class})
public abstract class CensoDataBase extends RoomDatabase {
    private static volatile CensoDataBase INSTANCE;

    public static CensoDataBase getDataBase(final Context context) {
        if (INSTANCE == null) {

            synchronized (CensoDataBase.class) {
                if (INSTANCE == null)
                    INSTANCE = Room.databaseBuilder(
                                    context,
                                    CensoDataBase.class,
                                    "censo_db")
                            .build();
            }
        }
        return INSTANCE;
    }

    public abstract SegmentosDao getSegmentosDAO();

    public abstract CuestionariosDao getCuestionariosDAO();

    public abstract OtrasEstructurasDao getOtrasEstructurasDAO();
    public abstract EntrevistaBaseDao getEntrevistaBaseDao();


}
