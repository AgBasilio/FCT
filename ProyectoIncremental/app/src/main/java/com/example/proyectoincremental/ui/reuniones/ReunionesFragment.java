package com.example.proyectoincremental.ui.reuniones;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoincremental.Adaptadores.AdaptadorReuniones;
import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
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
public class ReunionesFragment extends Fragment {
    private String userid;
    private AdaptadorReuniones adaptadorReuniones;
    private List<Asignatura> listaAsiganturasDefinidas;
    private List<Reuniones> listaReuniones;
    private String idgrupo;
    DatabaseReference refreuniones;
    private FirebaseDatabase database;
    private DatabaseReference referenceEventos;
    CardView cardView;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private int containerId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.containerId = container.getId();
        View view = inflater.inflate(R.layout.fragment_reuniones, container, false);
        database = FirebaseDatabase.getInstance();
        listaAsiganturasDefinidas = new ArrayList<>();
        listaReuniones = new ArrayList<>();
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database.getReference("Usuarios").child(userid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Usuario usr = dataSnapshot.getValue(Usuario.class);
                        idgrupo = usr.getIdgrupo();
                        //si es profesor
                        if(usr.getTipo().equals(Usuario.TIPO_PROFESOR)){
                            loadLayoutProfesor(usr);
                        }
                        else {
                            //sino
                            loadLayoutAlumno();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
        recyclerView = (RecyclerView) view.findViewById(R.id.listarasignaturasparareunion);
        /**/
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        return view;
    }
    private void loadLayoutProfesor(Usuario usr) {
        //buscar las asignaturas con idProfesor
        database.getInstance().getReference("AsignaturasDefinidas").child(userid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //si tiene asignaturas
                        if(dataSnapshot.hasChildren()){
                            final List<Asignatura> asignaturaList = new ArrayList<>();//lista asignaturas de este profesor
                            for(DataSnapshot a : dataSnapshot.getChildren()){
                                Asignatura asignatura = a.getValue(Asignatura.class);
                                asignatura.setId(a.getKey());
                                asignaturaList.add(asignatura);
                            }

                            adaptadorReuniones = new AdaptadorReuniones(null, asignaturaList, getContext(), R.layout.item_asignatura, new AdaptadorReuniones.OnItemClickListener() {
                                @Override
                                public void onItemClick(Reuniones reuniones, int position, View itemView) {
                                    recyclerView.setVisibility(View.INVISIBLE);

                                    ReunionesProfesorFragment fragment = new ReunionesProfesorFragment(asignaturaList.get(position), recyclerView);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                                 .replace(containerId, fragment, "ReunionesProfesor")
                                                 .addToBackStack(null)
                                                 .commit();

                                }
                            });
                            recyclerView.setAdapter(adaptadorReuniones);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    private void loadLayoutAlumno() {
        if (idgrupo.isEmpty()){
            Toast.makeText(getContext(), "Usted no tiene grupo asignado.", Toast.LENGTH_LONG).show();
            return;
        }
        //REFERENCIA ASIGNATURAS
        referenceEventos = database.getInstance().getReference("AsignaturasDefinidas").child(userid);
        referenceEventos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                //listaAsiganturasDefinidas
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Asignatura p = dataSnapshot1.getValue(Asignatura.class);
                    p.setId(dataSnapshot1.getKey());
                    listaAsiganturasDefinidas.add(p);
                }
                DatabaseReference refReuniones = database.getInstance().getReference("Reuniones").child(idgrupo);
                //se realiza el lisener continuamente por si hay cambios no com el resto que se ejecuta una vez
                refReuniones.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listaReuniones = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Reuniones p = dataSnapshot1.getValue(Reuniones.class);
                            p.setId(dataSnapshot1.getKey());
                            listaReuniones.add(p);
                        }
                        adaptadorReuniones = new AdaptadorReuniones(listaReuniones, listaAsiganturasDefinidas, getContext(), R.layout.item_asignatura, new AdaptadorReuniones.OnItemClickListener() {

                            @Override
                            public void onItemClick(Reuniones reunionTarjeta, int position, View itemView) {
                                cardView = (CardView) recyclerView.getChildAt(position);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                String currentDateandTime = simpleDateFormat.format(new Date());

                                refreuniones = database.getReference("Reuniones").child(idgrupo);

                                if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {
                                    //cardView.setCardBackgroundColor(Color.parseColor("#2d572c"));

                                    Reuniones reunion = new Reuniones();
                                    reunion.setAsignarra(listaAsiganturasDefinidas.get(position).getId());
                                    reunion.setGrupo(idgrupo);
                                    reunion.setHora(currentDateandTime);

                                    refreuniones.push().setValue(reunion);

                                } else {
                                    refreuniones.child(reunionTarjeta.getId()).removeValue();
                                }
                            }
                        });
                        recyclerView.setAdapter(adaptadorReuniones);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}