package com.example.mercadolaboral2.ui.menu2_survey.capture.cuestionariosVivienda.viviendahogar;

import static com.example.mercadolaboral2.R.drawable.ic_ecenso;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mercadolaboral2.R;

import com.example.mercadolaboral2.data.local.constants.AppConstants;

import com.example.mercadolaboral2.data.local.constants.SharedPreferencesManager;

import com.example.mercadolaboral2.data.local.dbEntities.Cuestionarios;

import com.example.mercadolaboral2.data.local.dbEntities.EntrevistaBase;
import com.example.mercadolaboral2.data.local.dbEntities.Muestra;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViviendaHogarDialogAdapter
        extends RecyclerView.Adapter<ViviendaHogarDialogAdapter.ViewHolder> {
    private static final String TAG = "ViviendaHogarDialogAdap";
    private final OnViviendaHogarListener onViviendaHogarListener;
    private final Activity activity;
    private final Muestra muestra;
    private final ViviendaHogarViewModel viewModel;
    private List<List<Cuestionarios>> cuestionariosViviendaHogaresList;
    private HogaresAdapter hogaresAdapter;
    private List<List<EntrevistaBase>> entrevistaViviendaHogaresList;


    public ViviendaHogarDialogAdapter(ViviendaHogarViewModel viewModel, Muestra muestra,
                                      List<List<Cuestionarios>> cuestionariosViviendaHogares,
                                      OnViviendaHogarListener onViviendaHogarListener,List<List<EntrevistaBase>> entrevistaViviendaHogares,
                                      Activity activity) {
        this.activity = activity;
        this.viewModel = viewModel;
        this.cuestionariosViviendaHogaresList = cuestionariosViviendaHogares;
        this.onViviendaHogarListener = onViviendaHogarListener;
        this.muestra = muestra;
        this.entrevistaViviendaHogaresList = entrevistaViviendaHogares;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_viviendashogar, parent, false);
        return new ViewHolder(view, onViviendaHogarListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            if (cuestionariosViviendaHogaresList != null) {
                Cuestionarios cuestionarios = cuestionariosViviendaHogaresList.get(position).get(0);
                if (cuestionarios.getEstado() == 0)
                    holder.cvViviendaHogar.setCardBackgroundColor(Color.rgb(255, 160, 122));
                if (cuestionarios.getEstado() == 1)
                    holder.cvViviendaHogar.setCardBackgroundColor(Color.rgb(255, 255, 155));
                if (cuestionarios.getEstado() == 2)
                    holder.cvViviendaHogar.setCardBackgroundColor(Color.rgb(169, 208, 142));
                if (cuestionarios.getEstado() == 3)
                    holder.cvViviendaHogar.setCardBackgroundColor(Color.rgb(189, 215, 238));

                holder.tvVHViviendaInfo.setText(String.format("VIV %s - HOG %s",
                        cuestionarios.getJefe(), cuestionarios.getHogar()));

//                if (cuestionarios.isECenso()) {
//                    holder.tvVHViviendaInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0,
//                            ic_ecenso, 0);
//                } else
//                    holder.tvVHViviendaInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0,
//                            0, 0);
//                //            holder.tvVHHogarInfo.setText(String.format("HOG.: %s", cuestionarios.getHogar()));

                if (cuestionarios.getErroresEstructura() != null
                        && cuestionarios.getErroresEstructura().length() > 4)
                    holder.vInconsistencias.setBackgroundColor(Color.RED);
                else
                    holder.vInconsistencias.setBackgroundColor(Color.BLACK);

                if (cuestionarios.getPiso() == null)
                    holder.tvVHJefeInfo.setText("");
                else
                    holder.tvVHJefeInfo.setText(cuestionarios.getPiso().trim());

                holder.rvHogares.setLayoutManager(new LinearLayoutManager(holder.mView.getContext()));
                hogaresAdapter = new HogaresAdapter(cuestionariosViviendaHogaresList.get(position),
                        holder.onViviendaHogarListener);
                holder.rvHogares.setAdapter(hogaresAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (cuestionariosViviendaHogaresList != null) {
            return cuestionariosViviendaHogaresList.size();
        } else return 0;
    }

    public void setData(List<List<Cuestionarios>> lista) {
        if (lista != null) {
            this.cuestionariosViviendaHogaresList = lista;
        }
        notifyDataSetChanged();
    }



    public interface OnViviendaHogarListener extends HogaresAdapter.OnHogarListener {
        void onViviendaHogarClick(List<Cuestionarios> position, View view, List<EntrevistaBase> listae);

        @Override
        void onHogarClick(Cuestionarios position, View view, int absoluteAdapterPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        final OnViviendaHogarListener onViviendaHogarListener;
        //        private final TextView tvVHSegmentoInfo;
//        private final TextView tvVHDivisonInfo;
        private final TextView tvVHViviendaInfo;
        private final View vInconsistencias;
        //        private final TextView tvVHHogarInfo;
        private final TextView tvVHJefeInfo;
        private final MaterialCardView cvViviendaHogar;

        private final RecyclerView rvHogares;

        public ViewHolder(View view, OnViviendaHogarListener onViviendaHogarListener) {
            super(view);
            mView = view;
//            tvVHSegmentoInfo = mView.findViewById(R.id.tvVHSegmentoInfo);
            tvVHJefeInfo = mView.findViewById(R.id.tvVHJefeInfo);
            tvVHViviendaInfo = mView.findViewById(R.id.tvVHViviendaInfo);
            vInconsistencias = mView.findViewById(R.id.vInconsistencias);
//            tvVHHogarInfo = mView.findViewById(R.id.tvVHHogarInfo);

            cvViviendaHogar = mView.findViewById(R.id.cvViviendaHogar);
            ImageButton ibAddHogar = mView.findViewById(R.id.ibAddHogar);
            rvHogares = mView.findViewById(R.id.rvHogares);

            ibAddHogar.setOnClickListener(this);
            cvViviendaHogar.setOnClickListener(this);

            this.onViviendaHogarListener = onViviendaHogarListener;
        }

        @Override
        public void onClick(View v) {
            List<Cuestionarios> cuestionarioSeleccionado =
                    cuestionariosViviendaHogaresList.get(getBindingAdapterPosition());}
//            if (v.getId() == R.id.cvViviendaHogar)
//                onViviendaHogarListener.onViviendaHogarClick(cuestionarioSeleccionado, v, entrevistaViviendaHogaresList.get(getBindingAdapterPosition()));
//            else if (v.getId() == R.id.ibAddHogar) {
//                String datosJson = cuestionarioSeleccionado.get(0).getEmpadronadorId();
//                if (datosJson != null) {
//                    JsonObject jsonObject = new JsonParser().parse(datosJson).getAsJsonObject();
//
//                    if (cuestionarioSeleccionado.get(0).getFechaAsignacion() > 0) {
//                        if (jsonObject.get("REC_VIVIENDA").getAsJsonObject()
//                                .get("V01_TIPO").getAsString().matches("^([1-4]|[0][1-4])$")
//                                && jsonObject.get("REC_VIVIENDA").getAsJsonObject()
//                                .get("V02_COND").getAsString().matches("^([1]|[0][1])$")) {//Cambiar espacios
//
//                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//                            builder.setTitle("Creación de hogar");
//                            builder.setMessage("¿Desea crear un nuevo hogar?");
//                            builder.setPositiveButton("Crear", (dialog, which) -> {
//                                SimpleDateFormat sdf =
//                                        new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
//                                Date date = new Date();
//                                String fecha = sdf.format(date);
//                                int numHogar = (Integer.parseInt(cuestionarioSeleccionado.get(
//                                        cuestionarioSeleccionado.size() - 1).getHogar())) + 1;
//                                String numVivienda = cuestionarioSeleccionado.get(0).getJefe();
//
//                                int hogarCuestionario;
//                                if (jsonObject.has("REC_VIVIENDA")) {
//                                    if (jsonObject.get("REC_VIVIENDA").getAsJsonObject().has("V17_NHOG")
//                                            && jsonObject.get("REC_VIVIENDA").getAsJsonObject()
//                                            .get("V17_NHOG").getAsString().matches("^([1-9])$")) {
//
//
//                                        hogarCuestionario = jsonObject.get("REC_VIVIENDA")
//                                                .getAsJsonObject().get("V17_NHOG").getAsInt();
//                                        if (numHogar <= hogarCuestionario) {
//                                            if (cuestionarioSeleccionado.get(cuestionarioSeleccionado.size() - 1)
//                                                    .getFechaAsignacion() >= 1) {
//                                                Cuestionarios cuestionaroNuevo = new Cuestionarios(
//                                                        muestra.getMuestraId() + muestra.getFechaCreacion() + numVivienda + numHogar,
//                                                        muestra.getMuestraId(),
//                                                        muestra.getPaR02_DESC(),
//                                                        muestra.getPaR03_ID(),
//                                                        muestra.getPaR03_DESC(),
//                                                        muestra.getPaR02_ID(),
//                                                        muestra.getPaR06_ID(),
//                                                        muestra.getLlave(),
//                                                        SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO),
//                                                        null,
//                                                        muestra.getFechaCreacion(),
//                                                        numVivienda,
//                                                        String.valueOf(numHogar),
//                                                        null,
//                                                        null,
//                                                        null,
//                                                        0,
//                                                        fecha,
//                                                        null,
//                                                        fecha,
//                                                        null,
//                                                        false,
//                                                        false,
//                                                        "",
//                                                        null,
//                                                        "",
//                                                        false
//                                                );
//                                                cuestionarioSeleccionado.add(cuestionaroNuevo);
//                                                viewModel.addVivienda(cuestionaroNuevo);
//                                                hogaresAdapter.setData(cuestionarioSeleccionado);
//                                            } else {
//                                                MaterialAlertDialogBuilder materialAlertDialogBuilder =
//                                                        new MaterialAlertDialogBuilder(activity);
//                                                materialAlertDialogBuilder.setTitle("Creación de hogar.");
//                                                materialAlertDialogBuilder.setMessage("Debe terminar con el hogar anterior para" +
//                                                        " proceder a crear uno nuevo.");
//                                                materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
//                                                materialAlertDialogBuilder.setPositiveButton("OK", (dialog1, which1) -> dialog1.dismiss());
//                                                materialAlertDialogBuilder.show();
//                                            }
//                                        } else {
//                                            MaterialAlertDialogBuilder materialAlertDialogBuilder =
//                                                    new MaterialAlertDialogBuilder(activity);
//                                            materialAlertDialogBuilder.setTitle("Creación de hogar.");
//                                            materialAlertDialogBuilder.setMessage("Solo hay " + hogarCuestionario + " hogar(es) en la vivienda " +
//                                                    "(Pregunta 17- Número de hogares)");
//                                            materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
//                                            materialAlertDialogBuilder.setPositiveButton("OK", (dialog12, which12) -> dialog12.dismiss());
//                                            materialAlertDialogBuilder.show();
//                                        }
//
//                                    } else {
//                                        MaterialAlertDialogBuilder materialAlertDialogBuilder =
//                                                new MaterialAlertDialogBuilder(activity);
//                                        materialAlertDialogBuilder.setTitle("Creación de hogar.");
//                                        materialAlertDialogBuilder.setMessage("No puede crear hogar adicional.");
//                                        materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
//                                        materialAlertDialogBuilder.setPositiveButton("OK", (dialog12, which12) -> dialog12.dismiss());
//                                        materialAlertDialogBuilder.show();
//                                    }
//                                }
//                            });
//
//                            builder.setNegativeButton("Cancelar", (dialog, which) -> Log.i(TAG, "onClick: Cancelado."));
//                            AlertDialog alertDialog = builder.create();
//                            alertDialog.show();
//                        } else {
//                            MaterialAlertDialogBuilder materialAlertDialogBuilder =
//                                    new MaterialAlertDialogBuilder(activity);
//                            materialAlertDialogBuilder.setTitle("Creación de hogar.");
//                            materialAlertDialogBuilder.setMessage("No puede crear hogar adicional para esta vivienda.");
//                            materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
//                            materialAlertDialogBuilder.setPositiveButton("OK", (dialog12, which12) -> dialog12.dismiss());
//                            materialAlertDialogBuilder.show();
//                        }
//                    } else {
//                        MaterialAlertDialogBuilder materialAlertDialogBuilder =
//                                new MaterialAlertDialogBuilder(activity);
//                        materialAlertDialogBuilder.setTitle("Creación de hogar.");
//                        materialAlertDialogBuilder.setMessage("Debe completar la vivienda principal para agregar un nuevo hogar.");
//                        materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
//                        materialAlertDialogBuilder.setPositiveButton("OK", (dialog12, which12) -> dialog12.dismiss());
//                        materialAlertDialogBuilder.show();
//                    }
//                } else {
//                    Snackbar.make(itemView, "El cuestionario no ha sido iniciado.", Snackbar.LENGTH_SHORT)
//                            .show();
//                }
//            }
//        }
    }
}
