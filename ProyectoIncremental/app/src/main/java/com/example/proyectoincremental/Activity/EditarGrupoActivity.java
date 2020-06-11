package com.example.proyectoincremental.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoincremental.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditarGrupoActivity extends AppCompatActivity {
    private DatabaseReference refBBD, refBBD2;
    private EditText nombreGrupo, numeroGrupo;
    private Button btnEditarGrupo;
    private String id;
    private String snombreeditGrupo = "", snumeroditGrupo = "";
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_grupo);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        nombreGrupo = (EditText) findViewById(R.id.editnombreGrupo);
        numeroGrupo = (EditText) findViewById(R.id.editnumerogrupo);


        if (getIntent().getExtras() != null) {
            nombreGrupo.setText(getIntent().getExtras().getString("NombreLocal"));
            id = getIntent().getExtras().getString("Id");
            numeroGrupo.setText(getIntent().getExtras().getString("Contenido"));

        }


        btnEditarGrupo = findViewById(R.id.btneditarGrupo);
        btnEditarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = FirebaseAuth.getInstance();

                FirebaseUser firebaseUser = auth.getCurrentUser();

                String userid = firebaseUser.getUid();
                snombreeditGrupo = nombreGrupo.getText().toString();
                snumeroditGrupo = numeroGrupo.getText().toString();
                refBBD = database.getReference("Grupos").child(userid).child(id).child("nombreGrupo");
                refBBD2 = database.getReference("Grupos").child(userid).child(id).child("numeroGrupo");
                refBBD.setValue(snombreeditGrupo);
                refBBD2.setValue(snumeroditGrupo);
                onBackPressed();


            }
        });
    }

}
