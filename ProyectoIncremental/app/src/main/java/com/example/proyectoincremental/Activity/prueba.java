package com.example.proyectoincremental.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.example.proyectoincremental.Utils.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class prueba extends AppCompatActivity {

    private EditText editnombre, editapellido, editemail, editedad, editcontra, editrepetircontra;

    private Button botonfoto, botonadd;

    private Spinner spinnerRol, spinnerGrupo;

    private String email, nombre, apellido, edad,contra, repetircontra,foto,rol,grupo, recordar, idUser,idUsuario;

    private ArrayList<String> asignaturas ;

    private boolean editar= false;

    private ArrayList<String> asigUser;

    private List<Asignatura> asigs;

    private ListView listView;


    private Usuario useredit;


    private ArrayAdapter<String> adapterRol;
    private ArrayAdapter<String> adapterGrupos;
    private ArrayAdapter<String> adapterAsignaturas;


    private static final int GALLERY_INTENT = 1;
    private Uri uri;
    private Boolean fileImg = false;

    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();


        registerForContextMenu(this.listView);


        asigs = new ArrayList<Asignatura>();


       // adapterRol = new ArrayAdapter<String>(getApplicationContext(), R.layout.text_spinner);
        adapterRol.add("Alumno");
        adapterRol.add("Profesor");
        spinnerRol.setAdapter(adapterRol);

        getGruposFromFirebase();
        getAsigsFromFirebase();


      //  adapterGrupos = new ArrayAdapter<String>(getApplicationContext(), R.layout.text_spinner);
        adapterGrupos.add("Sin Grupo");
        spinnerGrupo.setAdapter(adapterGrupos);

        if (getIntent().getExtras()!=null){
            idUsuario = getIntent().getStringExtra("idUser");
            editar=true;
            editemail.setVisibility(View.INVISIBLE);
            editcontra.setVisibility(View.INVISIBLE);
            editrepetircontra.setVisibility(View.INVISIBLE);
        }else {
            editar=false;
        }

        if(editar){
            spinnerRol.setVisibility(View.INVISIBLE);
            mDataBase.child("Users").child(idUsuario).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String nombreE = dataSnapshot.child("nombre").getValue().toString();
                    String apellidosE = dataSnapshot.child("apellidos").getValue().toString();
                    String edadE = dataSnapshot.child("edad").getValue().toString();

                    editnombre.setText(nombreE);
                    editapellido.setText(apellidosE);
                    editedad.setText(edadE);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        botonfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                //AQUI SE LLAMA AL METODO DE ABAJO DEL TODO
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        botonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editar){

                    nombre = editnombre.getText().toString();
                    apellido = editapellido.getText().toString();
                    edad = editedad.getText().toString();
                    grupo = spinnerGrupo.getSelectedItem().toString();
                    asignaturas = new ArrayList<String>();
                    CheckBox cb;
                    TextView tv;
                    //ListView mainListView = getListView();
                    for (int x = 0; x<listView.getChildCount();x++){
                        cb = (CheckBox)listView.getChildAt(x).findViewById(R.id.checkBox);
                        tv = (TextView) listView.getChildAt(x).findViewById(R.id.checkBox2);
                        if(cb.isChecked()){
                            String add = tv.getText().toString();
                            for(int i =0; i<asigs.size();i++){
                                if (add.equals(asigs.get(i).getNombre())){
                                    asignaturas.add(asigs.get(i).getNombre());
                                }
                            }

                        }
                    }

                    if(!nombre.isEmpty()&&!apellido.isEmpty()&&!edad.isEmpty()) {
                        mDataBase.child("Users").child(idUsuario).child("nombre").setValue(nombre);
                        mDataBase.child("Users").child(idUsuario).child("apellidos").setValue(apellido);
                        mDataBase.child("Users").child(idUsuario).child("edad").setValue(edad);
                        mDataBase.child("Users").child(idUsuario).child("grupo").setValue(grupo);
                        mDataBase.child("Users").child(idUsuario).child("asignaturas").setValue(asignaturas);
                        Toast.makeText(getApplication(), "COMPLETADO", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(getApplication(), "COMPLETE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    crearUser();
                }
            }
        });
    }

    private void getAsigsFromFirebase() {

        mDataBase.child("Asignaturas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                if (dataSnapshot.exists()){
                    //FOR PARA OBTENER TODOS LOS NOMBRES DE DEPARTAMENTOS
                    for (DataSnapshot ds: dataSnapshot.getChildren()){

                        String nombre = ds.child("nombre").getValue().toString();
                        String curso = "";
                        String descripcion = "";
                        String imagen = "";
                        String id= ds.getKey();
                        //LOS AÑADIMOS A LA LISTA
                        Asignatura getasigs= new Asignatura();
                        asigs.add(getasigs);

                    }
                    listView.setVisibility(View.VISIBLE);
                 //   adapter = new ListViewAdapter(getApplicationContext(),R.layout.listview_item,asigs) ;
                 //   listView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getGruposFromFirebase() {

        mDataBase.child("Grupos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()){
                    //FOR PARA OBTENER TODOS LOS NOMBRES DE DEPARTAMENTOS
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        String grupo = ds.child("nombre").getValue().toString();
                        //LOS AÑADIMOS A LA LISTA
                        adapterGrupos.add(grupo);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //SI ES CORRECTO
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            //HACEMOS UNA BARRA DE CARGA

            //GUARDAMOS LA IMAGEN EN EL URI Y VOLVEMOS TRUE EL FILEIMG
            uri = data.getData();
            fileImg = true;

        }

    }
    public void crearUser(){
        email = editemail.getText().toString();
        nombre = editnombre.getText().toString();
        apellido = editapellido.getText().toString();
        edad = editedad.getText().toString();
        contra = editcontra.getText().toString();
        repetircontra= editrepetircontra.getText().toString();
        recordar = "false";
        rol= spinnerRol.getSelectedItem().toString();
        asignaturas = new ArrayList<String>();
        if(rol.equals("Profesor"))   {
            grupo="Sin Grupo";
        }  else{
            grupo=spinnerGrupo.getSelectedItem().toString();
        }
        CheckBox cb;
        TextView tv;
        //ListView mainListView = getListView();
        for (int x = 0; x<listView.getChildCount();x++){
            cb = (CheckBox)listView.getChildAt(x).findViewById(R.id.checkBox);
            tv = (TextView) listView.getChildAt(x).findViewById(R.id.checkBox2);
            if(cb.isChecked()){
                String add = tv.getText().toString();
                for(int i =0; i<asigs.size();i++){
                    if (add.equals(asigs.get(i).getNombre())){
                        asignaturas.add(asigs.get(i).getNombre());
                    }
                }

            }
        }



        //VALIDACIONES
        if (!email.isEmpty() && !nombre.isEmpty() && !apellido.isEmpty() && !edad.isEmpty() && !contra.isEmpty() && !repetircontra.isEmpty() && !email.trim().equals("") && !nombre.trim().equals("") && !apellido.trim().equals("") && !edad.trim().equals("")) {

            if (contra.length() >= 6 || contra.trim().equals("")) {
                if (contra.equals(repetircontra)) {
                    //SUBIR IMAGEN
                    if (fileImg) {
                        //MARCAMOS LA RUTA DE LAS IMAGENES EN EL STORANGE
                        final StorageReference filePath = mStorage.child("Fotos de perfil").child(uri.getLastPathSegment());
                        //COGEMOS ESA RUTA Y LE METEMOS LA URI, QUE ES LA FOTO SELECCIONADA, GUARDADA ANTERIORMENTE EN EL ANTERIOR ON CLICK
                        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //GUARDA LA URL DE LA ULTIMA FOTO SUBIDA
                                Task<Uri> downloadUrl = mStorage.child("Fotos de perfil").child(uri.getLastPathSegment()).getDownloadUrl();
                                //SI ES CORRECTO ENTRAMOS AQUI
                                downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri2) {
                                        //URI DOS CORRESPONDE A downloadUrl Y SE LO AÑADIMOS A UN STRING QUE LE PASAMOS AL OBJETO
                                        foto = uri2.toString();
                                        //LLAMAMOS AL METODO PARA REGISTRO
                                        completarRegistro();

                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplication(), "ERROR AL CARGAR LA IMAGEN", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        //EN CASO DE NO SELECCIONAR NINGUNA FOTO SE GARGA ESTA POR DEFECTO
                        foto = "https://firebasestorage.googleapis.com/v0/b/aplicacinftc.appspot.com/o/por%20defecto.jpg?alt=media&token=e88772f9-b7c2-422c-967f-2d0016747b53";
                        completarRegistro();
                    }
                    //TERMINAR DE SUBIR IMAGEN
                    //MAS VALIDACIONES
                } else {
                    Toast.makeText(getApplication(), "LAS CONTRASEÑAS NO COINCIDEN", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplication(), "LA CONTRASEÑA NO ES SEGURA", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplication(), "COMPLETE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
        }
    }

    public void completarRegistro(){
        mAuth.createUserWithEmailAndPassword(email, contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //GUARDAMOS LA ID DEL USUARIO CREADO
                    String id = mAuth.getCurrentUser().getUid();
                    //CREAMOS OBJETO PARA PASARSELO A LA BASE
                    Usuario user = new Usuario();
                    //ENTRAMOS DONDE LOS USUARIOS Y LE METEMOS EL OBJETO LUEGO HAY VALIDACIONES
                    mDataBase.child("Users").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        //COMIENZO VALIDACIONES
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()) {
                                Toast.makeText(getApplication(), "USUARIO REGISTRADO", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplication(), "ERROR EN EL REGISTRO", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    mAuth.signOut();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplication(), "EMAIL YA EN USO", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), "ERROR EN EL REGISTRO", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplication(), "COMPRUEBE SI LOS DATOS SON CORRECTOS", Toast.LENGTH_SHORT).show();
            }
        });
    }
}