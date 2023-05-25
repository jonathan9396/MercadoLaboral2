package com.example.mercadolaboral2.data.remote.workers;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


import com.example.mercadolaboral2.data.local.constants.AppConstants;

import com.example.mercadolaboral2.data.local.constants.SharedPreferencesManager;

import com.example.mercadolaboral2.data.local.constants.clients.FTPMyClient;

import com.example.mercadolaboral2.data.remote.networkServices.FTPApiService;

import com.example.mercadolaboral2.utils.Utilidad;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackupWorker extends Worker {
    private static final String TAG = "BackupWorker";
    private final Context context;

    public BackupWorker(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    public static boolean copyALL(String source, String destination) {
        File fileSource = new File(source);
        File fileDestination = new File(destination);
        try {
            for (String dir : Objects.requireNonNull(fileSource.list())) {
                if (dir.equals("RESPALDO"))
                    continue;
                if (dir.equals("MAPAS"))
                    continue;
                File file = new File(fileSource + "/" + dir);
                BasicFileAttributes basicFileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                if (basicFileAttributes.isDirectory()) {
                    if (new File(fileDestination + "/" + dir).mkdirs()) {
                        Log.i(TAG, "Directorio Creado");
                    } else Log.e(TAG, "Directorio NO Creado");
                    Log.d(TAG, dir);
                    copyALL(fileSource + "/" + dir,
                            fileDestination + "/" + dir);
                } else if (basicFileAttributes.isRegularFile()) {
                    copy(fileSource + "/" + dir,
                            fileDestination + "/" + dir);
                    Log.e("FILES cop:", dir);
                }
            }
            return true;
        } catch (Exception e) {
            Log.e("Error CopyAll", "Error en copiar todo // " + e.getCause());
            return false;
        }
    }

    //Funcion para compiar un archivo de una posicion a otra, funciona aun que el destino tenga otro nombre
    public static void copy(String source, String destination) throws IOException {
        File src = new File(source);
        File dst = new File(destination);
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                    Log.i("buffer copy", String.valueOf(len));
                }
            }
        }
    }

    private void respaldoWorker() {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String inecMovilDirectory = directory + "/InecMovil";
        String DbDirectory = context.getDatabasePath("censo_db").getParent();

        String respaldoPath;
        String respaldoName = getPathRespaldo();

        //Instancia de la ruta de RESPALDO
        respaldoPath = inecMovilDirectory + "/RESPALDO/"
                + SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_LOG_USER)
                + "/" + respaldoName;

        //Si no existe la carpeta de RESPALDO, entonces  se crea
        if (!(new File(respaldoPath).exists())) {
            if (new File(respaldoPath).mkdirs())
                Log.d("BackupWorker", "Creado Satisfactoriamente FuncionesInec");
            else Log.e("BackupWorker", "No seli creo el directorio FuncionesIne");
        }

        try {
            if (copyALL(inecMovilDirectory, respaldoPath))
                Log.d("BackupWorker", "Creado Satisfactoriamente FuncionesInec");
            if (copyALL(DbDirectory, respaldoPath))
                Log.d("BackupWorker", "Creado Satisfactoriamente FuncionesInec");


            String zipPath = respaldoPath + "/" + respaldoName + ".zip";

            if (!Utilidad.zipFileAtPath(respaldoPath, zipPath)) {
                Log.d(TAG, "Error al crear .ZIP");
            }
            enviarFTP(zipPath);

        } catch (Exception e) {
            Log.e(TAG, "Error: " + e);
        }
        Log.e(TAG, "respaldoWorker: workerAndroid");
    }

    public void enviarFTP(String zipPath) {
        FTPMyClient ftpMyClient = FTPMyClient.getInstance();
        FTPApiService ftpApiService = ftpMyClient.getFtpApiService();
        //                File respaldoFile = new File(respaldoPath + "/" + respaldoName + ".zip");
        File respaldoFile = new File(zipPath);

        RequestBody requestBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        respaldoFile);

        MultipartBody.Part body = MultipartBody.Part.createFormData(
                "prueba", respaldoFile.getName(), requestBody);

        Call<Void> call = ftpApiService.sendZipFile(
                body, SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful())
                    Log.i(TAG, "onResponse: ");
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.i(TAG, "onFailure: ");
            }
        });
    }

    private String getPathRespaldo() {
        Calendar calendar = Calendar.getInstance();

        String dia = "00";
        String mes = "00";
        String anno = "0000";
        String hora = "00";
        String minutos = "00";

        dia += calendar.get(Calendar.DAY_OF_MONTH);
        mes += (calendar.get(Calendar.MONTH) + 1);
        anno += calendar.get(Calendar.YEAR);
        hora += calendar.get(Calendar.HOUR_OF_DAY);
        minutos += calendar.get(Calendar.MINUTE);

        return dia.substring(dia.length() - 2) +
                mes.substring(mes.length() - 2) +
                anno.substring(anno.length() - 4) + "_" +
                hora.substring(hora.length() - 2) +
                minutos.substring(minutos.length() - 2);
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {
        respaldoWorker();
        return Result.success();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.i(TAG, "onStopped: se ha cancelado el worker. ");
    }
}
