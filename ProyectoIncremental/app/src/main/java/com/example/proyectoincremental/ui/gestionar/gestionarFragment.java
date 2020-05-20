package com.example.proyectoincremental.ui.gestionar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.proyectoincremental.Activity.PagerController;
import com.example.proyectoincremental.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class gestionarFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem uno, dos, tres;
    PagerController pagerController;
    private GruposViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_geestionar, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpage);
        tabLayout = (TabLayout) view.findViewById(R.id.tab);
        uno = (TabItem) view.findViewById(R.id.uno);
        dos = (TabItem) view.findViewById(R.id.dos);
        tres = (TabItem) view.findViewById(R.id.tres);

        return view;
    }
}
