package com.example.proyectoincremental.ui.gestionar.fragmentos;

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

import com.example.proyectoincremental.Activity.CrearUsuarioActivity;
import com.example.proyectoincremental.Adaptadores.AdaptadorUsuarios;
import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Usuario;
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
public class UsuariosFragment extends Fragment {
    private AdaptadorUsuarios adaptadorUsuarios;
    private List<Usuario> listaUsuarios;
    private FirebaseDatabase database;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference referenceEventos;

    public UsuariosFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu2, menu);
        MenuItem searItem = menu.findItem(R.id.search2);
        SearchView searchView = (SearchView) searItem.getActionView();
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adaptadorUsuarios.getFilter().filter(newText);
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_usuarios, container, false);

        setHasOptionsMenu(true);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fabUsuarios);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CrearUsuarioActivity.class);
                startActivity(intent);
            }
        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = (RecyclerView) v.findViewById(R.id.listaUsuarios);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        listaUsuarios = new ArrayList<Usuario>();
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();
        referenceEventos = database.getInstance().getReference("Usuarios");
        referenceEventos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                listaUsuarios.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Usuario p = dataSnapshot1.getValue(Usuario.class);
                    //p.setId(dataSnapshot1.getKey());
                    listaUsuarios.add(p);
                }
                adaptadorUsuarios = new AdaptadorUsuarios(listaUsuarios, getContext(), R.layout.item_usuarios, new AdaptadorUsuarios.OnItemClickListener() {
                    @Override
                    public void onItemClick(Usuario city, int position) {

                    }
                });
                recyclerView.setAdapter(adaptadorUsuarios);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }
}
