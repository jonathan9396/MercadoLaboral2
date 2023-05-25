package com.example.mercadolaboral2.ui.menu2_survey.maps;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.mercadolaboral2.data.CensoRepository;

import com.example.mercadolaboral2.data.local.dbEntities.Muestra;

import java.util.List;

public class MapsViewModel extends ViewModel {
    private static final String TAG = "CaptureViewModel";
    private final CensoRepository censoRepository;

    public MapsViewModel() {
        censoRepository = new CensoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<List<Muestra>> getAllSubZonas() {
        return censoRepository.getAllSubZonas();
    }

//    public LiveData<List<Muestra>> getSegmentosSelected(String subZonaSelect, int segmentoID) {
//        return censoRepository.getSegmentosSelected(subZonaSelect, segmentoID);
//    }
}
