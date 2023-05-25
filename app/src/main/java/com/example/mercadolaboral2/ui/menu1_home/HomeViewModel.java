package com.example.mercadolaboral2.ui.menu1_home;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.mercadolaboral2.data.CensoRepository;

import com.example.mercadolaboral2.data.local.dbEntities.Cuestionarios;

import com.example.mercadolaboral2.data.local.dbEntities.Muestra;
import com.example.mercadolaboral2.data.local.dbEntities.OtrasEstructuras;

import com.example.mercadolaboral2.utils.ProcessNotifier;

import com.example.mercadolaboral2.utils.Resource;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private static final String TAG = "CaptureViewModel";
    private final CensoRepository censoRepository;
    private LiveData<String> estado;

    public HomeViewModel() {
        censoRepository = new CensoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<Resource<List<Muestra>>> getSegmentosNuevos(List<Muestra> muestraList) {
        return censoRepository.getSegmentosNuevos(muestraList);
    }

    public void getMapsByRegionZonaSubzona(FragmentActivity fragmentActivity,
                                           ProcessNotifier processNotifier, String region, String zona,
                                           String subzona) {
        censoRepository.getMapsByRegionZonaSubzona(fragmentActivity, processNotifier, region, zona, subzona);
    }

    public LiveData<String> getEstado() {
        return estado;
    }

    public void setEstado(LiveData<String> estado) {
        this.estado = estado;
    }

//    public LiveData<List<Muestra>> getSegmentosMaps() {
//        return censoRepository.getSegmentosMaps();
//    }

    public LiveData<List<Muestra>> getAllSegmentos() {
        return censoRepository.getAllSegmentos();
    }

    public LiveData<Resource<List<Muestra>>> getSegmentosBackup() {
        return censoRepository.getSegmentosBackup();
    }

//    public LiveData<Resource<List<Muestra>>> getSegmentosDetalleActualizado() {
//        return censoRepository.getSegmentosDetalleActualizado();
//    }

    public LiveData<Resource<List<Cuestionarios>>> getCuestionariosBackup() {
        return censoRepository.getCuestionariosBackup();
    }

    public LiveData<Resource<List<OtrasEstructuras>>> getOtrasEstructurasBackup(String id) {
        return censoRepository.getOtrasEstructurasBackup(id);
    }

    public void setErrorsLog(String id, String date, String llave) {
        censoRepository.guardarError(id, date, llave);
    }

    public void getInconsistencias(FragmentActivity fragmentActivity, ProcessNotifier processNotifier, String usuario) {
        censoRepository.getInconsistencias(fragmentActivity, processNotifier, usuario);
    }
}
