package com.example.proyectoincremental.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoincremental.Activity.CreateUserActivity;
import com.example.proyectoincremental.Activity.LoginActivity;
import com.example.proyectoincremental.Adaptadores.AdaptadorAsignaturas;
import com.example.proyectoincremental.Adaptadores.AdaptadorGrupos;
import com.example.proyectoincremental.Adaptadores.AdaptadorListaAsignturas;
import com.example.proyectoincremental.Adaptadores.AdaptadorListaGrupos;
import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.example.proyectoincremental.Utils.Grupos;
import com.example.proyectoincremental.Utils.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.proyectoincremental.Activity.CreateUserActivity.isNumeric;

public class CrearUsuarioActivity extends AppCompatActivity {
    private AdaptadorListaAsignturas adaptadorEventos;
    private AdaptadorListaGrupos adaptadorGrupos;

    private List<Asignatura> listaEventos;
    private List<Grupos> listaGrupos;

    private FirebaseDatabase database,database1;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private RecyclerView recyclerView,recyclerView1;
    private LinearLayoutManager mLayoutManager,mLayoutManager1;
    private DatabaseReference referenceEventos,referenceEventos2;
    private AlertDialog.Builder builder;

    private EditText email, contraseña, nombre, apellido1, apellido2, edad, tipo;
    private DatabaseReference refBBD, refBBD2;
    private String emailS = "", contraseñaS = "", nombreS = "", apellido1S = "", apellido2S = "", edadS = "", asignaturas = "", grupo = "", tipoS;
    private int edadI;
    private Button btnCrearUsuario, btnGoLogin;
    private FirebaseAuth mAuth;
    private SharedPreferences prefs, sfd;
    private Spinner spinner;
    private TextView y;
    private String ciudadSeleccionada = "";
    private String userid
;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuariodentro);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        database1 = FirebaseDatabase.getInstance();
        nombre = findViewById(R.id.nombre);
        email = findViewById(R.id.email);
        contraseña = findViewById(R.id.contraseña);
        apellido1 = findViewById(R.id.apellido1);
        y = findViewById(R.id.verselecion);

        apellido2 = findViewById(R.id.apellido2);
        edad = findViewById(R.id.edad);
        tipo = findViewById(R.id.tipo);
        btnCrearUsuario = findViewById(R.id.btnCrearUsuariodentro);
        refBBD2 = database.getReference("Asignaturas/");
//
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        recyclerView = (RecyclerView) findViewById(R.id.listaA);
        recyclerView1 = (RecyclerView) findViewById(R.id.listaG);
/**/
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager1 = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(mLayoutManager1);

        listaEventos = new ArrayList<Asignatura>();
        listaGrupos = new ArrayList<Grupos>();



        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

         userid = firebaseUser.getUid();

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

                adaptadorEventos = new AdaptadorListaAsignturas(listaEventos, CrearUsuarioActivity.this, R.layout.item_a, new AdaptadorListaAsignturas.OnItemClickListener() {

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


        referenceEventos2 = database1.getInstance().getReference("Grupos").child(userid);
        referenceEventos2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Grupos p = dataSnapshot1.getValue(Grupos.class);
                    p.setId(dataSnapshot1.getKey());
                    listaGrupos.add(p);
                }
                adaptadorGrupos = new AdaptadorListaGrupos(listaGrupos, CrearUsuarioActivity.this, R.layout.item_b, new AdaptadorListaGrupos.OnItemClickListener() {
                    @Override
                    public void onItemClick(Grupos city, int position) {


                    }
                });
                recyclerView1.setAdapter(adaptadorGrupos);
                adaptadorGrupos.setCheckedListner(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //
        //   loadAsignaturas();

        btnCrearUsuario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //rellenamos el formulario , enlazmaos las variables con los datos introducidos  ,los datos son String
                emailS = email.getText().toString();
                contraseñaS = contraseña.getText().toString();
                nombreS = nombre.getText().toString();
                apellido1S = apellido1.getText().toString();
                apellido2S = apellido2.getText().toString();
                edadS = edad.getText().toString();
                tipoS = tipo.getText().toString();

                asignaturas = " ";
                grupo = "";

                if (isNumeric(edadS) == true) {
                    if (!emailS.isEmpty() && !contraseñaS.isEmpty() && !nombreS.isEmpty() && !apellido1S.isEmpty() && !edadS.isEmpty()) {
                        mAuth.createUserWithEmailAndPassword(emailS, contraseñaS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                //edad recibe un string del editex , pasmos el string recibido a entero //edad del usuario
                                edadI = Integer.parseInt(edadS);
                                //instancia de la bbd
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                refBBD = database.getReference("Usuarios/");
                                Usuario usuario = new Usuario();
                                usuario.setEmail(emailS);
                                usuario.setNombre(nombreS);
                                usuario.setApellido1(apellido1S);
                                usuario.setApellido2(apellido2S);
                                usuario.setEdad(edadI);
                                usuario.setContraseña(contraseñaS);
                                usuario.setGrupo(grupo);
                                usuario.setContraseña(asignaturas);
                                usuario.setTipo(tipoS);
                                usuario.setImagen("https://firebasestorage.googleapis.com/v0/b/proyecto-fct-83b84.appspot.com/o/cuenta.png?alt=media&token=9b30a70e-28c2-4e29-be65-18c599d09ffb");
                                refBBD.push().setValue(usuario);

                            }
                        });

                    } else {
                        Toast.makeText(CrearUsuarioActivity.this, "rellene ls campos", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(CrearUsuarioActivity.this, "la edad debeser un numero", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void loadAsignaturas() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        String userid = firebaseUser.getUid();

        final List<Asignatura> asignaturas = new ArrayList<>();
        database.getReference().child("Asignaturas").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String nombre = ds.child("nombre").getValue().toString();
                        String id = ds.getKey();
                        Asignatura asignatura = new Asignatura();
                        asignatura.setNombre(nombre);
                        asignaturas.add(asignatura);

                    }
                    ArrayAdapter<Asignatura> asignaturaArrayAdapter = new ArrayAdapter<>(CrearUsuarioActivity.this, android.R.layout.simple_list_item_1, asignaturas);
                    spinner.setAdapter(asignaturaArrayAdapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            ciudadSeleccionada = parent.getItemAtPosition(position).toString();
                            y.setText("asignaturas :" + ciudadSeleccionada);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }

                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}