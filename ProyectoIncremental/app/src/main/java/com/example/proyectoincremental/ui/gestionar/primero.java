package com.example.proyectoincremental.ui.gestionar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectoincremental.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class primero extends Fragment {

    public primero() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reuniones, container, false);
    }
}
