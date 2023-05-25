package com.example.mercadolaboral2.ui.menu1_home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mercadolaboral2.R;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private final List<String> data;
    private final List<String> dataSub;
    private final List<Integer> listTxtImage;
    private static final String TAG = "HomeAdapter";

    private final OnInicioCardListener onInicioCardListener;

    public HomeAdapter(List<String> data, List<String> dataSub, List<Integer> listTxtImage,
                       OnInicioCardListener onInicioCardListener) {
        this.data = data;
        this.dataSub = dataSub;
        this.listTxtImage = listTxtImage;
        this.onInicioCardListener = onInicioCardListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_home, parent, false);
        return new ViewHolder(view, onInicioCardListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ivInicioImg.setImageResource(listTxtImage.get(position));
        holder.tvInicioTitulo.setText(data.get(position));
        holder.tvInicioSubtitulo.setText(dataSub.get(position));
    }

    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        private final ImageView ivInicioImg;
        private final TextView tvInicioTitulo;
        private final TextView tvInicioSubtitulo;
        final OnInicioCardListener onInicioCardListener;

        public ViewHolder(@NonNull View itemView, OnInicioCardListener onInicioCardListener) {
            super(itemView);
            mView = itemView;
            ivInicioImg = mView.findViewById(R.id.ivInicioImg);
            tvInicioTitulo = mView.findViewById(R.id.tvInicioTitulo);
            tvInicioSubtitulo = mView.findViewById(R.id.tvInicioSubtitulo);
            this.onInicioCardListener = onInicioCardListener;

            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onInicioCardListener.onInicioCardListener(getBindingAdapterPosition());
        }
    }

    public interface OnInicioCardListener {
        void onInicioCardListener(int position);
    }
}