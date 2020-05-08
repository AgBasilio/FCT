package com.example.proyectoincremental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateUserActivity extends AppCompatActivity {
    private EditText email, contraseña, nombre, apellido1, apellido2, edad;
    private FirebaseDatabase database;
    private DatabaseReference refBBD;
    private String emailS = "", contraseñaS = "", nombreS = "", apellido1S = "", apellido2S = "", edadS = "";
    private Button btnCrearUsuario, btnGoLogin;
    private FirebaseAuth mAuth;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        email = findViewById(R.id.emailormulario);
        contraseña = findViewById(R.id.contraseñaFormulario);
        nombre = findViewById(R.id.nombreFormulario);
        apellido1 = findViewById(R.id.apellido1Formulario);
        apellido2 = findViewById(R.id.apellido2Formulario);
        edad = findViewById(R.id.edadFormulario);


        btnCrearUsuario = findViewById(R.id.btnCrearUsuario);
        btnGoLogin = findViewById(R.id.btnGoLogin);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        btnCrearUsuario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                emailS = email.getText().toString();
                contraseñaS = contraseña.getText().toString();
                nombreS = nombre.getText().toString();
                apellido1S = apellido1.getText().toString();
                apellido2S = apellido2.getText().toString();
                edadS = edad.getText().toString();
                if (!emailS.isEmpty()&&!contraseñaS.isEmpty()&& !nombreS.isEmpty()&& !apellido1S.isEmpty()&&!apellido2S.isEmpty()&&!edadS.isEmpty()){
                    mAuth.createUserWithEmailAndPassword(emailS, contraseñaS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //instancia de la bbd
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            refBBD = database.getReference("Usuarios/" + currentUser.getUid());
                            Usuario usuario = new Usuario();
                            usuario.setEmail(emailS);
                            usuario.setNombre(nombreS);
                            usuario.setApellido1(apellido1S);
                            usuario.setApellido2(apellido2S);
                            usuario.setContraseña(contraseñaS);
                            usuario.setImagen("URL por defecto");
                            refBBD.setValue(usuario);
                        }
                    });
                }else
                {                    Toast.makeText(CreateUserActivity.this, "rellene ls campos", Toast.LENGTH_LONG).show();
                }






            }
        });
        btnGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateUserActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();


            }
        });



    }
    @Override
    protected void onStart(){
        super.onStart();

        //si el usuario ya a iniciado sesion , lo envia a la panatalla de inicio
        if (mAuth.getCurrentUser() !=null){
            startActivity(new Intent(CreateUserActivity.this, InicioActivity.class));
            Toast.makeText(CreateUserActivity.this, "ya tiene usuario", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
