package com.example.mercadolaboral2.ui.menu2_survey.capture;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import com.example.mercadolaboral2.data.local.dbEntities.TotCuestionarios;

import com.example.mercadolaboral2.ui.menu2_survey.capture.cuestionariosVivienda.ViviendaFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CaptureFragment extends Fragment
        implements CaptureRecyclerViewAdapter.OnSegmentoCardListener {
    private static final String TAG = "CaptureFragment";
    private FragmentManager fragmentManager;
    private TextView tvlocalizacion;
    private List<Muestra> muestraList;
    //    private List<Cuestionarios> cuestionariosList;
    private List<TotCuestionarios> totCuestionariosList;
    private CaptureViewModel captureViewModel;
    private CaptureRecyclerViewAdapter captureRecyclerViewAdapter;
    private List<String> subZonasListSpinner;

    private ArrayAdapter<String> arrayAdapter;
    private boolean flagInitFragment = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_capture_list, container, false);
        captureViewModel = new ViewModelProvider(this).get(CaptureViewModel.class);
        fragmentManager = requireActivity().getSupportFragmentManager();
        //Practica
        subZonasListSpinner = new ArrayList<>();


//        cuestionariosList = new ArrayList<>();
        totCuestionariosList = new ArrayList<>();

        tvlocalizacion = view.findViewById(R.id.tvlocalizacion);

        Spinner spinnerSubzona = view.findViewById(R.id.spinnerSubzonaCuestionario);
        //prueba
        arrayAdapter = new ArrayAdapter<>(
                requireActivity(),
                androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item,
                subZonasListSpinner
        );

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubzona.setAdapter(arrayAdapter);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSegmentos);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(),
                LinearLayoutManager.HORIZONTAL));
//        String upm= "00-00-00-00-000";
//        List<Muestra> obj = new ArrayList<>();
//        Muestra muestra = new Muestra(muestraList);
//        obj.add(muestra);
//        captureRecyclerViewAdapter = new CaptureRecyclerViewAdapter(obj,this);
        captureRecyclerViewAdapter = new CaptureRecyclerViewAdapter(muestraList,this);
        recyclerView.setAdapter(captureRecyclerViewAdapter);

          //prueba
//        captureRecyclerViewAdapter = new CaptureRecyclerViewAdapter(muestraList, totCuestionariosList,
//                this);
//        recyclerView.setAdapter(captureRecyclerViewAdapter);

        spinnerSubzona.setOnItemSelectedListener(new OnItemSelectedListener() {
            //prueba
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String subZonaSelect = subZonasListSpinner.get(position).substring(7);
                SharedPreferencesManager.setSomeBooleanValue(AppConstants.ESTADOS_STATUS, true);
                actualizarSegmentos(subZonaSelect);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "onNothingSelected: ");
            }
        });
//        subZonasListSpinner.add("Semana 1");
//        subZonasListSpinner.add("Semana 2");
//        subZonasListSpinner.add("Semana 3");
        arrayAdapter.notifyDataSetChanged();

        actualizarSubZona();
        return view;
    }

    private void actualizarSubZona() {
        captureViewModel.getAllSubZonas().observe(CaptureFragment.this.getViewLifecycleOwner(), segmentos -> {
            subZonasListSpinner.clear();
            if (segmentos != null && segmentos.size() != subZonasListSpinner.size()) {
                for (Muestra muestra1 : segmentos) {
                    //practica
                    subZonasListSpinner.add("Semana " + muestra1.getPaR06_ID());
                }
                arrayAdapter.notifyDataSetChanged();
            }
            captureViewModel.getAllSubZonas().removeObservers(CaptureFragment.this.getViewLifecycleOwner());
        });
    }

    private void actualizarSegmentos(String subZonaSelect) {
        captureViewModel.getSegmentosSelected(subZonaSelect,
                        SharedPreferencesManager.getSomeIntValue(AppConstants.PREF_ID))
                .observe(getViewLifecycleOwner(), segmentos -> {
                    if (segmentos.size() != 0) {
                        flagInitFragment = true;
                        muestraList = segmentos;
                        if (SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO).startsWith("00", 1)) {
                            tvlocalizacion.setVisibility(View.INVISIBLE);
                        } else {
                            //prueba
//                            tvlocalizacion.setText(String.format("Región: %s - Zona: %s",
//                                    muestraList.get(0).getRegionID(), muestraList.get(0).getZonaID()));
                            tvlocalizacion.setText("Región:- Zona:");

                        }
                        if (SharedPreferencesManager.getSomeBooleanValue(AppConstants.ESTADOS_STATUS)) {
                            captureViewModel.actualizarEstados(muestraList,
                                    SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME),
                                    SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_FECHA));
                            SharedPreferencesManager.setSomeBooleanValue(AppConstants.ESTADOS_STATUS, false);
                        }

                        if (Objects.equals(segmentos.get(0).getPaR02_ID(), subZonaSelect)) {
                            captureRecyclerViewAdapter.setData(muestraList, totCuestionariosList);
                            captureViewModel.getSegmentosSelected(
                                            subZonaSelect,
                                            SharedPreferencesManager.getSomeIntValue(AppConstants.PREF_ID))
                                    .removeObservers(getViewLifecycleOwner());
                        }
                    }
                });

        captureViewModel.getCuestionarios().observe(getViewLifecycleOwner(), totCuestionarios -> {
            if (totCuestionarios != null) {
                totCuestionariosList = totCuestionarios;
                captureRecyclerViewAdapter.setData(muestraList, totCuestionariosList);
            }
        });
    }

    @Override
    public void onSegmentoCardClick(int position) {
        if (muestraList.get(position).getEstado().equals("3") || muestraList.get(position).getEstado().equals("4")) {
            Toast.makeText(requireActivity(), "Segmento cerrado.", Toast.LENGTH_SHORT).show();
        } else {
            DialogFragment newFragment = new ViviendaFragment(requireActivity(),
                    muestraList.get(position));
            newFragment.show(fragmentManager, "ViviendaHogarDialogFragment");
        }
    }

    @Override
    public void onSegmentoCardLongClick(int position) {
        MaterialAlertDialogBuilder materialAlertDialogBuilder =
                new MaterialAlertDialogBuilder(requireContext());
        materialAlertDialogBuilder.setTitle("Descripción de segmento");
//        materialAlertDialogBuilder.setMessage(muestraList.get(position).getDetalle());
        materialAlertDialogBuilder.setPositiveButton("OK", (dialog1, which1)
                -> dialog1.dismiss());
        materialAlertDialogBuilder.show();
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible && flagInitFragment) {
            actualizarSubZona();
        }
    }
}
