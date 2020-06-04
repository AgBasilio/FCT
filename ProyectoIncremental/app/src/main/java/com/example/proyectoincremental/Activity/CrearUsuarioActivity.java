
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
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
    private DatabaseReference refBBD, refBBD1, refBBD2, refBBD3, refBBD4, refBBD5, refBBD6, refBBD0;
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
    private String[] asignaturasusurio;
    private String[] gruposusurio;

    private Button btnCrearUsuario, btnGoLogin, btnEditUsuario, btnlistarG, btnlistarA;
    private SharedPreferences prefs, sfd;
    private String id;
    private static String uu;


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
        apellido2 = findViewById(R.id.apellido2);
        edad = findViewById(R.id.edad);

        spinner = findViewById(R.id.tipo);
        btnCrearUsuario = findViewById(R.id.btnCrearUsuariodentro);

        recyclerView = (RecyclerView) findViewById(R.id.listaA);
        recyclerView1 = (RecyclerView) findViewById(R.id.listaG);

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


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        userid = firebaseUser.getUid();


        //-----------------------------------------------------------------------------------------------------------------------------//
        //SI RECIbE INFORMACION PODEMOS EDITAR USUARIO
        //-----------------------------------------------------------------------------------------------------------------------------//
        if (getIntent().getExtras() != null) {
            //ENLAZAMOS LOS DATOS RECIBIDOS CON LOS EDITEX
            email.setText(getIntent().getExtras().getString("Email"));
            nombre.setText(getIntent().getExtras().getString("NombreLocal"));
            apellido1.setText(getIntent().getExtras().getString("Apellido1"));
            apellido2.setText(getIntent().getExtras().getString("Apellido2"));
            id = getIntent().getExtras().getString("Id");
            String tiposss = getIntent().getStringExtra("Tipo");
            asignaturasusurio = getIntent().getStringExtra("Asignaturas").split(",");
            gruposusurio = getIntent().getStringExtra("Grupos").split(",");

            recuperamos_variable_integer = getIntent().getIntExtra("Edad", 0);
            edadS = Integer.toString(recuperamos_variable_integer);
            edad.setText(edadS);
            contraseña.setText(getIntent().getExtras().getString("Contraseña"));
            //OCULTAR BTN CREAR USUARIO  USUARIO Y MOSTRAMOS EL BTN EDITAR
            btnCrearUsuario.setVisibility(GONE);
            btnEditUsuario.setVisibility(VISIBLE);

            //LISTA DE TIPO DE USURIO A ELEGUIR EN UN SPINNER
            ArrayList<String> lista = new ArrayList<>();
            if (tiposss.equals("Profesor")) {
                lista.add(tiposss);
                lista.add("Alumno");

            } else {
                lista.add(tiposss);
                lista.add("Profesor");
            }
            ArrayAdapter<String> asignaturaArrayAdapter = new ArrayAdapter<>(CrearUsuarioActivity.this, android.R.layout.simple_list_item_1, lista);
            spinner.setAdapter(asignaturaArrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ciudadSeleccionada = parent.getItemAtPosition(position).toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            cargarAsignaturas();

            //LISTAR TODAS LAS ASIGNATURAS Y MARCAR LAS QUE YA TIENE ASIGNADAS
            adaptadorEventos = new AdaptadorListaAsignturas(listaEventos, CrearUsuarioActivity.this, R.layout.item_a, new AdaptadorListaAsignturas.OnItemClickListener() {


                @Override
                public void onItemClick(Asignatura asignatura, int position, CheckBox checkBoxx) {

                    if (checkBoxx.isChecked()) {
                        //alomjor funcina comentarlo con agus ,cremos otro nodo, preguntar a vr si se le ocurre como realizar esto cuando presionesmos el boton

                        listaAsignaturas.add(asignatura.getNombre());
                        refBBD = database.getReference("AsignaturasDefinidas").child(id).child(asignatura.getId());
                        refBBD.setValue(asignatura);


                    } else if (!checkBoxx.isChecked()) {

                        //alomjor funcina comentarlo con agus ,cramos otro nodo

                        listaAsignaturas.remove(asignatura.getNombre());
                        refBBD = database.getReference("AsignaturasDefinidas").child(id).child(asignatura.getId());
                        refBBD.removeValue();

                    }
                    //LISTA DE ASIGNATURAS SELECIONADAS ACTIVA EL CHECK

                    a = listaAsignaturas.toString();

                }

            }, asignaturasusurio);


            recyclerView.setAdapter(adaptadorEventos);

            cargarGrupos();
            adaptadorGrupos = new AdaptadorListaGrupos(listaGrupos, CrearUsuarioActivity.this, R.layout.item_b, new AdaptadorListaGrupos.OnItemClickListener() {
                @Override
                public void onItemClick(Grupos grupos, int position, CheckBox checkBox) {
                    if (checkBox.isChecked()) {
                        listaGruposs.add(grupos.getNombreGrupo());
                        refBBD0 = database.getReference("GruposDefinidos").child(grupos.getId()).child(id);
                        refBBD0.setValue("");

                    } else if (!checkBox.isChecked()) {
                        refBBD0 = database.getReference("GruposDefinidos").child(grupos.getId()).child(id);
                        refBBD0.removeValue();
                        listaGruposs.remove(grupos.getNombreGrupo());
                    }
                    f = listaGruposs.toString();
                }

            }, gruposusurio);
            recyclerView1.setAdapter(adaptadorGrupos);


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

                    refBBD = database.getReference("Usuarios").child(id).child("nombre");
                    refBBD1 = database.getReference("Usuarios").child(id).child("apellido1");
                    refBBD2 = database.getReference("Usuarios").child(id).child("apellido2");
                    refBBD3 = database.getReference("Usuarios").child(id).child("edad");
                    refBBD4 = database.getReference("Usuarios").child(id).child("tipo");
                    refBBD5 = database.getReference("Usuarios").child(id).child("asignaturas");
                    refBBD6 = database.getReference("Usuarios").child(id).child("grupos");

                    refBBD.setValue(nombreS);
                    refBBD1.setValue(apellido1S);
                    refBBD2.setValue(apellido2S);
                    refBBD3.setValue(edadI);
                    refBBD4.setValue(ciudadSeleccionada);


                    refBBD5.setValue(a);
                    //refBBD6.setValue(f);
                    //refBBD2.setValue(emailS);

                }
            });
        }

        //-----------------------------------------------------------------------------------------------------------------------------//
        //SI NO RECIBE INFOMACION PODEMOS CREAR USUARIOOS
        //-----------------------------------------------------------------------------------------------------------------------------//
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

            //REFERENCIA ASIGNATURAS
            referenceEventos = database.getInstance().getReference("Asignaturas").child(userid);
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
                        public void onItemClick(Asignatura city, int position, CheckBox checkBoxx) {
                            if (checkBoxx.isChecked()) {

                                listaAsignaturas.add(city.getNombre());
                                // refBBD = database.getReference("AsignaturasDefinidas").child(uu);
                                //  refBBD.setValue(city);


                            } else if (!checkBoxx.isChecked()) {
                                // refBBD = database.getReference("AsignaturasDefinidas").child(uu);
                                // refBBD.removeValue();

                                listaAsignaturas.remove(city.getNombre());

                            }
                            a = listaAsignaturas.toString();

                        }
                    }, null);
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
                    listaGrupos.clear();
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

                    }, null);
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

                            FirebaseOptions o = new FirebaseOptions.Builder()
                                    .setDatabaseUrl("https://proyecto-fct-83b84.firebaseio.com")
                                    .setApiKey("AIzaSyC_XHzyTqoJ7Vn5kegroWEYLxx9M0XovSQ")
                                    .setApplicationId("proyecto-fct-83b84").build();


                            FirebaseAuth authParaCrearUsuario = null;

                            try {
                                FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), o, "otro");
                                authParaCrearUsuario = FirebaseAuth.getInstance(myApp);
                            } catch (IllegalStateException e) {
                                authParaCrearUsuario = FirebaseAuth.getInstance(FirebaseApp.getInstance("AnyAppName"));
                            }

                            authParaCrearUsuario.createUserWithEmailAndPassword(emailS, contraseñaS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String a = "";
                                        for (int i = 0; i < listaAsignaturas.size(); i++) {
                                            a += listaAsignaturas.get(i) + ",";
                                        }
                                        a = a.substring(0, a.length() - 1);
                                        //edad recibe un string del editex , pasmos el string recibido a entero //edad del usuario
                                        edadI = Integer.parseInt(edadS);
                                        //instancia de la bbd
                                        refBBD = database.getReference("Usuarios/").child(task.getResult().getUser().getUid());
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
                                        usuario.setId(task.getResult().getUser().getUid());
                                        refBBD.setValue(usuario);
                                        Toast.makeText(CrearUsuarioActivity.this, "se a creado correctamente", Toast.LENGTH_LONG).show();
                                    } else {

                                        Toast.makeText(CrearUsuarioActivity.this, "no se ha creado", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            authParaCrearUsuario.signOut();

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

    public void cargarAsignaturas() {
        //REFERENCIA ASIGNATURAS OBTENER LISTA DE TODAS LAS ASIGNATURAS
        referenceEventos = database.getInstance().getReference("Asignaturas").child(userid);
        referenceEventos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                listaEventos.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Asignatura p = dataSnapshot1.getValue(Asignatura.class);
                    p.setId(dataSnapshot1.getKey());
                    listaEventos.add(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void cargarGrupos() {

        referenceEventos2 = database1.getInstance().getReference("Grupos").child(userid);
        referenceEventos2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                listaGrupos.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Grupos p = dataSnapshot1.getValue(Grupos.class);
                    p.setId(dataSnapshot1.getKey());
                    listaGrupos.add(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}