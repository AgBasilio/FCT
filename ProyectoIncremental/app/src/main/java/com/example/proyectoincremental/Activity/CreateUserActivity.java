package com.example.proyectoincremental.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateUserActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference refBBD;
    private EditText email, contraseña, nombre, apellido1, apellido2, edad;
    private String emailS = "", contraseñaS = "", nombreS = "", apellido1S = "", apellido2S = "", edadS = "";
    private int edadI;
    private Button btnCrearUsuario, btnGoLogin;
    private FirebaseAuth mAuth;
    private SharedPreferences prefs, sfd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);
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

        // btn para Creamos usuarios
        btnCrearUsuario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //rellenamos el formulario , enlazmaos las variables con los datos introducidos  ,los datos son String
                emailS = email.getText().toString();
                contraseñaS = contraseña.getText().toString();
                nombreS = nombre.getText().toString();
                apellido1S = apellido1.getText().toString();
                apellido2S = apellido2.getText().toString();
                edadS = edad.getText().toString();
                if (isNumeric(edadS) == true) {

                    //validacione de control de campos vacios el unico campo que no es necesario sera el segundo apellido, hay usauios que no tendran 2 apellido;
                    if (!emailS.isEmpty() && !contraseñaS.isEmpty() && !nombreS.isEmpty() && !apellido1S.isEmpty() && !edadS.isEmpty()) {
                        mAuth.createUserWithEmailAndPassword(emailS, contraseñaS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //edad recibe un string del editex , pasmos el string recibido a entero //edad del usuario
                                    edadI = Integer.parseInt(edadS);

                                    //instancia de la bbd
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    refBBD = database.getReference("Usuarios/" + currentUser.getUid());
                                    Usuario usuario = new Usuario();
                                    usuario.setEmail(emailS);
                                    usuario.setNombre(nombreS);
                                    usuario.setApellido1(apellido1S);
                                    usuario.setApellido2(apellido2S);
                                    usuario.setEdad(edadI);
                                    usuario.setContraseña(contraseñaS);
                                    usuario.setTipo("E");
                                    usuario.setGrupo(" ");
                                    usuario.setIdgrupo("");
                                    usuario.setAsignaturas(" ");
                                    usuario.setId(currentUser.getUid());
                                    usuario.setImagen("https://firebasestorage.googleapis.com/v0/b/proyecto-fct-83b84.appspot.com/o/cuenta.png?alt=media&token=9b30a70e-28c2-4e29-be65-18c599d09ffb");
                                    refBBD.setValue(usuario);
                                    Intent intent = new Intent(CreateUserActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(CreateUserActivity.this, "No se apodido crear usuario", Toast.LENGTH_LONG).show();


                                }
                            }


                        });
                    } else {
                        Toast.makeText(CreateUserActivity.this, "rellene ls campos", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(CreateUserActivity.this, "la edad debeser un numero", Toast.LENGTH_LONG).show();

                }
            }
        });
        //btn login // nos lleva al siguiente activity el Login
        btnGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateUserActivity.this, LoginActivity.class);
                startActivity(intent);


            }
        });


    }/*
//metodo para recordar usuario
    @Override
    protected void onStart() {
        super.onStart();

        //si el usuario ya a iniciado sesion , lo envia a la panatalla de inicio
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(CreateUserActivity.this, Main2Activity.class));
            Toast.makeText(CreateUserActivity.this, "ya tiene usuario", Toast.LENGTH_LONG).show();
            finish();
        }
    }*/

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
