package com.example.proyectoincremental.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Asignatura;
import com.example.proyectoincremental.Utils.Grupos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CrearGrupoActivity extends AppCompatActivity {
    private EditText curso, nombre, descipcion, imagen;
    private FirebaseDatabase database;
    private DatabaseReference refBBD;
    private String nombreS = "", crusoS = "", descripcionS = "", imagenS;
    private int edadI;
    private Button btnCrear, btnGoLogin;
    private FirebaseAuth mAuth;
    private SharedPreferences prefs, sfd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_grupo);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        nombre = findViewById(R.id.nombreGrupo);
        curso = findViewById(R.id.numeroCruso);

        btnCrear = findViewById(R.id.btn);
        btnCrear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                nombreS = nombre.getText().toString();
                crusoS = curso.getText().toString();

                FirebaseUser currentUser = mAuth.getCurrentUser();
                Grupos grupo = new Grupos();
                grupo.setNombreGrupo(nombreS);
                grupo.setNumeroGrupo(crusoS);
                refBBD = database.getReference("Grupos");
                DatabaseReference rf =refBBD.push();

                rf.setValue(grupo);

                //grupo.setId(rf.getKey());

            }
        });

    }

}
