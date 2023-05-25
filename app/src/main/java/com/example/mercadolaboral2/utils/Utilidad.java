package com.example.mercadolaboral2.utils;


import static com.example.mercadolaboral2.data.local.constants.AppConstants.fileAccessHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import com.airbnb.lottie.LottieAnimationView;

import com.example.mercadolaboral2.R;

import com.example.mercadolaboral2.app.MyApp;

import com.example.mercadolaboral2.data.local.constants.AppConstants;

import com.example.mercadolaboral2.data.local.constants.SharedPreferencesManager;

import com.example.mercadolaboral2.data.local.dbEntities.Cuestionarios;

import com.example.mercadolaboral2.data.local.dbEntities.Muestra;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Utilidad {
    public static final String UTF8_BOM = "\uFEFF";
    private static final String TAG = "Utilidad";
    private static final File downloadDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private static final String inecMovilDirectory = downloadDirectory + "/InecMovil";
    private final ProcessNotifier processNotifier;

    public Utilidad(ProcessNotifier processNotifier) {
        this.processNotifier = processNotifier;
    }

/*    public static String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }*/

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
                    Log.d("FILES dir:", dir);
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

    public static boolean zipFileAtPath(String sourcePath, String toLocation) {
        final int BUFFER = 2048;

        File sourceFile = new File(sourcePath);
        try {
            BufferedInputStream origin;
            FileOutputStream dest = new FileOutputStream(toLocation);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            if (sourceFile.isDirectory()) {
                zipSubFolder(out, sourceFile, Objects.requireNonNull(sourceFile.getParent()).length());
            } else {
                byte[] data = new byte[BUFFER];
                FileInputStream fi = new FileInputStream(sourcePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void deleteCsProFiles(Cuestionarios cuestionarioSelected) {
        fileAccessHelper.delete("/.",
                "C" + cuestionarioSelected.getEntrevistaId() + ".dat",
                true,
                true,
                strings -> null,
                s -> null);

        fileAccessHelper.delete("/.",
                "C" + cuestionarioSelected.getEntrevistaId() + ".dat.csidx",
                true,
                true,
                strings -> null, s -> null);

        fileAccessHelper.delete("/.",
                "C" + cuestionarioSelected.getEntrevistaId() + ".dat.lst",
                true,
                true,
                strings -> null, s -> null);

        fileAccessHelper.delete("/.",
                "C" + cuestionarioSelected.getEntrevistaId() + ".dat.sts",
                true,
                true,
                strings -> null,
                s -> null);
    }

    public static void respaldarCuestionarioConDatos(Muestra muestra, Cuestionarios cuestionarioSelected) {
        String dataDirectory = inecMovilDirectory + "/DATA";
        String datPath = dataDirectory + "/ENCUESTADOR/" +
                //EMPADRONADOR
                SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME) + "/" +
                //PROVINCIA + DISTRITO + CORREGIMIENTO / SEGMENTO
                muestra.getPaR02_ID() + "/"
                + muestra.getPaR02_DESC()
                + muestra.getPaR03_ID()
                + muestra.getPaR06_ID()
                + muestra.getLlave() + "/" +
                //VIVIENDA
                cuestionarioSelected.getJefe() + "/";

        if (!new File(datPath).exists()) {
            Toast.makeText(MyApp.getContext(), "Carpeta de cuestionario no encontrada.", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Calendar calendar = Calendar.getInstance();

                String dia = "00";
                String mes = "00";
                String anno = "0000";
                String hora = "00";
                String minutos = "00";
                String carpeta;
//                String path1;
                String path2;

                dia += calendar.get(Calendar.DAY_OF_MONTH);
                mes += (calendar.get(Calendar.MONTH) + 1);
                anno += calendar.get(Calendar.YEAR);
                hora += calendar.get(Calendar.HOUR_OF_DAY);
                minutos += calendar.get(Calendar.MINUTE);

                carpeta = dia.substring(dia.length() - 2)
                        + mes.substring(mes.length() - 2)
                        + anno.substring(anno.length() - 4) + "_"
                        + hora.substring(hora.length() - 2)
                        + minutos.substring(minutos.length() - 2);

                path2 = dataDirectory
                        + "/ADMIN/"
                        + carpeta + "/";
                if (!(new File(path2).exists())) {
                    if (new File(path2).mkdirs()) {
                        Log.i("Carpeta Eliminar", "Se ha creado el directorio para los archivos eliminados");
                    } else
                        Log.e("Carpeta Eliminar", "No se ha creado el directorio para los archivos eliminados");
                }
                String cuestionarioID = cuestionarioSelected.getEntrevistaNum()
                        + cuestionarioSelected.getHogar()
                        + cuestionarioSelected.getRecorrido()
                        + cuestionarioSelected.getLugarPoblado()
                        + cuestionarioSelected.getCalle()
                        + muestra.getFechaCreacion()
                        + cuestionarioSelected.getJefe()
                        + cuestionarioSelected.getHogar();
                String idDAT = "C" + cuestionarioID + ".dat";
//            String cuestionarioDATA = dataDirectory + datPath + "C" + idDAT;

                List<String> listaFiles;
                listaFiles = Utilidad.SearchFilesSameName(datPath, idDAT);

                for (int x = 0; x < listaFiles.size(); x++) {
                    Utilidad.copy(datPath + "/" + listaFiles.get(x), path2 + listaFiles.get(x));
                    if (new File(datPath + "/" + listaFiles.get(x)).delete())
                        Log.i("Eliminado", "Delete");
                }
                deleteCsProFiles(cuestionarioSelected);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void zipSubFolder(ZipOutputStream out, File folder, int basePathLength)
            throws IOException {

        final int BUFFER = 2048;
        String comp;

        File[] fileList = folder.listFiles();
        BufferedInputStream origin;
        for (File file : Objects.requireNonNull(fileList)) {
            String substring = file.toString().substring(file.toString().length() - 3);
            comp = substring;
            Log.d("SUBSTRING", substring);
            if (file.isDirectory()) {
                zipSubFolder(out, file, basePathLength);
            } else if (comp.equals("zip")) {
                Log.d("Ziping", "no es directorio ni archivo");
            } else {
                byte[] data = new byte[BUFFER];
                String unmodifiedFilePath = file.getPath();
                String relativePath = unmodifiedFilePath
                        .substring(basePathLength);
                FileInputStream fi = new FileInputStream(unmodifiedFilePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(relativePath);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                    Log.e("LOOP", Arrays.toString(data));
                }
                origin.close();
            }
        }
    }

    public static List<String> SearchFilesSameName(String path, String entrevistaId) {
        File folder = new File(path);
        List<String> rutas = new ArrayList<>();
// gets you the list of files at this folder
        File[] listOfFiles = folder.listFiles();
// loop through each of the files looking for filenames that match
        for (File listOfFile : Objects.requireNonNull(listOfFiles)) {
            String filename = listOfFile.getName();
            if (filename.startsWith(entrevistaId)) {
                rutas.add(listOfFile.getName());
            }
        }
        return rutas;
    }

    public static String getLastPathComponent(String filePath) {
        String[] segments = filePath.split("/");
        if (segments.length == 0)
            return "";
        return segments[segments.length - 1];
    }

    public static boolean verificarRed(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) return false;
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        return networkCapabilities != null
                && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

    public static void showMessageDialog(String titulo, String contenido, boolean mostrarContenido,
                                         Activity activity, int resource) {
        try {
            MaterialAlertDialogBuilder materialAlertDialogBuilder =
                    new MaterialAlertDialogBuilder(activity);
            materialAlertDialogBuilder.setTitle(titulo);
            if (mostrarContenido)
                materialAlertDialogBuilder.setMessage(contenido);
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.alertdialog_descargarbackup, null);
            LottieAnimationView lottie = view.findViewById(R.id.animation_view);
            lottie.setAnimation(resource);
            materialAlertDialogBuilder.setView(view);
            materialAlertDialogBuilder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            materialAlertDialogBuilder.show();
        } catch (Exception e) {
            Toast.makeText(activity, "Error Dialog. " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i(TAG, "showMessageDialog: ");
        }
    }

    //############## 9 CON SD}
    //##############Realizar respaldos
    public void respaldoInformacion(String carpeta, Activity activity) {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String inecMovilDirectory = directory + "/InecMovil";
        String DbDirectory = activity.getDatabasePath("censo_db").getParent();

        String respaldoPath;
//            Dim iArch As Integer = 0
        processNotifier.setTitle("Respaldo");
        processNotifier.setText("Respaldando archivos . . .");
        processNotifier.inflate();
        processNotifier.show();

        //Instancia de la ruta de RESPALDO
        respaldoPath = inecMovilDirectory + "/RESPALDO/"
                + SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_LOG_USER)
                + "/" + carpeta;

        //Si no existe la carpeta de RESPALDO, entonces  se crea
        if (!(new File(respaldoPath).exists())) {
            if (new File(respaldoPath).mkdirs())
                Log.d(TAG, "Creado Satisfactoriamente FuncionesInec");
            else Log.e(TAG, "No seli creo el directorio FuncionesIne");
        }
        AppExecutors appExecutors;
        appExecutors = AppExecutors.getInstance();
        appExecutors.diskIO().execute(() -> {
            try {
                if (copyALL(inecMovilDirectory, respaldoPath)) {
                    activity.runOnUiThread(() ->
                            Toast.makeText(activity, "Respaldo de carpeta InecMovil completado.",
                                    Toast.LENGTH_SHORT).show());
                }
                if (copyALL(DbDirectory, respaldoPath)) {
                    activity.runOnUiThread(()
                            -> Toast.makeText(activity, "Respaldo de Base de Datos completado.",
                            Toast.LENGTH_SHORT).show());
                }

                if (!zipFileAtPath(respaldoPath, respaldoPath + "/" + carpeta + ".zip")) {
                    Log.d(TAG, "Error al crear .ZIP");
                }
                processNotifier.dismiss();
            } catch (Exception e) {
                Log.e(TAG, "Error: " + e);
            }
        });
    }

    //############## 9 CON SD}
    //##############Realizar respaldos
    public void respaldoInformacionOTG(SdWorker sdWorker, Activity activity,
                                       ActivityResultLauncher<Intent> activityResultLauncher) {
        activity.runOnUiThread(() -> sdWorker.openDialogoSelector(activityResultLauncher));
    }
}
