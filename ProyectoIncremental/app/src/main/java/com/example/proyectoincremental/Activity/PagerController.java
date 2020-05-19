package com.example.proyectoincremental.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
                return new asignaturasListaFragment();
            case 1:
                return new gruposListaFragment();

            case 2:
                return new asignaturasListaFragment();

            default:

                return null;

        }
    }

    @Override
    public int getCount() {
        return num;
    }
}
