package com.example.mercadolaboral2.ui.menu2_survey.capture.cuestionariosVivienda.viviendahogar;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.mercadolaboral2.data.CensoRepository;

import com.example.mercadolaboral2.data.local.dbEntities.Cuestionarios;

import com.example.mercadolaboral2.data.local.dbEntities.EntrevistaBase;
import com.example.mercadolaboral2.data.local.dbEntities.Muestra;

import com.example.mercadolaboral2.utils.ProcessNotifier;

import com.example.mercadolaboral2.utils.Resource;

import java.util.List;

public class ViviendaHogarViewModel extends ViewModel {
    private static final String TAG = "CaptureViewModel";
    //    private LiveData<List<Muestra>> segmentosList;
    private final CensoRepository censoRepository;

    public ViviendaHogarViewModel() {
        censoRepository = new CensoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<List<Cuestionarios>> getCuestionariosBySegmento(String segmentoID) {
        return censoRepository.getCuestionariosBySegmento(segmentoID);
    }

/*    public LiveData<List<Cuestionarios>> getCuestionariosBySegmento2(String segmentoID) {
        return censoRepository.getCuestionariosBySegmento(segmentoID);
    }*/

    public LiveData<List<Cuestionarios>> getCuestionariosBySegmentoVivienda(String id) {
        return censoRepository.getCuestionariosBySegmentoVivienda(id);
    }

//    public LiveData<Cuestionarios> getCodigoECensoById(String llave) {
//        return censoRepository.getCodigoECensoById(llave);
//    }

//    public LiveData<List<Cuestionarios>> getCuestionariosByVivienda(String viviendaID) {
//        return censoRepository.getCuestionariosByVivienda(viviendaID);
//    }

    public void addVivienda(EntrevistaBase nuevaVivienda) {
        censoRepository.addVivienda(nuevaVivienda);
    }

    public void addCuestionarioDatosDat(Cuestionarios cuestionarioSelected) {
        censoRepository.addCuestionarioDatosDat(cuestionarioSelected);
    }

    public void sendCuestionarioCreate(ProcessNotifier processNotifier, Cuestionarios cuestionarioSelected,
                                       Muestra muestra) {
        censoRepository.enviarCuestionarioCreate(processNotifier, cuestionarioSelected, muestra);
    }

    public void sendCuestionarioUpdate(ProcessNotifier processNotifier, Cuestionarios cuestionarioSelected,
                                       Muestra muestra, String llave) {
        censoRepository.enviarCuestionarioUpdate(processNotifier, cuestionarioSelected, muestra, llave);
    }

    public void updateErrorHogar(String s, String l) {
        censoRepository.actualizarErrorHogar(s, l);
    }

    public void correctErrorHogar(String s, String l) {
        censoRepository.correctErrorHogar(s, l);
    }


    public void deleteCuestionario(String modo, ProcessNotifier processNotifier, Muestra muestra,
                                   Cuestionarios cuestionarioSelected, Activity activity) {
        censoRepository.eliminarCuestionario(modo, processNotifier, muestra, cuestionarioSelected,
                activity);
    }

/*    public void actualizarCodigoCenso(Cuestionarios codigoCenso) {
        censoRepository.actualizarCodigoCenso(codigoCenso);
    }*/

/*    public LiveData<> getCodigoECensoCall(Cuestionarios cuestionarios) {
        return censoRepository.getCodigoECensoCall(cuestionarios);
    }*/

//    public LiveData<Resource<Cuestionarios>> getCodigoECensoCall(Cuestionarios cuestionarios) {
//        return censoRepository.getCodigoECensoCall(cuestionarios);
//    }

    public void updateEstadoSegmento(String id) {
        censoRepository.updateEstadoSegmento(id);
    }
}
