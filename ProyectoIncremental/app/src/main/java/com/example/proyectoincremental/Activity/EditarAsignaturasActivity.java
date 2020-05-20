package com.example.proyectoincremental.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditarAsignaturasActivity  extends AppCompatActivity {
    private EditText curso, nombre, descipcion, imagen;
    private FirebaseDatabase database;
    private DatabaseReference refBBD,refBBD2;
    private String nombreS = "", crusoS = "", descripcionS = "", imagenS;
    private int edadI;
    private Button btnCrear, btnGoLogin;
    private FirebaseAuth mAuth;
    private SharedPreferences prefs, sfd;
    private String NombreLocal;
    private String descripcion;
    EditText nombreGrupo, numeroGrupo;
    Button btnEditar;
    String id;
    private  String snombreeditGrupo="",snumeroditGrupo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_asignatura);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        nombreGrupo = (EditText) findViewById(R.id.editnombreAsignatura);
        numeroGrupo = (EditText) findViewById(R.id.editcursoAsignatura);


        if (getIntent().getExtras() != null) {
            nombreGrupo.setText(getIntent().getExtras().getString("NombreLocal"));
            numeroGrupo.setText(getIntent().getExtras().getString("Contenido"));

        }


        btnEditar=findViewById(R.id.btneditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference refBBD0= database.getReference("Grupos");
                refBBD0.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        snombreeditGrupo=nombreGrupo.getText().toString();
                        snumeroditGrupo=numeroGrupo.getText().toString();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                             id = dataSnapshot1.getKey();
                            refBBD = database.getReference("Grupos").child(id).child("nombreGrupo");
                            refBBD2 = database.getReference("Grupos").child(id).child("numeroGrupo");

                            refBBD.setValue(snombreeditGrupo);
                            refBBD2.setValue(snumeroditGrupo);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });







            }
        });
    }

}
