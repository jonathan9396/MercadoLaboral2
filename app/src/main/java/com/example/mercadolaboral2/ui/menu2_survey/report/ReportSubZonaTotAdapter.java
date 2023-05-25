package com.example.mercadolaboral2.ui.menu2_survey.report;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mercadolaboral2.R;


import java.util.ArrayList;
import java.util.List;

public class ReportSubZonaTotAdapter extends RecyclerView.Adapter<ReportSubZonaTotAdapter.ViewHolder> {
    private List<Integer> reporteTotales;
    private final List<String> subZonaTitulos;
//    private static final String TAG = "ReportSubZonaTotAdapter";

    public ReportSubZonaTotAdapter(List<Integer> reporteTotales) {
        this.reporteTotales = reporteTotales;
        subZonaTitulos = new ArrayList<>();
        initTitulos();
    }

    private void initTitulos() {
        subZonaTitulos.add("TOTAL VIVIENDAS");
        subZonaTitulos.add("OCUPADAS");
        subZonaTitulos.add("CON PERSONAS AUSENTES");
        subZonaTitulos.add("DESOCUPADAS");
        subZonaTitulos.add("HOGARES ADICIONALES");
        subZonaTitulos.add("TOTAL POBLACION");
        subZonaTitulos.add("HOMBRES");
        subZonaTitulos.add("MUJERES");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_report_subzonastot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (reporteTotales.size() != 0) {
            holder.tvReportSubZonaTitulo.setText(subZonaTitulos.get(position));
            holder.tvReportSubZonaInfo.setText(String.valueOf(reporteTotales.get(position)));
//        if (segmentosList != null) {
//            Muestra segmentos = segmentosList.get(position);
//            holder.tvReportSubZonaInfo.setText("00");
//            Log.i(TAG, "onBindViewHolder: fila cargada ReportSegmentos");
//        }
        }
    }

    @Override
    public int getItemCount() {
        if (subZonaTitulos != null)
            return subZonaTitulos.size();
        else return 0;
    }

    public void setData(List<Integer> reporteTotales) {
        if (reporteTotales != null) {
            this.reporteTotales = reporteTotales;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private final TextView tvReportSubZonaTitulo;
        private final TextView tvReportSubZonaInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            tvReportSubZonaTitulo = mView.findViewById(R.id.tvReportTitulo);
            tvReportSubZonaInfo = mView.findViewById(R.id.tvReportInfo);
        }
    }
}
