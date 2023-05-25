package com.example.mercadolaboral2.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.example.mercadolaboral2.data.local.dbEntities.Usuarios;

@Dao
public interface UsuariosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(Usuarios usuarios);

    @Update
    void updateUsers(Usuarios usuarios);
}