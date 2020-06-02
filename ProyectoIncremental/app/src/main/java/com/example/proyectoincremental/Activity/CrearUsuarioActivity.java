
package com.example.proyectoincremental.Activity;

import android.app.AlertDialog;

import android.content.SharedPreferences;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.proyectoincremental.Activity.CreateUserActivity.isNumeric;

public class CrearUsuarioActivity extends AppCompatActivity {
    private AdaptadorListaAsignturas adaptadorEventos;
    private AdaptadorListaGrupos adaptadorGrupos;

    private List<Asignatura> listaEventos;
    private List<Grupos> listaGrupos;
    private List<String> listaGruposs;
    private List<String> listaAsignaturas;


    private FirebaseDatabase database, database1;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private DatabaseReference referenceEventos, referenceEventos2;
    private DatabaseReference refBBD, refBBD1, refBBD2, refBBD3, refBBD4, refBBD5, refBBD6;
    private FirebaseAuth mAuth;


    private RecyclerView recyclerView, recyclerView1;
    private LinearLayoutManager mLayoutManager, mLayoutManager1;
    private AlertDialog.Builder builder;

    private String f = "", a = "";
    private String emailS = "", contraseñaS = "", nombreS = "", apellido1S = "", apellido2S = "", edadS = "", asignaturas = "", grupo = "", tipoS;
    private String ciudadSeleccionada = "";
    private EditText email, contraseña, nombre, apellido1, apellido2, edad, tipo;
    private Spinner spinner;
    private String userid;
    private int edadI, recuperamos_variable_integer;
    private Switch aSwitchA;

    private Button btnCrearUsuario, btnGoLogin, btnEditUsuario, btnlistarG, btnlistarA;
    private SharedPreferences prefs, sfd;


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
        btnEditUsuario = findViewById(R.id.btnEditarUsuariodentro);
        // btnlistarA = findViewById(R.id.btnlistarA);
        // btnlistarG = findViewById(R.id.btnlistarG);

        apellido2 = findViewById(R.id.apellido2);
        edad = findViewById(R.id.edad);
        spinner = findViewById(R.id.tipo);
        btnCrearUsuario = findViewById(R.id.btnCrearUsuariodentro);
        //  refBBD2 = database.getReference("Asignaturas/");
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
        listaGruposs = new ArrayList<String>();
        listaAsignaturas = new ArrayList<String>();


        auth = FirebaseAuth.getInstance();

        firebaseUser = auth.getCurrentUser();

        userid = firebaseUser.getUid();

        //INFOMACION QUE TRAEMOS DEL ANTERIOR ACTIVITY para poder edditar
        if (getIntent().getExtras() != null) {

            email.setText(getIntent().getExtras().getString("Email"));
            nombre.setText(getIntent().getExtras().getString("NombreLocal"));
            apellido1.setText(getIntent().getExtras().getString("Apellido1"));
            apellido2.setText(getIntent().getExtras().getString("Apellido2"));
            String tiposss = getIntent().getStringExtra("Tipo");
            //   String asignaturassss = getIntent().getStringExtra("Asignaturas");


            //taremos la edad que es un entero , lo convertimos en sting ya que no nos deha pasarle al editex un entero par editarlo volver a ser un entero
            recuperamos_variable_integer = getIntent().getIntExtra("Edad", 0);
            edadS = Integer.toString(recuperamos_variable_integer);
            edad.setText(edadS);
            contraseña.setText(getIntent().getExtras().getString("Contraseña"));

            btnCrearUsuario.setVisibility(GONE);
            btnEditUsuario.setVisibility(VISIBLE);

            ArrayList<String> lista = new ArrayList<>();
            if (tiposss.equals("Profesor")){
                lista.add(tiposss);
                lista.add("Alumno");

            }else{                lista.add(tiposss);
                lista.add("Profesor");
            }

            ArrayAdapter<String> asignaturaArrayAdapter = new ArrayAdapter<>(CrearUsuarioActivity.this, android.R.layout.simple_list_item_1, lista);
            spinner.setAdapter(asignaturaArrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    ciudadSeleccionada = parent.getItemAtPosition(position).toString();
                    //y.setText("asignaturas :" + ciudadSeleccionada);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
     /*       adaptadorEventos = new AdaptadorListaAsignturas(listaEventos, CrearUsuarioActivity.this, R.layout.item_a, new AdaptadorListaAsignturas.OnItemClickListener() {


                @Override
                public void onItemClick(Asignatura city, int position, CheckBox checkBoxx) {


                    if (checkBoxx.isChecked()) {

                        listaAsignaturas.add(city.getNombre());

                    } else if (!checkBoxx.isChecked()) {

                        listaAsignaturas.remove(city.getNombre());

                    }

                    a = listaAsignaturas.toString();

                }
            });
            recyclerView.setAdapter(adaptadorEventos);
*/
        }
        //BTN EDITAR USUARIO
        btnEditUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                String userid = firebaseUser.getUid();
                nombreS = nombre.getText().toString();
                apellido1S = apellido1.getText().toString();
                apellido2S = apellido2.getText().toString();
                edadS = edad.getText().toString();
                edadI = Integer.parseInt(edadS);

                refBBD = database.getReference("Usuarios").child(userid).child("nombre");
                refBBD1 = database.getReference("Usuarios").child(userid).child("apellido1");
                refBBD2 = database.getReference("Usuarios").child(userid).child("apellido2");
                refBBD3 = database.getReference("Usuarios").child(userid).child("edad");
                refBBD4 = database.getReference("Usuarios").child(userid).child("tipo");
                refBBD5 = database.getReference("Usuarios").child(userid).child("asignaturas");
                refBBD6 = database.getReference("Usuarios").child(userid).child("grupos");


                refBBD.setValue(nombreS);
                refBBD1.setValue(apellido1S);
                refBBD2.setValue(apellido2S);
                refBBD3.setValue(edadI);
                refBBD4.setValue(ciudadSeleccionada);
                // refBBD5.setValue(a);
                //refBBD6.setValue(f);
                //refBBD2.setValue(emailS);

            }
        });
        if (getIntent().getExtras() == null) {

            //LISTA DE TIPOS DE USUATIOS
            ArrayList<String> lista = new ArrayList<>();
            lista.add("Alumno");
            lista.add("Profesor");
            ArrayAdapter<String> asignaturaArrayAdapter = new ArrayAdapter<>(CrearUsuarioActivity.this, android.R.layout.simple_list_item_1, lista);
            spinner.setAdapter(asignaturaArrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    ciudadSeleccionada = parent.getItemAtPosition(position).toString();
                    //y.setText("asignaturas :" + ciudadSeleccionada);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        //REFERENCIA ASIGNATURAS
        referenceEventos = database.getInstance().getReference("Asignaturas").child(userid);
        referenceEventos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Asignatura p = dataSnapshot1.getValue(Asignatura.class);
                    p.setId(dataSnapshot1.getKey());
                    listaEventos.add(p);
                }

                adaptadorEventos = new AdaptadorListaAsignturas(listaEventos, CrearUsuarioActivity.this, R.layout.item_a, new AdaptadorListaAsignturas.OnItemClickListener() {

                    @Override
                    public void onItemClick(Asignatura city, int position, CheckBox checkBoxx) {
                        if (checkBoxx.isChecked()) {

                            listaAsignaturas.add(city.getNombre());

                        } else if (!checkBoxx.isChecked()) {

                            listaAsignaturas.remove(city.getNombre());

                        }
                        a = listaAsignaturas.toString();

                    }
                });
                recyclerView.setAdapter(adaptadorEventos);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        //REFERENCIA A GRUPOS
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
                    public void onItemClick(Grupos grupos, int position, CheckBox checkBox) {
                        if (checkBox.isChecked()) {
                            listaGruposs.add(grupos.getNombreGrupo());
                        } else if (!checkBox.isChecked()) {
                            listaGruposs.remove(grupos.getNombreGrupo());
                        }
                        f = listaGruposs.toString();
                    }

                });
                recyclerView1.setAdapter(adaptadorGrupos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //BTN CREAR USUARIO
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
                                usuario.setGrupo(a);
                                usuario.setAsignaturas(f);
                                usuario.setTipo(ciudadSeleccionada);
                                usuario.setImagen("https://firebasestorage.googleapis.com/v0/b/proyecto-fct-83b84.appspot.com/o/cuenta.png?alt=media&token=9b30a70e-28c2-4e29-be65-18c599d09ffb");
                                String u = refBBD.push().getKey();
                                usuario.setId(u);
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


}