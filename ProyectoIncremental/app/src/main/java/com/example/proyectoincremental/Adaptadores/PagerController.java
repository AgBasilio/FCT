package com.example.proyectoincremental.Adaptadores;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.proyectoincremental.ui.gestionar.fragmentos.AsignaturasFagment;
import com.example.proyectoincremental.ui.gestionar.fragmentos.GruposFragment;
import com.example.proyectoincremental.ui.gestionar.fragmentos.UsuariosFragment;

public class PagerController extends FragmentPagerAdapter {
    int num;

    public PagerController(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.num = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AsignaturasFagment();
            case 1:
                return new GruposFragment();

            case 2:
                return new UsuariosFragment();

            default:

                return null;

        }
    }

    @Override
    public int getCount() {
        return num;
    }


    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}

