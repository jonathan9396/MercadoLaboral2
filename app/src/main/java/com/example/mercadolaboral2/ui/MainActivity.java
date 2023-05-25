package com.example.mercadolaboral2.ui;


import static com.example.mercadolaboral2.data.local.constants.AppConstants.fileAccessHelper;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.mercadolaboral2.R;
import com.example.mercadolaboral2.data.local.constants.AppConstants;
import com.example.mercadolaboral2.data.local.dbEntities.LogErrors;
import com.example.mercadolaboral2.utils.CsproJson;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import gov.census.cspro.csentry.fileaccess.FileAccessHelper;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    BottomNavigationView bottomNavigationView;
    private List<LogErrors> logErrorsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileAccessHelper = new FileAccessHelper(this);
        setContentView(R.layout.activity_main);
//        this.setTitle("orueba");
        ConfigurationLoginDialogViewModel configurationLoginDialogViewModel =
                new ViewModelProvider(this).get(ConfigurationLoginDialogViewModel.class);

        initDiccionario();
        createUtilsAssets();
//        List<Integer> listTxtraw = new ArrayList<>();
//        listTxtraw.add(R.raw.divpol);
//        listTxtraw.add(R.raw.paises);
//
//        List<String> listTxt = new ArrayList<>();
//        listTxt.add("divpol.dat");
//        listTxt.add("paises.dat");
//
//        for (int i = 0; i < listTxt.size(); i++) {
//            copyRAWFilesToPhone(listTxt.get(i), listTxtraw.get(i));
//        }

        bottomNavigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_surveys, R.id.navigation_sendData/*,
                R.id.navigation_network, R.id.navigation_backup*/)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        configurationLoginDialogViewModel.getLogErrors().observe(this, logErrors -> {
            if (logErrors != null) {
                logErrorsList = logErrors;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int seleccionado = item.getItemId();
        if (seleccionado == R.id.option_logout) {
            finish();
            return true;
        } else if (seleccionado == R.id.option_log) {
            showAlertDialgoLogs();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialgoLogs() {
        StringBuilder logs = new StringBuilder();
        if (logErrorsList != null && logErrorsList.size() > 0) {
            for (int x = 0; x < logErrorsList.size(); x++) {
                logs.append("-").append(logErrorsList.get(x).getFechaError())
                        .append(": ")
                        .append(logErrorsList.get(x).getLlave())
                        .append(" / ")
                        .append(logErrorsList.get(x).getError())
                        .append("\n");
            }
        } else {
            logs.append("No hay errores guardados.");
        }
        MaterialAlertDialogBuilder materialAlertDialogBuilder =
                new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setTitle("Lista de errores de red");
        materialAlertDialogBuilder.setMessage(logs);
        materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
        materialAlertDialogBuilder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        materialAlertDialogBuilder.show();
    }

    private void createUtilsAssets() {
        InputStream inPEN = getResources().openRawResource(R.raw.cen2020);
        InputStream inPFF = getResources().openRawResource(R.raw.cens2020);
        FileOutputStream outPEN;
        FileOutputStream outPFF;
        try {
//            File directory = requireActivity().getFilesDir();
//            File directory = getExternalFilesDir(null);
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String inecMovilDirectory = directory + "/InecMovil";

            //CREACIÓN DE DIRECTORIO INECMOVIL : DATA/COM.EXAMPLE.CENSO/FILE/INECMOVIL
            if (!new File(inecMovilDirectory).exists()) {
                if (new File(inecMovilDirectory).mkdir()) {
                    Log.i(TAG, "createUtilsAssets: Se creo el directorio InecMovil");
                } else
                    Log.e(TAG, "createUtilsAssets: No se creo el directorio InecMovil");
            }

            //CREACIÓN DE DIRECTORIO PROGRAMAS /INECMOVIL/PROGRAMAS
            if (!new File(inecMovilDirectory + "/PROGRAMAS/").exists()) {
                if (new File(inecMovilDirectory + "/PROGRAMAS/").mkdir()) {
                    Log.i("¡Exito!: Creado SA", "Se creo el directorio PROGRAMAS");
                } else
                    Log.e("¡Error:No Creado SA !", "No se creo el directorio PROGRAMAS");
            }

            outPEN = new FileOutputStream(inecMovilDirectory + "/PROGRAMAS/" + AppConstants.NOMBRE_PEN);
            outPFF = new FileOutputStream(inecMovilDirectory + "/PROGRAMAS/" + AppConstants.NOMBRE_PFF);
//            outPEN = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Android/data/"
//                    + AppConstants.APP_CSPRO + "/files/csentry/" + AppConstants.NOMBRE_PEN);

            byte[] buff = new byte[1024];
            byte[] buff2 = new byte[1024];
            int read;
            int read2;
            while ((read = inPEN.read(buff)) > 0) {
                outPEN.write(buff, 0, read);
            }

            while ((read2 = inPFF.read(buff2)) > 0) {
                outPFF.write(buff2, 0, read2);
            }
            inPEN.close();
            outPEN.close();
            inPFF.close();
            outPFF.close();
            fileAccessHelper.pushFiles(
                    inecMovilDirectory + "/PROGRAMAS/",
                    AppConstants.NOMBRE_PEN,
                    "/.",
                    false,
                    true,
                    strings -> null
                    , s -> null);

            fileAccessHelper.pushFiles(
                    inecMovilDirectory + "/PROGRAMAS/",
                    AppConstants.NOMBRE_PFF,
                    "/.",
                    false,
                    true,
                    strings -> null
                    , s -> null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDiccionario() {
        InputStream inputStreamDiccionario = getResources().openRawResource(R.raw.diccionario);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStreamDiccionario,
                    StandardCharsets.UTF_8));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            Log.e(TAG, "onActivityResult: " + e.getMessage());
        } finally {
            try {
                inputStreamDiccionario.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
        AppConstants.csproJson = new CsproJson(jsonString);
    }
}
