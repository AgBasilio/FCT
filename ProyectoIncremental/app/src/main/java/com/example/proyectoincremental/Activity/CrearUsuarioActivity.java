
package com.example.proyectoincremental.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.TextView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.proyectoincremental.Adaptadores.AdaptadorListaAsignturas;
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

    private List<Asignatura> asignaturaList;
    private List<Grupos> listaGrupos;
    private Grupos grupoParaGuardar;


    private List<String> listaIdsAsignaturasParaGuardar;

    private FirebaseDatabase database;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;

    private DatabaseReference refBBD, refBBD1, refBBD2, refBBD3, refBBD4, refBBD5, refBBD6, refBBD0, refBBD7, referenceEventos, referenceEventos2;


    private RecyclerView recyclerViewListaAsignatura, recyclerViewListaGrupos;
    private LinearLayoutManager mLayoutManager, mLayoutManager1;

    private TextView labelGrupos;
    private String emailS = "", contraseñaS = "", nombreS = "", apellido1S = "", apellido2S = "", edadS = "";
    private String tipoUsuarioSeleccionado = "";

    private Spinner spinner, grupoSelector;
    private String userid;
    private int edadI, recuperamos_variable_integer;

    private Button btnCrearUsuario, btnEditUsuario;
    private String id;


    private String idUsuario;

    private EditText email, contrasenna, nombre, apellido1, apellido2, edad, foto;

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
        contrasenna = findViewById(R.id.contraseña);
        apellido1 = findViewById(R.id.apellido1);
        apellido2 = findViewById(R.id.apellido2);
        edad = findViewById(R.id.edad);
        foto = findViewById(R.id.fotoedi);

        labelGrupos = findViewById(R.id.gruposLabel);

        //Btns y wiwets
        btnEditUsuario = findViewById(R.id.btnEditarUsuariodentro);
        spinner = findViewById(R.id.tipo);
        grupoSelector = findViewById(R.id.grupoSelector);
        btnCrearUsuario = findViewById(R.id.btnCrearUsuariodentro);

        //Recicler de la lista grupos
        recyclerViewListaAsignatura = (RecyclerView) findViewById(R.id.listaA);
        recyclerViewListaAsignatura.setHasFixedSize(true);
        recyclerViewListaAsignatura.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(this);
        recyclerViewListaAsignatura.setLayoutManager(mLayoutManager);


        //Listas
        asignaturaList = new ArrayList<Asignatura>();
        listaGrupos = new ArrayList<Grupos>();
        grupoParaGuardar = new Grupos();//new ArrayList<String>();
        listaIdsAsignaturasParaGuardar = new ArrayList<String>();


        //CARGAR ASIGNATURAS
        //REFERENCIA ASIGNATURAS OBTENER LISTA DE TODAS LAS ASIGNATURAS
        referenceEventos = database.getInstance().getReference("Asignaturas").child(userid);
        referenceEventos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                asignaturaList.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Asignatura p = dataSnapshot1.getValue(Asignatura.class);
                    p.setId(dataSnapshot1.getKey());
                    asignaturaList.add(p);
                }

                //cargar grupos
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

                        //Lista grupos para el usuario
                        listaGrupos.add(0, new Grupos());

                        ArrayAdapter<Grupos> gruposArrayAdapter = new ArrayAdapter<>(CrearUsuarioActivity.this, android.R.layout.simple_list_item_1, listaGrupos);
                        grupoSelector.setAdapter(gruposArrayAdapter);
                        grupoSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                grupoParaGuardar = (Grupos) parent.getItemAtPosition(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        //LISTA DE TIPOS DE USUATIOS
                        ArrayList<String> lista = new ArrayList<>();
                        lista.add(Usuario.TIPO_ALUMNO);
                        lista.add(Usuario.TIPO_PROFESOR);
                        ArrayAdapter<String> asignaturaArrayAdapter = new ArrayAdapter<>(CrearUsuarioActivity.this, android.R.layout.simple_list_item_1, lista);
                        spinner.setAdapter(asignaturaArrayAdapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                tipoUsuarioSeleccionado = parent.getItemAtPosition(position).toString();
                                if (((String) parent.getItemAtPosition(position)).equals(Usuario.TIPO_PROFESOR)) {
//                                    recyclerView1.setVisibility(view.GONE);
                                    labelGrupos.setVisibility(view.GONE);
                                    grupoSelector.setVisibility(view.GONE);
                                } else {
//                                    recyclerView1.setVisibility(view.VISIBLE);
                                    labelGrupos.setVisibility(view.VISIBLE);
                                    grupoSelector.setVisibility(view.VISIBLE);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        AdaptadorListaAsignturas.OnItemClickListener onItemClickListenerAdaptadorAsignaturas = new AdaptadorListaAsignturas.OnItemClickListener() {

                            @Override
                            public void onItemClick(Asignatura asignatura, int position, CheckBox checkBoxx) {

                                //EDITAR usuario, ASIGNATURA, checked
                                if (checkBoxx.isChecked()) {
                                    //si la asignatura a guardar no esta, la agrego
                                    if (!listaIdsAsignaturasParaGuardar.contains(asignatura.getId())) {
                                        listaIdsAsignaturasParaGuardar.add(asignatura.getId());
                                    }
                                    //EDITAR usuario, ASIGNATURA, NO checked
                                } else if (!checkBoxx.isChecked()) {
                                    //si la asignatura a quitar esta, la quito
                                    if (listaIdsAsignaturasParaGuardar.contains(asignatura.getId())) {
                                        listaIdsAsignaturasParaGuardar.remove(asignatura.getId());
                                    }
                                }
                            }

                        };

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
                            id = getIntent().getExtras().getString("Id");
                            String tiposss = getIntent().getStringExtra("Tipo");
                            for (String a : getIntent().getStringExtra("Asignaturas").split(",")) {
                                if (!a.isEmpty()) {
                                    listaIdsAsignaturasParaGuardar.add(a);
                                }
                            }

                            final String ID_GRUPO_ORIGINAL = getIntent().getStringExtra("IdGrupo");
                            grupoParaGuardar = ObtenerGrupoParaGuardar(ID_GRUPO_ORIGINAL);


                            recuperamos_variable_integer = getIntent().getIntExtra("Edad", 0);
                            edadS = Integer.toString(recuperamos_variable_integer);
                            edad.setText(edadS);
                            contrasenna.setText(getIntent().getExtras().getString("Contraseña"));
                            contrasenna.setEnabled(false);
                            email.setEnabled(false);

                            //OCULTAR BTN CREAR USUARIO  USUARIO Y MOSTRAMOS EL BTN EDITAR
                            btnCrearUsuario.setVisibility(GONE);
                            btnEditUsuario.setVisibility(VISIBLE);

                            if (tiposss.equals(Usuario.TIPO_PROFESOR)) {
                                spinner.setSelection(1);//0:Alumnoo;1:Profesor
                            }

                            //LISTAR TODAS LAS ASIGNATURAS Y MARCAR LAS QUE YA TIENE ASIGNADAS
                            recyclerViewListaAsignatura.setAdapter(new AdaptadorListaAsignturas(asignaturaList, listaIdsAsignaturasParaGuardar, CrearUsuarioActivity.this, R.layout.item_a, onItemClickListenerAdaptadorAsignaturas));

                            if (tiposss.equals(Usuario.TIPO_PROFESOR)) {
                                spinner.setSelection(1);//0:Alumnoo;1:Profesor
                            }

                            //si hay un grupo seleccionado
                            grupoSelector.setSelection(((ArrayAdapter) grupoSelector.getAdapter()).getPosition(grupoParaGuardar));

                            //GUARDAR USUARIO EDITADO
                            //BTN EDITAR USUARIO
                            btnEditUsuario.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String asignaturasParaGuardar = listaToString(listaIdsAsignaturasParaGuardar);


                                    nombreS = nombre.getText().toString();
                                    String fotoS = foto.getText().toString();
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
                                    refBBD6 = database.getReference("Usuarios").child(id).child("grupo");
                                    refBBD7 = database.getReference("Usuarios").child(id).child("idgrupo");
                                    DatabaseReference refBBDIMG = database.getReference("Usuarios").child(id).child("imagen");


                                    refBBD.setValue(nombreS);
                                    refBBD1.setValue(apellido1S);
                                    refBBD2.setValue(apellido2S);
                                    refBBD3.setValue(edadI);

                                    refBBD4.setValue(tipoUsuarioSeleccionado);
                                    refBBD5.setValue(asignaturasParaGuardar);

                                    //quitar todas las asignatura definidas para este usuario
                                    database.getReference("AsignaturasDefinidas").child(id).removeValue();
                                    //agregar a asignatura definida
                                    for (String idAsignatura : listaIdsAsignaturasParaGuardar) {
                                        //buscar asignatura por id y annadir a asignatura definida
                                        database.getReference("AsignaturasDefinidas").child(id).child(idAsignatura).setValue(asignaturaList.get(asignaturaList.indexOf(new Asignatura(idAsignatura))));
                                    }

                                    //quitar de grupo definido original
                                    database.getReference("GruposDefinidos").child(ID_GRUPO_ORIGINAL).child(id).removeValue();

                                    if (tipoUsuarioSeleccionado.equals(Usuario.TIPO_PROFESOR)) {
                                        refBBD6.setValue("");
                                        refBBD7.setValue("");
                                    } else {
                                        refBBD6.setValue(grupoParaGuardar.getNombreGrupo());
                                        refBBD7.setValue(grupoParaGuardar.getId());
                                        //si es alumno
                                        //agregar a grupo definido actual
                                        if (!grupoParaGuardar.getId().isEmpty())//si es que hay algoo
                                            database.getReference("GruposDefinidos").child(grupoParaGuardar.getId()).child(id).setValue(":)");
                                    }

                                    if (fotoS.isEmpty()) {
                                        refBBDIMG.setValue("https://firebasestorage.googleapis.com/v0/b/proyecto-fct-83b84.appspot.com/o/cuenta.png?alt=media&token=9b30a70e-28c2-4e29-be65-18c599d09ffb");
                                    } else {
                                        refBBDIMG.setValue(fotoS);
                                    }

                                    Toast.makeText(CrearUsuarioActivity.this, "Usuario actualizado", Toast.LENGTH_LONG).show();

                                    onBackPressed();
                                }
                            });
                        } else
                            //-----------------------------------------------------------------------------------------------------------------------------//
                            //SI NO RECIBE INFOMACION PODEMOS CREAR USUARIOOS
                            //-----------------------------------------------------------------------------------------------------------------------------//
                            if (getIntent().getExtras() == null) {
                                //adaptador para lista de asignaturas
                                recyclerViewListaAsignatura.setAdapter(new AdaptadorListaAsignturas(asignaturaList, null, CrearUsuarioActivity.this, R.layout.item_a, onItemClickListenerAdaptadorAsignaturas));
                                //TODO: crear usuario
                                //BTN CREAR USUARIO
                                btnCrearUsuario.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        //rellenamos el formulario , enlazmaos las variables con los datos introducidos  ,los datos son String
                                        emailS = email.getText().toString();
                                        contraseñaS = contrasenna.getText().toString();
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

                                                FirebaseAuth authParaCrearUsuario;

                                                try {
                                                    FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), o, "otro");
                                                    authParaCrearUsuario = FirebaseAuth.getInstance(myApp);
                                                } catch (IllegalStateException e) {
                                                    authParaCrearUsuario = FirebaseAuth.getInstance(FirebaseApp.getInstance("otro"));
                                                }

                                                final FirebaseAuth finalAuthParaCrearUsuario = authParaCrearUsuario;
                                                authParaCrearUsuario.createUserWithEmailAndPassword(emailS, contraseñaS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {

                                                            id = task.getResult().getUser().getUid();
                                                            String asignaturasParaGuardar = listaToString(listaIdsAsignaturasParaGuardar);

                                                            //edad recibe un string del editex , pasmos el string recibido a entero //edad del usuario
                                                            edadI = Integer.parseInt(edadS);
                                                            //instancia de la bbd
                                                            refBBD = database.getReference("Usuarios/").child(id);
                                                            Usuario usuario = new Usuario();
                                                            usuario.setEmail(emailS);
                                                            usuario.setNombre(nombreS);
                                                            usuario.setApellido1(apellido1S);
                                                            usuario.setApellido2(apellido2S);
                                                            usuario.setEdad(edadI);

                                                            usuario.setContrasenna(contraseñaS);
                                                            usuario.setGrupo(grupoParaGuardar.getNombreGrupo());
                                                            usuario.setIdgrupo(grupoParaGuardar.getId());
                                                            usuario.setAsignaturas(asignaturasParaGuardar);
                                                            usuario.setTipo(tipoUsuarioSeleccionado);

                                                            usuario.setImagen("https://firebasestorage.googleapis.com/v0/b/proyecto-fct-83b84.appspot.com/o/cuenta.png?alt=media&token=9b30a70e-28c2-4e29-be65-18c599d09ffb");
                                                            usuario.setId(id);

                                                            //si es profesor borrar el grupo
                                                            if (tipoUsuarioSeleccionado.equals(Usuario.TIPO_PROFESOR)) {
                                                                usuario.setGrupo("");
                                                                usuario.setIdgrupo("");
                                                            }

                                                            refBBD.setValue(usuario);

                                                            //agregar a asignatura definida
                                                            for (String idAsignatura : listaIdsAsignaturasParaGuardar) {
                                                                //buscar asignatura por id y annadir a asignatura definida
                                                                database.getReference("AsignaturasDefinidas").child(id).child(idAsignatura).setValue(asignaturaList.get(asignaturaList.indexOf(new Asignatura(idAsignatura))));
                                                            }

                                                            //si es alumno
                                                            if (tipoUsuarioSeleccionado.equals(Usuario.TIPO_ALUMNO) && !grupoParaGuardar.getId().isEmpty()) {
                                                                //agregar a grupo definido actual
                                                                database.getReference("GruposDefinidos").child(grupoParaGuardar.getId()).child(id).setValue(":)");
                                                            }

                                                            Toast.makeText(CrearUsuarioActivity.this, "El usuario se ha creado correctamente", Toast.LENGTH_LONG).show();

                                                            onBackPressed();
                                                        } else {
                                                            Toast.makeText(CrearUsuarioActivity.this, "No se ha creado el usuario", Toast.LENGTH_LONG).show();
                                                        }
                                                        finalAuthParaCrearUsuario.signOut();
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

    private Grupos ObtenerGrupoParaGuardar(String idGrupo) {
        Grupos grupo = new Grupos();
        //recorrer lista de grupos, buscando idGrupo
        for (Grupos g : listaGrupos) {
            if (g.getId().equals(idGrupo)) {
                grupo = g;
                break;
            }
        }
        return grupo;
    }

    private String listaToString(List<String> stringList) {
        String s = stringList.toString().replace(", ", ",");
        return s.substring(1, s.length() - 1);// s=['a'] --> s='a'
    }
}