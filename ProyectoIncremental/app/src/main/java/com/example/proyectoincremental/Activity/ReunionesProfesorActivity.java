package com.example.proyectoincremental.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoincremental.Adaptadores.AdaptadorReunionesPorAsignatura;
import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.example.proyectoincremental.Utils.Grupos;
import com.example.proyectoincremental.Utils.Reuniones;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReunionesProfesorActivity extends AppCompatActivity {

    private Asignatura asignatura;

    private FirebaseDatabase database;

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_reuniones_profesor);

        asignatura = (Asignatura) getIntent().getExtras().getSerializable("Asignatura");
        if(asignatura != null && !asignatura.getNombre().isEmpty()){
            ((TextView)findViewById(R.id.textViewAsignatura)).setText(asignatura.getNombre());
        }

        database = FirebaseDatabase.getInstance();

        loadLayoutReuniones();

        recyclerView = (RecyclerView) findViewById(R.id.listarasignaturasparareunionprofesores);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(ReunionesProfesorActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

    }

    private void loadLayoutReuniones() {
        //el el onclick de asignatura
        //buscar reuniones para la asignatura y si hay
        //escucho solo una vez, porque estoy escuchando todas las reuniones
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


                                    recyclerView.setAdapter(new AdaptadorReunionesPorAsignatura(reunionesList, numeroGrupoList, R.layout.item_reunion_profesor, new AdaptadorReunionesPorAsignatura.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(final Reuniones reunion, int position, View itemView) {
                                            //Toast.makeText(ReunionesProfesorActivity.this, "Eliminar esta reunion.", Toast.LENGTH_LONG).show();
                                            AlertDialog.Builder builder;
                                            builder = new AlertDialog.Builder(ReunionesProfesorActivity.this);
                                            builder.setTitle("ATENCION");
                                            builder.setMessage("\n" + "Seguro que quieres eliminar la reunion del dia "+reunion.getHora());
                                            // Set up the buttons
                                            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (which == DialogInterface.BUTTON_POSITIVE) {
                                                        database.getReference("Reuniones").child(reunion.getGrupo()).child(reunion.getId()).removeValue();
                                                        //quitar reunion de reunionesList y notificar al adaptador
                                                        onBackPressed();
                                                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                                        dialog.cancel();
                                                    }
                                                }
                                            });
                                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                            builder.show();
                                        }
                                    }));
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
