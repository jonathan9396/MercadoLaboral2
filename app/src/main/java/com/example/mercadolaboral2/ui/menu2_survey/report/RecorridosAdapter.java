package com.example.mercadolaboral2.ui.menu2_survey.report;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mercadolaboral2.R;

import com.example.mercadolaboral2.data.local.constants.Recorridos;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class RecorridosAdapter extends RecyclerView.Adapter<RecorridosAdapter.ViewHolder> {
    private static final String TAG = "RecorridosAdapter";
    private List<Recorridos> recorridosList;

    public RecorridosAdapter(List<Recorridos> recorridos) {
        recorridosList = recorridos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_viviendareporte, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (recorridosList != null) {
            Recorridos recorridos = recorridosList.get(position);
            holder.tvReporteVivinda.setText(String.format("Recorrido n° %s", recorridos.getRecorrido()));
            holder.tvCondicionVivinda.setText(String.format("%s", recorridos.getCondicionVivienda()));
            holder.tvReporteTot.setText(String.format("Total: %s", recorridos.getTotalPersonas()));
            holder.tvReporteTotHombres.setText(String.format("Hombres: %s", recorridos.getTotHombres()));
            holder.tvReporteTotMujeres.setText(String.format("Mujeres: %s", recorridos.getTotMujeres()));
            Log.i(TAG, "onBindViewHolder: fila cargada ReportSegmentos");
            if (recorridos.getCondicionVivienda().equals("desocupada"))
                holder.mcvRecorridos.setCardBackgroundColor(Color.rgb(141, 185, 223));
            else
                holder.mcvRecorridos.setCardBackgroundColor(Color.rgb(2, 119, 189));
        }
    }

    @Override
    public int getItemCount() {
        if (recorridosList != null)
            return recorridosList.size();
        else return 0;
    }

    public void setData(List<Recorridos> recorridos) {
        if (recorridos != null) {
            this.recorridosList = recorridos;
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvReporteVivinda;
        private final TextView tvReporteTot;
        private final TextView tvReporteTotHombres;
        private final TextView tvReporteTotMujeres;
        private final TextView tvCondicionVivinda;
        private final MaterialCardView mcvRecorridos;

        public ViewHolder(View view) {
            super(view);
            tvReporteVivinda = view.findViewById(R.id.tvReporteVivinda);
            tvReporteTot = view.findViewById(R.id.tvReporteTot);
            tvReporteTotHombres = view.findViewById(R.id.tvReporteTotHombres);
            tvReporteTotMujeres = view.findViewById(R.id.tvReporteTotMujeres);
            tvCondicionVivinda = view.findViewById(R.id.tvCondicionVivinda);
            mcvRecorridos = view.findViewById(R.id.mcvRecorridos);
        }
    }
}
