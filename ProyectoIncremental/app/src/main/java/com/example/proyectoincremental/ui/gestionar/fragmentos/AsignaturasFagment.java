package com.example.proyectoincremental.ui.gestionar.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectoincremental.Activity.CrearAsignaturaActivity;
import com.example.proyectoincremental.Activity.CreateUserActivity;
import com.example.proyectoincremental.Activity.LoginActivity;
import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AsignaturasFagment extends Fragment {
    private AdaptadorAsignaturas adaptadorEventos;
    private List<Asignatura> listaEventos;
    private FirebaseDatabase database;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference referenceEventos;

    public AsignaturasFagment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista_asignaturas, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fabAsignaturas);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CrearAsignaturaActivity.class);
                startActivity(intent);
            }
        });


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = (RecyclerView) v.findViewById(R.id.listaAsignaturas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        listaEventos = new ArrayList<Asignatura>();
        auth = FirebaseAuth.getInstance();

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        referenceEventos = database.getInstance().getReference().child("Asignaturas");
        referenceEventos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Asignatura p = dataSnapshot1.getValue(Asignatura.class);
                    listaEventos.add(p);
                }
                adaptadorEventos = new AdaptadorAsignaturas(listaEventos, getContext(), R.layout.item_asignatura, new AdaptadorAsignaturas.OnItemClickListener() {
                    @Override
                    public void onItemClick(Asignatura city, int position) {
                   /*     Intent intent = new Intent(getContext(), InfoEventosActivit.class);
                        intent.putExtra("NombreLocal", city.getNombre());
                        intent.putExtra("Contenido", city.getFecha());
                        intent.putExtra("Id", city.getId());
                        intent.putExtra("Fecha", city.getFecha());
                        intent.putExtra("PlazasDisponibles", city.getPlazasDisponibles());

                        startActivity(intent);
*/
                    }
                });
                recyclerView.setAdapter(adaptadorEventos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }
}
