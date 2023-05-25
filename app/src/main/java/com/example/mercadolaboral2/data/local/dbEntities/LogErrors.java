package com.example.mercadolaboral2.data.local.dbEntities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "logerrors")
public class LogErrors {
    @SerializedName("llave")
    @PrimaryKey
    @ColumnInfo(name = "llave")
    @Expose
    @NonNull
    private String llave;

    @SerializedName("error")
    @ColumnInfo(name = "error")
    @Expose
    private String error;

    @SerializedName("fechaError")
    @ColumnInfo(name = "fechaError")
    @Expose
    private String fechaError;


    public LogErrors(@NotNull String llave, String error, String fechaError) {
        this.llave = llave;
        this.error = error;
        this.fechaError = fechaError;
    }

    @NotNull
    public String getLlave() {
        return llave;
    }

    public void setLlave(@NotNull String llave) {
        this.llave = llave;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getFechaError() {
        return fechaError;
    }

    public void setFechaError(String fechaError) {
        this.fechaError = fechaError;
    }
}
