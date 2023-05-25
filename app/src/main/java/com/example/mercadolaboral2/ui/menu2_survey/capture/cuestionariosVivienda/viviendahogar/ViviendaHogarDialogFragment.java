package com.example.mercadolaboral2.ui.menu2_survey.capture.cuestionariosVivienda.viviendahogar;


import static com.example.mercadolaboral2.data.local.constants.AppConstants.fileAccessHelper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mercadolaboral2.R;

import com.example.mercadolaboral2.data.local.constants.AppConstants;

import com.example.mercadolaboral2.data.local.constants.SharedPreferencesManager;

import com.example.mercadolaboral2.data.local.dbEntities.Cuestionarios;

import com.example.mercadolaboral2.data.local.dbEntities.EntrevistaBase;
import com.example.mercadolaboral2.data.local.dbEntities.Muestra;

import com.example.mercadolaboral2.utils.ProcessNotifier;

import com.example.mercadolaboral2.utils.Status;

import com.example.mercadolaboral2.utils.Utilidad;
import com.example.mercadolaboral2.utils.ValidacionDeEstructuraCenso;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ViviendaHogarDialogFragment extends Fragment
        implements ViviendaHogarDialogAdapter.OnViviendaHogarListener, View.OnClickListener {

    private static final String TAG = "ViviendaHogarDialogFrag";
    private final Muestra muestra;
    private final FragmentActivity activity;
    private final File downloadDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private final String inecMovilDirectory = downloadDirectory + "/InecMovil";
    ActivityResultLauncher<Intent> activityResultLauncher;
    private String inputDataPath;
    private String inputDataPath2;
    private String idDAT;
    private List<Cuestionarios> viviendaList;
    private List<Cuestionarios> hogarList;
    private List<List<Cuestionarios>> cuestionariosViviendaHogares;
    private Cuestionarios cuestionarioSelected;
    private ViviendaHogarDialogAdapter viviendaHogarDialogAdapter;
    private ViviendaHogarViewModel viviendaHogarViewModel;
    private View view;
    private ProcessNotifier processNotifier;
    private boolean flagHogaresObserver;
    private boolean flagEliminarObserver;
    private int intentosCall = 0;
    private boolean flagCodigoObserver;

    public ViviendaHogarDialogFragment(FragmentActivity activity, Muestra muestra) {
        this.muestra = muestra;
        this.activity = activity;
    }

/*    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        flagHogaresObserver = true;
//        flagEliminarObserver = true;
        flagEliminarObserver = false;
        processNotifier = new ProcessNotifier(activity);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result ->
                        fileAccessHelper.pullFiles("/.",
                                "C" + idDAT,
                                inputDataPath2,
                                true,
                                true,
                                strings -> {
                                    String csproPath = Environment.getExternalStorageDirectory()
                                            + "/Android/data/" + AppConstants.APP_CSPRO + "/files/csentry/C" + idDAT;
                                    if (new File(csproPath + ".csnot").exists()) {
                                        pullFilesCsProToDevice("C" + idDAT + ".csnot");
                                        startCuestionarioResults(true);
                                    } else
                                        startCuestionarioResults(false);

                                    if (new File(csproPath + ".lst").exists())
                                        pullFilesCsProToDevice("C" + idDAT + ".lst");

                                    if (new File(csproPath + ".sts").exists())
                                        pullFilesCsProToDevice("C" + idDAT + ".sts");

                                    if (new File(csproPath + ".csidx").exists())
                                        pullFilesCsProToDevice("C" + idDAT + ".csidx");
                                    return null;
                                }, s -> null));
        // Inflate the layout to use as dialog or embedded fragment
        view = inflater.inflate(R.layout.dialogfragment_viviendas, container, false);
        viviendaHogarViewModel = new ViewModelProvider(this).get(ViviendaHogarViewModel.class);

        TextView tvTituloSubzona = view.findViewById(R.id.tvTituloSubzona);
        TextView tvTituloSegmento = view.findViewById(R.id.tvTituloSegmento);
        tvTituloSubzona.setText(String.format("Semana: %s", muestra.getPaR06_ID()));
//        tvTituloSubzona.setText(String.format("SubZona: %s", muestra.getPaR02_ID()));
        tvTituloSegmento.setText(String.format("UPM: %s - %s - %s",
                muestra.getLlave().substring(0, 6),
                muestra.getLlave().substring(6,8),
                muestra.getLlave().substring(8, 10)));

//                muestra.getMuestraId().substring(0, 6),
//                muestra.getMuestraId().substring(6, 10),
//                muestra.getMuestraId().substring(10, 12)));

        Button btnAgregarVivienda = view.findViewById(R.id.btnAgregarVivienda);
//        ImageView ivClose = view.findViewById(R.id.ivCloseViviendasHogar);

        btnAgregarVivienda.setOnClickListener(this);
//        ivClose.setOnClickListener(v -> dismiss());

        viviendaList = new ArrayList<>();
        cuestionariosViviendaHogares = new ArrayList<>();

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.rvViviendasHogar);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        viviendaHogarDialogAdapter = new ViviendaHogarDialogAdapter(viviendaHogarViewModel, muestra,
                cuestionariosViviendaHogares,this,, activity);
        recyclerView.setAdapter(viviendaHogarDialogAdapter);

        setData();
        return view;
    }

    private void setData() {
        viviendaHogarViewModel.getCuestionariosBySegmentoVivienda(muestra.getMuestraId()).observe(
                activity, viviendas -> {
                    if (viviendas != null) {
                        viviendaList = viviendas;
                        if (flagHogaresObserver)
                            getHogaresObserve();
                        if (!flagEliminarObserver)
                            flagEliminarObserver = true;
                    }
                });
    }

    private void getHogaresObserve() {
        try {
            viviendaHogarViewModel.getCuestionariosBySegmento(muestra.getMuestraId())
                    .observe(activity, hogares -> {
                        if (hogares != null && viviendaList != null) {
//                            viviendaList = hogares;
                            if (flagEliminarObserver) {

                                flagHogaresObserver = false;
                                List<List<Cuestionarios>> cuestionariosViviendaHogaresfin = new ArrayList<>();
                                for (int x = 0; x < viviendaList.size(); x++) {
                                    hogarList = new ArrayList<>();
                                    for (int z = 0; z < hogares.size(); z++) {
                                        if (viviendaList.get(x).getJefe()
                                                .equals(hogares.get(z).getJefe())) {
                                            hogarList.add(hogares.get(z));
                                        }
                                    }
                                    cuestionariosViviendaHogaresfin.add(hogarList);
                                }
                                cuestionariosViviendaHogares = cuestionariosViviendaHogaresfin;
                                viviendaHogarDialogAdapter.setData(cuestionariosViviendaHogares);
                                if (cuestionariosViviendaHogares.size() < 1)
                                    viviendaHogarViewModel.updateEstadoSegmento(muestra.getMuestraId());
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "getHogaresObserve: ");
        }
    }

    @Override
    public void onClick(View v) {
        int opcion = v.getId();
        if (opcion == R.id.btnAgregarVivienda) {
            String numVivienda;
            SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
            Date date = new Date();
            String fecha = sdf.format(date);
            int numViviendaSelected;

            if (cuestionariosViviendaHogares.size() == 0) {
                numVivienda = "01";
            } else {
                numViviendaSelected = Integer.parseInt(cuestionariosViviendaHogares.get(
                        cuestionariosViviendaHogares.size() - 1).get(0).getJefe());
                if (numViviendaSelected < 9)
                    numVivienda = "0" + (Integer.parseInt(cuestionariosViviendaHogares.get(
                            cuestionariosViviendaHogares.size() - 1).get(0).getJefe()) + 1);
                else
                    numVivienda = String.valueOf(Integer.parseInt(cuestionariosViviendaHogares.get(
                            cuestionariosViviendaHogares.size() - 1).get(0).getJefe()) + 1);
            }

            if (cuestionariosViviendaHogares.size() > 0) {
                if (cuestionariosViviendaHogares.get(cuestionariosViviendaHogares.size() - 1)
                        .get(0).getEstado() >= 1) {
                    viviendaHogarViewModel.addVivienda(getNuevaVivienda(numVivienda, fecha));
                    viviendaHogarDialogAdapter.notifyDataSetChanged();
                } else {
                    Utilidad.showMessageDialog(
                            "Creación de vivienda",
                            "Ya tiene una vivienda creada sin capturar.",
                            true, activity, R.raw.error_sign);
                }
            } else {
                viviendaHogarViewModel.addVivienda(getNuevaVivienda(numVivienda, fecha));
//                viviendaHogarDialogAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onViviendaHogarClick(List<Cuestionarios> cuestionariosSelect, View v, List<EntrevistaBase> entrevistaBase) {
        PopupMenu popup = new PopupMenu(activity, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(item -> {
            this.cuestionarioSelected = cuestionariosSelect.get(0);
             if (item.getItemId() == R.id.popup_listarerrores) {
                showErrorsDialog();
                return true;
            } else if (item.getItemId() == R.id.popup_eliminarcuestionario) {
                if (this.cuestionarioSelected.getEstado() == 0) {
                    flagHogaresObserver = true;
                    showDeleteDialog(this.cuestionarioSelected);
                } else if (this.cuestionarioSelected.getEstado() > 0) {
                    flagHogaresObserver = true;
                    showConfirmDeleteCuestionarioDialog(this.cuestionarioSelected);
                } else
                    Toast.makeText(activity, "Eliminación no autorizada, el cuestionario tiene datos.",
                            Toast.LENGTH_SHORT).show();
                return true;
            }  else
                return false;
        });
        popup.inflate(R.menu.menu_popup_viviendahogar);
        popup.show();
    }

    @Override
    public void onHogarClick(Cuestionarios cuestionario, View v, int position) {
        PopupMenu popup = new PopupMenu(activity, v);

        popup.setOnMenuItemClickListener(item -> {
            cuestionarioSelected = cuestionario;
            if (item.getItemId() == R.id.popup_revisarcuestionario) {
                if (abrirCsPro(cuestionarioSelected))
                    Log.d(TAG, "onViviendaHogarClick: revise el cuestionario.");
                return true;
            } else if (item.getItemId() == R.id.popup_listarerrores) {
                showErrorsDialog();
                return true;
            } else if (item.getItemId() == R.id.popup_eliminarcuestionario) {
                if (cuestionarioSelected.getEstado() == 0)
                    showDeleteDialog(cuestionarioSelected);
                else if (this.cuestionarioSelected.getEstado() > 0) {
                    flagHogaresObserver = true;
                    showConfirmDeleteCuestionarioDialog(this.cuestionarioSelected);
                } else
                    Toast.makeText(activity, "Eliminación no autorizada, el cuestionario tiene datos.",
                            Toast.LENGTH_SHORT).show();
                return true;
            } else
                return false;
        });
        popup.inflate(R.menu.menu_popup_viviendahogar);
        popup.show();
    }

    private EntrevistaBase getNuevaVivienda(String numVivienda, String fecha) {
        return new EntrevistaBase(
                muestra.getLlave() + numVivienda + "1",
                muestra.getMuestraId(),
                1,
                1,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "1",
                1,
                null,
                fecha,
                1,
                null,
                null,
                null);
    }

    private boolean verificarRed() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) return false;
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        return networkCapabilities != null
                && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

    private void showConfirmDeleteCuestionarioDialog(Cuestionarios cuestionarioSelected) {
        if (verificarRed()) {
            MaterialAlertDialogBuilder madConfirmacion =
                    new MaterialAlertDialogBuilder(requireActivity());
            madConfirmacion.setTitle("Eliminación de cuestionario");
            madConfirmacion.setMessage("¿Desea eliminar un cuestionario con datos?\n" +
                    "Vivienda: " + cuestionarioSelected.getJefe() + "  Hogar: " + cuestionarioSelected.getHogar());
            View viewInflated = LayoutInflater.from(requireActivity())
                    .inflate(R.layout.alertdialog_confirmacion_descargacuest, null);
            madConfirmacion.setView(viewInflated);
            final EditText editTextConfirmPss = viewInflated.findViewById(R.id.editTextConfirmPss);
            madConfirmacion.setPositiveButton("Aceptar", (dialog, which) -> {
                String password = editTextConfirmPss.getText().toString().trim();
                if (!password.equals(AppConstants.CODIGO_ELIMINAR_CUESTIONARIO))
                    Toast.makeText(requireActivity(), "Contraseña incorrecta ",
                            Toast.LENGTH_SHORT).show();
                else {
                    flagEliminarObserver = false;
                    processNotifier = new ProcessNotifier(activity);
                    viviendaHogarViewModel.deleteCuestionario("remoto", processNotifier, muestra,
                            cuestionarioSelected, activity);
                }
            });
            madConfirmacion.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            madConfirmacion.show();
        } else
            Toast.makeText(activity, "Debe estar conectado a una red.", Toast.LENGTH_SHORT).show();
    }

    private void showErrorsDialog() {
        if (cuestionarioSelected.getFechaActualizacion() != null
                && !cuestionarioSelected.getFechaActualizacion().trim().equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.popup_listarerrores);
            builder.setMessage(cuestionarioSelected.getFechaActualizacion());
            builder.setPositiveButton("OK", (dialog, which) -> Toast.makeText(activity,
                    "Lista de errores cerrado.", Toast.LENGTH_SHORT).show());
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Toast.makeText(requireActivity(), "No hay errores encontrado por el sistema.",
                    Toast.LENGTH_SHORT).show();
        }
    }

//    private void showCodigoECensoDialog(String llave) {
//        RemoveLiveDataObserver.observeOnce(viviendaHogarViewModel.getCodigoECensoById(llave), cuestionarios -> {
//            if (cuestionarios != null) {
//                MaterialAlertDialogBuilder madCodigoECenso =
//                        new MaterialAlertDialogBuilder(requireActivity());
//                madCodigoECenso.setTitle("Código E-Censo");
//                View viewInflated = LayoutInflater.from(requireActivity())
//                        .inflate(R.layout.alertdialog_codigoecenso, null);
//                madCodigoECenso.setView(viewInflated);
//                final TextView tvCodigoCenso = viewInflated.findViewById(R.id.tvCodigoCenso);
//                final TextView btnGenerarCodigo = viewInflated.findViewById(R.id.btnGenerarCodigo);
//                final TextView btnCerrarCodigo = viewInflated.findViewById(R.id.btnCerrarCodigo);
//
//                if (cuestionarios.getCodigoECenso() != null && !cuestionarios.getCodigoECenso().equals("")) {
//                    tvCodigoCenso.setText(cuestionarios.getCodigoECenso().trim());
//                    btnGenerarCodigo.setEnabled(false);
//                } else
//                    tvCodigoCenso.setText("");
//                AlertDialog dialog = madCodigoECenso.create();
//
//                btnGenerarCodigo.setOnClickListener(view -> {
//                    if (cuestionarios.getCodigoECenso() != null && !cuestionarios.getCodigoECenso().equals("")) {
//                        btnGenerarCodigo.setEnabled(false);
//                    }
//                    mostrarAlertDialogConfirmacion(cuestionarios, tvCodigoCenso);
//                });
//
//                btnCerrarCodigo.setOnClickListener(view -> dialog.dismiss());
//                dialog.show();
//            }
//        });
//    }

    private void mostrarAlertDialogConfirmacion(Cuestionarios cuestionarios, TextView tvCodigoCenso) {
        MaterialAlertDialogBuilder madConfirmacion =
                new MaterialAlertDialogBuilder(requireActivity());
        madConfirmacion.setTitle("Confirmar E-Censo");
        madConfirmacion.setMessage("¿Está seguro que habilitará esta vivienda como E-Censo?\n" +
                "Vivienda: " + cuestionarioSelected.getJefe() + "  Hogar: " + cuestionarioSelected.getHogar());
        View viewInflated = LayoutInflater.from(requireActivity())
                .inflate(R.layout.alertdialog_confirmacion_descargacuest, null);
        madConfirmacion.setView(viewInflated);
        final EditText editTextConfirmPss = viewInflated.findViewById(R.id.editTextConfirmPss);
        madConfirmacion.setPositiveButton("Aceptar", (dialog, which) -> {
            String password = editTextConfirmPss.getText().toString().trim();
            if (!password.equals(AppConstants.CODIGO_ECENSO))
                Toast.makeText(requireActivity(), "Contraseña incorrecta ",
                        Toast.LENGTH_SHORT).show();
            else {
                getCodigoECensoCall(cuestionarios, tvCodigoCenso);
                Log.e(TAG, "onClick: ");
            }
        });
        madConfirmacion.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        madConfirmacion.show();
    }

    private void getCodigoECensoCall(Cuestionarios cuestionarios, TextView tvCodigoCenso) {
        processNotifier = new ProcessNotifier(activity);
        processNotifier.inflate();
        processNotifier.setTitle("Solicitud de código E-Censo");
        processNotifier.setText("Verificando datos . . .");
        flagCodigoObserver = true;
        try {
            viviendaHogarViewModel.getCodigoECensoCall(cuestionarios).observe(this,
                    cuestionarioResponse -> {
                        if (cuestionarioResponse.status.equals(Status.SUCCESS)) {
                            if (flagCodigoObserver) {
                                if (cuestionarioResponse.data != null) {
                                    if (cuestionarioResponse.data.getCodigoECenso() != null) {
                                        intentosCall = 0;
                                        tvCodigoCenso.setText(cuestionarioResponse.data.getCodigoECenso());
                                    } else {
                                        if (cuestionarioResponse.data.getFechaActualizacion() != null) {
                                            Utilidad.showMessageDialog("Error",
                                                    "Error en descargar código E-Censo. "
                                                            + cuestionarioResponse.data.getFechaActualizacion(),
                                                    true, activity, R.raw.error_sign);
                                        }
                                    }
                                }
                            }
                            flagCodigoObserver = false;
                            processNotifier.deInflate();
                            processNotifier.dismiss();
                            viviendaHogarViewModel.getCodigoECensoCall(cuestionarios).removeObservers(this);
                        }

                        if (cuestionarioResponse.status.equals(Status.ERROR)) {
                            if (flagCodigoObserver) {
                                intentosCall++;
                                if (intentosCall == 1) {
                                    processNotifier.deInflate();
                                    processNotifier.dismiss();
                                    getCodigoECensoCall(cuestionarios, tvCodigoCenso);
                                } else {
                                    Utilidad.showMessageDialog("Error de red",
                                            "Error al cargar código E-Censo. " + Objects.requireNonNull(cuestionarioResponse.message) + " | " +
                                                    Objects.requireNonNull(cuestionarioResponse.data).getFechaActualizacion(),
                                            true, activity, R.raw.error_sign);

                                }
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "getCodigoECensoCall: ");
        }
    }

    private void showDeleteDialog(Cuestionarios cuestionarioSelected) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.popup_eliminarcuestionario);
        builder.setMessage("¿Desea eliminar este cuestionario?");
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            flagEliminarObserver = false;
            processNotifier = new ProcessNotifier(activity);
            viviendaHogarViewModel.deleteCuestionario("local", processNotifier, muestra,
                    cuestionarioSelected, activity);
        });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean abrirCsPro(Cuestionarios cuestionarios) {
        String csEntryDirectory = Environment.getExternalStorageDirectory() + "/Android/data/" + AppConstants.APP_CSPRO;
        String dataDirectory = inecMovilDirectory + "/DATA";

        String datPath = "/ENCUESTADOR/" +
                //EMPADRONADOR
                SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME) + "/" +
                //PROVINCIA + DISTRITO + CORREGIMIENTO / SEGMENTO
                muestra.getPaR02_ID() + "/"
                + muestra.getPaR02_DESC()
                + muestra.getPaR03_ID()
                + muestra.getPaR06_ID()
                + muestra.getLlave() + "/" +
                //VIVIENDA
                cuestionarios.getJefe() + "/";

        //    private String startMode;
        String parametro;
        try {
            //VERIFICACIÓN DE CS ENTRY APP INSTALADO
            if (!(new File(csEntryDirectory).exists())) {
                Toast.makeText(activity, "Verificar la instalacion de CSentry, no se " +
                        "encontró el archivo: " + AppConstants.APP_CSPRO, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "abrirCsPro: no se encontró el archivo de CSEntry.");
                return false;
            }

/*            //VERIFICACIÓN DE .PEN INSTALADO
            if (!(new File(penPath).exists())) {
                Toast.makeText(activity, "Verificar la instalación del programa de "
                        + "captura .PEN en /PROGRAMAS/", Toast.LENGTH_SHORT).show();
                return false;
            }*/

            //VERIFICAR SI EXISTE LAS CARPETAS DAT
            if (!(new File(dataDirectory + datPath).exists())) {
                if (new File(dataDirectory + datPath).mkdirs()) {
                    Log.i(TAG, "abrirCsPro: se ha creado la carpeta datPath");
                } else Log.e(TAG, "abrirCsPro: no se ha creado la carpeta");
            }

            String cuestionarioID = cuestionarios.getEntrevistaNum()
                    + cuestionarios.getHogar()
                    + cuestionarios.getRecorrido()
                    + cuestionarios.getLugarPoblado()
                    + cuestionarios.getCalle()
                    + muestra.getFechaCreacion()
                    + cuestionarios.getJefe()
                    + cuestionarios.getHogar();

            StringBuilder lugarPobladoDesc;
            if (muestra.getPaR04_DESC() != null &&
                    !muestra.getPaR04_DESC().trim().equals("NULL")) {
                lugarPobladoDesc = new StringBuilder(muestra.getPaR04_DESC());
                if (lugarPobladoDesc.length() != 43) {
                    for (int x = lugarPobladoDesc.length(); x <= 39; x++)
                        lugarPobladoDesc.append(" ");
                }
            } else {
                lugarPobladoDesc = new StringBuilder();
                if (lugarPobladoDesc.length() != 43) {
                    for (int x = lugarPobladoDesc.length(); x <= 39; x++)
                        lugarPobladoDesc.append(" ");
                }
            }

            StringBuilder barrioDesc;
            if (muestra.getPaR05_ID() != null &&
                    !muestra.getPaR05_ID().trim().equals("NULL")) {
                barrioDesc = new StringBuilder(muestra.getPaR05_ID());
                if (barrioDesc.length() != 43) {
                    for (int x = barrioDesc.length(); x <= 39; x++)
                        barrioDesc.append(" ");
                }
            } else {
                barrioDesc = new StringBuilder();
                if (barrioDesc.length() != 43) {
                    for (int x = barrioDesc.length(); x <= 39; x++)
                        barrioDesc.append(" ");
                }
            }

            parametro = cuestionarioID
                    + muestra.getPaR01_ID()
                    + muestra.getPaR01_DESC()
                    + muestra.getPaR04_ID()
                    + lugarPobladoDesc.substring(0, 40)
                    + barrioDesc.substring(0, 40);
//                    + muestra.getArea();//Area

            idDAT = cuestionarioID + ".dat";
            inputDataPath = dataDirectory + datPath + "C" + idDAT;
//            inputDataPath2 = csEntryDirectory + "/files/csentry/C" + idDAT;
            inputDataPath2 = dataDirectory + datPath;

            if (new File(inputDataPath).exists() && new File(csEntryDirectory
                    + "/files/csentry/C" + idDAT).exists()) {
                launchIntentCsPro(cuestionarioID, csEntryDirectory, parametro);
//                casos = LeerLineas(cuestionarioID, inputDataPath);
            } else if (cuestionarios.getResultadoId() != null) {
                crearDat(cuestionarios, inputDataPath);
                fileAccessHelper.pushFiles(inputDataPath2,
                        "C" + idDAT,
                        "/.",
                        false,
                        true,
                        strings -> {
                            launchIntentCsPro(cuestionarioID, csEntryDirectory, parametro);
                            return null;
                        },
                        s -> null);
//                casos = LeerLineas(cuestionarioID, inputDataPath);
            } else {
                launchIntentCsPro(cuestionarioID, csEntryDirectory, parametro);
            }
        } catch (Exception e) {
            Log.e(TAG, "abrirCsPro: " + e.getMessage());
            return false;
        }
        return true;
    }

    private void launchIntentCsPro(String key, String csEntryDirectory, String parametro) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setComponent(new ComponentName(AppConstants.APP_CSPRO,
                    "gov.census.cspro.csentry.ui.EntryActivity"));
            intent.putExtra("PffFilename", AppConstants.NOMBRE_PFF);
            intent.putExtra("Key", key);
            intent.putExtra("InputData", csEntryDirectory + "/files/csentry/C" + idDAT);
//            intent.putExtra("InputData", inputDataPath);
            intent.putExtra("Parameter", parametro);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activityResultLauncher.launch(intent);
        } catch (Exception e) {
            Log.e(TAG, "abrirCsPro: " + e.getMessage());
        }
    }

    private void crearDat(Cuestionarios cuestionarios, String inputDataPath) {
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;
        try {
            File datFile = new File(inputDataPath);
            if (datFile.createNewFile()) {
                Log.i(TAG, "abrirCsPro: archivo creado");
            }
            fileWriter = new FileWriter(datFile);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(inputDataPath), StandardCharsets.UTF_8));
            bufferedWriter.write("\uFEFF");
            bufferedWriter.write(cuestionarios.getResultadoId());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null)
                    bufferedWriter.close();

                if (fileWriter != null)
                    fileWriter.close();
            } catch (Exception e) {
                Log.e(TAG, "abrirCsPro: " + e.getMessage());
            }
        }
    }

    private void startCuestionarioResults(boolean flagCsNot) {
        StringBuilder fileDats = new StringBuilder();
        StringBuilder fileDatsNot = new StringBuilder();
        String resultado;
        String csNotPath = inputDataPath + ".csnot";
        InputStream inputStream = null;
//        InputStream inputStreamDATNot = null;
        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
        Date date = new Date();
        String Linea;
        int countLineas = 0;
//            boolean info = false;
        try {
            File datFile = new File(inputDataPath);
            Date lastModified = new Date(datFile.lastModified());

            try {
                inputStream = new FileInputStream(datFile);
                BufferedReader br = new BufferedReader(new FileReader(inputDataPath));
                while ((Linea = br.readLine()) != null && !("".equals(Linea))) {
//                        Linea = Utilidad.removeUTF8BOM(Linea);
                    fileDats.append(Linea).append("\n");
//                    fileDats.append(Linea);
                    countLineas++;
                }
                br.close();
            } catch (FileNotFoundException e) {
                Log.e(TAG, "onActivityResult: " + e.getMessage());
            }

            if (flagCsNot) {
                try {
                    //                File csnotFile = new File(csNotPath);
                    //                inputStreamDATNot = new FileInputStream(csnotFile);
                    BufferedReader bufferedReaderNot = new BufferedReader(new FileReader(csNotPath));
                    while ((Linea = bufferedReaderNot.readLine()) != null && !("".equals(Linea))) {
                        //                        Linea = Utilidad.removeUTF8BOM(Linea);
                        fileDatsNot.append(Linea).append("\n");
                    }
                    bufferedReaderNot.close();
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "onActivityResult: " + e.getMessage());
                }
            }

            int comparacionDatos = 1;
            if (cuestionarioSelected.getResultadoId() != null)
                comparacionDatos = fileDats.substring(1).compareTo(cuestionarioSelected.getResultadoId());
            cuestionarioSelected.setFechaEntrada(sdf.format(date));

            if (countLineas == 1 && fileDats.length() > 10
                    && fileDats.substring(1, 2).equals("1") && fileDats.substring(18, 416).trim().equals("")) {
                Snackbar.make(view, "El cuestionario está en blanco.", Snackbar.LENGTH_SHORT)
                        .show();
            } else if (inputStream != null /*&& info */ && fileDats.length() > 10 && comparacionDatos != 0) {
                resultado = AppConstants.csproJson.getJson(Objects.requireNonNull(inputStream));
                JsonObject jsonObject = new JsonParser().parse(resultado).getAsJsonObject();
                String jefe;

                if (jsonObject.size() > 0) {
                    cuestionarioSelected.setFechaAsignacion(getEstadoCuestionario(jsonObject));
                    StringBuilder errores = new StringBuilder();
                    ValidacionDeEstructuraCenso estructura = null;
                    try {
                        estructura = new ValidacionDeEstructuraCenso(cuestionarioSelected.getJefe(),
                                jsonObject);

                        for (String e : estructura.validarViviendaParticularOcupada()) {
                            errores.append("\n\n").append(e);
                        }

                        for (String e : estructura.validarViviendaConOcupantesAusentesODesocupadas()) {
                            errores.append("\n\n").append(e);
                        }

                        for (String e : estructura.validarViviendaEspecial()) {
                            errores.append("\n\n").append(e);
                        }

                        for (String e : estructura.validarViviendaColecctiva()) {
                            errores.append("\n\n").append(e);
                        }
                    } catch (Exception e) {
                        Snackbar.make(view, "Error en validación de ESTRUCTURA. " + e.getMessage(),
                                Snackbar.LENGTH_SHORT).show();
                        cuestionarioSelected.setFechaActualizacion(errores.toString());
                    }

                    cuestionarioSelected.setFechaActualizacion(errores.toString());

                    try {
                        if (Integer.parseInt(cuestionarioSelected.getHogar()) > 1) {
                            String viviendaLlave = Objects.requireNonNull(estructura)
                                    .validarCantidadDeHogaresEnVivienda(cuestionariosViviendaHogares);
                            //                                .validarCantidadDeHogaresEnVivienda2(cuestionarioSelected);
                            String mensaje = "El número de registros de hogares debe coincidir con la " +
                                    "cantidad de hogares registrado en la Preg.17-Número de hogares en la vivienda.";
                            if (viviendaLlave == null) {
                                viviendaHogarViewModel.updateErrorHogar(mensaje, null);
                            } else {
                                viviendaHogarViewModel.correctErrorHogar(mensaje, viviendaLlave);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    if (jsonObject.get("REC_LIST_OCUP") != null) {
                    if (jsonObject.get("REC_LIST_OCUP") != null && jsonObject.getAsJsonArray("REC_LIST_OCUP")
                            .get(0).getAsJsonObject().get("LO_NOMBRE") != null) {
                        jefe = jsonObject.getAsJsonArray("REC_LIST_OCUP")
                                .get(0).getAsJsonObject().get("LO_NOMBRE").getAsString();
                        cuestionarioSelected.setPiso(jefe.trim());
                    } else {
                        cuestionarioSelected.setPiso("");
                    }
//                    resultado = resultado.replace("\"", "\\\"");
                    cuestionarioSelected.setResultadoId(fileDats.toString());
                    cuestionarioSelected.setNotas(fileDatsNot.toString());
                    cuestionarioSelected.setEmpadronadorId(resultado);
                    cuestionarioSelected.setMuestra(sdf.format(lastModified));

                    processNotifier = new ProcessNotifier(activity);
                    if (cuestionarioSelected.isFlagPrimerEnvio()) {
                        viviendaHogarViewModel.sendCuestionarioUpdate(processNotifier,
                                cuestionarioSelected, muestra, cuestionarioSelected.getEntrevistaId());
                    } else {
//                    primer envio y el normal
                        viviendaHogarViewModel.sendCuestionarioCreate(processNotifier,
                                cuestionarioSelected, muestra);
                    }
                    viviendaHogarViewModel.addCuestionarioDatosDat(cuestionarioSelected);

                } else {
                    Snackbar.make(view, "El cuestionario no ha sido iniciado.", Snackbar.LENGTH_SHORT)
                            .show();
                    Log.e(TAG, "onActivityResult: El cuestionario no se ha modificado.");
                }
            } else {
                Snackbar.make(view, "El cuestionario no ha sido modificado.", Snackbar.LENGTH_SHORT)
                        .show();
            }
        } catch (Exception e) {
            Log.e(TAG, "onActivityResult: " + e.getMessage());
        }
    }

    private void pullFilesCsProToDevice(String patternFile) {
        try {
            fileAccessHelper.pullFiles("/.",
                    patternFile,
                    inputDataPath2,
                    true,
                    true,
                    strings -> null, s -> null);
        } catch (Exception e) {
            Log.e(TAG, "pullFilesCsProToDevice: ");
        }
    }

    private int getEstadoCuestionario(JsonObject jsonObject) {
        int estado = 1;
        try {
            JsonElement recViviendaObj = jsonObject.get("REC_VIVIENDA");
            JsonElement recHogarObj = jsonObject.get("REC_HOGAR");
            JsonElement recPersonaObj = jsonObject.get("REC_PERSONA");
            String regexFive = "^(?:[1-9][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$";//NO ZERO

            // SIN INICIAR
            if (recViviendaObj == null && recHogarObj == null && recPersonaObj == null) {
                Log.i(TAG, "getEstadoCuestionario: Estado 1 Amarrillo");
            } else {
                int wCompleta;
                int wListaOcupante_Completa;
                int wPersona_Completa;
                if (recViviendaObj != null) {
                    String vTipo = recViviendaObj.getAsJsonObject().get("V01_TIPO").getAsString();
                    // NO ENCUESTABLE
                    if (vTipo.matches("^([1-4]|[0][1-4])$")
                            && recViviendaObj.getAsJsonObject().get("V02_COND").getAsString().equals("2")) {
                        Log.i(TAG, "getEstadoCuestionario: Estado 1 Amarrillo");

                    } else if (vTipo.matches("^([1-4]|[0][1-4])$")
                            && recViviendaObj.getAsJsonObject().get("V02_COND").getAsString().matches("[3-8]")) {
                        estado = 3;//AZUL
                    } else if (recHogarObj != null) {
                        JsonObject recHogarArrayJObj = recHogarObj.getAsJsonArray().get(0).getAsJsonObject();
                        if (cuestionarioSelected.getHogar().equals("1")
                                && vTipo.matches("^([1-4]|[0][1-4])$")
                                && recViviendaObj.getAsJsonObject().get("V02_COND") != null
                                && recViviendaObj.getAsJsonObject().get("V17_NHOG") != null
                                && recHogarArrayJObj.get("H21C_ACTIV") != null
                                && recHogarArrayJObj.get("H_PERSONAS") != null) {

                            if (recViviendaObj.getAsJsonObject().get("V02_COND").getAsString().equals("1")
                                    && recViviendaObj.getAsJsonObject().get("V17_NHOG").getAsString().matches("[1-8]")
                                    && recHogarArrayJObj.get("H21C_ACTIV").getAsString().matches("[1-2]")
                                    && recHogarArrayJObj.get("H_PERSONAS").getAsString().matches("^([0-9]|[0-9][0-9])$")
                                    && recHogarArrayJObj.get("H_PERSONAS").getAsInt() == jsonObject.get("REC_LIST_OCUP").getAsJsonArray().size()
                                    && recHogarArrayJObj.get("H_PERSONAS").getAsInt() == recPersonaObj.getAsJsonArray().size()) {
                                wCompleta = 1;
                                wListaOcupante_Completa = 0;

                                for (int i = 0; i < recHogarArrayJObj.get("H_PERSONAS").getAsInt(); i++) {
                                    if (jsonObject.getAsJsonArray("REC_LIST_OCUP")
                                            .get(i).getAsJsonObject().get("LO_ROWS").getAsString().matches("[1-2]"))
                                        wListaOcupante_Completa = wListaOcupante_Completa + 1;
                                }
                                wPersona_Completa = 0;
                                for (int i = 0; i < recHogarArrayJObj.get("H_PERSONAS").getAsInt(); i++) {
                                    JsonObject recPersonaArrayJObj = recPersonaObj.getAsJsonArray().get(i).getAsJsonObject();
                                    if (recPersonaArrayJObj.get("P221_SUEL").getAsString().trim().matches(regexFive)
                                            || recPersonaArrayJObj.get("P222_INGR").getAsString().trim().matches(regexFive)
                                            || recPersonaArrayJObj.get("P223_JUBI").getAsString().trim().matches(regexFive)
                                            || recPersonaArrayJObj.get("P224_BECA").getAsString().trim().matches(regexFive)
                                            || recPersonaArrayJObj.get("P225A_REDOPOR").getAsString().equals("1")
                                            || recPersonaArrayJObj.get("P225B_120A65").getAsString().equals("2")
                                            || recPersonaArrayJObj.get("P225C_ANGELG").getAsString().equals("3")
                                            || recPersonaArrayJObj.get("P226_OTRIN").getAsString().trim().matches(regexFive)
                                            || recPersonaArrayJObj.get("P22A_INGR").getAsString().matches("^([1-2])$")) {
                                        wPersona_Completa = wPersona_Completa + 1;
                                    }
                                }

                                if (wListaOcupante_Completa < recHogarArrayJObj.get("H_PERSONAS").getAsInt()
                                        || wPersona_Completa < recHogarArrayJObj.get("H_PERSONAS").getAsInt())
                                    wCompleta = 0;
                                if (wCompleta == 1)
                                    estado = 2;
                                //‘--Local no destinado, damnificado, hogar particular
                            }
                        } else if (vTipo.matches("^([0][5]|[5]|[0][6]|[6]|[0][8]|[8])$")//TODO POR QUE EL 7 NO
                                && recHogarArrayJObj.get("H21C_ACTIV") != null
                                && recHogarArrayJObj.get("H_PERSONAS") != null) {

                            if (recHogarArrayJObj.get("H21C_ACTIV").getAsString().matches("[1-2]")
                                    && recHogarArrayJObj.get("H_PERSONAS").getAsString().matches("^([0-9]|[0-9][0-9])$")
                                    && recHogarArrayJObj.get("H_PERSONAS").getAsInt() == jsonObject.get("REC_LIST_OCUP").getAsJsonArray().size()
                                    && recHogarArrayJObj.get("H_PERSONAS").getAsInt() == recPersonaObj.getAsJsonArray().size()) {

                                wCompleta = 1;
                                wListaOcupante_Completa = 0;

                                for (int i = 0; i < recHogarArrayJObj.get("H_PERSONAS").getAsInt(); i++) {
                                    if (jsonObject.getAsJsonArray("REC_LIST_OCUP")
                                            .get(i).getAsJsonObject().get("LO_ROWS").getAsString().matches("[1-2]"))
                                        wListaOcupante_Completa = wListaOcupante_Completa + 1;
                                }

                                wPersona_Completa = 0;
                                for (int i = 0; i < recHogarArrayJObj.get("H_PERSONAS").getAsInt(); i++) {
                                    JsonObject recPersonaArrayJObj = recPersonaObj.getAsJsonArray().get(i).getAsJsonObject();
                                    if (recPersonaArrayJObj.get("P221_SUEL").getAsString().trim().matches(regexFive)
                                            || recPersonaArrayJObj.get("P222_INGR").getAsString().trim().matches(regexFive)
                                            || recPersonaArrayJObj.get("P223_JUBI").getAsString().trim().matches(regexFive)
                                            || recPersonaArrayJObj.get("P224_BECA").getAsString().trim().matches(regexFive)
                                            || recPersonaArrayJObj.get("P225A_REDOPOR").getAsString().equals("1")
                                            || recPersonaArrayJObj.get("P225B_120A65").getAsString().equals("2")
                                            || recPersonaArrayJObj.get("P225C_ANGELG").getAsString().equals("3")
                                            || recPersonaArrayJObj.get("P226_OTRIN").getAsString().trim().matches(regexFive)
                                            || recPersonaArrayJObj.get("P22A_INGR").getAsString().matches("^([1-2])$")) {
                                        wPersona_Completa = wPersona_Completa + 1;
                                    }
                                }

                                if (wListaOcupante_Completa < recHogarArrayJObj.get("H_PERSONAS").getAsInt()
                                        || wPersona_Completa < recHogarArrayJObj.get("H_PERSONAS").getAsInt())
                                    wCompleta = 0;

                                if (wCompleta == 1)
                                    estado = 2;
                            }
                            //INDIGENTE Y COLECTIVAS
                        } else if (vTipo.matches("^([0][7]|[7]|[0][9]|[9]|[1][0-6])$")) {//TODO PREGUNTAR A MAGA
                            JsonElement recHogarArrayHPersonasJObj = recHogarObj.getAsJsonArray().get(0)
                                    .getAsJsonObject().get("H_PERSONAS");

                            if (recHogarArrayHPersonasJObj.getAsString().matches("^([0-9]|[0-9][0-9])$")
                                    && recHogarArrayHPersonasJObj.getAsInt() == recPersonaObj.getAsJsonArray().size()) {
                                wCompleta = 1;
                                wPersona_Completa = 0;
                                for (int i = 0; i < recHogarArrayHPersonasJObj.getAsInt(); i++) {
                                    JsonObject recPersonaArrayJObj = recPersonaObj.getAsJsonArray().get(i).getAsJsonObject();
                                    if (recPersonaArrayJObj.get("P221_SUEL").getAsString().trim().matches(regexFive)
                                            || recPersonaArrayJObj.get("P222_INGR").getAsString().trim().matches(regexFive)
                                            || recPersonaArrayJObj.get("P223_JUBI").getAsString().trim().matches(regexFive)
                                            || recPersonaArrayJObj.get("P224_BECA").getAsString().trim().matches(regexFive)
                                            || recPersonaArrayJObj.get("P225A_REDOPOR").getAsString().equals("1")
                                            || recPersonaArrayJObj.get("P225B_120A65").getAsString().equals("2")
                                            || recPersonaArrayJObj.get("P225C_ANGELG").getAsString().equals("3")
                                            || recPersonaArrayJObj.get("P226_OTRIN").getAsString().trim().matches(regexFive)
                                            || recPersonaArrayJObj.get("P22A_INGR").getAsString().matches("^([1-2])$")) {
                                        wPersona_Completa = wPersona_Completa + 1;
                                    }
                                }

                                if (wPersona_Completa < recHogarArrayHPersonasJObj.getAsInt())
                                    wCompleta = 0;

                                if (wCompleta == 1)
                                    estado = 2;
                            }
                        }
                    }
                } else { //Vivienda particular HOGAR adicional
                    assert recHogarObj != null;
                    JsonObject recHogarArrayJObj = recHogarObj.getAsJsonArray().get(0).getAsJsonObject().getAsJsonObject();
                    if (Integer.parseInt(cuestionarioSelected.getHogar()) > 1
                            && recHogarArrayJObj.get("H21C_ACTIV") != null
                            && recHogarArrayJObj.get("H_PERSONAS") != null) {

                        if (recHogarArrayJObj.get("H21C_ACTIV").getAsString().matches("[1-2]")
                                && recHogarArrayJObj.get("H_PERSONAS").getAsString().matches("^([0-9]|[0-9][0-9])$")
                                && recHogarArrayJObj.get("H_PERSONAS").getAsInt() == jsonObject.get("REC_LIST_OCUP").getAsJsonArray().size()
                                && recHogarArrayJObj.get("H_PERSONAS").getAsInt() == recPersonaObj.getAsJsonArray().size()) {
                            wCompleta = 1;
                            wListaOcupante_Completa = 0;

                            for (int i = 0; i < recHogarArrayJObj.get("H_PERSONAS").getAsInt(); i++) {
                                if (jsonObject.getAsJsonArray("REC_LIST_OCUP")
                                        .get(i).getAsJsonObject().get("LO_ROWS").getAsString().matches("[1-2]"))
                                    wListaOcupante_Completa = wListaOcupante_Completa + 1;
                            }

                            wPersona_Completa = 0;
                            for (int i = 0; i < recHogarArrayJObj.get("H_PERSONAS").getAsInt(); i++) {
                                JsonObject recPersonaArrayJObj = recPersonaObj.getAsJsonArray().get(i).getAsJsonObject();
                                if (recPersonaArrayJObj.get("P221_SUEL").getAsString().trim().matches(regexFive)
                                        || recPersonaArrayJObj.get("P222_INGR").getAsString().trim().matches(regexFive)
                                        || recPersonaArrayJObj.get("P223_JUBI").getAsString().trim().matches(regexFive)
                                        || recPersonaArrayJObj.get("P224_BECA").getAsString().trim().matches(regexFive)
                                        || recPersonaArrayJObj.get("P225A_REDOPOR").getAsString().equals("1")
                                        || recPersonaArrayJObj.get("P225B_120A65").getAsString().equals("2")
                                        || recPersonaArrayJObj.get("P225C_ANGELG").getAsString().equals("3")
                                        || recPersonaArrayJObj.get("P226_OTRIN").getAsString().trim().matches(regexFive)
                                        || recPersonaArrayJObj.get("P22A_INGR").getAsString().matches("^([1-2])$")) {
                                    wPersona_Completa = wPersona_Completa + 1;
                                }
                            }

                            if (wListaOcupante_Completa < recHogarArrayJObj.get("H_PERSONAS").getAsInt()
                                    || wPersona_Completa < recHogarArrayJObj.get("H_PERSONAS").getAsInt())
                                wCompleta = 0;

                            if (wCompleta == 1)
                                estado = 2;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Snackbar.make(view, "Error en asignación de ESTADO. " + e.getMessage(), Snackbar.LENGTH_SHORT)
                    .show();
            return estado;
        }
        return estado;
    }
}
