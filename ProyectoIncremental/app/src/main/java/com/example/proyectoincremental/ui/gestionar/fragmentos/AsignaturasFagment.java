package com.example.proyectoincremental.ui.gestionar.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectoincremental.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AsignaturasFagment extends Fragment {

    public AsignaturasFagment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_lista_asignaturas, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fabAsignaturas);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action de acion crear asignatura", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}
