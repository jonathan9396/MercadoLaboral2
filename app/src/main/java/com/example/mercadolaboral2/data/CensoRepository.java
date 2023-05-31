package com.example.mercadolaboral2.data;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;


import com.example.mercadolaboral2.R;
import com.example.mercadolaboral2.app.MyApp;
import com.example.mercadolaboral2.data.local.CuestionariosDao;
import com.example.mercadolaboral2.data.local.EntrevistaBaseDao;
import com.example.mercadolaboral2.data.local.OtrasEstructurasDao;
import com.example.mercadolaboral2.data.local.SegmentosDao;
import com.example.mercadolaboral2.data.local.constants.AppConstants;
import com.example.mercadolaboral2.data.local.constants.SharedPreferencesManager;
import com.example.mercadolaboral2.data.local.constants.clients.CensoClient;
import com.example.mercadolaboral2.data.local.constants.clients.MapClient;

import com.example.mercadolaboral2.data.local.dbEntities.Cuestionarios;

import com.example.mercadolaboral2.data.local.dbEntities.CuestionariosPendientes;

import com.example.mercadolaboral2.data.local.dbEntities.EntrevistaBase;
import com.example.mercadolaboral2.data.local.dbEntities.LogErrors;

import com.example.mercadolaboral2.data.local.dbEntities.Muestra;
import com.example.mercadolaboral2.data.local.dbEntities.OtrasEstructuras;

import com.example.mercadolaboral2.data.local.dbEntities.TotCuestionarios;

import com.example.mercadolaboral2.data.remote.ApiResponse;

import com.example.mercadolaboral2.data.remote.NetworkBoundResource;

import com.example.mercadolaboral2.data.remote.networkEntities.CodigoAccesoResponse;

import com.example.mercadolaboral2.data.remote.networkEntities.GetInconsistenciasResponse;

import com.example.mercadolaboral2.data.remote.networkEntities.GetRefreshEstado;

import com.example.mercadolaboral2.data.remote.networkEntities.GetSegmentosResponse;

import com.example.mercadolaboral2.data.remote.networkServices.CensoApiService;

import com.example.mercadolaboral2.data.remote.networkServices.CensoMapService;

import com.example.mercadolaboral2.data.repo.CensoDataBase;

import com.example.mercadolaboral2.utils.AppExecutors;

import com.example.mercadolaboral2.utils.ProcessNotifier;

import com.example.mercadolaboral2.utils.RateLimiter;

import com.example.mercadolaboral2.utils.Resource;

import com.example.mercadolaboral2.utils.Utilidad;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CensoRepository {
    private static final String TAG = "CensoRepository";
    private static CensoRepository instance;
    private final CensoApiService censoApiService;
    private final CensoMapService censoApiServiceMap;
    //    private final UsuariosDao usuariosDao;
    private final SegmentosDao segmentosDao;
    private final CuestionariosDao cuestionariosDAO;

    private final EntrevistaBaseDao entrevistaBaseDao;
    private final OtrasEstructurasDao otrasEstructurasDAO;
    private final AppExecutors appExecutors;
    private final RateLimiter<String> repoListRateLimit = new RateLimiter<>(30, TimeUnit.MINUTES);
    private String msgResponse;

    public CensoRepository() {
        appExecutors = AppExecutors.getInstance();
        CensoDataBase directorioRoomDatabase = CensoDataBase.getDataBase(MyApp.getInstance());
//        usuariosDao = directorioRoomDatabase.getUserDAO();
        segmentosDao = directorioRoomDatabase.getSegmentosDAO();
        cuestionariosDAO = directorioRoomDatabase.getCuestionariosDAO();
        otrasEstructurasDAO = directorioRoomDatabase.getOtrasEstructurasDAO();
        entrevistaBaseDao = directorioRoomDatabase.getEntrevistaBaseDao();


        CensoClient censoClient = CensoClient.getInstance();
        MapClient mapClient = MapClient.getInstance();

        censoApiService = censoClient.getCensoApiService();
        censoApiServiceMap = mapClient.getCensoMapService();
    }

    public static CensoRepository getInstance() {
        if (instance == null) {
            instance = new CensoRepository();
        }
        return instance;
    }

    public LiveData<Resource<List<Muestra>>> getSegmentos() {
        return new NetworkBoundResource<List<Muestra>, List<GetSegmentosResponse>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<GetSegmentosResponse> responseSeg) {
                if (responseSeg.size() > 0) {
                    List<Muestra> muestraNuevos = new ArrayList<>();
                    for (int x = 0; x < Objects.requireNonNull(responseSeg).size(); x++) {
                        Muestra segmento = new Muestra(responseSeg.get(x).getMuestraId(),
                                responseSeg.get(x).getPaR01_ID(), responseSeg.get(x).getPaR01_DESC(),
                                responseSeg.get(x).getPaR02_ID(), responseSeg.get(x).getPaR02_DESC(),
                                responseSeg.get(x).getPaR03_ID(), responseSeg.get(x).getPaR03_DESC(),
                                responseSeg.get(x).getPaR04_ID(), responseSeg.get(x).getPaR04_DESC(),
                                responseSeg.get(x).getPaR05_ID(), responseSeg.get(x).getPaR06_ID(),
                                responseSeg.get(x).getLlave(),responseSeg.get(x).getEstado(),
                                responseSeg.get(x).getRevisado(), responseSeg.get(x).getSupervisorId(),
                                responseSeg.get(x).getFechaCreacion(), responseSeg.get(x).getFechaDeHabilitacion(),
                                responseSeg.get(x).getFechaDeModificacion(), responseSeg.get(x).getFechaCierre(),
                                responseSeg.get(x).getFechaUltimaCarga(),responseSeg.get(x).getCompletamenteAsignado(),
                                responseSeg.get(x).getMovil(), responseSeg.get(x).getSupervisor(), responseSeg.get(x).getEntrevistas(), responseSeg.get(x).getEntrevistasBase());
                        muestraNuevos.add(segmento);
                    }
                    segmentosDao.insertSegmentos(muestraNuevos);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Muestra> data) {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
            }

            @NonNull
            @Override
            protected LiveData<List<Muestra>> loadFromDb() {
                return segmentosDao.loadSegmentos();
            }

//            @NonNull
//            @Override
//            protected LiveData<ApiResponse<List<GetSegmentosResponse>>> createCall() {
//                return censoApiService.getSegmentosAsignados(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
//            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<GetSegmentosResponse>>> createCall() {
                return censoApiService.getSegmentosAsignados();
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
            }
        }.asLiveData();
    }

    /**
     * Si es 1, desvuelve solamente los segmentos en estado 1 de la BD del servidor.
     *
     * @return LiveData<Resource < List < Muestra>>>
     */
    public LiveData<Resource<List<Muestra>>> getSegmentosNuevos(List<Muestra> muestraListBD) {
        return new NetworkBoundResource<List<Muestra>, List<GetSegmentosResponse>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull @NotNull List<GetSegmentosResponse> responseSeg) {
                if (responseSeg.size() > 0) {
                    List<Muestra> muestraNuevos = new ArrayList<>();
                    List<Muestra> segmentsListDelete = new ArrayList<>();
                    Muestra segmentoEliminar = null;
                    for (int i = 0; i < muestraListBD.size(); i++) {
                        for (int x = 0; x < responseSeg.size(); x++) {
                            if (muestraListBD.get(i).getMuestraId().equals(responseSeg.get(x).getMuestraId())) {
                                segmentoEliminar = null;
                                break;
                            } else {
                                if (Integer.parseInt(muestraListBD.get(i).getEstado()) <= 1)
                                    segmentoEliminar = muestraListBD.get(i);
                            }
                        }
                        if (segmentoEliminar != null) segmentsListDelete.add(segmentoEliminar);
                    }

                    if (segmentsListDelete.size() > 0)
                        segmentosDao.deleteSegmentos(segmentsListDelete);

                    for (int x = 0; x < Objects.requireNonNull(responseSeg).size(); x++) {
                        int idAsignacionAlterna = 0;
                        Muestra segmento = new Muestra(responseSeg.get(x).getMuestraId(), responseSeg.get(x).getPaR01_ID(), responseSeg.get(x).getPaR01_DESC(), responseSeg.get(x).getPaR02_ID(), responseSeg.get(x).getPaR02_DESC(), responseSeg.get(x).getPaR03_ID(), responseSeg.get(x).getPaR03_DESC(), responseSeg.get(x).getPaR04_ID(), responseSeg.get(x).getPaR04_DESC(), responseSeg.get(x).getPaR05_ID(), responseSeg.get(x).getPaR06_ID(), responseSeg.get(x).getLlave(),responseSeg.get(x).getEstado(),responseSeg.get(x).getRevisado(), responseSeg.get(x).getSupervisorId(), responseSeg.get(x).getFechaCreacion(),responseSeg.get(x).getFechaDeHabilitacion(),responseSeg.get(x).getFechaDeModificacion(), responseSeg.get(x).getFechaCierre(), responseSeg.get(x).getFechaUltimaCarga(),responseSeg.get(x).getCompletamenteAsignado(), responseSeg.get(x).getMovil(), responseSeg.get(x).getSupervisor(), responseSeg.get(x).getEntrevistas(), responseSeg.get(x).getEntrevistasBase());
                        muestraNuevos.add(segmento);
                    }
                    segmentosDao.insertSegmentosAdicionales(muestraNuevos);
                } else {
                    segmentosDao.deleteAllSegmentosEstado1();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Muestra> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Muestra>> loadFromDb() {
                return segmentosDao.loadSegmentosFaltantesBackup();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<GetSegmentosResponse>>> createCall() {
                return censoApiService.getSegmentosAsignadosNuevosFiltro(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME), "1");
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<Cuestionarios>>> getCuestionariosBackup() {
        return new NetworkBoundResource<List<Cuestionarios>, List<Cuestionarios>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull @NotNull List<Cuestionarios> item) {
                for (int x = 0; x < item.size(); x++) {
                    item.get(x).setFlagPrimerEnvio(true);
                }
                cuestionariosDAO.insertCuestionarios(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable @org.jetbrains.annotations.Nullable List<Cuestionarios> data) {
                return data == null || data.isEmpty() ||
                        repoListRateLimit.shouldFetch(
                                SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
            }

            @NonNull
            @NotNull
            @Override
            protected LiveData<List<Cuestionarios>> loadFromDb() {
                return cuestionariosDAO.getAllCuestionarios();
            }

            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<List<Cuestionarios>>> createCall() {
                return censoApiService.getCuestionarios();
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
            }

        }.asLiveData();
    }

    public LiveData<Resource<List<OtrasEstructuras>>> getOtrasEstructurasBackup(String id) {
        return new NetworkBoundResource<List<OtrasEstructuras>, List<OtrasEstructuras>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<OtrasEstructuras> otrasEstructurasList) {
                for (int x = 0; x < otrasEstructurasList.size(); x++) {
                    otrasEstructurasList.get(x).setFlagPrimerEnvio(true);
                }
                otrasEstructurasDAO.insertOtraEstructuraList(otrasEstructurasList);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<OtrasEstructuras> data) {
                return data == null || repoListRateLimit.shouldFetch(id);
            }

            @NonNull
            @Override
            protected LiveData<List<OtrasEstructuras>> loadFromDb() {
                return otrasEstructurasDAO.cargarOtrasEstructuras();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<OtrasEstructuras>>> createCall() {
                return censoApiService.cargarOtraEstructuraRecuperacion(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO));
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset(id);
            }
        }.asLiveData();
    }

    /***
     * Si es 0, desvuelve todos los segmentos en la BD del servidor.
     * @return LiveData<Resource < List < Muestra>>>
     */
    public LiveData<Resource<List<Muestra>>> getSegmentosBackup() {
        return new NetworkBoundResource<List<Muestra>, List<GetSegmentosResponse>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull @NotNull List<GetSegmentosResponse> responseSeg) {
                List<Muestra> muestraNuevos = new ArrayList<>();
                for (int x = 0; x < Objects.requireNonNull(responseSeg).size(); x++) {
                    int idAsignacionAlterna = 0;
                    Muestra muestra = new Muestra(responseSeg.get(x).getMuestraId(),
                            responseSeg.get(x).getPaR01_ID(),
                            responseSeg.get(x).getPaR01_DESC(),
                            responseSeg.get(x).getPaR02_ID(),
                            responseSeg.get(x).getPaR02_DESC(),
                            responseSeg.get(x).getPaR03_ID(),
                            responseSeg.get(x).getPaR03_DESC(),
                            responseSeg.get(x).getPaR04_ID(),
                            responseSeg.get(x).getPaR04_DESC(),
                            responseSeg.get(x).getPaR05_ID(),
                            responseSeg.get(x).getPaR06_ID(),
                            responseSeg.get(x).getLlave(),
                            responseSeg.get(x).getEstado(),
                            responseSeg.get(x).getRevisado(),
                            responseSeg.get(x).getSupervisorId(),
                            responseSeg.get(x).getFechaCreacion(),
                            responseSeg.get(x).getFechaDeHabilitacion(),
                            responseSeg.get(x).getFechaDeModificacion(),
                            responseSeg.get(x).getFechaCierre(),
                            responseSeg.get(x).getFechaUltimaCarga(),
                            responseSeg.get(x).getCompletamenteAsignado(),
                            responseSeg.get(x).getMovil(),
                            responseSeg.get(x).getSupervisor(),
                            responseSeg.get(x).getEntrevistas(),
                            responseSeg.get(x).getEntrevistasBase());
                    muestraNuevos.add(muestra);
                }
                segmentosDao.insertSegmentosAdicionales(muestraNuevos);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Muestra> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Muestra>> loadFromDb() {
                return segmentosDao.loadSegmentosFaltantesBackup();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<GetSegmentosResponse>>> createCall() {
                return censoApiService.getSegmentosAsignadosNuevosFiltro(
                        SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME), "0");
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
            }
        }.asLiveData();
    }

    //--------------------------------PRUEBA-----------------------------------------
//    public LiveData<Resource<List<Muestra>>> getSegmentosDetalleActualizado() {
//        return new NetworkBoundResource<List<Muestra>, List<GetSegmentosResponse>>(appExecutors) {
//            @Override
//            protected void saveCallResult(@NonNull @NotNull List<GetSegmentosResponse> responseSeg) {
////                List<Muestra> segmentosNuevos = new ArrayList<>();
//                for (int x = 0; x < Objects.requireNonNull(responseSeg).size(); x++) {
//                    int idAsignacionAlterna = 0;
//                    if (responseSeg.get(x).getAsignacionAlterna() != null) {
//                        idAsignacionAlterna = responseSeg.get(x).getAsignacionAlterna().getId();
//                    }
//                    segmentosDao.updateDetalleSegmento(responseSeg.get(x).getDetalle(),
//                            responseSeg.get(x).getMuestraId(), responseSeg.get(x).getPaR04_ID(),
//                            responseSeg.get(x).getPaR04_DESC(), responseSeg.get(x).getPaR05_ID(),
//                            responseSeg.get(x).getPaR06_ID(), idAsignacionAlterna);
//                }
//            }
//
//            @Override
//            protected boolean shouldFetch(@Nullable List<Muestra> data) {
//                return true;
//            }
//
//            @NonNull
//            @Override
//            protected LiveData<List<Muestra>> loadFromDb() {
//                return segmentosDao.loadSegmentosFaltantesBackup();
//            }
//
//            @NonNull
//            @Override
//            protected LiveData<ApiResponse<List<GetSegmentosResponse>>> createCall() {
//                return censoApiService.getSegmentosAsignadosNuevosFiltro(
//                        SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME), "0");
//            }
//
//            @Override
//            protected void onFetchFailed() {
//                repoListRateLimit.reset(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
//            }
//        }.asLiveData();
//    }

    public LiveData<List<Muestra>> getAllSubZonas() {
        return segmentosDao.getAllSubZonas();
    }

    public LiveData<List<Muestra>> getAllSegmentos() {
        return segmentosDao.loadSegmentos();
    }

    public LiveData<List<Muestra>> getAllSubZonasSend() {
        return segmentosDao.getAllSubZonasSend();
    }


    public LiveData<List<Muestra>> getSegmentosSelected(String subZonaSelect, int segmentoID) {
        return segmentosDao.getSegmentosSelected(subZonaSelect);
    }

    public LiveData<List<CuestionariosPendientes>> getSegmentosCuestionariosNoEnviados(String subzona) {
        return segmentosDao.getSegmentosCuestionariosNoEnviados(subzona);
    }

    public LiveData<List<CuestionariosPendientes>> getOtrasEstructrurasSubZonaNoEnviados(String subzona) {
        return segmentosDao.getOtrasEstructrurasSubZonaNoEnviados(subzona);
    }

    public LiveData<List<Cuestionarios>> getSegmentosCuestionariosNoEnviados3() {
        return segmentosDao.getSegmentosCuestionariosNoEnviados3();
    }

    public LiveData<List<OtrasEstructuras>> getOtrasEstructurasNoEnviados() {
        return segmentosDao.getOtrasEstructurasNoEnviados();
    }

    public LiveData<List<Muestra>> getSegmentosCuestionariosNoEnviados2(String subzona) {
        return segmentosDao.getSegmentosCuestionariosNoEnviados2(subzona);
    }

    public LiveData<List<Muestra>> getSegmentosPendientesByZona(String subzona) {
        return segmentosDao.getSegmentosPendientesByZona(subzona);
    }

    public LiveData<List<Muestra>> getSegmentosSelectedGroup(String subZonaSelect) {
        return segmentosDao.getSegmentosSelectedGroup(subZonaSelect);
    }

    public LiveData<List<Cuestionarios>> getCuestionariosBySegmentoVivienda(String id) {
        return cuestionariosDAO.getCuestionariosBySegmentoVivienda(id);
    }

//    public LiveData<Cuestionarios> getCodigoECensoById(String id) {
//        return cuestionariosDAO.getCodigoECensoById(id);
//    }

    public LiveData<List<Cuestionarios>> getCuestionariosBySegmentoNotSended(String id) {
        return cuestionariosDAO.getCuestionariosBySegmentoNotSended(id);
    }

    public LiveData<List<OtrasEstructuras>> getOtrasEstructurasBySegmentoNotSended(String id) {
        return otrasEstructurasDAO.getOtrasEstructurasBySegmentoNotSended(id);
    }

    public LiveData<List<Cuestionarios>> getCuestionariosBySegmento(String id) {
        return cuestionariosDAO.getCuestionariosBySegmento(id);
    }

    public LiveData<List<OtrasEstructuras>> getOtrasEstructurasBySegmento(String id) {
        return otrasEstructurasDAO.getOtrasEstructurasBySegmento(id);
    }

    public LiveData<List<TotCuestionarios>> getAllCuestionarios() {
        return cuestionariosDAO.getAllCuestionariosCapture();
    }

    public LiveData<List<Cuestionarios>> getAllCuestionariosByZona(String subzona) {
        return cuestionariosDAO.getAllCuestionariosByZona(subzona);
    }

//    public LiveData<List<Muestra>> getSegmentosMaps() {
//        return segmentosDao.getSegmentosMaps();
//    }

    public LiveData<List<LogErrors>> getLogErrors() {
        return cuestionariosDAO.getLogErrors();
    }

    public void addVivienda(EntrevistaBase nuevaVivienda) {
        appExecutors.diskIO().execute(() -> entrevistaBaseDao.addVivienda(nuevaVivienda));
    }

    public void addOtraEstructura(OtrasEstructuras otrasEstructuras) {
        appExecutors.diskIO().execute(() -> otrasEstructurasDAO.insertOtraEstructura(otrasEstructuras));
    }

    public void saveObservacion(String observacion, String llave, String fechaUpdate) {
        appExecutors.diskIO().execute(() -> otrasEstructurasDAO.saveObservacion(observacion, llave, fechaUpdate));
    }

    public void saveGps(String gps, String llave, String fechaUpdate) {
        appExecutors.diskIO().execute(() -> otrasEstructurasDAO.saveGps(gps, llave, fechaUpdate));
    }

    public void addCuestionarioDatosDat(Cuestionarios cuestionarioSelected) {
        appExecutors.diskIO().execute(() -> cuestionariosDAO.addCuestionarioDatosDat(cuestionarioSelected));
    }

    public void actualizarErrorHogar(String s, String l) {
        appExecutors.diskIO().execute(() -> cuestionariosDAO.updateErrorHogar(s, l));
    }

    public void updateEstadoSegmento(String id) {
        appExecutors.diskIO().execute(() -> segmentosDao.updateEstadoSegmento(id));
    }

    public void correctErrorHogar(String s, String l) {
        appExecutors.diskIO().execute(() -> cuestionariosDAO.correctErrorHogar(s, l));
    }

    /**
     * Elimina cuestionarios en el servidor y después en la bd local Android.
     *
     * @param modo                 - Eliminar solod DB  o Elimnar BD y server
     * @param processNotifier      - Para mostrar las notificaciones de AlertDialog
     * @param muestra            - Para enviar paramentros
     * @param cuestionarioSelected - Cuestionario a eliminar
     * @param activity             - ref
     */
    public void eliminarCuestionario(String modo, ProcessNotifier processNotifier, Muestra muestra, Cuestionarios cuestionarioSelected, Activity activity) {
        if (modo.equals("remoto")) {
            processNotifier.setTitle("Eliminando del servidor");
            processNotifier.setText("Espere...");
            processNotifier.show();
            Call<Void> call = censoApiService.eliminarCuestionarioServer(cuestionarioSelected.getEntrevistaId());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        Utilidad.respaldarCuestionarioConDatos(muestra, cuestionarioSelected);
                        appExecutors.diskIO().execute(() -> cuestionariosDAO.eliminarCuestionarioSeleccionado(cuestionarioSelected));

                    } else if (response.code() == 404) {
                        appExecutors.diskIO().execute(() -> cuestionariosDAO.eliminarCuestionarioSeleccionado(cuestionarioSelected));
                        Toast.makeText(MyApp.getContext(), "Cuestionario eliminado.", Toast.LENGTH_SHORT).show();
                    }
                    processNotifier.deInflate();
                    processNotifier.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    mostrarAlertDialog(processNotifier.getContext(), "Envio", "Error de envio" /*" + t.getCause()*/, 1);
                    SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                    Date date = new Date();
                    String fecha = sdf.format(date);
                    guardarError(cuestionarioSelected.getEntrevistaId(), fecha, t.getMessage());
                    processNotifier.deInflate();
                    processNotifier.dismiss();
                }
            });
        } else {
            Utilidad.showMessageDialog("Cuestionario Eliminado", "", false, activity, R.raw.ok_sign);
            appExecutors.diskIO().execute(() -> cuestionariosDAO.eliminarCuestionarioSeleccionado(cuestionarioSelected));
        }
    }

/*    public void actualizarCodigoCenso(Cuestionarios cuestionarioSelected) {
        appExecutors.diskIO().execute(() -> cuestionariosDAO.actualizarCodigoCenso(cuestionarioSelected));
    }*/

    public void eliminarOtraEstructura(String modo, ProcessNotifier processNotifier, Muestra muestra, OtrasEstructuras otrasEstructurasSeleccionado) {
        if (modo.equals("remoto")) {
            processNotifier.setTitle("Eliminando del servidor");
            processNotifier.setText("Espere...");
            processNotifier.show();
            Call<Void> call = censoApiService.eliminarOtraEstructuraServer(otrasEstructurasSeleccionado.getLlave());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        appExecutors.diskIO().execute(() -> otrasEstructurasDAO.eliminarOtraEstructura(otrasEstructurasSeleccionado));
/*                    } else if (response.code() == 404) {
                        if (response.errorBody() != null)
                            Toast.makeText(MyApp.getContext(), "Error " + response.code() + " " +
                                    response.errorBody(), Toast.LENGTH_SHORT).show();*/
                    } else {
                        Toast.makeText(MyApp.getContext(), "Error " + response.code() + " " + "Otra Estructura no existente.", Toast.LENGTH_SHORT).show();
                    }
                    processNotifier.deInflate();
                    processNotifier.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    mostrarAlertDialog(processNotifier.getContext(), "Envio", "Error de envio" /*" + t.getCause()*/, 1);
                    SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                    Date date = new Date();
                    String fecha = sdf.format(date);
                    guardarError(otrasEstructurasSeleccionado.getLlave(), fecha, t.getMessage());
                    processNotifier.deInflate();
                    processNotifier.dismiss();
                }
            });
        } else {
            appExecutors.diskIO().execute(() -> otrasEstructurasDAO.eliminarOtraEstructura(otrasEstructurasSeleccionado));
        }
    }

    public void guardarError(String llave, String fecha, String errorBody) {
        if (errorBody != null) {
            try {
                LogErrors logErrors = new LogErrors(llave, errorBody, fecha);
                appExecutors.diskIO().execute(() -> cuestionariosDAO.addErrorLogs(logErrors));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void actualizarEnvio(Cuestionarios cuestionarioSelected, Muestra muestra,
                                 boolean flagPrimerEnvio, boolean flagEnvio, String mensaje) {
        try {
            if (mensaje.contains("Llave recibida existente")
                    || mensaje.contains("Los datos proporcionados no concuerdan con la llave")
                    || mensaje.contains("Error en DatosJson")
                    || mensaje.contains("Revise las fechas o algun otro valor que no deberia ser nulo")) {
                cuestionarioSelected.setFlagPrimerEnvio(false);
            } else {
                cuestionarioSelected.setFlagPrimerEnvio(flagPrimerEnvio);
            }

            cuestionarioSelected.setFlagEnvio(flagEnvio);
            muestra.setEstado("2");
            appExecutors.diskIO().execute(() -> cuestionariosDAO.updateCuestionario(cuestionarioSelected));
            appExecutors.diskIO().execute(() -> segmentosDao.actualizarEstado(muestra));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarOtrasEstructurasEnvio(OtrasEstructuras otrasEstructurasSelected, Muestra muestra, boolean flagPrimerEnvio, boolean flagEnvio, String mensaje) {
        try {
            otrasEstructurasSelected.setFlagEnvio(flagEnvio);
            otrasEstructurasSelected.setFlagPrimerEnvio(flagEnvio);
            appExecutors.diskIO().execute(() -> otrasEstructurasDAO.updateOtrasEstructuras(otrasEstructurasSelected));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarEnvioUpdate(Cuestionarios cuestionarioSelected, Muestra muestra, boolean flagPrimerEnvio, String errorMsg) {
        try {
            if (errorMsg.contains("El cuestionario ya fue revisado por el supervisor") || errorMsg.contains("El segmento del cuestionario esta cerrado") || errorMsg.contains("El segmento de este cuestionario no esta asignado a este usuario") || errorMsg.contains("Error en DatosJson"))
                cuestionarioSelected.setFlagEnvio(flagPrimerEnvio);

            cuestionarioSelected.setFlagEnvio(flagPrimerEnvio);
            cuestionarioSelected.setFlagPrimerEnvio(flagPrimerEnvio);
            muestra.setEstado("2");
            appExecutors.diskIO().execute(() -> cuestionariosDAO.updateCuestionario(cuestionarioSelected));
            appExecutors.diskIO().execute(() -> segmentosDao.actualizarEstado(muestra));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarEnvioUpdateOtrasEstructuras(OtrasEstructuras cuestionarioSelected, Muestra muestra, boolean flagPrimerEnvio, String errorMsg) {
        try {
            if (errorMsg.contains("El cuestionario ya fue revisado por el supervisor") || errorMsg.contains("El segmento del cuestionario esta cerrado") || errorMsg.contains("El segmento de este cuestionario no esta asignado a este usuario") || errorMsg.contains("Error en DatosJson"))
                cuestionarioSelected.setFlagEnvio(flagPrimerEnvio);

            cuestionarioSelected.setFlagEnvio(flagPrimerEnvio);
            cuestionarioSelected.setFlagPrimerEnvio(flagPrimerEnvio);
            muestra.setEstado("2");
//            appExecutors.diskIO().execute(() -> cuestionariosDAO.updateCuestionario(cuestionarioSelected));
            appExecutors.diskIO().execute(() -> segmentosDao.actualizarEstado(muestra));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enviarCuestionarioCreate(ProcessNotifier processNotifier, Cuestionarios cuestionarioSelected, Muestra muestra) {
        processNotifier.setTitle("Enviando al servidor");
        processNotifier.setText("Espere...");
        processNotifier.show();
        Call<Void> call = censoApiService.sendCuestionario(cuestionarioSelected);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                try {
                    if (response.isSuccessful()) {
                        actualizarEnvio(cuestionarioSelected, muestra, true, true,
                                msgResponse = "");
                        Toast.makeText(MyApp.getContext(), "Cuestionario enviado: "
                                + cuestionarioSelected.getEntrevistaId(), Toast.LENGTH_SHORT).show();
                    } else {
                        msgResponse = Objects.requireNonNull(response.errorBody()).string();

                        SimpleDateFormat sdf =
                                new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                        Date date = new Date();
                        String fecha = sdf.format(date);
                        guardarError(cuestionarioSelected.getEntrevistaId(), fecha, msgResponse);

                        actualizarEnvio(cuestionarioSelected, muestra, true, false,
                                msgResponse);
                        if (msgResponse.contains("El cuestionario ya fue revisado por el supervisor")) {
                            Toast.makeText(MyApp.getContext(), "El cuestionario ya fue revisado por el supervisor",
                                    Toast.LENGTH_SHORT).show();
                        } else if (!cuestionarioSelected.isFlagPrimerEnvio())
                            enviarCuestionarioUpdate(processNotifier, cuestionarioSelected,
                                    muestra, cuestionarioSelected.getEntrevistaId());
                        if (!msgResponse.isEmpty())
                            Toast.makeText(MyApp.getContext(), "Error envío actualizar cuestionario: "
                                    + msgResponse, Toast.LENGTH_LONG).show();
                    }

                    if (response.code() == 500) {
                        mostrarAlertDialog(processNotifier.getContext(),
                                "Envio",
                                "Error de envio. Error: " + response.code(),
                                1);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }
                processNotifier.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                mostrarAlertDialog(processNotifier.getContext(), "Envio", "Error de envio" /*" + t.getCause()*/, 1);
                SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                Date date = new Date();
                String fecha = sdf.format(date);
                guardarError(cuestionarioSelected.getEntrevistaId(), fecha, t.getMessage());
                actualizarEnvio(cuestionarioSelected, muestra, false, false, msgResponse = "");
                processNotifier.dismiss();
            }
        });
    }

    public void enviarOtrasEstructurasRed(ProcessNotifier processNotifier,
                                          OtrasEstructuras otrasEstructuras, Muestra muestra) {
        processNotifier.setTitle("Enviando al servidor");
        processNotifier.setText("Espere...");
        processNotifier.show();
        Call<Void> call = censoApiService.sendOtraEstructura(otrasEstructuras);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {

                try {
                    if (response.isSuccessful()) {
                        actualizarOtrasEstructurasEnvio(otrasEstructuras, muestra,
                                true, true, msgResponse = "");
                        Toast.makeText(MyApp.getContext(), "Registro enviado: "
                                + otrasEstructuras.getLlave(), Toast.LENGTH_LONG).show();
                    } else {
                        msgResponse = Objects.requireNonNull(response.errorBody()).string();

                        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                        Date date = new Date();
                        String fecha = sdf.format(date);
                        guardarError(otrasEstructuras.getLlave(), fecha, msgResponse);
                        Toast.makeText(MyApp.getContext(), /*"Error en el envío. "*/
                                otrasEstructuras.getLlave() + "\n " + msgResponse, Toast.LENGTH_LONG).show();
                    }
                    processNotifier.dismiss();
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                mostrarAlertDialog(processNotifier.getContext(), "Envio", "Error de envio" /*" + t.getCause()*/, 2);
                SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                Date date = new Date();
                String fecha = sdf.format(date);
                guardarError(otrasEstructuras.getLlave(), fecha, t.getMessage());
                /*actualizarEnvio(otrasEstructuras, muestra, false, false,
                        msgResponse = "");*/
                processNotifier.dismiss();
            }
        });
    }


    public void enviarCuestionarioCreate2(ProcessNotifier processNotifier,
                                          List<Cuestionarios> cuestionarioSelected, Muestra muestra) {
        processNotifier.setTitle("Enviando al servidor");
        processNotifier.setText("Espere...");
        processNotifier.show();
        Cuestionarios cuestionarioEnv = cuestionarioSelected.get(0);
        cuestionarioEnv.setDatos("\uFEFF" + cuestionarioEnv.getDatos());
        Call<Void> callSendCuestionario = censoApiService.sendCuestionario(cuestionarioEnv);
        callSendCuestionario.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
//                String errorMensajeCreate2 = "";
                if (response.isSuccessful()) {
                    actualizarEnvio(cuestionarioEnv, muestra, true, true, msgResponse = "");
                    Toast.makeText(MyApp.getContext(), "Cuestionario enviado: "
                            + cuestionarioEnv.getEntrevistaId(), Toast.LENGTH_SHORT).show();
                    if (cuestionarioSelected.size() > 1)
                        enviarRestantes(processNotifier, cuestionarioSelected, muestra);
                } else {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                        Date date = new Date();
                        String fecha = sdf.format(date);
                        if (response.code() == 400)
                            msgResponse = Objects.requireNonNull(response.errorBody()).string();
                        if (msgResponse != null)
                            guardarError(cuestionarioEnv.getEntrevistaId(), fecha, msgResponse);
                        else msgResponse = "";
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: " + e.getMessage());
                    }

                    actualizarEnvio(cuestionarioEnv, muestra, false, false, msgResponse);

                    if (msgResponse != null && Objects.requireNonNull(msgResponse).contains("Error al enviar cuestionario: el cuestionario ya fue revisado por el supervisor")) {
                        Toast.makeText(MyApp.getContext(), "El cuestionario ya fue revisado por el supervisor",
                                Toast.LENGTH_SHORT).show();
                    } else if (!cuestionarioEnv.isFlagPrimerEnvio())
                        enviarCuestionarioUpdate(processNotifier, cuestionarioEnv, muestra, cuestionarioEnv.getEntrevistaId());
                    enviarRestantes(processNotifier, cuestionarioSelected, muestra);
                }
                processNotifier.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                Date date = new Date();
                String fecha = sdf.format(date);
                guardarError(cuestionarioEnv.getEntrevistaId(), fecha, t.getMessage());
                mostrarAlertDialog(processNotifier.getContext(), "Envio", "Error de envio"/*: " + t.getCause()*/, 1);
                actualizarEnvio(cuestionarioEnv, muestra, false, false, msgResponse = "");
                processNotifier.dismiss();
            }
        });
    }

    public void enviarOtrasEstructurasRed2(ProcessNotifier processNotifier, List<OtrasEstructuras> otrasEstructurasList, Muestra muestra) {
        processNotifier.setTitle("Enviando al servidor");
        processNotifier.setText("Espere...");
        processNotifier.show();

        Call<Void> callSendCuestionario = censoApiService.sendOtraEstructura(otrasEstructurasList.get(0));
        callSendCuestionario.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
//                String errorMensajeCreate2 = "";
                if (response.isSuccessful()) {
                    actualizarOtrasEstructurasEnvio(otrasEstructurasList.get(0), muestra,
                            true, true, msgResponse = "");
                    Toast.makeText(MyApp.getContext(), "Cuestionario enviado: "
                            + otrasEstructurasList.get(0).getLlave(), Toast.LENGTH_SHORT).show();
                    if (otrasEstructurasList.size() > 1)
                        enviarRestantesOtrasEstructuras(processNotifier, otrasEstructurasList, muestra);
                } else {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                        Date date = new Date();
                        String fecha = sdf.format(date);
                        if (response.code() == 400)
                            msgResponse = Objects.requireNonNull(response.errorBody()).string();

                        guardarError(otrasEstructurasList.get(0).getLlave(), fecha, msgResponse);
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: " + e.getMessage());
                    }

                    actualizarOtrasEstructurasEnvio(otrasEstructurasList.get(0), muestra,
                            false, false, msgResponse);

                    if (msgResponse != null) {
                        if (msgResponse.contains("Error al enviar cuestionario: el cuestionario ya " + "fue revisado por el supervisor")) {
                            Toast.makeText(MyApp.getContext(), "El cuestionario ya fue revisado por el supervisor", Toast.LENGTH_SHORT).show();
                        } else if (!otrasEstructurasList.get(0).isFlagPrimerEnvio())
                            enviarOtrasEstructurasUpdate(processNotifier, otrasEstructurasList.get(0), muestra, otrasEstructurasList.get(0).getLlave());
                    }
//                    enviarRestantesOtrasEstructuras(processNotifier, otrasEstructurasList, muestra);
                }
                processNotifier.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                Date date = new Date();
                String fecha = sdf.format(date);
                guardarError(otrasEstructurasList.get(0).getLlave(), fecha, t.getMessage());
                mostrarAlertDialog(processNotifier.getContext(), "Envio", "Error de envio"/*: " + t.getCause()*/, 1);
                actualizarOtrasEstructurasEnvio(otrasEstructurasList.get(0), muestra, false, false, msgResponse = "");
                processNotifier.dismiss();
            }
        });
    }

    private void enviarRestantes(ProcessNotifier processNotifier,
                                 List<Cuestionarios> cuestionarioSelected, Muestra muestra) {
        for (int x = 1; x < cuestionarioSelected.size(); x++) {
            processNotifier.setTitle("Enviando al servidor");
            processNotifier.setText("Espere...");
            processNotifier.show();
            Cuestionarios cuestionarioEnv = cuestionarioSelected.get(x);
            cuestionarioEnv.setDatos("\uFEFF" + cuestionarioEnv.getDatos());

            Call<Void> call = censoApiService.sendCuestionario(cuestionarioEnv);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
//                    String errorEnviarRest = "";
                    if (response.isSuccessful()) {
                        Toast.makeText(MyApp.getContext(), "Cuestionario enviado: "
                                + cuestionarioEnv.getEntrevistaId(), Toast.LENGTH_SHORT).show();
                        actualizarEnvio(cuestionarioEnv, muestra, true, true, msgResponse);
                    } else {
                        try {
                            if (response.code() == 400)
                                msgResponse = Objects.requireNonNull(response.errorBody()).string();
                            SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                            Date date = new Date();
                            String fecha = sdf.format(date);
                            guardarError(cuestionarioEnv.getEntrevistaId(), fecha, msgResponse);
                        } catch (Exception e) {
                            Log.e(TAG, "onResponse: " + e.getMessage());
                        }

                        actualizarEnvio(cuestionarioEnv, muestra, true, false, msgResponse);
                        if (msgResponse.contains("El cuestionario ya fue revisado por el supervisor")) {
                            Toast.makeText(MyApp.getContext(), "El cuestionario ya fue revisado por el supervisor",
                                    Toast.LENGTH_SHORT).show();
                        } else if (!cuestionarioEnv.isFlagPrimerEnvio())
                            enviarCuestionarioUpdate(processNotifier, cuestionarioEnv, muestra,
                                    cuestionarioEnv.getEntrevistaId());
                    }
                    processNotifier.dismiss();
                }

                @Override
                public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                    SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                    Date date = new Date();
                    String fecha = sdf.format(date);
                    guardarError(cuestionarioEnv.getEntrevistaId(), fecha, t.getMessage());
                    actualizarEnvio(cuestionarioEnv, muestra, false, false, msgResponse = "");
                    processNotifier.dismiss();
                }
            });
        }
    }

    private void enviarRestantesOtrasEstructuras(ProcessNotifier processNotifier,
                                                 List<OtrasEstructuras> otrasEstructurasList,
                                                 Muestra muestra) {
        for (int x = 1; x < otrasEstructurasList.size(); x++) {
            processNotifier.setTitle("Enviando al servidor");
            processNotifier.setText("Espere...");
            processNotifier.show();

            Call<Void> call = censoApiService.sendOtraEstructura(otrasEstructurasList.get(x));
            int finalX = x;
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
//                    String errorEnviarRest = "";
                    if (response.isSuccessful()) {
                        Toast.makeText(MyApp.getContext(), "Cuestionario enviado: "
                                + otrasEstructurasList.get(finalX).getLlave(), Toast.LENGTH_SHORT).show();
                        actualizarOtrasEstructurasEnvio(otrasEstructurasList.get(finalX), muestra, true, true, msgResponse);
                    } else {
                        try {
                            if (response.code() == 400)
                                msgResponse = Objects.requireNonNull(response.errorBody()).string();
                            SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                            Date date = new Date();
                            String fecha = sdf.format(date);
                            guardarError(otrasEstructurasList.get(finalX).getLlave(), fecha, msgResponse);
                        } catch (Exception e) {
                            Log.e(TAG, "onResponse: " + e.getMessage());
                        }

                        actualizarOtrasEstructurasEnvio(otrasEstructurasList.get(finalX), muestra, true, false, msgResponse);
                        if (msgResponse.contains("El cuestionario ya fue revisado por el supervisor")) {
                            Toast.makeText(MyApp.getContext(), "El cuestionario ya fue revisado por el supervisor",
                                    Toast.LENGTH_SHORT).show();
                        } else if (!otrasEstructurasList.get(finalX).isFlagPrimerEnvio())
                            enviarOtrasEstructurasUpdate(processNotifier, otrasEstructurasList.get(finalX), muestra, otrasEstructurasList.get(finalX).getLlave());
                    }
                    processNotifier.dismiss();
                }

                @Override
                public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                    SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                    Date date = new Date();
                    String fecha = sdf.format(date);
                    guardarError(otrasEstructurasList.get(finalX).getLlave(), fecha, t.getMessage());
                    actualizarOtrasEstructurasEnvio(otrasEstructurasList.get(finalX), muestra,
                            false, false, msgResponse = "");
                    processNotifier.dismiss();
                }
            });
        }
    }

    public void enviarCuestionarioUpdate(ProcessNotifier processNotifier,
                                         Cuestionarios cuestionarioSelected, Muestra muestra, String llave) {
        processNotifier.setTitle("Enviando al servidor");
        processNotifier.setText("Espere...");
        processNotifier.show();

        Call<Void> call = censoApiService.sendCuestionarioUpdate(cuestionarioSelected, llave);
//        call.timeout().timeout(0, TimeUnit.MINUTES);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
//                String errorMensaje = "";
                if (response.isSuccessful()) {
                    actualizarEnvioUpdate(cuestionarioSelected, muestra, true, msgResponse = "");
                    Toast.makeText(MyApp.getContext(), "Cuestionario enviado: " + cuestionarioSelected.getEntrevistaId(), Toast.LENGTH_SHORT).show();
                    processNotifier.dismiss();
                    Log.d(TAG, "Se ha enviado el cuestionario");
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                    Date date = new Date();
                    String fecha = sdf.format(date);
                    try {
                        msgResponse = Objects.requireNonNull(response.errorBody()).string();
                        guardarError(cuestionarioSelected.getEntrevistaId(), fecha, msgResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MyApp.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    processNotifier.dismiss();
                    Toast.makeText(MyApp.getContext(), /*"Error envío actualizar cuestionario: "
                            +*/ msgResponse, Toast.LENGTH_SHORT).show();
                    actualizarEnvioUpdate(cuestionarioSelected, muestra, false, msgResponse);
                }
                processNotifier.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                mostrarAlertDialog(processNotifier.getContext(), "Envio", "Error de envio"/* " + t.getCause()*/, 1);
                SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                Date date = new Date();
                String fecha = sdf.format(date);
                guardarError(cuestionarioSelected.getEntrevistaId(), fecha, t.getMessage());
                actualizarEnvioUpdate(cuestionarioSelected, muestra, false, msgResponse = "");
                processNotifier.dismiss();
            }
        });
    }

    public void enviarOtrasEstructurasUpdate(ProcessNotifier processNotifier,
                                             OtrasEstructuras cuestionarioSelected, Muestra muestra, String llave) {
        processNotifier.setTitle("Enviando al servidor");
        processNotifier.setText("Espere...");
        processNotifier.show();

        Call<Void> call = censoApiService.sendCuestionarioUpdate(cuestionarioSelected, llave);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
//                String errorMensaje = "";
                if (response.isSuccessful()) {
                    actualizarEnvioUpdateOtrasEstructuras(cuestionarioSelected, muestra, true, msgResponse = "");
                    Toast.makeText(MyApp.getContext(), "Cuestionario enviado: "
                            + cuestionarioSelected.getLlave(), Toast.LENGTH_SHORT).show();
                    processNotifier.dismiss();
                    Log.d(TAG, "Se ha enviado el cuestionario");
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                    Date date = new Date();
                    String fecha = sdf.format(date);
                    try {
                        msgResponse = Objects.requireNonNull(response.errorBody()).string();
                        guardarError(cuestionarioSelected.getLlave(), fecha, msgResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MyApp.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    processNotifier.dismiss();
                    Toast.makeText(MyApp.getContext(), msgResponse, Toast.LENGTH_SHORT).show();
                    actualizarEnvioUpdateOtrasEstructuras(cuestionarioSelected, muestra, false, msgResponse);
                }
                processNotifier.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                mostrarAlertDialog(processNotifier.getContext(), "Envio", "Error de envio"/* " + t.getCause()*/, 1);
                SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                Date date = new Date();
                String fecha = sdf.format(date);
                guardarError(cuestionarioSelected.getLlave(), fecha, t.getMessage());
                actualizarEnvioUpdateOtrasEstructuras(cuestionarioSelected, muestra,
                        false, msgResponse = "");
                processNotifier.dismiss();
            }
        });
    }

    public void mostrarAlertDialog(Context activity, String titulo, String msg, int tipo) {
        try {
            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(activity);
            materialAlertDialogBuilder.setTitle(titulo);
            materialAlertDialogBuilder.setMessage(msg);
            if (tipo == 0) materialAlertDialogBuilder.setIcon(R.drawable.ic_send_ok);
            else materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
            materialAlertDialogBuilder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            materialAlertDialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*    public void eliminarDataBase() {
        appExecutors.diskIO().execute(() -> {
            cuestionariosDAO.deleteCuestionario();
            cuestionariosDAO.deleteSegmentos();
            cuestionariosDAO.deleteUsuarios();
        });
    }*/

    /*public void getMapsByContingente(FragmentActivity fragmentActivity, ProcessNotifier processNotifier, String region, String zona, String subzona) {
        processNotifier.setTitle("Descarga de Mapa");
        processNotifier.setText("Espere...");
        processNotifier.show();
        Call<ResponseBody> call = censoApiServiceMap.getMapsByRegionZonaSubzonaZIP(region, SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO), zona, subzona);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    appExecutors.diskIO().execute(() -> {
                        if (response.body() != null) {
                            saveToDisk(fragmentActivity, processNotifier, response.body(), "m_" + region + zona + subzona);
                        }
                    });
                } else {
                    if (response.raw().message().equals("Not Found") && response.raw().code() == 404) {
                        Toast.makeText(MyApp.getContext(), "No hay mapas para descargar.", Toast.LENGTH_SHORT).show();
                    }

                    if (response.raw().code() == 404) {
                        Toast.makeText(MyApp.getContext(), "No hay mapas para descargar. Error: " + response.raw().code(), Toast.LENGTH_SHORT).show();
                    }
                }
                processNotifier.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.i(TAG, "onFailure: ");
                mostrarAlertDialog(processNotifier.getContext(), "Envio", "Error de descarga"*//*: " + t.getCause()*//*, 1);
                processNotifier.dismiss();
            }
        });
    }*/

    public void getMapsByRegionZonaSubzona(FragmentActivity fragmentActivity, ProcessNotifier processNotifier, String region, String zona, String subzona) {
        processNotifier.setTitle("Descarga de Mapa");
        processNotifier.setText("Espere...");
        processNotifier.show();
        Call<ResponseBody> call = censoApiServiceMap.getMapsByRegionZonaSubzonaZIP(region, SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO), zona, subzona);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    appExecutors.diskIO().execute(() -> {
                        if (response.body() != null) {
                            if (SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO).startsWith("E00")) {
                                saveToDisk(fragmentActivity, processNotifier, response.body(),
                                        "mc_" + subzona);
                            } else {
                                saveToDisk(fragmentActivity, processNotifier, response.body(),
                                        "m_" + region + zona + subzona);
                            }

                        }
                    });
                } else {
                    if (response.raw().message().equals("Not Found") && response.raw().code() == 404) {
                        Toast.makeText(MyApp.getContext(), "No hay mapas para descargar.", Toast.LENGTH_SHORT).show();
                    }

                    if (response.raw().code() == 404) {
                        Toast.makeText(MyApp.getContext(), "No hay mapas para descargar. Error: " + response.raw().code(), Toast.LENGTH_SHORT).show();
                    }
                }
                processNotifier.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.i(TAG, "onFailure: ");
                mostrarAlertDialog(processNotifier.getContext(), "Envio",
                        "Error de descarga"/*: " + t.getCause()*/, 1);
                processNotifier.dismiss();
            }
        });
    }

    private void saveToDisk(FragmentActivity fragmentActivity, ProcessNotifier processNotifier,
                            ResponseBody body, String filename) {
        ZipInputStream zipInputStream;
        processNotifier.inflate();
        try {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String inecMovilDirectory = directory + "/InecMovil/MAPAS";

            if (new File(inecMovilDirectory).mkdir()) {
                Log.i(TAG, "saveToDisk: inecMovil Directory MAPAS");
            }

            File destinationFile = new File(inecMovilDirectory + "/" + filename);

            if ((destinationFile).exists()) {
                if (destinationFile.delete()) {
                    Log.i(TAG, "abrirCsPro: se ha eliminado el archivo .pff exitosamente");
                } else Log.e(TAG, "No se ha eliminado el archivo .pff ");
            }

            InputStream is = null;
            OutputStream os = null;

            try {
                long filesize = body.contentLength();
                is = body.byteStream();
                os = new FileOutputStream(destinationFile);

                byte[] data = new byte[4096];
                int count;
                int progress = 0;
                float tot;
                while ((count = is.read(data)) != -1) {
                    os.write(data, 0, count);
                    progress += count;
                    tot = (((float) progress / filesize) * 100);
                    processNotifier.setText("Descargando: " + (int) tot + "%");
                }

                os.flush();

                Log.d(TAG, "File saved successfully!");
            } catch (IOException e) {
                processNotifier.dismiss();
                e.printStackTrace();
                Log.d(TAG, "Failed to save the file!");
            } finally {
                if (is != null) is.close();
                if (os != null) os.close();
            }

            long total;
            long kbs;
            int countMapZip = 0;
            String fileZipName;
            ZipFile zipFileMap = new ZipFile(destinationFile);
            is = new FileInputStream(destinationFile);
            zipInputStream = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry zipEntry;
            byte[] buffer = new byte[1024];

            File dirMapFilePath = new File(inecMovilDirectory + "/dir" + filename);
            if (dirMapFilePath.exists()) {
                String[] children = dirMapFilePath.list();
                if (children != null && children.length > 0) {
                    for (String child : children) {
                        if (new File(dirMapFilePath, child).delete()) Log.i(TAG, "saveToDisk: ");
                    }
                }

                if (dirMapFilePath.delete()) {
                    Log.i(TAG, "abrirCsPro: se ha eliminado el archivo .pff exitosamente");
                } else Log.e(TAG, "No se ha eliminado el archivo .pff ");
            }

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                countMapZip++;

                fileZipName = zipEntry.getName();
                File mapFile = new File(dirMapFilePath + "/" + fileZipName);

                if (dirMapFilePath.mkdirs())
                    Log.i(TAG, "Se ha creado el dirrectorio satisfactoriamente");
                else Log.e(TAG, "No se ha creado el dirrectorio satisfactoriamente");

                if (mapFile.exists()) {
                    if (mapFile.delete()) {
                        Log.i(TAG, "abrirCsPro: se ha eliminado el archivo .pff exitosamente");
                    } else Log.e(TAG, "No se ha eliminado el archivo .pff ");
                }

                if (mapFile.createNewFile())
                    Log.i(TAG, "Se ha creado el archivo satisfactoriamente");
                else Log.e(TAG, "No se ha creado el archivo satisfactoriamente");

                FileOutputStream fOut = new FileOutputStream(mapFile);
                total = 0;
                kbs = 0;
                int count;

                // cteni zipu a zapis
                while ((count = zipInputStream.read(buffer)) != -1) {
                    total += count;
                    if (kbs != (total / 1024) / 1024) {
                        kbs = (total / 1024) / 1024;
//                        mrq.actualizar("descomprimiendo...: " + (total / 1024) + "Kb");
                        processNotifier.setText("Descomp. .tif: " + countMapZip + " / " + zipFileMap.size() + " - " + kbs + " mbs");
                    }
                    fOut.write(buffer, 0, count);
                }
                fOut.close();
                zipInputStream.closeEntry();
            }
            fragmentActivity.runOnUiThread(() -> Toast.makeText(fragmentActivity,
                    "Mapas guardados con exito.", Toast.LENGTH_SHORT).show());
            zipInputStream.close();
            processNotifier.dismiss();
        } catch (Exception e) {
            processNotifier.dismiss();
            mostrarAlertDialog(fragmentActivity.getApplicationContext(), "Mapas", "Error al guardar mapas.", 1);
            Log.d(TAG, "Failed to save the file! // " + e.getMessage());
        }
        processNotifier.dismiss();
    }

    public void getInconsistencias(FragmentActivity fragmentActivity, ProcessNotifier processNotifier, String usuario) {
        processNotifier.setTitle("Descargando inconsistencias");
        processNotifier.setText("Espere...");
        processNotifier.show();
        Call<List<GetInconsistenciasResponse>> call = censoApiService.getInconsistencias(usuario);
        call.enqueue(new Callback<List<GetInconsistenciasResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<GetInconsistenciasResponse>> call,
                                   @NotNull Response<List<GetInconsistenciasResponse>> response) {
                if (response.isSuccessful()) {
                    List<GetInconsistenciasResponse> inconsistenciasResponseList = response.body();
                    if (inconsistenciasResponseList != null && inconsistenciasResponseList.size() > 0) {
                        mostrarLlavesInconsistencias(fragmentActivity, inconsistenciasResponseList);
                    } else {
                        Toast.makeText(fragmentActivity, "No hay inconsistencias.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                    Date date = new Date();
                    String fecha = sdf.format(date);
                    try {
                        guardarError("Inconsistencia de " + usuario, fecha,
                                Objects.requireNonNull(response.errorBody()).string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                processNotifier.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<List<GetInconsistenciasResponse>> call, @NotNull Throwable t) {
                mostrarAlertDialog(processNotifier.getContext(), "Inconsistencias", "Error de descarga" /*de envio: " + t.getCause()*/, 1);
                SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                Date date = new Date();
                String fecha = sdf.format(date);
                guardarError("Inconsistencia de " + usuario, fecha, t.getMessage());
                processNotifier.dismiss();
            }
        });
    }

    private void mostrarLlavesInconsistencias(FragmentActivity fragmentActivity,
                                              List<GetInconsistenciasResponse> inconsistenciasResponseList) {
        boolean flagIncon;
        List<String> segmentos = new ArrayList<>();
        String llave = inconsistenciasResponseList.get(0).getLlave().substring(0, 12);

        segmentos.add(llave);/*
        llave.substring(0, 6) + "-" +
                llave.substring(6, 10) + "-" +
                llave.substring(10, 12)*/
        for (int x = 0; x < inconsistenciasResponseList.size(); x++) {
            flagIncon = false;
            try {
                llave = inconsistenciasResponseList.get(x).getLlave().substring(0, 12);
                for (int y = 0; y < segmentos.size(); y++) {
                    if (llave.equals(segmentos.get(y))) {
                        flagIncon = false;
                        break;
                    } else {
                        flagIncon = true;
                    }
                }
                if (flagIncon) {
                    segmentos.add(llave);
                }
            } catch (Exception e) {
                Toast.makeText(fragmentActivity, "Error en descarga de inconsistencia: " + x,
                        Toast.LENGTH_SHORT).show();
                x++;//TODO Probar
            }
        }
//        CharSequence[] llaves = new CharSequence[segmentos.size()];
//        llaves[x] = inconsistenciasResponseList.get(x).getLlave();
//        llaves[x] = inconsistenciasResponseList.get(x).getLlave().substring(0, 6) + "-" +
//                inconsistenciasResponseList.get(x).getLlave().substring(6, 10) + "-" +
//                inconsistenciasResponseList.get(x).getLlave().substring(10, 12) + "-" +
//                inconsistenciasResponseList.get(x).getLlave().charAt(12) + "-" //+
                /*    inconsistenciasResponseList.get(x).getLlave().substring(13, 15) + "-" +
                    inconsistenciasResponseList.get(x).getLlave().charAt(15)*/

        CharSequence[] segmentosFiltrados = new CharSequence[segmentos.size()];
        for (int x = 0; x < segmentos.size(); x++)
            segmentosFiltrados[x] = segmentos.get(x).substring(0, 6) + "-"
                    + segmentos.get(x).substring(6, 10) + "-" + segmentos.get(x).substring(10, 12);
        AlertDialog.Builder builderInconsistencias = new AlertDialog.Builder(fragmentActivity);
        builderInconsistencias.setTitle("Seleccione un segmento");
        builderInconsistencias.setSingleChoiceItems(segmentosFiltrados, 0, null);

        builderInconsistencias.setPositiveButton("Aceptar", (dialog, which) -> {
            int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
            abrirPDF(fragmentActivity, inconsistenciasResponseList, segmentos.get(selectedPosition));

        });
        builderInconsistencias.setNegativeButton("Cancelar", null);
        AlertDialog alertDialog = builderInconsistencias.create();
        alertDialog.show();
    }

    private void abrirPDF(FragmentActivity fragmentActivity,
                          List<GetInconsistenciasResponse> inconsistenciasResponseList, String segmentoSelected) {
        StringBuilder inconsistencias = new StringBuilder();
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
        String archivoIncons;

        archivoIncons = dia.substring(dia.length() - 2)
                + mes.substring(mes.length() - 2)
                + anno.substring(anno.length() - 4) + "_"
                + hora.substring(hora.length() - 2)
                + minutos.substring(minutos.length() - 2);

        for (int x = 0; x < inconsistenciasResponseList.size(); x++) {
            if (inconsistenciasResponseList.get(x).getLlave().contains(segmentoSelected)) {
                String llave = inconsistenciasResponseList.get(x).getLlave();
                inconsistencias.append("Cuestionario: ")
                        .append(llave.substring(0, 6))
                        .append("-").append(llave.substring(6, 10))
                        .append("-").append(llave.substring(10, 12))
                        .append("-").append(llave.charAt(12)).append("-")
                        .append(llave.substring(13, 15)).append("-")
                        .append(llave.charAt(15)).append("\n");
                String mensaje = Arrays.toString(inconsistenciasResponseList.get(x).getMensajes().split(llave)).replace("[", "");
                inconsistencias.append(mensaje.replace("]", ""));
                inconsistencias.append("\n\n");
            }
        }

        Document document = new Document();
        String outPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/" + "incon_" + archivoIncons + ".pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(outPath));
            document.open();
            document.add(new Paragraph(inconsistencias.toString()));
            document.close();
            Toast.makeText(MyApp.getContext(), "PDF creado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.fromFile(new File(outPath)),"application/pdf");

            Uri uri = FileProvider.getUriForFile(MyApp.getContext(), "gov.census.cspro.fileaccess.fileprovider", new File(outPath));
            String mime = MyApp.getContext().getContentResolver().getType(uri);
            intent.setDataAndType(uri, mime);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//            Intent chooser = Intent.createChooser(intent, "Abrir pdf");
            fragmentActivity.startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(MyApp.getContext(), "Error al crear el PDF. " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Se envia la fecha de bd correspondiente a la ultima vez qu se actualizó
     *
     * @param muestraSelected segmento seleccionado
     * @param usuario           El usuario con que inicios seción en la ap
     * @param fechaUltimoSync   Fecha en que se realizó la actualiaación
     */
    public void actualizarEstados(List<Muestra> muestraSelected, String usuario, String fechaUltimoSync) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN_ESTADOS, Locale.US);
            Date sdf2 = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN_ESTADOS, Locale.US).parse(fechaUltimoSync);

            String fecha = sdf.format(Objects.requireNonNull(sdf2));

            Call<List<GetRefreshEstado>> call = censoApiService.actualizarEstados(usuario, fecha);
            call.enqueue(new Callback<List<GetRefreshEstado>>() {
                @Override
                public void onResponse(@NotNull Call<List<GetRefreshEstado>> call,
                                       @NotNull Response<List<GetRefreshEstado>> response) {
                    if (response.isSuccessful()) {
                        List<GetRefreshEstado> refreshEstado = response.body();
                        List<Muestra> newSegmentos = new ArrayList<>();
                        for (int x = 0; x < Objects.requireNonNull(refreshEstado).size(); x++) {
                            for (int y = 0; y < muestraSelected.size(); y++) {
                                if (refreshEstado.get(x).getId().equals(muestraSelected.get(y).getMuestraId())) {
                                    newSegmentos.add(segmentoUpdate(refreshEstado.get(x), muestraSelected.get(y)));
                                    if (refreshEstado.size() == x + 1) {
                                        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN_ESTADOS, Locale.US);
                                        Date date = new Date();
                                        String fecha = sdf.format(date);
                                        SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_FECHA, fecha);
                                    }
                                    break;
                                }
                            }
                        }

                        if (newSegmentos.size() > 0)
                            appExecutors.diskIO().execute(() -> cuestionariosDAO.updateSegmentosEstado2(newSegmentos));
                        SharedPreferencesManager.setSomeBooleanValue(AppConstants.ESTADOS_STATUS, false);
                    } else {
                        Toast.makeText(MyApp.getContext(), "No hay estado que actualizar.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<GetRefreshEstado>> call, @NotNull Throwable t) {
                    Toast.makeText(MyApp.getContext(), "Error al actualizar los estados. " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Muestra segmentoUpdate(GetRefreshEstado getRefreshEstado, Muestra muestra) {
        muestra.setEstado(String.valueOf(getRefreshEstado.getEstado()));
        return muestra;
    }

//    public LiveData<Resource<Cuestionarios>> getCodigoECensoCall(Cuestionarios cuestionarios) {
//        return new NetworkBoundResource<Cuestionarios, CodigoAccesoResponse>(appExecutors) {
//            @Override
//            protected void saveCallResult(@NonNull CodigoAccesoResponse item) {
//                if (item.isExitoso()) {
//                    if (item.getValorRetorno() != null && item.getValorRetorno().getToken() != null) {
//                        cuestionarios.setCodigoECenso(item.getValorRetorno().getToken());
//                        cuestionarios.setECenso(true);
//                        cuestionariosDAO.actualizarCodigoCenso(cuestionarios);
//                    } else {
//                        cuestionarios.setFechaActualizacion(item.getErrores().toString());
//                    }
//                } else {
//                    if (item.getErrores() != null) {
//                        if (!item.getErrores().get(0).trim().equals("")) {
//                            String cadenaCodigo = item.getErrores().get(0);
//                            cuestionarios.setCodigoECenso(cadenaCodigo.substring(
//                                    cadenaCodigo.lastIndexOf(":") + 1).trim());
//                            cuestionarios.setECenso(true);
//                        }
//                        cuestionarios.setFechaActualizacion(item.getErrores().toString());
//                        cuestionariosDAO.actualizarCodigoCenso(cuestionarios);
//                    }
//                }
//            }
//
//            @Override
//            protected boolean shouldFetch(@Nullable Cuestionarios data) {
//                return Objects.requireNonNull(data).getCodigoECenso() == null;
//            }
//
//            @NonNull
//            @Override
//            protected LiveData<Cuestionarios> loadFromDb() {
//                return cuestionariosDAO.getCuestionarioECenso(cuestionarios.getEntrevistaId());
//            }
//
//            @NonNull
//            @Override
//            protected LiveData<ApiResponse<CodigoAccesoResponse>> createCall() {
//                return censoApiService.sendCodigoECenso(cuestionarios.getEntrevistaId());
//            }
//
//            @Override
//            protected void onFetchFailed() {
//                repoListRateLimit.reset(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
//            }
//        }.asLiveData();
//    }
}
