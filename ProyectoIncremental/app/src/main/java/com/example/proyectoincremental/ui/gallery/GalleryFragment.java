package com.example.proyectoincremental.ui.gallery;

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
import androidx.viewpager.widget.ViewPager;

import com.example.proyectoincremental.R;
import com.example.proyectoincremental.ui.home.HomeViewModel;
import com.example.proyectoincremental.ui.home.PagerController;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class GalleryFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem uno, dos, tres;
    private PagerController pagerController;
    private HomeViewModel homeViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpage);
        tabLayout = (TabLayout) view.findViewById(R.id.tab);
        uno = (TabItem) view.findViewById(R.id.uno);
        dos = (TabItem) view.findViewById(R.id.dos);
        tres = (TabItem) view.findViewById(R.id.tres);

        return view;
    }
}
