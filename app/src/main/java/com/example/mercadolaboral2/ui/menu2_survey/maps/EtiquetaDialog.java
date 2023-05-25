package com.example.mercadolaboral2.ui.menu2_survey.maps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;


import com.example.mercadolaboral2.R;

import com.example.mercadolaboral2.data.local.dbEntities.Muestra;

public class EtiquetaDialog extends DialogFragment {
    private final Muestra muestra;

    public EtiquetaDialog(FragmentActivity activity, Muestra muestra) {
        this.muestra = muestra;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_etiqueta, container, false);
//        TextView tvEtiquetaSegmento = v.findViewById(R.id.tvEtiquetaSegmento);
        TextView tvRegionEtiqueta = v.findViewById(R.id.tvRegionEtiqueta);
        TextView tvZonaEtiqueta = v.findViewById(R.id.tvZonaEtiqueta);
        TextView tvSubZonaEtiqueta = v.findViewById(R.id.tvSubZonaEtiqueta);
        TextView tvProvEtiqueta = v.findViewById(R.id.tvProvEtiqueta);
        TextView tvDistritoEtiqueta = v.findViewById(R.id.tvDistritoEtiqueta);
        TextView tvCorregimientoEtiqueta = v.findViewById(R.id.tvCorregimientoEtiqueta);
        TextView tvLugarPEtiqueta = v.findViewById(R.id.tvLugarPEtiqueta);
        TextView tvBarrioEtiqueta = v.findViewById(R.id.tvBarrioEtiqueta);
        TextView tvSegmentoEtiqueta = v.findViewById(R.id.tvSegmentoEtiqueta);
        TextView tvSegmentoDescripcion = v.findViewById(R.id.tvSegmentoDescripcion);

        tvRegionEtiqueta.setText(String.format("Región: %s", muestra.getPaR01_ID()));
        tvZonaEtiqueta.setText(String.format("Zona: %s", muestra.getPaR01_DESC()));
        tvSubZonaEtiqueta.setText(String.format("SubZona: %s", muestra.getPaR02_ID()));
        tvProvEtiqueta.setText(String.format("Provincia: %s", muestra.getEntrevistas()));
        tvDistritoEtiqueta.setText(String.format("Distrito: %s", muestra.getEntrevistasBase()));
//        tvCorregimientoEtiqueta.setText(String.format("Corregimiento: %s", muestra.getCorregNombre()));
        tvLugarPEtiqueta.setText(String.format("Lugar Poblado: %s", muestra.getPaR04_DESC()));
        tvBarrioEtiqueta.setText(String.format("Barrio: %s", muestra.getPaR05_ID()));
        tvSegmentoEtiqueta.setText(String.format("Segmento: %s - %s - %s",
                muestra.getMuestraId().substring(0, 6),
                muestra.getMuestraId().substring(6, 10),
                muestra.getMuestraId().substring(10, 12)));
//        tvSegmentoDescripcion.setText(String.format("Detalle: %s", muestra.getDetalle()));

/*        tvEtiquetaSegmento.setText(String.format("Segmento \n%s - %s - %s - %s - %s",
                muestra.getProvinciaID(),
                muestra.getDistritoID(),
                muestra.getCorregimientoID(),
                muestra.getSegmentoID(),
                muestra.getDivisionID()));*/

//        RecyclerView recyclerView = v.findViewById(R.id.rvEtiqueta);
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        EtiquetaAdapter etiquetaAdapter = new EtiquetaAdapter(infoEtiquete, tipoEtiquete, activity);
//        recyclerView.setAdapter(etiquetaAdapter);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
