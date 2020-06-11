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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.proyectoincremental.Activity.CreateUserActivity.isNumeric;

public class EditarGrupoActivity extends AppCompatActivity {
    private DatabaseReference refBBD, refBBD2;
    private EditText nombreGrupo, numeroGrupo;
    private Button btnEditarGrupo;
    private String id;
    private String snombreeditGrupo = "", snumeroditGrupo = "";
    private FirebaseAuth auth;
    private String userid;

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

                if (!isNumeric(snumeroditGrupo)) {
                    Toast.makeText(EditarGrupoActivity.this, "El numero de grupo debe ser valor numerico", Toast.LENGTH_SHORT).show();
                    return;
                }

                database.getReference("Grupos").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean repetido = false;

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            for (DataSnapshot a : ds.getChildren()) {
                                if (a.getValue(Grupos.class).getNumeroGrupo().equals(snumeroditGrupo)) {
                                    if(a.getKey().equals(id))
                                        break;
                                    Toast.makeText(EditarGrupoActivity.this, "Existe un grupo con este numero de grupo.", Toast.LENGTH_SHORT).show();
                                    repetido = true;
                                    break;
                                }
                            }
                            if (repetido)
                                return;
                        }

                        refBBD = database.getReference("Grupos").child(userid).child(id).child("nombreGrupo");
                        refBBD2 = database.getReference("Grupos").child(userid).child(id).child("numeroGrupo");
                        refBBD.setValue(snombreeditGrupo);
                        refBBD2.setValue(snumeroditGrupo);
                        onBackPressed();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

}
