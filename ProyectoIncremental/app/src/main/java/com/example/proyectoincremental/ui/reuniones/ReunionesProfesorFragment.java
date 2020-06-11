package com.example.proyectoincremental.ui.reuniones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoincremental.Adaptadores.AdaptadorReuniones;
import com.example.proyectoincremental.Adaptadores.AdaptadorReunionesPorAsignatura;
import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.example.proyectoincremental.Utils.Grupos;
import com.example.proyectoincremental.Utils.Reuniones;
import com.example.proyectoincremental.Utils.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReunionesProfesorFragment extends Fragment {

    private final Asignatura asignatura;
    private final RecyclerView parentRecyclerView;
    private FirebaseDatabase database;

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;

    public ReunionesProfesorFragment(Asignatura asignatura, RecyclerView recyclerView) {
        this.asignatura = asignatura;
        this.parentRecyclerView = recyclerView;
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reuniones_profesor, container, false);

        if(asignatura != null && !asignatura.getNombre().isEmpty()){
            ((TextView)view.findViewById(R.id.textViewAsignatura)).setText(asignatura.getNombre());
        }

        database = FirebaseDatabase.getInstance();

        loadLayoutReuniones();

        recyclerView = (RecyclerView) view.findViewById(R.id.listarasignaturasparareunionprofesores);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void onDetach() {
        parentRecyclerView.setVisibility(View.VISIBLE);
        super.onDetach();
    }

    private void loadLayoutReuniones() {
        //el el onclick de asignatura
        //buscar reuniones para la asignatura y si hay,
        database.getReference("Reuniones").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final List<Reuniones> reunionesList = new ArrayList<>();
                        //recorrer lista de reuniones y guardar las reuniones para esa materia
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            for(DataSnapshot dsds : ds.getChildren())
                            {
                                if (dsds.getValue(Reuniones.class).getAsignarra().equals(asignatura.getId())) {
                                    Reuniones r = dsds.getValue(Reuniones.class);
                                    r.setId(dsds.getKey());
                                    reunionesList.add(r);
                                }
                            }
                        }

                        if(reunionesList.size() > 0){
                            //buscar nombre grupos
                            database.getReference("Grupos").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ArrayList<String> numeroGrupoList = new ArrayList<>();
                                    for(Reuniones reunion : reunionesList) {
                                        Boolean numeroGrupoEncontrado = false;

                                        for (DataSnapshot ds : dataSnapshot.getChildren())
                                        {
                                            for(DataSnapshot dsds : ds.getChildren()){
                                                if (dsds.getKey().equals(reunion.getGrupo())) {
                                                    numeroGrupoEncontrado = true;
                                                    //guardar nombre grupo
                                                    numeroGrupoList.add(dsds.getValue(Grupos.class).getNumeroGrupo());
                                                    break;
                                                }
                                            }
                                            if(numeroGrupoEncontrado)
                                                break;
                                        }
                                        if(!numeroGrupoEncontrado)
                                            numeroGrupoList.add("Grupo sin Numero");
                                    }
                                    AdaptadorReunionesPorAsignatura a = new AdaptadorReunionesPorAsignatura(reunionesList, numeroGrupoList, R.layout.item_reunion_profesor, new AdaptadorReunionesPorAsignatura.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(Reuniones reunion, int position, View itemView) {
                                            Toast.makeText(getContext(), "Eliminar esta reunion.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    recyclerView.setAdapter(a);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        //no tiene reuniones para esa asignatura
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }
}
