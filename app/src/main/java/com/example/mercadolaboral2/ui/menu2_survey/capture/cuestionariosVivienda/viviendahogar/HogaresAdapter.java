package com.example.mercadolaboral2.ui.menu2_survey.capture.cuestionariosVivienda.viviendahogar;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mercadolaboral2.R;
import com.example.mercadolaboral2.data.local.dbEntities.Cuestionarios;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class HogaresAdapter extends RecyclerView.Adapter<HogaresAdapter.ViewHolder> {
    private List<Cuestionarios> cuestionariosList;
    private final OnHogarListener onHogarListener;

    public HogaresAdapter(List<Cuestionarios> cuestionarios, OnHogarListener onHogarListener) {
        this.cuestionariosList = cuestionarios;
        this.onHogarListener = onHogarListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_hogares, parent, false);
        return new ViewHolder(view, onHogarListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cuestionariosList != null) {
            Cuestionarios cuestionarios = cuestionariosList.get(position + 1);
            if (cuestionarios.getFechaAsignacion() == 0)
                holder.cvHogar.setCardBackgroundColor(Color.rgb(255, 160, 122));
            if (cuestionarios.getFechaAsignacion() == 1)
                holder.cvHogar.setCardBackgroundColor(Color.rgb(255, 255, 155));
            if (cuestionarios.getFechaAsignacion() == 2)
                holder.cvHogar.setCardBackgroundColor(Color.rgb(169, 208, 142));
            if (cuestionarios.getFechaAsignacion() == 3)
                holder.cvHogar.setCardBackgroundColor(Color.rgb(189, 215, 238));

            holder.tvHSegmentoInfo.setText(String.format("VIV %s - HOG %s",
                    cuestionarios.getJefe(), cuestionarios.getHogar()));
//            holder.tvHDivisonInfo.setText(String.format("HOG.: %s", cuestionarios.getHogar()));
            if (cuestionarios.getFechaActualizacion() != null
                    && cuestionarios.getFechaActualizacion().length() > 4)
                holder.vHogarInconsistencia.setBackgroundColor(Color.RED);
            else
                holder.vHogarInconsistencia.setBackgroundColor(Color.BLACK);

            if (cuestionarios.getPiso() == null)
                holder.tvHViviendaInfo.setText("");
            else
                holder.tvHViviendaInfo.setText(cuestionarios.getPiso().trim());
//            holder.tvHHogarInfo.setText(cuestionarios.getHogar());
        }
    }

    @Override
    public int getItemCount() {
        if (cuestionariosList != null && cuestionariosList.size() > 1)
            return cuestionariosList.size() - 1;
        else return 0;
    }

    public void setData(List<Cuestionarios> cuestionarios) {
        this.cuestionariosList = cuestionarios;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;

        private final TextView tvHSegmentoInfo;
        private final View vHogarInconsistencia;
        //        private final TextView tvHDivisonInfo;
        private final TextView tvHViviendaInfo;
//        private final TextView tvHHogarInfo;

        private final MaterialCardView cvHogar;

        final OnHogarListener onHogarListener;

        public ViewHolder(@NonNull View itemView, OnHogarListener onHogarListener) {
            super(itemView);
            mView = itemView;

            tvHSegmentoInfo = mView.findViewById(R.id.tvHSegmentoInfo);
            vHogarInconsistencia = mView.findViewById(R.id.vHogarInconsistencia);
//            tvHDivisonInfo = mView.findViewById(R.id.tvHDivisonInfo);
            tvHViviendaInfo = mView.findViewById(R.id.tvHViviendaInfo);
//            tvHHogarInfo = mView.findViewById(R.id.tvHHogarInfo);

            cvHogar = mView.findViewById(R.id.cvHogar);

            cvHogar.setOnClickListener(this);
            this.onHogarListener = onHogarListener;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.cvHogar)
                onHogarListener.onHogarClick(cuestionariosList.get(getBindingAdapterPosition() + 1),
                        v, getBindingAdapterPosition() + 1);
        }
    }

    public interface OnHogarListener {
        void onHogarClick(Cuestionarios position, View view, int absoluteAdapterPosition);
    }
}
