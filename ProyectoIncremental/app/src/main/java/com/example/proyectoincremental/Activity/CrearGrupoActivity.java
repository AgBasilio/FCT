package com.example.proyectoincremental.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Grupos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.proyectoincremental.Activity.CreateUserActivity.isNumeric;

public class CrearGrupoActivity extends AppCompatActivity {
    private EditText curso, nombre;
    private FirebaseDatabase database;
    private DatabaseReference refBBD;
    private String ngrupo = "";
    private Button btnCrearGrupo;
    private FirebaseAuth mAuth;
    private Grupos grupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_grupo);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        nombre = findViewById(R.id.nombreGrupo);
        curso = findViewById(R.id.numeroCruso);

        btnCrearGrupo = findViewById(R.id.btn);
        btnCrearGrupo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                grupo = new Grupos();
                grupo.setNombreGrupo(nombre.getText().toString());
                grupo.setNumeroGrupo(curso.getText().toString());

                database.getReference("Grupos").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean repetido = false;
                        for (DataSnapshot a : dataSnapshot.getChildren()) {

                            if (a.getValue(Grupos.class).getNumeroGrupo().equals(grupo.getNumeroGrupo())) {
                                Toast.makeText(CrearGrupoActivity.this, "Existe un grupo con este numero de grupo.", Toast.LENGTH_SHORT).show();
                                repetido = true;
                                break;
                            }
                        }
                        ngrupo = grupo.getNumeroGrupo();

                        if (isNumeric(ngrupo) == true) {

                            if (!repetido) {
                                refBBD = database.getReference("Grupos").child(mAuth.getCurrentUser().getUid());
                                DatabaseReference rf = refBBD.push();
                                rf.setValue(grupo);
                                Toast.makeText(CrearGrupoActivity.this, "Grupo creado!", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        } else {
                            Toast.makeText(CrearGrupoActivity.this, "Numeros", Toast.LENGTH_SHORT).show();

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
