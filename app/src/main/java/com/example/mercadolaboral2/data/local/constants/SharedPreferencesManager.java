package com.example.mercadolaboral2.data.local.constants;

import android.content.Context;
import android.content.SharedPreferences;


import com.example.mercadolaboral2.app.MyApp;

import java.util.Objects;

public class SharedPreferencesManager {

    private static final String APP_SETTINGS_FILE = "APP_SETTINGS";

    private SharedPreferencesManager() {

    }

    private static SharedPreferences getSharedPreferences() {
        return MyApp.getContext().getSharedPreferences(APP_SETTINGS_FILE, Context.MODE_PRIVATE);
    }

    public static void setSomeStringValue(String dataLabel, String dataValue) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(dataLabel, dataValue);
        editor.apply();
    }

    public static void setSomeIntValue(String dataLabel, int dataValue) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(dataLabel, dataValue);
        editor.apply();
    }

    public static void setSomeBooleanValue(String dataLabel, boolean dataValue) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(dataLabel, dataValue);
        editor.apply();
    }

    public static String getSomeStringValue(String dataLabel) {
        return getSharedPreferences().getString(dataLabel, null);
    }

    public static boolean getSomeBooleanValue(String dataLabel) {
        return getSharedPreferences().getBoolean(dataLabel, false);
    }

    public static int getSomeIntValue(String dataLabel) {
        return getSharedPreferences().getInt(dataLabel, 0);
    }

    public static String getPath() {
        return Objects.requireNonNull(MyApp.getContext().getFilesDir().getParentFile()).getPath()
                + "/shared_prefs/"
                + APP_SETTINGS_FILE
                + ".xml";
    }
}
