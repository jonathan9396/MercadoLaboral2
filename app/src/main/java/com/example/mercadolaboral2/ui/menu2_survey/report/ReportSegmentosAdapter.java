package com.example.mercadolaboral2.ui.menu2_survey.report;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mercadolaboral2.R;

import com.example.mercadolaboral2.data.local.dbEntities.Muestra;

import java.util.List;

public class ReportSegmentosAdapter extends RecyclerView.Adapter<ReportSegmentosAdapter.ViewHolder> {
    private static final String TAG = "ReportSegmentosAdapter";
    private List<Muestra> muestraList;

    public ReportSegmentosAdapter(List<Muestra> items) {
        muestraList = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_report_segmentos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (muestraList != null) {
            Muestra muestra = muestraList.get(position);
            holder.tvReportSegmento.setText(muestra.getMuestraId());
            Log.i(TAG, "onBindViewHolder: fila cargada ReportSegmentos");
        }
    }

    @Override
    public int getItemCount() {
        if (muestraList != null)
            return muestraList.size();
        else return 0;
    }

    public void setData(List<Muestra> muestraList) {
        if (muestraList != null) {
            this.muestraList = muestraList;

        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private final TextView tvReportSegmento;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            tvReportSegmento = mView.findViewById(R.id.tvReportSegmento);
        }
    }
}
