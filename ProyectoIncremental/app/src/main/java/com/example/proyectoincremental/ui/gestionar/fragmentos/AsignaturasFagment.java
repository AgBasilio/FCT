package com.example.proyectoincremental.ui.gestionar.fragmentos;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectoincremental.Activity.CrearAsignaturaActivity;
import com.example.proyectoincremental.Adaptadores.AdaptadorAsignaturas;
import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    public List<Asignatura> listaEventos;
    private FirebaseDatabase database;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference referenceEventos;
    private AlertDialog.Builder builder;

    public AsignaturasFagment() {
        // Required empty public constructor
    }



    @Override
    public void onCreateOptionsMenu( Menu menu,  MenuInflater inflater) {
        inflater.inflate(R.menu.main2,menu);
        MenuItem searItem = menu.findItem(R.id.search);
        SearchView searchView=(SearchView)searItem.getActionView();
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adaptadorEventos.getFilter().filter(newText);
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista_asignaturas, container, false);

        setHasOptionsMenu(true);

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
        listaEventos.clear();




        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        String userid = firebaseUser.getUid();

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        referenceEventos = database.getInstance().getReference().child("Asignaturas").child(userid);
        referenceEventos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                listaEventos.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Asignatura p = dataSnapshot1.getValue(Asignatura.class);
                    p.setId(dataSnapshot1.getKey());
                    listaEventos.add(p);
                }

                adaptadorEventos = new AdaptadorAsignaturas(listaEventos, getContext(), R.layout.item_asignatura, new AdaptadorAsignaturas.OnItemClickListener() {

                    @Override
                    public void onItemClick(Asignatura city, int position) {


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
