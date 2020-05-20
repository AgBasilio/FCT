package com.example.proyectoincremental.ui.editarUsuario;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.proyectoincremental.R;
import com.example.proyectoincremental.ui.gestionar.GruposViewModel;
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

import static android.app.Activity.RESULT_OK;

public class editarUsuarioFragment extends Fragment {
    private static final int GALLERY_INTENT = 1;

    private EditText ediNombre, ediApellido1, ediApellido2, foto;
    private Button btneditar,btnfoto;
    private GruposViewModel homeViewModel;
    private DatabaseReference refBBD,refBBD2,refBBD3,refBBD4,refBBD5;
    private  String sNombre="",sApellido1="",sApellido2,sfoto,sEdad;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private FirebaseDatabase dataBase;
    private StorageReference storageReference, storageReference2;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_usuario, container, false);


        mAuth = FirebaseAuth.getInstance();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();
        ediApellido1=view.findViewById(R.id.editarnombre);
        ediApellido2=view.findViewById(R.id.editarnombre);
        ediNombre=view.findViewById(R.id.editarnombre);
        dataBase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        btnfoto=view.findViewById(R.id.btnCambiarFoto);
        btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);

            }
        });

        btneditar=view.findViewById(R.id.btnEditar);
        btneditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sApellido1=ediApellido1.getText().toString();
                sApellido2=ediApellido1.getText().toString();
                sNombre=ediApellido1.getText().toString();
                sfoto=ediApellido1.getText().toString();
                sEdad=ediApellido1.getText().toString();

                int edad=Integer.parseInt(sEdad);

                refBBD = database.getReference("Usuarios/" + currentUser.getUid()).child("apellido1");
                refBBD2 = database.getReference("Usuarios/" + currentUser.getUid()).child("apellido2");
                refBBD3 = database.getReference("Usuarios/" + currentUser.getUid()).child("edad");
                refBBD4 = database.getReference("Usuarios/" + currentUser.getUid()).child("imagen");
                refBBD5  = database.getReference("Usuarios/" + currentUser.getUid()).child("nombre");

                refBBD.setValue(sApellido1);
                refBBD2.setValue(sApellido2);
                refBBD3.setValue(edad);
                refBBD5.setValue(sNombre);




            }
        });


        refBBD = database.getReference("Usuarios/" + currentUser.getUid());
        refBBD.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                Toast toast1 = Toast.makeText(getContext(), "Tost por defecto"+dataSnapshot, Toast.LENGTH_SHORT);
                toast1.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                        String downloadURL = downloadUri.toString();
                        //REFERENCIAMOS AL STRING FOTO Y LE PASAMOS LA URL CONSEGUIDA
                        DatabaseReference ref = dataBase.getReference("Usuarios/" + currentUser.getUid()).child("imagen");
                        ref.setValue(downloadURL);


                    } else {
                    }
                }
            });




        }

    }

}
