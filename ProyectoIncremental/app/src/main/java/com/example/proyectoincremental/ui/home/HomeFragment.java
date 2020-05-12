package com.example.proyectoincremental.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.proyectoincremental.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem uno, dos, tres;
    PagerController pagerController;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpage);
        tabLayout = (TabLayout) view.findViewById(R.id.tab);
        uno = (TabItem) view.findViewById(R.id.uno);
        dos = (TabItem) view.findViewById(R.id.dos);
        tres = (TabItem) view.findViewById(R.id.tres);

        return view;
    }
}
