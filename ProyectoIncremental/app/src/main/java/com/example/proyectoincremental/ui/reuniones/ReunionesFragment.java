package com.example.proyectoincremental.ui.reuniones;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.proyectoincremental.Adaptadores.AdaptadorListaGrupos;
import com.example.proyectoincremental.Adaptadores.AdaptadorReuniones;
import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.example.proyectoincremental.Utils.Grupos;
import com.example.proyectoincremental.Utils.Reuniones;
import com.example.proyectoincremental.ui.gestionar.GruposViewModel;
import com.example.proyectoincremental.Adaptadores.PagerController;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem uno, dos, tres;
    private PagerController pagerController;
    private GruposViewModel homeViewModel;
    private EditText email, contraseña, nombre, apellido1, apellido2, edad, tipo;
    private Spinner spinner;
    private String userid;
    private int edadI, recuperamos_variable_integer;
    private Switch aSwitchA;
    String[] asignaturasusurio;
    private Button btnCrearUsuario, btnGoLogin, btnEditUsuario, btnlistarG, btnlistarA;
    private SharedPreferences prefs, sfd;
    private AdaptadorReuniones adaptadorEventos;
    private AdaptadorListaGrupos adaptadorGrupos;

    private List<Asignatura> listaAsiganturasDefinidas;
    private List<Reuniones> listaReuniones;
    private List<Grupos> listaGrupos;
    private List<String> listaGruposs;
    private List<String> listaAsignaturas;
    private String a, idgrupo;
    DatabaseReference refreuniones;

    private AlertDialog.Builder builder;

    private FirebaseDatabase database, database1;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private DatabaseReference referenceEventos, referenceEventos2;
    private DatabaseReference refBBD, refBBD1, refBBD2, refBBD3, refBBD4, refBBD5, refBBD6;
    private FirebaseAuth mAuth;
    private String d;
    CardView cardView;


    private RecyclerView recyclerView, recyclerView1;
    private LinearLayoutManager mLayoutManager, mLayoutManager1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reuniones, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = (RecyclerView) view.findViewById(R.id.listarasignaturasparareunion);
        /**/
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);


        listaAsiganturasDefinidas = new ArrayList<Asignatura>();
        listaReuniones = new ArrayList<Reuniones>();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        database1 = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        userid = firebaseUser.getUid();

        database.getReference("Usuarios").child(userid).child("idgrupo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                idgrupo = dataSnapshot.getValue(String.class);

                if (idgrupo.isEmpty()) {
                    Toast toast1 = Toast.makeText(getContext(), "Usted no tiene grupo asignado.", Toast.LENGTH_LONG);
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

                                adaptadorEventos = new AdaptadorReuniones(listaReuniones, listaAsiganturasDefinidas, getContext(), R.layout.item_asignatura, new AdaptadorReuniones.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(final Reuniones reunionTarjeta, int position, View itemView) {
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
                                            builder = new AlertDialog.Builder(getContext());
                                            builder.setTitle("ATENCION");
                                            builder.setMessage("\n" + "Seguro que quieres eliminar la reunion del dia "+reunionTarjeta.getHora());
                                            // Set up the buttons
                                            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (which == DialogInterface.BUTTON_POSITIVE) {
                                                        refreuniones.child(reunionTarjeta.getId()).removeValue();

                                                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                                        dialog.cancel();
                                                    }
                                                }
                                            });
                                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (which == DialogInterface.BUTTON_POSITIVE) {

                                                        refreuniones.child(reunionTarjeta.getId()).removeValue();

                                                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                                        dialog.cancel();
                                                    }
                                                }
                                            });
                                            builder.show();

                                            //cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));

                                            //mal, remueve todas las reuniones, revisar
                                            //      refreuniones.child(reunionTarjeta.getId()).removeValue();
                                        }

                                        Toast toast1 = Toast.makeText(getContext(), "Toast por defecto" + position, Toast.LENGTH_SHORT);
                                        toast1.show();

                                    }
                                });
                                recyclerView.setAdapter(adaptadorEventos);

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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }
}
