package com.example.mercadolaboral2.ui;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;


import com.example.mercadolaboral2.R;

import com.example.mercadolaboral2.data.local.constants.AppConstants;

import com.example.mercadolaboral2.data.local.constants.SharedPreferencesManager;

import com.example.mercadolaboral2.data.local.constants.clients.AuthClient;

import com.example.mercadolaboral2.data.remote.networkEntities.AuthResponse;

import com.example.mercadolaboral2.data.remote.networkEntities.PostLogin;

import com.example.mercadolaboral2.data.remote.networkServices.CensoAuthService;

import com.example.mercadolaboral2.utils.ProcessNotifier;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfigurationLoginDialogFragment extends DialogFragment {
    private static final String TAG = "ConfigurationLoginDialogFragment";
    private final Context context;
//    private static final String TAG = ConfigurationLoginDialogFragment.class.getSimpleName();

    private EditText etUser;
    private EditText etPassword;
    private ProcessNotifier notifier;

    private CensoAuthService censoAuthService;

    public ConfigurationLoginDialogFragment(Context context) {
        this.context = context;
    }

    @NotNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ActivityResultLauncher<String> requestPermissionLauncher
                = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                result -> Log.e("TAG", "PRUEBA"));

        if (!SharedPreferencesManager.getSomeBooleanValue(AppConstants.PREF_PERMISSION)) {
            requestPermission();
            requestPermission2();
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

            SharedPreferencesManager.setSomeBooleanValue(AppConstants.PREF_PERMISSION, true);
        }
        AuthClient authClient = AuthClient.getInstance();
        censoAuthService = authClient.getCensoAuthService();
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Configuración de Ingreso");
        builder.setMessage("La configuración de primer ingreso descargará la base de datos " +
                "correspondiente a las credenciales insertadas en esta vista.");

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        //    private View view;
        View view = inflater.inflate(R.layout.configuration_login_dialog_fragment, null);

        etUser = view.findViewById(R.id.etNControl);
        etPassword = view.findViewById(R.id.etCondicion);
        Button btnEntraConfig = view.findViewById(R.id.btnEntraConfigPrueba);
        Button btnCancelarConfig = view.findViewById(R.id.btnCancelarConfig);

        btnEntraConfig.setOnClickListener(v -> {
            String user = etUser.getText().toString();
            String password = etPassword.getText().toString();

            if (user.isEmpty())
                etUser.setError("El usuario es requerido");
            else if (password.isEmpty())
                etPassword.setError("La contraseña es requerida");
            else {
                PostLogin postLogin = new PostLogin(user.trim(), password.trim());
                Call<AuthResponse> call = censoAuthService.doLogin(postLogin);
                notifier = new ProcessNotifier(getActivity());
                notifier.setTitle("Consulta de servidor");
                notifier.setText("Verificando Token login...");
                notifier.inflate();

                call.enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<AuthResponse> call,
                                           @NotNull Response<AuthResponse> response) {
                        Log.i("---------", ""+response.raw());
                        if (response.isSuccessful()) {
                            try {
                                if (response.body() != null) {
                                    if (response.body().getRol().equals("E")) {
                                        assert response.body() != null;
                                        SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_USERNAME,
                                                response.body().getNombre());
                                        SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_CODIGO,
                                                response.body().getCodigo());
                                        SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_PASSWORD,
                                                password);
                                        SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_ROL,
                                                response.body().getRol());
                                        SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_ACCESS_TOKEN,
                                                response.body().getToken());
                                        SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_EXPIREAT,
                                                response.body().getExpireAt());


                                        SimpleDateFormat sdf = new SimpleDateFormat(
                                                AppConstants.TIMESTAMP_PATTERN, Locale.US);
                                        Date date = new Date();
                                        String fecha = sdf.format(date);
                                        SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_FECHA,
                                                fecha);

                                        Toast.makeText(context, "Bienvenido emp.: " + SharedPreferencesManager
                                                        .getSomeStringValue(AppConstants.PREF_USERNAME),
                                                Toast.LENGTH_LONG).show();
                                        String nameFrag = "ConfigurationLoginDialogFragment";
                                        if (this.getClass().getName().contains(nameFrag))
                                            ((LoginActivity) Objects.requireNonNull(context)).descargarInit();
                                        dismiss();
                                    } else {
                                        Toast.makeText(context, "Error de inicio, verificar el" +
                                                        "el del empadronador:  " + response.body().getRol(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "onResponse: ");
                            }
                        } else {
                            Toast.makeText(context, "Error al ingresar: " + response.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        notifier.deInflate();
                        notifier.dismiss();
                    }

                    @Override
                    public void onFailure(@NotNull Call<AuthResponse> call, @NotNull Throwable t) {
                        Log.e("Error", Objects.requireNonNull(t.getMessage()));
                        MaterialAlertDialogBuilder materialAlertDialogBuilder =
                                new MaterialAlertDialogBuilder(requireContext());
                        materialAlertDialogBuilder.setTitle("Error en consulta al servidor");
                        materialAlertDialogBuilder.setMessage("Problemas al contactar con el servidor." +
                                " Error: " + t.getMessage());
                        materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
                        materialAlertDialogBuilder.setPositiveButton("OK", (dialog1, which1)
                                -> dialog1.dismiss());
                        materialAlertDialogBuilder.show();
                        notifier.deInflate();
                        notifier.dismiss();
                    }
                });
            }
        });

        btnCancelarConfig.setOnClickListener(v -> requireActivity().finish());
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        return dialog;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", requireActivity().getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(requireActivity(), new String[]{WRITE_EXTERNAL_STORAGE},
                    2);
        }
    }

    private void requestPermission2() {
        if (requireActivity().checkSelfPermission(
                WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                requireActivity().checkSelfPermission(
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireActivity(), "Permiso concedido", Toast.LENGTH_SHORT).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requestPermissions(new String[]{
                        WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length != 0)
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(requireActivity(), "Permiso concedido.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireActivity(), "Permiso Denegado.", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        }
    }
}
