package com.example.proyectoincremental.ui.editarUsuario;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.proyectoincremental.Activity.EditarAsignaturaActivity;
import com.example.proyectoincremental.Activity.MainActivity;
import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Reuniones;
import com.example.proyectoincremental.Utils.Usuario;
import com.example.proyectoincremental.ui.gestionar.GruposViewModel;
import com.example.proyectoincremental.ui.gestionar.fragmentos.AsignaturasFagment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class editarUsuarioFragment extends Fragment {
    private static final int GALLERY_INTENT = 1;

    private EditText ediNombre, ediApellido1, ediApellido2, ediEdad;
    private TextView edifoto;
    private Button btneditar, btnfoto;
    private ImageView img;
    private GruposViewModel homeViewModel;
    private DatabaseReference refBBD, refBBD2, refBBD3, refBBD4, refBBD5;
    private String sNombre = "", sApellido1 = "", sApellido2, sfoto, sEdad;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private FirebaseDatabase dataBase;
    String downloadURL;
    private StorageReference storageReference, storageReference2;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_usuario, container, false);


        mAuth = FirebaseAuth.getInstance();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();
        ediApellido1 = view.findViewById(R.id.editarapellido1);
        ediApellido2 = view.findViewById(R.id.editarapellido2);
        ediNombre = view.findViewById(R.id.editarnombre);
        ediEdad = view.findViewById(R.id.editarEdad);
        img = view.findViewById(R.id.foto);

        dataBase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        btnfoto = view.findViewById(R.id.btnCambiarFoto);

        //
        //   FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //Referenciamos al nodo Users
            DatabaseReference reference = dataBase.getReference("Usuarios/" + currentUser.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    String nombreUsuario = " ";
                    String apellido1 = " ";
                    String apellido2 = " ";
                    int f = usuario.getEdad();


                    String edad = String.valueOf(f);

                    String imgPerfil = "";

                    if (usuario != null) {
                        nombreUsuario = usuario != null ? usuario.getNombre() : "";
                        imgPerfil = usuario != null ? usuario.getImagen() : "";
                        apellido1 = usuario != null ? usuario.getApellido1() : "";
                        apellido2 = usuario != null ? usuario.getApellido2() : "";
                        ediNombre.setText(nombreUsuario);
                        ediApellido1.setText(apellido1);
                        ediApellido2.setText(apellido2);
                        ediEdad.setText(edad);
                        //      edifoto.setText(usuario.getImagen());
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            ///
        }


        btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);

            }
        });

        btneditar = view.findViewById(R.id.btnEditar);
        btneditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sApellido1 = ediApellido1.getText().toString();
                sApellido2 = ediApellido2.getText().toString();
                sNombre = ediNombre.getText().toString();
                sEdad = ediEdad.getText().toString();
                sfoto = downloadURL;

                if (isNumeric(sEdad) == true) {
                    int edad = Integer.parseInt(sEdad);
                    refBBD = database.getReference("Usuarios/" + currentUser.getUid()).child("apellido1");
                    refBBD2 = database.getReference("Usuarios/" + currentUser.getUid()).child("apellido2");
                    refBBD3 = database.getReference("Usuarios/" + currentUser.getUid()).child("edad");
                    refBBD4 = database.getReference("Usuarios/" + currentUser.getUid()).child("imagen");
                    refBBD5 = database.getReference("Usuarios/" + currentUser.getUid()).child("nombre");
                    refBBD.setValue(sApellido1);
                    refBBD2.setValue(sApellido2);
                    refBBD3.setValue(edad);
                    if (sfoto == null) {
                        refBBD4.setValue("https://firebasestorage.googleapis.com/v0/b/proyecto-fct-83b84.appspot.com/o/cuenta.png?alt=media&token=9b30a70e-28c2-4e29-be65-18c599d09ffb");

                    } else {
                        refBBD4.setValue(sfoto);

                    }
                    refBBD5.setValue(sNombre);
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast toast1 =
                            Toast.makeText(getContext(),
                                    "Introducir un numero en edad, no letras", Toast.LENGTH_SHORT);

                    toast1.show();
                }


            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String uriq = uri.toString();
            final StorageReference filepath = storageReference.child("fotos").child(uri.getLastPathSegment());
            UploadTask vc = filepath.putFile(uri);
            Task<Uri> uriTask = vc.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        currentUser = mAuth.getCurrentUser();
                        Uri downloadUri = task.getResult();
                        downloadURL = downloadUri.toString();
                        //    edifoto.setText(downloadURL);
                        Picasso.get().load(downloadURL).into(img);


                        //REFERENCIAMOS AL STRING FOTO Y LE PASAMOS LA URL CONSEGUIDA
                        //   DatabaseReference ref = dataBase.getReference("Usuarios/" + currentUser.getUid()).child("imagen");
                        // ref.setValue(downloadURL);


                    } else {
                    }
                }
            });


        }

    }

    public static boolean isNumeric(final String str) {

        // null or empty
        if (str == null || str.length() == 0) {
            return false;
        }

        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }


}
