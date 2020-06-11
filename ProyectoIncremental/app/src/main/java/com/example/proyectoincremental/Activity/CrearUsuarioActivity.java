
package com.example.proyectoincremental.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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


    private FirebaseDatabase database;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private DatabaseReference refBBD, refBBD1, refBBD2, refBBD3, refBBD4, refBBD5, refBBD6, refBBD0, refBBD7, referenceEventos, referenceEventos2;

    private AdaptadorListaAsignturas adaptadorListaAsignturas;
    private AdaptadorListaGrupos adaptadorListaGrupos;

    private List<Asignatura> listaAsignaturas;
    private List<Grupos> listaGrupos;
    private List<String> listaGruposS, listaIdGruposs, listaAsignaturasS;

    private RecyclerView recyclerViewListaAsignatura, recyclerViewListaGrupos;
    private LinearLayoutManager mLayoutManager, mLayoutManager1;

    private String f = "", a = "", b = "";
    private String emailS = "", contraseñaS = "", nombreS = "", apellido1S = "", apellido2S = "", edadS = "";
    private String tipoSeleccion = "";
    private String userid;
    private String[] asignaturasusurio;
    private String gruposusurio;
    private String idUsuario;
    private int edadI, recuperamos_variable_integer;

    private EditText email, contraseña, nombre, apellido1, apellido2, edad, foto;
    private Spinner spinner;
    private Button btnCrearUsuario, btnEditUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuariodentro);

        //Variables de firebase
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        //ID usuario
        userid = firebaseUser.getUid();

        //Editex del formulario
        nombre = findViewById(R.id.nombre);
        email = findViewById(R.id.email);
        contraseña = findViewById(R.id.contraseña);
        apellido1 = findViewById(R.id.apellido1);
        apellido2 = findViewById(R.id.apellido2);
        edad = findViewById(R.id.edad);
        foto = findViewById(R.id.fotoedi);

        //Btns y wiwets
        btnEditUsuario = findViewById(R.id.btnEditarUsuariodentro);
        spinner = findViewById(R.id.tipo);
        btnCrearUsuario = findViewById(R.id.btnCrearUsuariodentro);

        //Recicler de la lista grupos
        recyclerViewListaAsignatura = (RecyclerView) findViewById(R.id.listaA);
        recyclerViewListaAsignatura.setHasFixedSize(true);
        recyclerViewListaAsignatura.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(this);
        recyclerViewListaAsignatura.setLayoutManager(mLayoutManager);

        //Recicler de la lista asignaturas
        recyclerViewListaGrupos = (RecyclerView) findViewById(R.id.listaG);
        recyclerViewListaGrupos.setHasFixedSize(true);
        recyclerViewListaGrupos.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager1 = new LinearLayoutManager(this);
        recyclerViewListaGrupos.setLayoutManager(mLayoutManager1);

        //Listas
        listaAsignaturas = new ArrayList<Asignatura>();
        listaGrupos = new ArrayList<Grupos>();
        listaGruposS = new ArrayList<String>();
        listaIdGruposs = new ArrayList<String>();
        listaAsignaturasS = new ArrayList<String>();


        //-----------------------------------------------------------------------------------------------------------------------------//
        //SI RECIbE INFORMACION PODEMOS EDITAR USUARIO
        //-----------------------------------------------------------------------------------------------------------------------------//
        if (getIntent().getExtras() != null) {
            //ENLAZAMOS LOS DATOS RECIBIDOS CON LOS EDITEX
            email.setText(getIntent().getExtras().getString("Email"));
            nombre.setText(getIntent().getExtras().getString("NombreLocal"));
            apellido1.setText(getIntent().getExtras().getString("Apellido1"));
            apellido2.setText(getIntent().getExtras().getString("Apellido2"));
            foto.setText(getIntent().getExtras().getString("Foto"));

            idUsuario = getIntent().getExtras().getString("Id");

            String tiposss = getIntent().getStringExtra("Tipo");
            asignaturasusurio = getIntent().getStringExtra("Asignaturas").split(",");
            for (String a : asignaturasusurio) {
                if (!a.isEmpty()) {
                    listaAsignaturasS.add(a);

                }
            }
            gruposusurio = getIntent().getStringExtra("Grupos");
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
                    tipoSeleccion = parent.getItemAtPosition(position).toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            cargarAsignaturas();

            //LISTAR TODAS LAS ASIGNATURAS Y MARCAR LAS QUE YA TIENE ASIGNADAS
            adaptadorListaAsignturas = new AdaptadorListaAsignturas(listaAsignaturas, CrearUsuarioActivity.this, R.layout.item_a, new AdaptadorListaAsignturas.OnItemClickListener() {


                @Override
                public void onItemClick(Asignatura asignatura, int position, CheckBox checkBoxx) {

                    if (checkBoxx.isChecked()) {
                        //alomjor funcina comentarlo con agus ,cremos otro nodo, preguntar a vr si se le ocurre como realizar esto cuando presionesmos el boton
                        listaAsignaturasS.add(asignatura.getNombre());
                        String a = "";
                        for (int i = 0; i < listaAsignaturasS.size(); i++) {
                            a += listaAsignaturasS.get(i) + ",";
                        }
                        if (!a.isEmpty()) {
                            a = a.substring(0, a.length() - 1);
                        }

                        refBBD5 = database.getReference("Usuarios").child(idUsuario).child("asignaturas");
                        refBBD5.setValue(a);

                        refBBD = database.getReference("AsignaturasDefinidas").child(idUsuario).child(asignatura.getId());
                        refBBD.setValue(asignatura);


                    } else if (!checkBoxx.isChecked()) {
                        listaAsignaturasS.remove(asignatura.getNombre());
                        String a = "";
                        for (int i = 0; i < listaAsignaturasS.size(); i++) {
                            a += listaAsignaturasS.get(i) + ",";
                        }
                        if (!a.isEmpty()) {
                            a = a.substring(0, a.length() - 1);
                        }
                        refBBD5 = database.getReference("Usuarios").child(idUsuario).child("asignaturas");
                        refBBD5.setValue(a);
                        //alomjor funcina comentarlo con agus ,cramos otro nodo

                        refBBD = database.getReference("AsignaturasDefinidas").child(idUsuario).child(asignatura.getId());
                        refBBD.removeValue();

                    }
                    //LISTA DE ASIGNATURAS SELECIONADAS ACTIVA EL CHECK

                    a = listaAsignaturasS.toString();

                }

            }, asignaturasusurio);


            recyclerViewListaAsignatura.setAdapter(adaptadorListaAsignturas);

            cargarGrupos();
            adaptadorListaGrupos = new AdaptadorListaGrupos(listaGrupos, CrearUsuarioActivity.this, R.layout.item_b, new AdaptadorListaGrupos.OnItemClickListener() {
                @Override
                public void onItemClick(Grupos grupos, int position, CheckBox checkBox) {
                    if (checkBox.isChecked()) {
                        refBBD6 = database.getReference("Usuarios").child(idUsuario).child("grupo");
                        refBBD7 = database.getReference("Usuarios").child(idUsuario).child("idgrupo");
                        refBBD0 = database.getReference("GruposDefinidos").child(grupos.getId()).child(idUsuario);
                        refBBD6.setValue(grupos.getNombreGrupo());
                        refBBD7.setValue(grupos.getId());
                        refBBD0.setValue("");
                        Toast.makeText(CrearUsuarioActivity.this, "solo se guardara el ultimo selecionado", Toast.LENGTH_LONG).show();

                    } else if (!checkBox.isChecked()) {
                        refBBD6.setValue("");
                        refBBD7.setValue("");
                        refBBD0 = database.getReference("GruposDefinidos").child(grupos.getId()).child(idUsuario);
                        refBBD0.removeValue();
                    }
                    b = listaIdGruposs.toString();
                    f = listaGruposS.toString();
                }

            }, gruposusurio);
            recyclerViewListaGrupos.setAdapter(adaptadorListaGrupos);


            //BTN EDITAR USUARIO
            btnEditUsuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userid = firebaseUser.getUid();
                    nombreS = nombre.getText().toString();
                    String fotoS = foto.getText().toString();
                    apellido1S = apellido1.getText().toString();
                    apellido2S = apellido2.getText().toString();
                    edadS = edad.getText().toString();
                    edadI = Integer.parseInt(edadS);

                    refBBD = database.getReference("Usuarios").child(idUsuario).child("nombre");
                    refBBD1 = database.getReference("Usuarios").child(idUsuario).child("apellido1");
                    refBBD2 = database.getReference("Usuarios").child(idUsuario).child("apellido2");
                    refBBD3 = database.getReference("Usuarios").child(idUsuario).child("edad");
                    refBBD4 = database.getReference("Usuarios").child(idUsuario).child("tipo");
                    refBBD5 = database.getReference("Usuarios").child(idUsuario).child("asignaturas");
                    DatabaseReference refBBDIMG = database.getReference("Usuarios").child(idUsuario).child("imagen");


                    refBBD.setValue(nombreS);
                    refBBD1.setValue(apellido1S);
                    refBBD2.setValue(apellido2S);
                    refBBD3.setValue(edadI);
                    refBBD4.setValue(tipoSeleccion);
                    refBBD5.setValue(a);
                    if (fotoS.isEmpty()) {
                        refBBDIMG.setValue("https://firebasestorage.googleapis.com/v0/b/proyecto-fct-83b84.appspot.com/o/cuenta.png?alt=media&token=9b30a70e-28c2-4e29-be65-18c599d09ffb");
                    } else {
                        refBBDIMG.setValue(fotoS);
                    }
                    onBackPressed();
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

                    tipoSeleccion = parent.getItemAtPosition(position).toString();
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
                    listaAsignaturas.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        Asignatura p = dataSnapshot1.getValue(Asignatura.class);
                        p.setId(dataSnapshot1.getKey());
                        listaAsignaturas.add(p);
                    }

                    adaptadorListaAsignturas = new AdaptadorListaAsignturas(listaAsignaturas, CrearUsuarioActivity.this, R.layout.item_a, new AdaptadorListaAsignturas.OnItemClickListener() {

                        @Override
                        public void onItemClick(Asignatura city, int position, CheckBox checkBoxx) {
                            if (checkBoxx.isChecked()) {

                                listaAsignaturasS.add(city.getNombre());
                                // refBBD = database.getReference("AsignaturasDefinidas").child(uu);
                                //  refBBD.setValue(city);


                            } else if (!checkBoxx.isChecked()) {
                                // refBBD = database.getReference("AsignaturasDefinidas").child(uu);
                                // refBBD.removeValue();

                                listaAsignaturasS.remove(city.getNombre());

                            }
                            a = listaAsignaturasS.toString();

                        }
                    }, null);
                    recyclerViewListaAsignatura.setAdapter(adaptadorListaAsignturas);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
            //REFERENCIA A GRUPOS
            referenceEventos2 = database.getInstance().getReference("Grupos").child(userid);
            referenceEventos2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    listaGrupos.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Grupos p = dataSnapshot1.getValue(Grupos.class);
                        p.setId(dataSnapshot1.getKey());
                        listaGrupos.add(p);
                    }
                    adaptadorListaGrupos = new AdaptadorListaGrupos(listaGrupos, CrearUsuarioActivity.this, R.layout.item_b, new AdaptadorListaGrupos.OnItemClickListener() {
                        @Override
                        public void onItemClick(Grupos grupos, int position, CheckBox checkBox) {
                            if (checkBox.isChecked()) {
                                listaGruposS.add(grupos.getNombreGrupo());
                                listaIdGruposs.add(grupos.getId());

                            } else if (!checkBox.isChecked()) {
                                listaGruposS.remove(grupos.getNombreGrupo());
                                listaIdGruposs.add(grupos.getId());

                            }
                            f = listaGruposS.toString();
                            b = listaIdGruposs.toString();

                        }

                    }, null);
                    recyclerViewListaGrupos.setAdapter(adaptadorListaGrupos);
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
                                        String b = "";
                                        String c = "";


                                        for (int i = 0; i < listaAsignaturasS.size(); i++) {
                                            a += listaAsignaturasS.get(i) + ",";
                                        }
                                        for (int i = 0; i < listaIdGruposs.size(); i++) {
                                            b += listaIdGruposs.get(i) + ",";
                                        }
                                        for (int i = 0; i < listaGruposS.size(); i++) {
                                            c += listaGruposS.get(i) + ",";
                                        }
                                        if (!b.isEmpty()) {
                                            b = b.substring(0, b.length() - 1);
                                        }
                                        if (!a.isEmpty()) {
                                            a = a.substring(0, a.length() - 1);
                                        }
                                        if (!c.isEmpty()) {
                                            c = c.substring(0, c.length() - 1);
                                        }
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
                                        usuario.setGrupo(c);
                                        usuario.setIdgrupo(b);
                                        usuario.setAsignaturas(a);
                                        usuario.setTipo(tipoSeleccion);
                                        usuario.setImagen("https://firebasestorage.googleapis.com/v0/b/proyecto-fct-83b84.appspot.com/o/cuenta.png?alt=media&token=9b30a70e-28c2-4e29-be65-18c599d09ffb");
                                        usuario.setId(task.getResult().getUser().getUid());
                                        refBBD.setValue(usuario);
                                        Toast.makeText(CrearUsuarioActivity.this, "se a creado correctamente", Toast.LENGTH_LONG).show();

                                        onBackPressed();
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
                listaAsignaturas.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Asignatura p = dataSnapshot1.getValue(Asignatura.class);
                    p.setId(dataSnapshot1.getKey());
                    listaAsignaturas.add(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void cargarGrupos() {

        referenceEventos2 = database.getInstance().getReference("Grupos").child(userid);
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