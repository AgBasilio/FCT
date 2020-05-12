package com.example.proyectoincremental.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class PagerController extends FragmentPagerAdapter {
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
            case 1:
            case 2:
            default:

                return null;

        }
    }

    @Override
    public int getCount() {
        return num;
    }
}
