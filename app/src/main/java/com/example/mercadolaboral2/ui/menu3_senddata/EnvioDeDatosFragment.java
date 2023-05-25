package com.example.mercadolaboral2.ui.menu3_senddata;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mercadolaboral2.R;
import com.example.mercadolaboral2.data.local.constants.AppConstants;
import com.example.mercadolaboral2.data.local.constants.SharedPreferencesManager;
import com.example.mercadolaboral2.ui.menu2_survey.SurveyFragmentStateAdapter;
import com.example.mercadolaboral2.ui.menu3_senddata.send_cuestionarios.SendDataFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Objects;

public class EnvioDeDatosFragment extends Fragment {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        SurveyViewModel surveyViewModel = new ViewModelProvider(this).get(SurveyViewModel.class);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar())
                .setTitle(getText(R.string.title_sendData) + " // " +
                        SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
        //        View root = inflater.inflate(R.layout.fragment_survey, container, false);

        return inflater.inflate(R.layout.fragment_survey, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tabLayout = view.findViewById(R.id.tlHostSurveys);
        viewPager2 = view.findViewById(R.id.vpHostSurveys);
        setUpViewPaper();
        super.onViewCreated(view, savedInstanceState);
    }

    private ArrayList<Fragment> agregarFragment() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new SendDataFragment());
        return fragments;
    }

    private void setUpViewPaper() {
        viewPager2.setAdapter(new SurveyFragmentStateAdapter(requireActivity(), agregarFragment()));
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    if (position == 0)
                        tab.setText("Cuestionarios");
                    else if (position == 1)
                        tab.setText("Otras Estructuras");
                }).attach();
    }
}
