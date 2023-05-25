package com.example.mercadolaboral2.ui.menu2_survey.report;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mercadolaboral2.R;

import com.example.mercadolaboral2.data.local.constants.Recorridos;

import com.example.mercadolaboral2.data.local.dbEntities.Cuestionarios;

import com.example.mercadolaboral2.data.local.dbEntities.Muestra;
import com.example.mercadolaboral2.utils.JsonQueriesCenso;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends Fragment {

    private static final String TAG = "ReportFragment";
    List<Recorridos> recorridos;
    private ReportViewModel reportViewModel;
    //    private ReportSegmentosAdapter reportSegmentosAdapter;
    private ReportSubZonaTotAdapter reportSubZonaTotAdapter;
    private RecorridosAdapter recorridosAdapter;
    private List<String> subZonasListSpinner;
    private List<String> segmentosListSpinner;
    private List<Integer> reporteTotales;
    private ArrayAdapter<String> subzonasArrayAdapter;
    private ArrayAdapter<String> segmentosArrayAdapter;
    private Spinner spinnerReportSegmentos;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        spinnerReportSegmentos = view.findViewById(R.id.spinnerReportSegmentos);
        subZonasListSpinner = new ArrayList<>();
        reporteTotales = new ArrayList<>();
        recorridos = new ArrayList<>();
//        subZonaList = new ArrayList<>();
//        segmentosList = new ArrayList<>();
        setDataSpinner(view);
//        RecyclerView recyclerView = view.findViewById(R.id.rvReportSegmento);
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
//        reportSegmentosAdapter = new ReportSegmentosAdapter(segmentosList);
//        recyclerView.setAdapter(reportSegmentosAdapter);

        RecyclerView rvSubZonaTotal = view.findViewById(R.id.rvTotZona);
        RecyclerView rvRecorridos = view.findViewById(R.id.rvRecorridos);
        rvSubZonaTotal.setLayoutManager(new GridLayoutManager(getContext(), 2));
        reportSubZonaTotAdapter = new ReportSubZonaTotAdapter(reporteTotales);
        rvSubZonaTotal.setAdapter(reportSubZonaTotAdapter);

        rvRecorridos.setLayoutManager(new LinearLayoutManager(getContext()));
        recorridosAdapter = new RecorridosAdapter(recorridos);
        rvRecorridos.setAdapter(recorridosAdapter);
        return view;
    }

    private void setDataSpinner(View view) {
        Spinner spinnerReporteSubzona = view.findViewById(R.id.spinnerReporteSubzona);

        subzonasArrayAdapter = new ArrayAdapter<>(
                requireActivity(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                subZonasListSpinner
        );

        reportViewModel.getAllSubZonas().observe(requireActivity(), segmentos -> {
            if (segmentos != null) {
                subZonasListSpinner.clear();
                for (Muestra muestra1 : segmentos) {
                    subZonasListSpinner.add("Subzona " + muestra1.getPaR02_ID());
                }
                subzonasArrayAdapter.notifyDataSetChanged();
            }
        });

        subzonasArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReporteSubzona.setAdapter(subzonasArrayAdapter);

        spinnerReporteSubzona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String subZonaSelect = subZonasListSpinner.get(position).substring(8);
                actualizarSegmentos(subZonaSelect);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "onNothingSelected: ");
            }
        });
    }

    private void actualizarSegmentos(String subZonaSelect) {
        segmentosListSpinner = new ArrayList<>();
        segmentosArrayAdapter = new ArrayAdapter<>(
                requireActivity(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                segmentosListSpinner
        );

        reportViewModel.getSegmentosSelectedGroup(subZonaSelect).observe(this, segmentos -> {
            segmentosListSpinner.clear();
            segmentosListSpinner.add("TODOS");
//            if (segmentos.size() != 0) {
//                segmentosList = segmentos;
////                reportSegmentosAdapter.setData(segmentosList);
//            }

            for (Muestra muestra1 : segmentos) {
                segmentosListSpinner.add("Segmento " + String.format("%s - %s - %s",
                        muestra1.getMuestraId().substring(0, 6),
                        muestra1.getMuestraId().substring(6, 10),
                        muestra1.getMuestraId().substring(10, 12)));
            }
            segmentosArrayAdapter.notifyDataSetChanged();
        });

        segmentosArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReportSegmentos.setAdapter(segmentosArrayAdapter);

        spinnerReportSegmentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view, int position, long id) {
                String segmentoSelect;
                if (segmentosListSpinner.get(position).equals("TODOS"))
                    segmentoSelect = segmentosListSpinner.get(position);
                else
                    segmentoSelect = segmentosListSpinner.get(position).substring(9);
                actualizarTotales(segmentoSelect.replace(" - ", ""), subZonaSelect);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "onNothingSelected: ");
            }
        });
    }

    private void actualizarTotales(String segmentoSelect, String subZonaSelect) {
        if (segmentoSelect.equals("TODOS")) {
            reportViewModel.getAllCuestionariosByZona(subZonaSelect).observe(this, cuestionarios -> {
                reporteTotales.clear();
                if (cuestionarios != null) {
                    getTotalesQueries(cuestionarios, true);
                }
                reportSubZonaTotAdapter.setData(reporteTotales);
            });

        } else {
            reportViewModel.getCuestionarios(segmentoSelect).observe(this, cuestionarios -> {
                reporteTotales.clear();
                recorridos.clear();
                if (cuestionarios != null) {
                    getTotalesQueries(cuestionarios, false);
                }
                reportSubZonaTotAdapter.setData(reporteTotales);
            });
        }
    }

    private void getTotalesQueries(List<Cuestionarios> cuestionarios, boolean flag) {
        Recorridos recorridos1;
        int totViviendaParticular = 0;
        int ocupadas = 0;
        int personasAusentes = 0;
        int desocupadas = 0;
        int hogaresAdicionales = 0;
        int hombres = 0;
        int mujeres = 0;
        int totHombres = 0;
        int totMujeres = 0;
        int totPersonas = 0;

        if (cuestionarios.size() > 0) {
            for (Cuestionarios c : cuestionarios) {
                if (c.getFechaAsignacion() > 0) {

                    if (c.getHogar().equals("1")) {
                        totViviendaParticular += 1;
                    }

                    if (Integer.parseInt(c.getHogar()) > 1 && c.getFechaAsignacion() > 0) {
                        hogaresAdicionales += 1;
                    }

                    if (c.getEmpadronadorId() != null) {//0810020721002021 02 1
                        JsonObject cuestionarioObject = new JsonParser().parse(c.getEmpadronadorId()).getAsJsonObject();

                        try {
                            ocupadas += JsonQueriesCenso.INSTANCE.IsViviendaParticularOcupadaReporte(cuestionarioObject) ? 1 : 0;
                            personasAusentes += JsonQueriesCenso.INSTANCE.IsViviendaOcupantesAusentes(cuestionarioObject) ? 1 : 0;
                            desocupadas += JsonQueriesCenso.INSTANCE.IsViviendaDesocupada(cuestionarioObject) ? 1 : 0;
                      /*      if (Integer.parseInt(c.getHogar()) > 0) {
                                int hogares = JsonQueriesCenso.INSTANCE.GetTotalHogaresDeclarados(cuestionarioObject) - 1;
                                hogaresAdicionales += Math.max(hogares, 0);
                            }*/
                            hombres = JsonQueriesCenso.INSTANCE.GetTotalHombresDeclarados(cuestionarioObject);
                            totHombres += hombres;
                            mujeres = JsonQueriesCenso.INSTANCE.GetTotalMujeresDeclaradas(cuestionarioObject);
                            totMujeres += mujeres;
                        } catch (Exception e) {
                            Log.e(TAG, "actualizarTotales: " + e.getMessage());
                        }
                        String condicion = "";

                        try {
                            if (cuestionarioObject.getAsJsonObject("REC_VIVIENDA") == null
                                /*&& cuestionarioObject.getAsJsonObject("REC_VIVIENDA").get("V01_TIPO") == null*/) {
                                Recorridos rec = recorridos.get(recorridos.size() - 1);
                                rec.setTotalPersonas(String.valueOf(Integer.parseInt(rec.getTotalPersonas()) + hombres + mujeres));
                                rec.setTotHombres(String.valueOf(Integer.parseInt(rec.getTotHombres()) + hombres));
                                rec.setTotMujeres(String.valueOf(Integer.parseInt(rec.getTotMujeres()) + mujeres));
                                recorridos.set(recorridos.size() - 1, rec);
                            } else {
                                if (cuestionarioObject.getAsJsonObject("REC_VIVIENDA") != null) {
                                    if (cuestionarioObject.getAsJsonObject("REC_VIVIENDA").get("V02_COND") != null) {
                                        String V02_COND = cuestionarioObject.getAsJsonObject("REC_VIVIENDA")
                                                .get("V02_COND").getAsString();

                                        if (V02_COND != null) {
                                            if (V02_COND.matches("^([1]|[0][1])$"))
                                                condicion = "Ocupada";
                                            else if (V02_COND.matches("^([3-7]|[0][3-7])$"))
                                                condicion = "Desocupada";
                                            else if (V02_COND.matches("^([2]|[0][2])$"))
                                                condicion = "Ausente";
                                        }
                                    }

                                    if (cuestionarioObject.getAsJsonObject("REC_VIVIENDA").get("V01_TIPO") != null
                                            && !cuestionarioObject.getAsJsonObject("REC_VIVIENDA").get("V01_TIPO").getAsString().equals("")) {
                                        String V01_TIPO = String.valueOf(cuestionarioObject.getAsJsonObject("REC_VIVIENDA")
                                                .get("V01_TIPO").getAsInt());
                                        if (V01_TIPO.matches("^([5-9]|[1][0-6])$")) {
                                            condicion = "No particular";
                                        }
                                    }
                                }

                                recorridos1 = new Recorridos(c.getEntrevistaId(),
                                        c.getJefe(),
                                        condicion,
                                        String.valueOf((hombres + mujeres)),
                                        String.valueOf(hombres),
                                        String.valueOf(mujeres));
                                recorridos.add(recorridos1);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "getTotalesQueries: ");
                        }
                    }
                }
                totPersonas = totHombres + totMujeres;
            }
            if (!flag)
                recorridosAdapter.setData(recorridos);
            else {
                recorridos.clear();
                recorridosAdapter.setData(recorridos);
            }
        }else{
            recorridos.clear();
            recorridosAdapter.setData(recorridos);
        }
        reporteTotales.add(totViviendaParticular);
        reporteTotales.add(ocupadas);
        reporteTotales.add(personasAusentes);
        reporteTotales.add(desocupadas);
        reporteTotales.add(hogaresAdicionales);
        reporteTotales.add(totPersonas);
        reporteTotales.add(totHombres);
        reporteTotales.add(totMujeres);
    }
}
