package com.example.mercadolaboral2.ui.menu2_survey.maps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mercadolaboral2.R;

import com.example.mercadolaboral2.data.local.dbEntities.Muestra;

import java.util.List;

public class MapsAdapter extends RecyclerView.Adapter<MapsAdapter.ViewHolder> {
    private final OnMapsSelectListener onMapsSelectListener;
    private List<Muestra> muestraMapsList;

    public MapsAdapter(List<Muestra> muestraMapsList, OnMapsSelectListener onMapsSelectListener) {
        this.muestraMapsList = muestraMapsList;
        this.onMapsSelectListener = onMapsSelectListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_maps, parent, false);
        return new ViewHolder(view, onMapsSelectListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (muestraMapsList != null) {
            Muestra muestra = muestraMapsList.get(position);
            holder.tvMapsInfo.setText(String.format("Segmento: %s - %s - %s",
                    muestra.getMuestraId().substring(0, 6),
                    muestra.getMuestraId().substring(6, 10),
                    muestra.getMuestraId().substring(10, 12)));
        }
    }

    @Override
    public int getItemCount() {
        if (muestraMapsList != null)
            return muestraMapsList.size();
        else return 0;
    }

    public void setData(List<Muestra> muestraMapsList) {
        if (muestraMapsList != null) {
            if (muestraMapsList.size() != 0) {
                this.muestraMapsList = muestraMapsList;
            }
            notifyDataSetChanged();
        }
    }

    public interface OnMapsSelectListener {
        void onMapsSelectListener(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        final OnMapsSelectListener onMapsSelectListener;
        private final TextView tvMapsInfo;

        public ViewHolder(@NonNull View itemView, OnMapsSelectListener onMapsSelectListener) {
            super(itemView);
            mView = itemView;
            tvMapsInfo = mView.findViewById(R.id.tvMapsInfo);
            this.onMapsSelectListener = onMapsSelectListener;
            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMapsSelectListener.onMapsSelectListener(getBindingAdapterPosition());
        }
    }
}
