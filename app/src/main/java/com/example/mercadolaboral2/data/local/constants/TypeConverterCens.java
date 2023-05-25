package com.example.mercadolaboral2.data.local.constants;

import androidx.room.TypeConverter;

import java.util.Date;

public class TypeConverterCens {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}