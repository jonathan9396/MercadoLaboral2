package com.example.mercadolaboral2.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by EBECKFORD on 07/18/2018.
 */

@SuppressLint("Registered")
public class SdWorker extends AppCompatActivity {

    private static DocumentFile pickedDir;
    private static Activity Actividad;
    private static Fragment Fragmento;

    public SdWorker(Activity actividad) {
        Actividad = actividad;
        Fragmento = null;
    }

/*    public SdWorker(Activity actividad, Fragment fragmento) {
        Actividad = actividad;
        Fragmento = fragmento;
    }*/

    public static boolean exists(String path) {
        if (dirIsPicked()) {
            return false;
        }
        File f = new File(path);
        List<String> directorios = pathSplitter(path);
        if (directorios == null) {
            return false;
        }
        DocumentFile padre = pickedDir;
        for (int i = 0; i < directorios.size(); i++) {
            if (Objects.requireNonNull(padre).findFile(directorios.get(i)) == null) {
                return false;
            } else {
                padre = padre.findFile(directorios.get(i));
            }
        }
//        String a = Objects.requireNonNull(padre).getName();
//        String b = f.getName();
//        boolean v = f.getName().equals(padre.getName());
        return Objects.requireNonNull(Objects.requireNonNull(padre).getName()).equals(f.getName());
    }

//    public void clearPickedDir() {
//        synchronized (this) {
//            //if (pDir != null) {
//            pickedDir = null;
//            //}
////            checkPickedDir();
//            this.notify();
//        }
//    }

    public static DocumentFile mkdirs(String path) {
        if (!dirIsPicked()) {
            return null;
        }
        List<String> directorios = pathSplitter(path);
        if (directorios == null) {
            return null;
        }
        DocumentFile padre = pickedDir;
        DocumentFile next;
        for (int i = 0; i < directorios.size(); i++) {
            if (Objects.requireNonNull(padre).findFile(directorios.get(i)) == null) {
                padre.createDirectory(directorios.get(i));
                next = padre.findFile(directorios.get(i));
                padre = next;
            } else {
                padre = padre.findFile(directorios.get(i));
            }
        }
        return padre;
    }

    public static boolean dirIsPicked() {
        return pickedDir != null;
    }


    //**************************utilities****************
  /*  public static boolean delete(String path) {
        if (dirIsPicked()) {
            return false;
        }
        List<String> directorios = pathSplitter(path);
        if (directorios == null) {
            return false;
        }
        DocumentFile padre = pickedDir;
        for (int i = 0; i < directorios.size(); i++) {
            if (Objects.requireNonNull(padre).findFile(directorios.get(i)) == null) {
                Log.e("SDWorker::", "no se encontro el directorio a eliminar");
                return false;
            } else {
                padre = padre.findFile(directorios.get(i));
            }
        }
        Objects.requireNonNull(padre).delete();
        return true;
    }*//**/

    public static DocumentFile copyALL(String source, String destination, DocumentFile ultimoDir)
            throws IOException {
        File src = new File(source);
        File dst = new File(destination);

        for (String f : Objects.requireNonNull(src.list())) {
            File file = new File(src + "/" + f);
            if (file.isDirectory()) {
                ultimoDir = mkdirs(dst + "/" + f);
                //new File(dst.toString()+"/"+f).mkdirs();
                Log.d("FILES dir:", f);
                ultimoDir = copyALL(src + "/" + f, dst + "/" + f, ultimoDir);
            } else if (file.isFile()) {
                Log.e("FILES copDIR:", Objects.requireNonNull(Objects.requireNonNull(ultimoDir.getParentFile()).getName()));
                copy(src + "/" + f, dst + "/" + f, ultimoDir);
                Log.e("FILES cop:", f);
            }
        }
        return ultimoDir.getParentFile();
    }

    public static boolean copyALL(String source, String destination) throws IOException {
        File src = new File(source);
        File dst = new File(destination);
        DocumentFile ultimoDir = pickedDir;
        if (src.list() != null) {
            for (String f : Objects.requireNonNull(src.list())) {
                File file = new File(src + "/" + f);
                if (file.isDirectory()) {
                    ultimoDir = mkdirs(dst + "/" + f);
                    //new File(dst.toString()+"/"+f).mkdirs();
                    Log.d("FILES dir:", f);
                    ultimoDir = copyALL(src + "/" + f, dst + "/" + f, ultimoDir);
                } else if (file.isFile()) {
                    copy(src + "/" + f, dst + "/" + f, ultimoDir);
                    Log.e("FILES cop:", f);
                }
            }
        } else
            return false;
        return true;
    }

    public static DocumentFile getDocPath(String path) {
        if (dirIsPicked()) {
            return null;
        }
        List<String> directorios = pathSplitter(path);
        if (directorios == null) {
            return null;
        }
        DocumentFile padre = pickedDir;
        for (int i = 0; i < directorios.size(); i++) {
            if (Objects.requireNonNull(padre).findFile(directorios.get(i)) == null) {
                return null;
            } else {
                padre = padre.findFile(directorios.get(i));
            }
        }
        return padre;
    }

    public static void copy(String source, String destination, DocumentFile _ultimoDir) throws IOException {
        File src = new File(source);
        File dst = new File(destination);
        String MIME = getMimeType(dst, Actividad);
        //String MIME = extension.equals(".zip") ? "multipart/x-zip" : "Application/realm*";
        //MIME = extension.equals(".realm") ? "application/realm" : MIME;

        DocumentFile newFile = _ultimoDir.createFile(MIME, dst.getName());
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = Actividad.getContentResolver().openOutputStream(
                    Objects.requireNonNull(newFile).getUri())) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    Objects.requireNonNull(out).write(buf, 0, len);
                }
            }
        }
    }

    public static String getMimeType(File file, Context context) {
        Uri uri = Uri.fromFile(file);
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private static List<String> pathSplitter(String path) {
        List<String> resultado = new ArrayList<>();
        int slashPosition;
        String corte;

        // verificaciones
        if (path.endsWith("/")) {
            Log.e("mkdirs:::", "no puede terminar la dirección con '/'");
            return null;
        }
//        if (slashPosition == 0) {
//            Log.e("mkdirs:::", "Inicie su dirección sin '/'");
//            return null;
//        }

        //ciclo de trabajo
        do {

            slashPosition = path.indexOf("/");
            if (slashPosition == 0) {
                path = path.substring(1);
            } else if (slashPosition == -1) {
                resultado.add(path);
                break;
            } else {
                corte = path.substring(0, slashPosition);
                resultado.add(corte);
                path = path.substring(slashPosition + 1);
            }
        } while (!path.equals(""));
        return resultado;
    }

    public static List<String> SDcards(DocumentFile pickedDir) {
//        String listOfFiles = "";
        List<String> listaDeDrives = new ArrayList<>();
//        String treeUriPathLast;
        String treeUriPath = "";
        try {
            if (pickedDir != null) {
                treeUriPath = "/storage/"
                        + pickedDir.getUri().getLastPathSegment().replace(":", "/");
            }

//            Process process = Runtime.getRuntime().exec("ls /storage" + treeUriPathLast +"/InecMovil");
//            Process process = Runtime.getRuntime().exec("ls " + subCadenaSDc);
//            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
            listaDeDrives.add(treeUriPath);
//                Toast.makeText(Actividad, line, Toast.LENGTH_SHORT).show();
//            }
        } catch (Exception e) {
            Log.e("listOfFiles", "ERROR" + e.getMessage());
        }
        String[] resp = new String[listaDeDrives.size()];
        listaDeDrives.toArray(resp);
        return listaDeDrives;
    }

/*    public static void copy(String source, String destination) throws IOException {
        File src = new File(source);
        File dst = new File(destination);
        DocumentFile _ultimoDir = getDocPath(dst.getParent());
        String extension;
        extension = dst.getName().substring(dst.getName().indexOf("."));
        Log.i("Extension::::::", extension);
        String MIME = getMimeType(dst, Actividad);
        //String MIME = extension.equals(".zip") ? "multipart/x-zip" : "Application/realm*";
        //MIME = extension.equals(".realm") ? "application/realm" : MIME;

        DocumentFile newFile = Objects.requireNonNull(_ultimoDir).createFile(MIME, dst.getName());
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = Actividad.getContentResolver().openOutputStream(Objects.requireNonNull(newFile).getUri())) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    Objects.requireNonNull(out).write(buf, 0, len);
                }
            }
        }
    }*/

    public void setPickedDir(DocumentFile pDir) {
        //if (pDir != null) {
        pickedDir = pDir;
        //}
        checkPickedDir();
    }

    public DocumentFile getPickerDir() {
        checkPickedDir();
        return pickedDir;
    }

    public void openDialogoSelector(ActivityResultLauncher<Intent> activityResultLauncher) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        if (Fragmento != null) {
        activityResultLauncher.launch(intent);
//            Fragmento.startActivityForResult(intent, 42);
//        } else {
//        Actividad.startActivityForResult(intent, 42);
//        }
    }

    public void copyOut(String source, String destination) throws IOException {
        DocumentFile src = getDocPath(source);
        File dst = new File(destination);
        DocumentFile _ultimoDir = DocumentFile.fromFile(Objects.requireNonNull(dst.getParentFile()));
        String extension;
        extension = dst.getName().substring(dst.getName().indexOf("."));
        Log.i("Extension::::::", extension);
        String MIME = getMimeType(dst, Actividad);
        DocumentFile newFile = _ultimoDir.createFile(MIME, dst.getName());
        try (InputStream in = Actividad.getContentResolver()
                .openInputStream(Objects.requireNonNull(src).getUri())) {
            try (OutputStream out = Actividad.getContentResolver()
                    .openOutputStream(Objects.requireNonNull(newFile).getUri())) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = Objects.requireNonNull(in).read(buf)) > 0) {
                    Objects.requireNonNull(out).write(buf, 0, len);

                }
            }
        }
    }

    //**********************PRIVADAS********************
    private void checkPickedDir() {
        List<String> Drives = SDcards(pickedDir);
        try {
            if (pickedDir != null) {
                String pickedDirUri = pickedDir.getUri().getLastPathSegment();
                String pickedDirEdit = pickedDirUri.substring(pickedDirUri.lastIndexOf("/"));
                if (!(Drives.get(0).substring(pickedDirUri.lastIndexOf("/")).contains(pickedDirEdit))) {
                    pickedDir = null;
                    Toast.makeText(Actividad, "debe elegir una SD o disco extraible",
                            Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(Actividad, "debe elegir una SD o disco extraible", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("SDWriter:", Objects.requireNonNull(e.getMessage()));
        }
    }
}

