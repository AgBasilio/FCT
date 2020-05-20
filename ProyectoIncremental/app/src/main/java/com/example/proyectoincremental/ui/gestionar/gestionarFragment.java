package com.example.proyectoincremental.ui.gestionar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.proyectoincremental.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class gestionarFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem uno, dos, tres;
    PagerController pagerController;
    private GruposViewModel homeViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_geestionar, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpagegestionar);


        tabLayout = (TabLayout) view.findViewById(R.id.tabg);
        uno = (TabItem) view.findViewById(R.id.usuariosg);
        dos = (TabItem) view.findViewById(R.id.gruposg);
        tres = (TabItem) view.findViewById(R.id.asignaturasg);
        pagerController = new PagerController(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerController);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    pagerController.notifyDataSetChanged();
                }else  if (tab.getPosition() == 1) {
                    pagerController.notifyDataSetChanged();

                }  else     if (tab.getPosition() == 2) {
                            pagerController.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onTabUnselected (TabLayout.Tab tab){

                    }

                    @Override
                    public void onTabReselected (TabLayout.Tab tab){

                    }
                });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

          //      setupViewPager(viewPager);
                return view;

            }


        }
