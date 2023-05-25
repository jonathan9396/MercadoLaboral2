package com.example.mercadolaboral2.ui.menu2_survey.maps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mercadolaboral2.R;

import com.example.mercadolaboral2.data.local.constants.AppConstants;

import com.example.mercadolaboral2.data.local.constants.SharedPreferencesManager;
import com.example.mercadolaboral2.data.local.dbEntities.Muestra;

import com.example.mercadolaboral2.utils.Utilidad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapFragment extends Fragment implements MapsAdapter.OnMapsSelectListener {

    private static final String TAG = "MapFragment";
    ActivityResultLauncher<Intent> activityResultLauncher;
    private MapsViewModel mapsViewModel;
    private MapsAdapter mapsAdapter;
    private ArrayAdapter<String> arrayAdapterSubZonas;
    private List<String> subZonasListSpinner;
    private List<Muestra> muestraMapsList;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mapsViewModel = new ViewModelProvider(this).get(MapsViewModel.class);
        subZonasListSpinner = new ArrayList<>();
        muestraMapsList = new ArrayList<>();
        fragmentManager = requireActivity().getSupportFragmentManager();

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    Log.i(TAG, "onActivityResult: a");
                    Log.i(TAG, "onActivityResult: ");
                }
        );

        Spinner spinner = view.findViewById(R.id.spinnerSubZonasMaps);

        arrayAdapterSubZonas = new ArrayAdapter<>(
                requireActivity(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                subZonasListSpinner
        );

        mapsViewModel.getAllSubZonas().observe(getViewLifecycleOwner(), segmentos -> {
            subZonasListSpinner.clear();
            for (Muestra muestra1 : segmentos) {
                subZonasListSpinner.add("Subzona " + muestra1.getPaR02_ID());
            }
            arrayAdapterSubZonas.notifyDataSetChanged();
        });

        arrayAdapterSubZonas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapterSubZonas);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.rvMapas);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(),
                LinearLayoutManager.HORIZONTAL));
        mapsAdapter = new MapsAdapter(muestraMapsList, this);
        recyclerView.setAdapter(mapsAdapter);

//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String subZonaSelect = subZonasListSpinner.get(position).substring(8);
//                actualizarSegmentos(subZonaSelect);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Log.i(TAG, "onNothingSelected: ");
//            }
//        });

        return view;
    }

//    private void actualizarSegmentos(String subZonaSelect) {
//        mapsViewModel.getSegmentosSelected(
//                        subZonaSelect, SharedPreferencesManager.getSomeIntValue(AppConstants.PREF_ID))
//                .observe(getViewLifecycleOwner(), segmentos -> {
//                    if (segmentos.size() != 0) {
//                        muestraMapsList = segmentos;
//                        mapsAdapter.setData(muestraMapsList);
//                    }
//                    mapsViewModel.getSegmentosSelected(subZonaSelect,
//                                    SharedPreferencesManager.getSomeIntValue(AppConstants.PREF_ID))
//                            .removeObservers(getViewLifecycleOwner());
//                });
//    }

    @Override
    public void onMapsSelectListener(int position) {
//        abrirMapa("Avenza Maps", muestraMapsList.get(position));

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Mapas");

        CharSequence[] myList = {"Avenza Maps", "Etiqueta"};
//                boolean[] opcion = new boolean[myList.length];

        builder.setSingleChoiceItems(myList, 0, null);
//                builder.setMultiChoiceItems(myList, opcion, (dialog, which, isChecked) -> opcion[which] = isChecked);

        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
            if (myList[selectedPosition] == "Avenza Maps") {
                abrirMapa(muestraMapsList.get(position));
            } else if (myList[selectedPosition] == "Etiqueta") {
                mostrarEtiqueta(muestraMapsList.get(position));
            } else {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void mostrarEtiqueta(Muestra muestra) {
        DialogFragment newFragment = new EtiquetaDialog(requireActivity(),
                muestra);
        newFragment.show(fragmentManager, "ViviendaHogarDialogFragment");
    }

    private void abrirMapa(Muestra segmento) {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String inecMovilDirectory = directory + "/InecMovil";
//        String mapaPath = inecMovilDirectory + "/MAPAS/";// + "
        File mapaDirPathFile = new File(inecMovilDirectory + "/MAPAS");//030111-0072-00.tif";
        try {
            if (mapaDirPathFile.exists()) {
                File mapPackageFile;
                if (SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO)
                        .startsWith("E00")) {
                    mapPackageFile = new File(mapaDirPathFile + "/dirmc_"
                            + segmento.getPaR02_ID());//+ "/"
                } else {
                    mapPackageFile = new File(mapaDirPathFile + "/dirm_" + segmento.getPaR01_ID()
                            + segmento.getPaR01_DESC()
                            + segmento.getPaR02_ID());//+ "/"
                }
                if (mapPackageFile.exists()) {
                    File mapFile = new File(mapPackageFile + "/" + segmento.getPaR02_DESC()
                            + segmento.getPaR03_ID()
                            + segmento.getPaR03_DESC()
                            + "-" + segmento.getPaR06_ID()
                            + "-" + segmento.getLlave() + ".tiff");

                    File mapFile2 = new File(mapPackageFile + "/" + segmento.getPaR02_DESC()
                            + segmento.getPaR03_ID()
                            + segmento.getPaR03_DESC()
                            + "-" + segmento.getPaR06_ID()
                            + "-" + segmento.getLlave() + ".tif");//"/dirm_06050011/060701-0074-00.tif");
                    if (mapFile.exists()) {
                        abrirAvenza(mapFile);
                    } else if (mapFile2.exists())
                        abrirAvenza(mapFile2);
                    else {
                        if (Objects.requireNonNull(mapPackageFile.listFiles()).length > 0) {
                            List<File> mapas = new ArrayList<>();
                            for (int x = 0; x < Objects.requireNonNull(mapPackageFile.listFiles()).length; x++) {
                                if (Objects.requireNonNull(mapPackageFile.listFiles())[x].getName().substring(0, 14).
                                        contains(mapFile.getName().substring(0, 14))) {
                                    if (Objects.requireNonNull(mapPackageFile.listFiles())[x].toString().endsWith(".tif")
                                            || Objects.requireNonNull(mapPackageFile.listFiles())[x].toString().endsWith(".tiff"))
                                        mapas.add(Objects.requireNonNull(mapPackageFile.listFiles())[x]);
                                }
                            }
                            if (mapas.size() > 0)
                                mostrarADMapas(mapas);
                            else {
                                Utilidad.showMessageDialog("Mapas", "Error al abrir el mapa seleccionado.",
                                        true, requireActivity(), R.raw.error_sign);
                            }
                        } else {
                            Utilidad.showMessageDialog("Mapas", "Error al abrir el mapa seleccionado.",
                                    true, requireActivity(), R.raw.error_sign);
                        }
                    }
                } else
                    Utilidad.showMessageDialog("Mapas", "Error al abrir el mapa seleccionado.",
                            true, getActivity(), R.raw.error_sign);
            } else {
                Utilidad.showMessageDialog("Mapas", "Error al abrir el mapa seleccionado.",
                        true, requireActivity(), R.raw.error_sign);
            }
        } catch (Exception e) {
            Log.e(TAG, "onMapsSelectListener: " + e.getMessage());
        }
    }

    private void mostrarADMapas(List<File> mapas) {
        CharSequence[] mapasNombres = new CharSequence[mapas.size()];

        for (int x = 0; x < mapas.size(); x++) {
            mapasNombres[x] = mapas.get(x).getName();
        }
        AlertDialog.Builder builderDescarga = new AlertDialog.Builder(requireActivity());
        builderDescarga.setTitle("Selección de mapa");
        builderDescarga.setSingleChoiceItems(mapasNombres, 0, null);

        builderDescarga.setPositiveButton("Aceptar", (dialog, which) -> {
            int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
            abrirAvenza(mapas.get(selectedPosition));
        });
        builderDescarga.setNegativeButton("Cancelar", null);
        AlertDialog alertDialog = builderDescarga.create();
        alertDialog.show();
    }

    private void abrirAvenza(File mapFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(requireActivity(),
                "gov.census.cspro.fileaccess.fileprovider", mapFile);
//        if (tipo.equals("Avenza Maps")) {
        //                File mapFile = new File(mapaDirPathFile + segmento.get(0).getId());
        String mime = requireActivity().getContentResolver().getType(uri);
        intent.setDataAndType(uri, mime);
//        } else {
//            //                File mapFile = new File(mapaDirPathFile + segmento.get(0).getId());
//            //                    String mime = requireActivity().getContentResolver().getType(uri);
//            intent.setDataAndType(uri, "image/*");
//        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activityResultLauncher.launch(intent);
    }
}
