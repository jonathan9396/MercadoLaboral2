package com.example.mercadolaboral2.ui.menu1_home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mercadolaboral2.R;

import com.example.mercadolaboral2.data.local.constants.AppConstants;

import com.example.mercadolaboral2.data.local.constants.SharedPreferencesManager;

import com.example.mercadolaboral2.data.local.dbEntities.Muestra;

import com.example.mercadolaboral2.ui.menu2_survey.SurveyFragment;

import com.example.mercadolaboral2.ui.menu3_senddata.EnvioDeDatosFragment;

import com.example.mercadolaboral2.utils.ProcessNotifier;

import com.example.mercadolaboral2.utils.SdWorker;

import com.example.mercadolaboral2.utils.Status;

import com.example.mercadolaboral2.utils.Utilidad;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment implements HomeAdapter.OnInicioCardListener {

    private static final String TAG = "HomeFragment";
    String respaldoPath;
    private HomeViewModel homeViewModel;
    private SdWorker sdWorker;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private List<String> data;
    private List<Muestra> muestraSubzonaList;
    private List<Muestra> muestraList;
    private ProcessNotifier notifier;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::saveSdCard);//TODO RESOLVER

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar())
                .setTitle(getText(R.string.title_home) + " // " +
                        SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME)
                        + "(" + SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO)
                        + ")");
//        final TextView textView = root.findViewById(R.id.text_dashboard);
        data = new ArrayList<>();
        data.add(getString(R.string.title_surveys));
        data.add(getString(R.string.title_sendData));
        data.add(getString(R.string.title_network));
        data.add(getString(R.string.title_backup));

        List<String> dataSub = new ArrayList<>();
        dataSub.add(getString(R.string.body_menu_encuestas));
        dataSub.add(getString(R.string.body_menu_enviardatos));
        dataSub.add(getString(R.string.body_menu_red));
        dataSub.add(getString(R.string.body_menu_respaldo));

        List<Integer> listTxtImage = new ArrayList<>();
        listTxtImage.add(R.drawable.ic_menu_surveys);
        listTxtImage.add(R.drawable.ic_send_data);
        listTxtImage.add(R.drawable.ic_download);
        listTxtImage.add(R.drawable.ic_backup);

//        getSegmentos();

        RecyclerView recyclerView = root.findViewById(R.id.rvInicio);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        HomeAdapter homeAdapter = new HomeAdapter(data, dataSub, listTxtImage, this);
        recyclerView.setAdapter(homeAdapter);
        return root;
    }

    private void saveSdCard(ActivityResult result) {
        String carpeta = respaldoPath;
        String respaldoPath;
        String zipPath;
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        File directoryDB = requireActivity().getDatabasePath("censo_db");
        String inecMovilDirectory = directory + "/InecMovil";
        respaldoPath = inecMovilDirectory + "/RESPALDO/"
                + SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_LOG_USER)
                + "/" + carpeta;
        Uri treeUri;
        try {
            if (result.getData() != null) {
                treeUri = result.getData().getData();

                sdWorker.setPickedDir(DocumentFile.fromTreeUri(requireActivity(),
                        Objects.requireNonNull(treeUri)));
                if (SdWorker.dirIsPicked()) {
                    sdWorker.getPickerDir().getName();
                } else {
                    sdWorker.setPickedDir(null);
                }
            } else {
                sdWorker.setPickedDir(null);
            }

            if (SdWorker.dirIsPicked()) {
                zipPath = "/InecMovil/Respaldo/" +
                        SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME)
                        + "/" + carpeta;
                if (!(new File(sdWorker.getPickerDir().getName() + zipPath).exists())) {
                    SdWorker.mkdirs(zipPath);
                }
                try {
                    if (SdWorker.copyALL(respaldoPath, zipPath))
                        Toast.makeText(requireActivity(), "Respaldo a memoria extraible completado.",
                                Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(requireActivity(), "Respaldo a memoria extraible no realizado. " +
                                        "Verifique las carpetas en Mis Archivos -> Almacenamiento Interno" +
                                        "-> Download/InecMovil/.",
                                Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("Error:", Objects.requireNonNull(e.getMessage()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

//    private void getSegmentos() {
//        muestraSubzonaList = new ArrayList<>();
//        muestraList = new ArrayList<>();
//        homeViewModel.getSegmentosMaps().observe(getViewLifecycleOwner(), segmentos -> {
//            if (segmentos != null && segmentos.size() != muestraSubzonaList.size()) {
//                muestraSubzonaList = segmentos;
//            }
//        });
//
//        homeViewModel.getAllSegmentos().observe(getViewLifecycleOwner(), segmentos -> {
//            if (segmentos != null && segmentos.size() != muestraList.size()) {
//                muestraList = segmentos;
//            }
//        });
//    }

    private void descargarInconsistencias() {
        ProcessNotifier processNotifier = new ProcessNotifier(requireActivity());

        homeViewModel.getInconsistencias(requireActivity(), processNotifier,
                SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
    }

    private void mostrarConfirmacionRecuperacionDialog() {
        MaterialAlertDialogBuilder madConfirmacion =
                new MaterialAlertDialogBuilder(requireActivity());
        madConfirmacion.setTitle("Recuperación de data");
        madConfirmacion.setMessage("¿Desea proceder a realizar la recuperación de los datos enviados al servidor?");
        View viewInflated = LayoutInflater.from(requireActivity())
                .inflate(R.layout.alertdialog_confirmacion_descargacuest, null);
        madConfirmacion.setView(viewInflated);
        final EditText editTextConfirmPss = viewInflated.findViewById(R.id.editTextConfirmPss);
        madConfirmacion.setPositiveButton("Aceptar", (dialog, which) -> {
            String password = editTextConfirmPss.getText().toString().trim();
            if (!password.equals(AppConstants.CODIGO_RECUPERACIONCUESTIONARIOS))
                Toast.makeText(requireActivity(), "Contraseña incorrecta ",
                        Toast.LENGTH_SHORT).show();
            else
                getSegmentosBackup();
        });
        madConfirmacion.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        madConfirmacion.show();
    }

//    private void mostrarActualizarEtiquetaSegmento() {
//        MaterialAlertDialogBuilder madConfirmacion =
//                new MaterialAlertDialogBuilder(requireActivity());
//        madConfirmacion.setTitle("Actualizar Etiqueta");
//        madConfirmacion.setMessage("¿Desea proceder a actualizar la etiqueta de los segmentos?");
//        View viewInflated = LayoutInflater.from(requireActivity())
//                .inflate(R.layout.alertdialog_confirmacion_descargacuest, null);
//        madConfirmacion.setView(viewInflated);
//        final EditText editTextConfirmPss = viewInflated.findViewById(R.id.editTextConfirmPss);
//        madConfirmacion.setPositiveButton("Aceptar", (dialog, which) -> {
//            String password = editTextConfirmPss.getText().toString().trim();
//            if (!password.equals(AppConstants.CODIGO_ACTUALIZARSEGMENTOS))
//                Toast.makeText(requireActivity(), "Contraseña incorrecta ",
//                        Toast.LENGTH_SHORT).show();
//            else
//                getSegmentosActualizados();
//        });
//        madConfirmacion.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
//        madConfirmacion.show();
//    }

    private void getSegmentosBackup() {
        notifier = new ProcessNotifier(getActivity());
        notifier.setTitle("Consulta de servidor");
        notifier.setText("Verificando Token segmentos respaldo...");
        notifier.inflate();

        //Si es 0, desvuelve todos los segmentos en la BD del servidor.
        homeViewModel.getSegmentosBackup().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource.status.equals(Status.SUCCESS)) {
                if (listResource.data != null) {
                    if (listResource.data.size() == 0) {
                        Toast.makeText(requireActivity(), "No hay segmentos por recuperar.",
                                Toast.LENGTH_SHORT).show();
                        notifier.deInflate();
                    } else {
                        Toast.makeText(requireActivity(), "Muestra Descargados: " +
                                Objects.requireNonNull(listResource.data).size(), Toast.LENGTH_SHORT).show();
                        String id;
                        Log.i(TAG, "Entro");
                        id = listResource.data.get(0).getMuestraId();
                        getCuestionariosBackup(id);
                    }
                } else {
                    Toast.makeText(requireActivity(), "Error en la descarga. Los datos son nulos. "
                            + listResource.status, Toast.LENGTH_SHORT).show();
                }
            }

            if (listResource.status.equals(Status.ERROR)) {
                Utilidad.showMessageDialog("Descarga de Muestra",
                        "Error al descargar los segmentos. Verifique su " +
                                "conexión o consulte con su supervisor. " + listResource.message,
                        true,
                        requireActivity(),
                        R.raw.error_sign);
                notifier.deInflate();
                notifier.dismiss();
            }

            if (listResource.status.equals(Status.LOADING)) {
                Log.i(TAG, "Entro");
            }
        });
    }

//    private void getSegmentosActualizados() {
//        notifier = new ProcessNotifier(getActivity());
//        notifier.setTitle("Consulta de servidor");
//        notifier.setText("Verificando Token segmentos respaldo...");
//        notifier.inflate();
//
//        //Si es 0, desvuelve todos los segmentos en la BD del servidor.
//        homeViewModel.getSegmentosDetalleActualizado().observe(getViewLifecycleOwner(), listResource -> {
//            if (listResource.status.equals(Status.SUCCESS)) {
//                if (listResource.data != null) {
//                    if (listResource.data.size() == 0) {
//                        Toast.makeText(requireActivity(), "No hay segmentos por actualizar.",
//                                Toast.LENGTH_SHORT).show();
//                        notifier.deInflate();
//                    } else {
//                        Toast.makeText(requireActivity(), "Muestra Actualizados: " +
//                                Objects.requireNonNull(listResource.data).size(), Toast.LENGTH_SHORT).show();
//                        notifier.deInflate();
//                    }
//                } else {
//                    Toast.makeText(requireActivity(), "Error en la descarga. Los datos son nulos. "
//                            + listResource.status, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            if (listResource.status.equals(Status.ERROR)) {
//                Utilidad.showMessageDialog("Descarga de Muestra",
//                        "Error al actualizar los segmentos. Verifique su " +
//                                "conexión o consulte con su supervisor. " + listResource.message,
//                        true,
//                        requireActivity(),
//                        R.raw.error_sign);
//                notifier.deInflate();
//                notifier.dismiss();
//            }
//
//            if (listResource.status.equals(Status.LOADING)) {
//                Log.i(TAG, "Entro");
//            }
//        });
//    }

    private void getOtrasEstructurasBackup(String id) {
        try {
            homeViewModel.getOtrasEstructurasBackup(id).observe(getViewLifecycleOwner(), listResource -> {
                if (listResource.status.equals(Status.SUCCESS)) {
                    if (listResource.data != null) {
                        if (listResource.data.size() == 0)
                            Toast.makeText(requireActivity(), "No hay registros de Otras Estructuras por recuperar.",
                                    Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(requireActivity(), "Otras Estructuras Descargados: " +
                                    Objects.requireNonNull(listResource.data).size(), Toast.LENGTH_SHORT).show();
                        }
                        homeViewModel.getSegmentosBackup().removeObservers(getViewLifecycleOwner());
                        homeViewModel.getCuestionariosBackup().removeObservers(getViewLifecycleOwner());
                        homeViewModel.getOtrasEstructurasBackup(id).removeObservers(getViewLifecycleOwner());
                        notifier.deInflate();
                        notifier.dismiss();
                        Utilidad.showMessageDialog("Descarga completa", "", false,
                                requireActivity(), R.raw.ok_sign);
                    }
                }

                if (listResource.status.equals(Status.ERROR)) {
                    SimpleDateFormat sdf =
                            new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                    Date date = new Date();
                    String fecha = sdf.format(date);
                    homeViewModel.setErrorsLog(id, fecha, listResource.message);

                    Utilidad.showMessageDialog("Descarga Otras Estructuras",
                            "Error al descargar la subzona. Verifique su " +
                                    "conexión o consulte con su supervisor.",
                            true,
                            requireActivity(), R.raw.error_sign);
                    notifier.deInflate();
                    notifier.dismiss();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "getOtrasEstructurasBackup: ");
        }
    }

    private void getCuestionariosBackup(String id) {
        homeViewModel.getCuestionariosBackup().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource.status.equals(Status.SUCCESS)) {
                if (listResource.data != null) {
                    if (listResource.data.size() == 0) {
                        Toast.makeText(requireActivity(), "No hay Cuestionarios que recuperar. ",
                                Toast.LENGTH_SHORT).show();
                        notifier.deInflate();
                    } else {
                        Toast.makeText(requireActivity(), "Cuestionarios Descargados: " +
                                Objects.requireNonNull(listResource.data).size(), Toast.LENGTH_SHORT).show();
                        getOtrasEstructurasBackup(id);
                    }
                }
            }

            if (listResource.status.equals(Status.ERROR)) {
                SimpleDateFormat sdf =
                        new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                Date date = new Date();
                String fecha = sdf.format(date);
                homeViewModel.setErrorsLog(id, fecha, listResource.message);

                Utilidad.showMessageDialog("Descarga de Cuestionarios",
                        "Error al descargar los cuestionarios. Verifique su " +
                                "conexión o consulte con su supervisor. " + listResource.message,
                        true,
                        requireActivity(),
                        R.raw.error_sign);
                notifier.deInflate();
            }

            if (listResource.status.equals(Status.LOADING)) {
                Log.i(TAG, "Entro");
            }
        });
    }

    private void showListSubzonasMapas() {
        if (muestraSubzonaList.size() > 0) {
            CharSequence[] subZonasArray = new CharSequence[muestraSubzonaList.size()];
            ProcessNotifier processNotifier = new ProcessNotifier(requireActivity());

            for (int x = 0; x < muestraSubzonaList.size(); x++) {
                subZonasArray[x] = muestraSubzonaList.get(x).getPaR02_ID();
            }

            AlertDialog.Builder adSubzonas =
                    new AlertDialog.Builder(requireActivity());
            adSubzonas.setTitle("Seleccionar Subzona");
            adSubzonas.setSingleChoiceItems(subZonasArray, -1, null);
//        materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
            adSubzonas.setPositiveButton("OK", (dialog, which) -> {
                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                if (selectedPosition >= 0) {
                    homeViewModel.getMapsByRegionZonaSubzona(requireActivity(), processNotifier,
                            muestraSubzonaList.get(selectedPosition).getPaR01_ID(),
                            muestraSubzonaList.get(selectedPosition).getPaR01_DESC(),
                            muestraSubzonaList.get(selectedPosition).getPaR02_ID());
                } else
                    Toast.makeText(requireActivity(), "Debe elegir una subzona.", Toast.LENGTH_SHORT).show();
            });
            adSubzonas.setNegativeButton("Cancelar", null);
            adSubzonas.show();
        } else Toast.makeText(requireActivity(), "No hay segmentos.", Toast.LENGTH_SHORT).show();
    }

    private void descargarAsignacionesFaltantes() {
        notifier = new ProcessNotifier(getActivity());
        notifier.setTitle("Consulta de servidor");
        notifier.setText("Verificando Token segmentos respaldo...");
        notifier.inflate();
        //Si es 1, desvuelve todos los segmentos en la BD del servidor.
        homeViewModel.getSegmentosNuevos(muestraList).observe(getViewLifecycleOwner(), listResource -> {
            if (listResource.status.equals(Status.SUCCESS)) {
                if (listResource.data != null) {
                    if (listResource.data.size() == 0)
                        Toast.makeText(requireActivity(), "No hay segmentos por descargar.",
                                Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(requireActivity(), "Muestra Descargados: " +
                                Objects.requireNonNull(listResource.data).size(), Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(requireActivity(), "Muestra Descargados: " +
                            Objects.requireNonNull(listResource.data).size(), Toast.LENGTH_SHORT).show();
                    Utilidad.showMessageDialog("Descarga completa", "", false,
                            requireActivity(), R.raw.ok_sign);
                } else
                    Toast.makeText(requireActivity(), "Error de desarga.", Toast.LENGTH_SHORT).show();
                notifier.deInflate();
            }

            if (listResource.status.equals(Status.ERROR)) {
                Utilidad.showMessageDialog("Descarga de Muestra",
                        "Error al descargar los segmentos. Verifique su " +
                                "conexión o consulte con su supervisor. " + listResource.message,
                        true,
                        requireActivity(),
                        R.raw.error_sign);
                notifier.deInflate();
            }
        });
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

    @Override
    public void onInicioCardListener(int position) {
        if (data.get(position).equals(getString(R.string.title_surveys))) {
            SurveyFragment nextFrag = new SurveyFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, nextFrag, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
        } else if (data.get(position).equals(getString(R.string.title_sendData))) {
            EnvioDeDatosFragment nextFrag = new EnvioDeDatosFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, nextFrag, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
        } else if (data.get(position).equals(getString(R.string.title_network))) {
            if (verificarRed()) {
                String[] menu_InicioArray = requireActivity().getResources().getStringArray(R.array.menu_inicio);

                AlertDialog.Builder builderDescarga = new AlertDialog.Builder(requireActivity());
                builderDescarga.setTitle("Descarga de datos del servidor");
                CharSequence[] opcionesDescarga = {menu_InicioArray[0], menu_InicioArray[1],
                        menu_InicioArray[2], menu_InicioArray[3], menu_InicioArray[4]};
                builderDescarga.setSingleChoiceItems(opcionesDescarga, 0, null);

                builderDescarga.setPositiveButton("Aceptar", (dialog, which) -> {
                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    if (opcionesDescarga[selectedPosition] == menu_InicioArray[0]) {
                        descargarAsignacionesFaltantes();//Descargar Asignaciones
                    } else if (opcionesDescarga[selectedPosition] == menu_InicioArray[1]) {
                        showListSubzonasMapas(); //Mapas
                    } else if (opcionesDescarga[selectedPosition] == menu_InicioArray[2]) {
                        mostrarConfirmacionRecuperacionDialog();//Recuperacion de Cuestionarios
                    } else if (opcionesDescarga[selectedPosition] == menu_InicioArray[3]) {
                        descargarInconsistencias();//Inconsistencias
//                    } else if (opcionesDescarga[selectedPosition] == menu_InicioArray[4]) {
//                        mostrarActualizarEtiquetaSegmento();//Actualizar etiqueta de segmento
//                    } else {
                        dialog.dismiss();
                    }
                });
                builderDescarga.setNegativeButton("Cancelar", null);
                AlertDialog alertDialog = builderDescarga.create();
                alertDialog.show();
            } else {
                MaterialAlertDialogBuilder materialAlertDialogBuilder =
                        new MaterialAlertDialogBuilder(requireActivity());
                materialAlertDialogBuilder.setTitle("Descarga de subzona.");
                materialAlertDialogBuilder.setMessage("Debe verificar su conexiòn de red.");
                materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
                materialAlertDialogBuilder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                materialAlertDialogBuilder.show();
            }
        } else if (data.get(position).equals(getString(R.string.title_backup))) {
            respaldoPath = getPathRespaldo();
            String[] menu_InicioArray = requireActivity().getResources().getStringArray(R.array.menu_respaldo);

            AlertDialog.Builder builderDescarga = new AlertDialog.Builder(requireActivity());
            builderDescarga.setTitle("Respaldo de información");
            CharSequence[] opcionesDescarga = {menu_InicioArray[0], menu_InicioArray[1]};
            builderDescarga.setSingleChoiceItems(opcionesDescarga, 0, null);

            builderDescarga.setPositiveButton("Aceptar", (dialog, which) -> {
                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                if (opcionesDescarga[selectedPosition] == menu_InicioArray[0]) {
                    crearRespaldo(requireActivity(), getPathRespaldo());
                } else if (opcionesDescarga[selectedPosition] == menu_InicioArray[1]) {
                    crearRespaldoOTG(requireActivity(), activityResultLauncher);
                } else {
                    dialog.dismiss();
                }
            });
            builderDescarga.setNegativeButton("Cancelar", null);
            AlertDialog alertDialog = builderDescarga.create();
            alertDialog.show();
        }
    }

    public void crearRespaldo(Activity activity, String respaldoPath) {
        try {
            sdWorker = new SdWorker(activity);
            new Utilidad(new ProcessNotifier(activity)).respaldoInformacion(respaldoPath,
                    activity);
        } catch (Exception e) {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void crearRespaldoOTG(Activity activity,
                                 ActivityResultLauncher<Intent> activityResultLauncher) {
        try {
            sdWorker = new SdWorker(activity);
            new Utilidad(new ProcessNotifier(activity)).respaldoInformacionOTG(
                    sdWorker, activity, activityResultLauncher);
        } catch (Exception e) {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
