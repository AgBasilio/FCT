package com.example.proyectoincremental.ui.reuniones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.proyectoincremental.R;
import com.example.proyectoincremental.ui.gestionar.GruposViewModel;
import com.example.proyectoincremental.ui.gestionar.PagerController;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ReunionesFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem uno, dos, tres;
    private PagerController pagerController;
    private GruposViewModel homeViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reuniones, container, false);


        return view;
    }
}
