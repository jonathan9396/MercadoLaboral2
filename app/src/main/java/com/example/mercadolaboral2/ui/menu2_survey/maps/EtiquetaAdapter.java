package com.example.mercadolaboral2.ui.menu2_survey.maps;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mercadolaboral2.R;

import java.util.List;

public class EtiquetaAdapter extends RecyclerView.Adapter<EtiquetaAdapter.ViewHolder> {

    private final List<String> tipoEtiqueta;
    private final List<String> infoEtiqueta;

    public EtiquetaAdapter(List<String> infoEtiqueta, List<String> tipoEtiqueta, Activity activity) {
        this.tipoEtiqueta = tipoEtiqueta;
        this.infoEtiqueta = infoEtiqueta;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alertdialog_etiqueta, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (tipoEtiqueta != null && infoEtiqueta != null) {
            holder.tvTipoEtiqueta.setText(tipoEtiqueta.get(position));
            holder.tvInfoEtiqueta.setText(infoEtiqueta.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return tipoEtiqueta.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTipoEtiqueta;
        private final TextView tvInfoEtiqueta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipoEtiqueta = itemView.findViewById(R.id.tvTipoEtiqueta);
            tvInfoEtiqueta = itemView.findViewById(R.id.tvInfoEtiqueta);
        }
    }
}
