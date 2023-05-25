package com.example.mercadolaboral2.ui.menu2_survey.capture;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.example.mercadolaboral2.R;

import com.example.mercadolaboral2.data.local.constants.AppConstants;

import com.example.mercadolaboral2.data.local.dbEntities.Muestra;

import com.example.mercadolaboral2.data.local.dbEntities.TotCuestionarios;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CaptureRecyclerViewAdapter
        extends RecyclerView.Adapter<CaptureRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "CaptureRecyclerViewAdap";
    private final OnSegmentoCardListener onSegmentoCardListener;
    private List<Muestra> muestraList;
    //    private List<Cuestionarios> cuestionariosList;
    private List<TotCuestionarios> totCuestionariosList;


    public CaptureRecyclerViewAdapter(List<Muestra> items, OnSegmentoCardListener onSegmentoCardListener) {
        muestraList = items;
        this.onSegmentoCardListener = onSegmentoCardListener;
    }


      //prueba
//    public CaptureRecyclerViewAdapter(List<Muestra> items, List<TotCuestionarios> totCuestionariosList,
//                                      OnSegmentoCardListener onSegmentoCardListener) {
//        muestraList = items;
//        this.totCuestionariosList = totCuestionariosList;
//        this.onSegmentoCardListener = onSegmentoCardListener;
//    }



    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_capture, parent, false);
        return new ViewHolder(view, onSegmentoCardListener);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        if (muestraList != null) {

            Muestra muestra = muestraList.get(position);
//            holder.tvSubZonaInfo.setText(muestra.getSubZonaID());
            holder.tvSegmentoInfo.setText(String.format("%s - %s - %s",
                    muestra.getLlave().substring(0, 6),
                    muestra.getLlave().substring(6,8),
                    muestra.getLlave().substring(8, 10)));

            String estado = "0";
            switch (muestra.getEstado()) {
                case "1":
                    estado = AppConstants.SEG_ESTADO_HABILITADO;
                    break;
                case "2":
                    estado = AppConstants.SEG_ESTADO_ENPROCESO;
                    break;
                case "3":
                    estado = AppConstants.SEG_ESTADO_CERRADO_INCOMPLETO;
                    break;
                case "4":
                    estado = AppConstants.SEG_ESTADO_CERRADO_COMPLETO;
                    break;
            }
            holder.tvEstadoInfo.setText(estado);

  /*          int totCuestionarios = 0;
            for (Cuestionarios cuest : cuestionariosList) {
                if (cuest.getCodigoSegmento().equals(muestra.getId())) {
                    Log.i(TAG, "onBindViewHolder: ");
                    totCuestionarios++;
                }
            }*/
            for (int x = 0; x < totCuestionariosList.size(); x++) {
                if (totCuestionariosList.get(x).getCodigoSegmento().equals(muestra.getMuestraId())) {
                    holder.tvCuestionariosInfo.setText(
                            String.valueOf(totCuestionariosList.get(x).getTotCuestionarios()));
                    break;
                } else
                    holder.tvCuestionariosInfo.setText("0");
            }
        }
    }

    public void setData(List<Muestra> muestraList, List<TotCuestionarios> totCuestionariosList) {
        if (muestraList != null) {
            this.muestraList = muestraList;
        }

        if (totCuestionariosList != null) {
            this.totCuestionariosList = totCuestionariosList;
        }
        notifyDataSetChanged();
    }

//    public void setDataCuestionarios(List<Cuestionarios> cuestionarios) {
//        if (cuestionariosList.size() != 0) {
//            this.cuestionariosList = cuestionarios;
//        }
//        notifyDataSetChanged();
//    }

    @Override
    public int getItemCount() {
        if (muestraList != null)
            return muestraList.size();
        else return 0;
    }

    public interface OnSegmentoCardListener {
        void onSegmentoCardClick(int position);

        void onSegmentoCardLongClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        public final View mView;
        final OnSegmentoCardListener onSegmentoCardListener;
        //        private final TextView tvSubZonaInfo;
        private final TextView tvSegmentoInfo;
        private final TextView tvEstadoInfo;
        private final TextView tvCuestionariosInfo;

        public ViewHolder(View view, OnSegmentoCardListener onSegmentoCardListener) {
            super(view);
            mView = view;
//            tvSubZonaInfo = mView.findViewById(R.id.txtSubZonaInfo);
            tvSegmentoInfo = mView.findViewById(R.id.txtSegmentoInfo);
            tvEstadoInfo = mView.findViewById(R.id.txtEstadoInfo);
            tvCuestionariosInfo = mView.findViewById(R.id.txtCuestionariosInfo);
            this.onSegmentoCardListener = onSegmentoCardListener;

            mView.setOnClickListener(this);
            mView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onSegmentoCardListener.onSegmentoCardClick(getBindingAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            onSegmentoCardListener.onSegmentoCardLongClick(getBindingAdapterPosition());
            return false;
        }
    }
}
