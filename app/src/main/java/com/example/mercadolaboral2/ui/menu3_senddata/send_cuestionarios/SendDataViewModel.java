package com.example.mercadolaboral2.ui.menu3_senddata.send_cuestionarios;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.mercadolaboral2.data.CensoRepository;

import com.example.mercadolaboral2.data.local.dbEntities.Cuestionarios;

import com.example.mercadolaboral2.data.local.dbEntities.CuestionariosPendientes;

import com.example.mercadolaboral2.data.local.dbEntities.Muestra;

import com.example.mercadolaboral2.utils.ProcessNotifier;

import java.util.List;

public class SendDataViewModel extends ViewModel {
    private final CensoRepository censoRepository;
    private static final String TAG = "SendDataViewModel";

    public SendDataViewModel() {
        censoRepository = new CensoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<List<Cuestionarios>> getCuestionariosBySegmentoNotSended(String id) {
        return censoRepository.getCuestionariosBySegmentoNotSended(id);
    }

    public LiveData<List<Muestra>> getAllSubZonas() {
        return censoRepository.getAllSubZonasSend();
    }

//    public LiveData<List<Muestra>> getSegmentosSelected(String subZonaSelect) {
//        return censoRepository.getSegmentosSelected(subZonaSelect);
//    }

    public LiveData<List<CuestionariosPendientes>> getSegmentosCuestionariosNoEnviados(String subzona) {
        return censoRepository.getSegmentosCuestionariosNoEnviados(subzona);
    }

    public LiveData<List<Cuestionarios>> getSegmentosCuestionariosNoEnviados3() {
        return censoRepository.getSegmentosCuestionariosNoEnviados3();
    }

    public LiveData<List<Muestra>> getSegmentosCuestionariosNoEnviados2(String subzona) {
        return censoRepository.getSegmentosCuestionariosNoEnviados2(subzona);
    }

/*
    public void sendCuestionarioCreate(ProcessNotifier processNotifier, Cuestionarios cuestionarioSelected,
                                       Muestra segmentos) {
        censoRepository.enviarCuestionarioCreate(processNotifier, cuestionarioSelected, segmentos);
    }
*/

    public void sendCuestionarioCreate2(ProcessNotifier processNotifier, List<Cuestionarios> cuestionarioSelected,
                                        Muestra muestra) {
        censoRepository.enviarCuestionarioCreate2(processNotifier, cuestionarioSelected, muestra);
    }

/*    public void sendCuestionarioUpdate(ProcessNotifier processNotifier, Cuestionarios cuestionarioSelected,
                                       Muestra segmentos, String llave) {
        censoRepository.enviarCuestionarioUpdate(processNotifier, cuestionarioSelected, segmentos, llave);
    }*/

//    public boolean verificarRefresh(Cuestionarios cuestionarioSelected, FragmentActivity fragmentActivity) {
//        return censoRepository.verificarRefresh(cuestionarioSelected, fragmentActivity);
//    }
}
