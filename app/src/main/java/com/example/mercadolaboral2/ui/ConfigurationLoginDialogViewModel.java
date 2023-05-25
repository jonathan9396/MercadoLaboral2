package com.example.mercadolaboral2.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mercadolaboral2.data.CensoRepository;
import com.example.mercadolaboral2.data.local.dbEntities.LogErrors;
import com.example.mercadolaboral2.data.local.dbEntities.Muestra;
import com.example.mercadolaboral2.utils.Resource;

import java.util.List;

public class ConfigurationLoginDialogViewModel extends ViewModel {
//    private static final String TAG = "ConfigurationLoginDialo";
    private final CensoRepository censoRepository;


    public ConfigurationLoginDialogViewModel() {
        censoRepository = CensoRepository.getInstance();
    }

    public LiveData<Resource<List<Muestra>>> getSegmentos() {
        return censoRepository.getSegmentos();
    }

    public LiveData<List<LogErrors>> getLogErrors() {
        return censoRepository.getLogErrors();
    }

/*    public void deleteAll() {
        censoRepository.eliminarDataBase();
        Log.i(TAG, "Delete: Se eliminó la base de datos.");
    }*/
}
