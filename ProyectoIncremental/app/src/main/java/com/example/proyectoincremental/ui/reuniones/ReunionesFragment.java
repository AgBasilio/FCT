package com.example.proyectoincremental.ui.reuniones;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

import com.example.proyectoincremental.Activity.CrearUsuarioActivity;
import com.example.proyectoincremental.Adaptadores.AdaptadorListaAsignturas;
import com.example.proyectoincremental.Adaptadores.AdaptadorListaGrupos;
import com.example.proyectoincremental.Adaptadores.AdaptadorReuniones;
import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.example.proyectoincremental.Utils.Grupos;
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

import java.util.ArrayList;
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

    private List<Asignatura> listaEventos;
    private List<Grupos> listaGrupos;
    private List<String> listaGruposs;
    private List<String> listaAsignaturas;
    private String a;


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


        listaEventos = new ArrayList<Asignatura>();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        database1 = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        userid = firebaseUser.getUid();
        //Referencia a las asgnaturas asignadas del usuario

       // referenceEventos2 = database.getInstance().getReference("Usuarios").child(userid).child("asignaturas");

/*
        referenceEventos2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                d = dataSnapshot.getValue().toString();
                Toast toast1 = Toast.makeText(getContext(), "Toast por defecto" + d, Toast.LENGTH_SHORT);
                toast1.show();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
*/
        //REFERENCIA ASIGNATURAS
        referenceEventos = database.getInstance().getReference("AsignaturasDefinidas").child(userid);
        referenceEventos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Asignatura p = dataSnapshot1.getValue(Asignatura.class);
                    p.setId(dataSnapshot1.getKey());
                    listaEventos.add(p);
                }
                adaptadorEventos = new AdaptadorReuniones(listaEventos, getContext(), R.layout.item_asignatura, new AdaptadorReuniones.OnItemClickListener() {

                    @Override
                    public void onItemClick(Asignatura asignatura, int position) {
                        cardView=(CardView)recyclerView.getChildAt(position);

                        if (cardView.getCardBackgroundColor().getDefaultColor() == -1){
                            cardView.setCardBackgroundColor(Color.parseColor("#000000"));
                        }else{
                            cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));

                        }

                        Toast toast1 = Toast.makeText(getContext(),"Toast por defecto"+position, Toast.LENGTH_SHORT);
                        toast1.show();

                    }
                });
                recyclerView.setAdapter(adaptadorEventos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        return view;
    }
}
