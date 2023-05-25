package com.example.mercadolaboral2.ui.menu3_senddata.send_cuestionarios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mercadolaboral2.R;

import com.example.mercadolaboral2.data.local.dbEntities.CuestionariosPendientes;

import java.util.List;

public class SendDataAdapter extends RecyclerView.Adapter<SendDataAdapter.ViewHolder> {
    //    private List<Muestra> segmentosList;
    private List<CuestionariosPendientes> cuestionariosPendientes;
//    private static final String TAG = "SendDataAdapter";
    private boolean[] checkeds;
    private final OnEnvioClickListener onEnvioClickListener;


    public SendDataAdapter(List<CuestionariosPendientes> cuestionariosPendientes, boolean[] checkeds,
                           OnEnvioClickListener onEnvioClickListener) {
        this.cuestionariosPendientes = cuestionariosPendientes;
        this.checkeds = checkeds;
        this.onEnvioClickListener = onEnvioClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_senddata, parent, false);
        return new ViewHolder(view, onEnvioClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cuestionariosPendientes != null) {
            CuestionariosPendientes cPendientes = cuestionariosPendientes.get(position);
//            holder.tvEnvioSubZonaInfo.setText(segmentos.getSubZonaID());
            holder.tvEnvioSegmentoInfo.setText(String.format("%s - %s - %s",
                    cPendientes.getCodigoSegmento().substring(0, 6),
                    cPendientes.getCodigoSegmento().substring(6, 10),
                    cPendientes.getCodigoSegmento().substring(10, 12)));
//            holder.tvEnvioEstadoInfo.setText(segmentos.getEstado());
            holder.tvEnvioEnviadosInfo.setText(cPendientes.getTotPendientes());
            if (checkeds.length != 0)
                holder.cbEnvioItem.setChecked(checkeds[position]);
        }
    }

    public void setDataChecked(boolean[] checkeds) {
        this.checkeds = checkeds;
        notifyDataSetChanged();
    }

    public void setData(List<CuestionariosPendientes> cuestionariosPendientes, boolean[] checkeds) {
        if (cuestionariosPendientes != null) {
            this.checkeds = checkeds;
            this.cuestionariosPendientes = cuestionariosPendientes;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (cuestionariosPendientes != null) {
            return cuestionariosPendientes.size();
        } else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        //        private final TextView tvEnvioSubZonaInfo;
        private final TextView tvEnvioSegmentoInfo;
        //        private final TextView tvEnvioEstadoInfo;
        private final TextView tvEnvioEnviadosInfo;
        private final CheckBox cbEnvioItem;

        private final OnEnvioClickListener onEnvioClickListener;

        public ViewHolder(@NonNull View view, OnEnvioClickListener onEnvioClickListener) {
            super(view);
            mView = view;
//            tvEnvioSubZonaInfo = mView.findViewById(R.id.tvEnvioSubZonaInfo);
            tvEnvioSegmentoInfo = mView.findViewById(R.id.tvEnvioSegmentoInfo);
//            tvEnvioEstadoInfo = mView.findViewById(R.id.tvEnvioEstadoInfo);
            tvEnvioEnviadosInfo = mView.findViewById(R.id.tvEnvioEnviadosInfo);
            cbEnvioItem = mView.findViewById(R.id.cbEnvioItem);

            this.onEnvioClickListener = onEnvioClickListener;

            cbEnvioItem.setOnClickListener(this);
            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int view = v.getId();
            if (view == R.id.cbEnvioItem) {
                onEnvioClickListener.onCheckBoxSegmentoClick(getBindingAdapterPosition());
//                if (cbEnvioItem.isChecked()) {
//                    Toast.makeText(v.getContext(), "Seleccionado "
//                            + segmentosList.get(getBindingAdapterPosition()), Toast.LENGTH_SHORT).show();
//                }
            } else if (view == mView.getId()) {
                onEnvioClickListener.onCardViewSegmentoClick(getBindingAdapterPosition());
            }
        }
    }

    public interface OnEnvioClickListener {
        void onCardViewSegmentoClick(int position);

        void onCheckBoxSegmentoClick(int position);
    }
}
