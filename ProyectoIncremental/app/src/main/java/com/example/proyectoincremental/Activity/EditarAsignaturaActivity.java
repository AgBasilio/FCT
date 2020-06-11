package com.example.proyectoincremental.Activity;

import android.content.SharedPreferences;
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

public class EditarAsignaturaActivity extends AppCompatActivity {
    private DatabaseReference refBBD, refBBD2, refBBD3, refBBD4;
    private EditText nombreAsignatura, cursoAsignatura, fotoAsignatura, desripcionAsignatura;
    private Button btnEditar;
    private String id;
    private String snombreeditAsignatura = "", scursoditAsignatura = "", sfotoeditAsignatura = "", sdescripcioneditAsignatura = "";
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_asignatura);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        nombreAsignatura = (EditText) findViewById(R.id.editnombreAsignatura);
        cursoAsignatura = (EditText) findViewById(R.id.editcursoAsignatura);
        fotoAsignatura = (EditText) findViewById(R.id.editfotoAsignatura);
        desripcionAsignatura = (EditText) findViewById(R.id.editdescripcionAsignatura);

        if (getIntent().getExtras() != null) {
            nombreAsignatura.setText(getIntent().getExtras().getString("NombreAsignatura"));
            id = getIntent().getExtras().getString("Id");
            cursoAsignatura.setText(getIntent().getExtras().getString("Curso"));
            fotoAsignatura.setText(getIntent().getExtras().getString("Img"));
            desripcionAsignatura.setText(getIntent().getExtras().getString("Descripcion"));

        }


        btnEditar = findViewById(R.id.btneditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = FirebaseAuth.getInstance();

                FirebaseUser firebaseUser = auth.getCurrentUser();

                String userid = firebaseUser.getUid();
                snombreeditAsignatura = nombreAsignatura.getText().toString();
                scursoditAsignatura = cursoAsignatura.getText().toString();
                sfotoeditAsignatura = fotoAsignatura.getText().toString();
                sdescripcioneditAsignatura = desripcionAsignatura.getText().toString();

                refBBD = database.getReference("Asignaturas").child(userid).child(id).child("curso");
                refBBD2 = database.getReference("Asignaturas").child(userid).child(id).child("descricion");
                refBBD3 = database.getReference("Asignaturas").child(userid).child(id).child("imgAsignatura");
                refBBD4 = database.getReference("Asignaturas").child(userid).child(id).child("nombre");

                refBBD.setValue(scursoditAsignatura);
                refBBD2.setValue(sdescripcioneditAsignatura);
                refBBD3.setValue(sfotoeditAsignatura);
                refBBD4.setValue(snombreeditAsignatura);
                onBackPressed();


            }
        });
    }

}
