package com.example.proyectoincremental.ui.gestionar.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectoincremental.Activity.CrearAsignaturaActivity;
import com.example.proyectoincremental.Activity.CrearGrupoActivity;
import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.example.proyectoincremental.Utils.Grupos;
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
public class GruposFragment extends Fragment {
    private AdaptadorGrupos adaptadorGrupos;
    private List<Grupos> listaEventos;
    private FirebaseDatabase database;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference referenceEventos;

    public GruposFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista_grupos, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fabGrupos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CrearGrupoActivity.class);
                startActivity(intent);
            }
        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = (RecyclerView) v.findViewById(R.id.listaGrupos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        listaEventos = new ArrayList<Grupos>();
        auth = FirebaseAuth.getInstance();

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        referenceEventos = database.getInstance().getReference().child("Grupos");
        referenceEventos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Grupos p = dataSnapshot1.getValue(Grupos.class);
                    listaEventos.add(p);
                }
                adaptadorGrupos = new AdaptadorGrupos(listaEventos, getContext(), R.layout.item_grupo, new AdaptadorGrupos.OnItemClickListener() {
                    @Override
                    public void onItemClick(Grupos city, int position) {


                    }
                });
                recyclerView.setAdapter(adaptadorGrupos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }


}
