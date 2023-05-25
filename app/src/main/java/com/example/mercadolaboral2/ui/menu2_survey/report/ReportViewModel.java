package com.example.mercadolaboral2.ui.menu2_survey.report;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.mercadolaboral2.data.CensoRepository;

import com.example.mercadolaboral2.data.local.dbEntities.Cuestionarios;

import com.example.mercadolaboral2.data.local.dbEntities.Muestra;

import java.util.List;

public class ReportViewModel extends ViewModel {
    private final CensoRepository censoRepository;
    private static final String TAG = "CaptureViewModel";

    public ReportViewModel() {
        censoRepository = new CensoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<List<Muestra>> getAllSubZonas() {
        return censoRepository.getAllSubZonas();
    }

 /*   public LiveData<List<Muestra>> getSegmentosSelected(String subZonaSelect) {
        return censoRepository.getSegmentosSelected(subZonaSelect);
    }*/

    public LiveData<List<Muestra>> getSegmentosSelectedGroup(String subZonaSelect) {
        return censoRepository.getSegmentosSelectedGroup(subZonaSelect);
    }

    public LiveData<List<Cuestionarios>> getCuestionarios(String subZonaSelect) {
        return censoRepository.getCuestionariosBySegmento(subZonaSelect);
    }

/*    public LiveData<List<Cuestionarios>> getCuestionariosTODOS() {
        return censoRepository.getAllCuestionarios();
    }*/

    public LiveData<List<Cuestionarios>> getAllCuestionariosByZona(String subzona) {
        return censoRepository.getAllCuestionariosByZona(subzona);
    }
}
