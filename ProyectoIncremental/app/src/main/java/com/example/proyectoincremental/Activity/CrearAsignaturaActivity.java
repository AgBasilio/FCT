package com.example.proyectoincremental.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CrearAsignaturaActivity extends AppCompatActivity {
    private EditText cursoAsignatura, nombreAsignatura, descipcionAsignatura, imagenAsignatura;
    private FirebaseDatabase database;
    private DatabaseReference refBBD;
    private String nombreS = "", crusoS = "", descripcionS = "", imagenS;
    private Button btnCrearAsignatura;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_asignatura);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        nombreAsignatura = findViewById(R.id.nombreAsignatura);
        cursoAsignatura = findViewById(R.id.cursoAsignatura);
        descipcionAsignatura = findViewById(R.id.descripcionAsignatura);
        imagenAsignatura = findViewById(R.id.imagenAsignatura);


        btnCrearAsignatura = findViewById(R.id.btnCrearAsignatura);
        btnCrearAsignatura.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                nombreS = nombreAsignatura.getText().toString();
                crusoS = cursoAsignatura.getText().toString();
                descripcionS = descipcionAsignatura.getText().toString();
                imagenS = imagenAsignatura.getText().toString();
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                String userid = firebaseUser.getUid();
                Asignatura asignatura = new Asignatura();
                asignatura.setNombre(nombreS);
                asignatura.setCurso(crusoS);
                asignatura.setDescricion(descripcionS);
                if (!imagenS.isEmpty()) {
                    asignatura.setImgAsignatura(imagenS);
                } else {
                    asignatura.setImgAsignatura("https://firebasestorage.googleapis.com/v0/b/proyecto-fct-83b84.appspot.com/o/cuenta.png?alt=media&token=9b30a70e-28c2-4e29-be65-18c599d09ffb");

                }
                refBBD = database.getReference("Asignaturas").child(userid);
                DatabaseReference rf = refBBD.push();
                rf.setValue(asignatura);
                Toast.makeText(CrearAsignaturaActivity.this, "Grupo creado!", Toast.LENGTH_SHORT).show();
                onBackPressed();

            }
        });

    }

}
