package com.example.mercadolaboral2.ui.menu2_survey.capture.cuestionariosVivienda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;


import com.example.mercadolaboral2.R;

import com.example.mercadolaboral2.data.local.dbEntities.Muestra;

import com.example.mercadolaboral2.ui.menu2_survey.SurveyFragmentStateAdapter;

import com.example.mercadolaboral2.ui.menu2_survey.capture.cuestionariosVivienda.viviendahogar.ViviendaHogarDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class ViviendaFragment extends DialogFragment {

    private ViewPager2 vpHostVivienda;
    private TabLayout tlHostVivienda;
    private FragmentActivity activity;
    private Muestra muestra;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    public ViviendaFragment() {
        // Required empty public constructor
    }

    public ViviendaFragment(FragmentActivity requireActivity, Muestra muestra) {
        this.activity = requireActivity;
        this.muestra = muestra;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vivienda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tlHostVivienda = view.findViewById(R.id.tlHostVivienda);
        vpHostVivienda = view.findViewById(R.id.vpHostVivienda);
        setUpViewPaper();
        super.onViewCreated(view, savedInstanceState);
    }

    private ArrayList<Fragment> agregarFragment() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new ViviendaHogarDialogFragment(activity, muestra));
        return fragments;
    }

    private void setUpViewPaper() {
        vpHostVivienda.setAdapter(new SurveyFragmentStateAdapter(activity, agregarFragment()));
        new TabLayoutMediator(tlHostVivienda, vpHostVivienda,
                (tab, position) -> {
                    if (position == 0)
                        tab.setText("Viviendas");
                    else if (position == 1)
                        tab.setText("Otras estructuras");
                }).attach();
    }
}
