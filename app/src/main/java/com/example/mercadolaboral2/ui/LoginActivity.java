package com.example.mercadolaboral2.ui;

import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;


import com.example.mercadolaboral2.R;
import com.example.mercadolaboral2.data.local.constants.AppConstants;
import com.example.mercadolaboral2.data.local.constants.SharedPreferencesManager;
import com.example.mercadolaboral2.data.remote.workers.BackupWorker;
import com.example.mercadolaboral2.utils.ProcessNotifier;
import com.example.mercadolaboral2.utils.Status;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private final String[] PERMISSIONS = {
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_MEDIA_LOCATION,
            Manifest.permission.QUERY_ALL_PACKAGES,
            MANAGE_EXTERNAL_STORAGE
    };
    private ConfigurationLoginDialogViewModel configurationLoginDialogViewModel;
    private ProcessNotifier notifier;
    private TextView etUser;
    //    private WorkManager workManager;
//    private WorkRequest workRequest;
    private TextView etPassword;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //    private CensoApiService censoApiService;
        ActivityResultContracts.RequestMultiplePermissions requestMultiplePermissionsContract =
                new ActivityResultContracts.RequestMultiplePermissions();
        ActivityResultLauncher<String[]> requestPermissionLauncher
                = registerForActivityResult(requestMultiplePermissionsContract, isGranted
                -> Log.d(TAG, "onCreate: Launcher result:" + isGranted.toString()));

        if (!SharedPreferencesManager.getSomeBooleanValue(AppConstants.PREF_PERMISSION)) {
            requestPermission();
//            requestPermissionOld();
            requestPermissionLauncher.launch(PERMISSIONS);

            SharedPreferencesManager.setSomeBooleanValue(AppConstants.PREF_PERMISSION, true);
        }

        Objects.requireNonNull(getSupportActionBar()).hide();
        configurationLoginDialogViewModel = new ViewModelProvider(this)
                .get(ConfigurationLoginDialogViewModel.class);

        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        TextView tvActualizarContrasena = findViewById(R.id.tvActualizarContrasena);
//        TextView tvResetBd = findViewById(R.id.tvResetBD);
        TextView tvVersion = findViewById(R.id.tvVersion);
//        TextView tvResetToken = findViewById(R.id.tvResetToken);
        TextView btnLogIn = findViewById(R.id.btnLogIn);
        fragmentManager = this.getSupportFragmentManager();

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName + " || 1";

          /*  if (AppConstants.BASE_URL.contains("censos-api"))
                version = version + " || 1";
            else
                version = version + " || 2";*/
            tvVersion.setText(String.format("Version %s ", version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_ACCESS_TOKEN) == null) {
            ConfigurationLoginDialogFragment dialogNuevaNota =
                    new ConfigurationLoginDialogFragment(this);
            dialogNuevaNota.show(fragmentManager, "ConfigurationLoginDialogFragment");
        } else {
//            SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_LOGGED_USER, SharedPreferencesManager.getSomeStringValue( AppConstants.PREF_USERNAME));
//            SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_LOG_USER,SharedPreferencesManager.getSomeStringValue( AppConstants.PREF_USERNAME));
            etUser.setText(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO));
//            etPassword.setText(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_PASSWORD));
        }

        tvActualizarContrasena.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);
//        tvResetBd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int opcion = v.getId();

        if (opcion == R.id.btnLogIn) {
            try {
                String user = etUser.getText().toString();
                String password = etPassword.getText().toString();
                if (user.isEmpty())
                    etUser.setError("El usuario es requerido");
                else if (password.isEmpty())
                    etPassword.setError("La contraseña es requerida");
                else {
                    String usuarioPref = SharedPreferencesManager.getSomeStringValue(
                            AppConstants.PREF_CODIGO);
                    String contrasenaPref = SharedPreferencesManager.getSomeStringValue(
                            AppConstants.PREF_PASSWORD);

                    if (user.equals(usuarioPref) && password.equals(contrasenaPref)) {
                        if (!SharedPreferencesManager.getSomeBooleanValue(AppConstants.PREF_BACKUP_WORKER)) {
//                            setBackUpWorker();
                            SharedPreferencesManager.setSomeBooleanValue(AppConstants.PREF_BACKUP_WORKER, true);
                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                        SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_LOG_USER, user);
                        startActivity(intent);
                    } else etPassword.setError("La contraseña no es valida.");
                }
            } catch (Exception e) {
                Toast.makeText(this, "Ingrese los datos correctamente. " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        } /*else if (opcion == R.id.tvResetBD) {
            configurationLoginDialogViewModel.deleteAll();
            File file = new File(SharedPreferencesManager.getPath());
            boolean shared = file.delete();
            if (shared) {
                SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_REFRESH_TOKEN, null);
                SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_ACCESS_TOKEN, null);
                Intent i = getBaseContext().getPackageManager().
                        getLaunchIntentForPackage(getBaseContext().getPackageName());
                Objects.requireNonNull(i).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
            Log.i(TAG, "onClick: " + "tvResetBD");
        }*/ else if (opcion == R.id.tvActualizarContrasena) {
            MaterialAlertDialogBuilder materialAlertDialogBuilder =
                    new MaterialAlertDialogBuilder(this);
            materialAlertDialogBuilder.setTitle("Cambiar contraseña");
            materialAlertDialogBuilder.setMessage("¿Está seguro que desea cambiar la contraseña?");
            materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
            materialAlertDialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                ConfigurationLoginDialogFragment dialogNuevaNota =
                        new ConfigurationLoginDialogFragment(this);
                dialogNuevaNota.show(fragmentManager, "ConfigurationLoginDialogFragment");
            });

            materialAlertDialogBuilder.setNegativeButton("Cancelar",
                    (dialog, which) -> Log.e(TAG, "onClick: Cancelado."));
            materialAlertDialogBuilder.show();
        }
    }

    private void setBackUpWorker() {
        int numEmpadronador = Integer.parseInt(SharedPreferencesManager
                .getSomeStringValue(AppConstants.PREF_USERNAME).substring(1));
        String numConvert = String.valueOf(numEmpadronador);
        int hora = 6;
        if (numConvert.matches("^([1]|[0-9][0-9]|[0-9][0-9][0-9]|[0-2][0-9][0-9][0-9])$")) {
            hora = 12;
        } else if (numConvert.matches("^([2][0-9][0-9][0-9])$")) {//2000-2999
            hora = 13;
        } else if (numConvert.matches("^([3][0-9][0-9][0-9])$")) {//3000-3999
            hora = 14;
        } else if (numConvert.matches("^([4][0-9][0-9][0-9])$")) {//4000-4999
            hora = 15;
        } else if (numConvert.matches("^([5-6][0-9][0-9][0-9])$")) {//5000-5999
            hora = 16;
        }
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.METERED)
                .setRequiresDeviceIdle(false)
                .build();

        WorkManager workManager = WorkManager.getInstance(this);
        WorkRequest workRequest = new PeriodicWorkRequest.Builder(
                BackupWorker.class,
                hora,
                TimeUnit.HOURS)
                .setBackoffCriteria(
                        BackoffPolicy.EXPONENTIAL,
                        OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                        TimeUnit.MILLISECONDS
                )
                .setConstraints(constraints)
                .build();
        workManager.enqueue(workRequest);
    }

    public void descargarInit() {
        notifier = new ProcessNotifier(this);
        notifier.setTitle("Consulta de servidor");
        notifier.setText("Verificando token segmentos...");
        notifier.inflate();

        configurationLoginDialogViewModel.getSegmentos().observe(this, listResource -> {
            if (listResource.data != null) {
/*                for (Muestra segmentos : listResource.data) {
                    //GetData
                    Log.v("TAG", "onChanged " + segmentos.getId());
                }*/
                etUser.setText(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
                etPassword.setText(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_PASSWORD));
                notifier.setText("Datos de la tabla SEGMENTOS descargados del servidor.");
                notifier.deInflate();
                notifier.dismiss();
            }

            if (listResource.status.equals(Status.ERROR) && Objects.requireNonNull(listResource.data).size() == 0) {
                SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_ACCESS_TOKEN, null);
                Intent intentReload = getBaseContext().getPackageManager().
                        getLaunchIntentForPackage(getBaseContext().getPackageName());
                Objects.requireNonNull(intentReload).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentReload.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentReload);
                Toast.makeText(this, "Problema con la red: "
                        + Objects.requireNonNull(listResource.message), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "descargarInit: Error de descarga SEGMENTOS");
            }
        });
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", this.getPackageName())));
                startActivityForResult(intent, 2296);
//                requestPermissionOld();
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE,
                            READ_EXTERNAL_STORAGE,
                            MANAGE_EXTERNAL_STORAGE},
                    2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
