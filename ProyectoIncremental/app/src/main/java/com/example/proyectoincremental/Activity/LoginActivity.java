package com.example.proyectoincremental.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btnLogin;
    private String email = "";
    private String password = "";
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private Button resetPass;
    private Button crear;
    private Switch switchRemember;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        switchRemember = (Switch) findViewById(R.id.switch1);
        mAuth = FirebaseAuth.getInstance();

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExist();

        resetPass = (Button) findViewById(R.id.btnOlvidar);
        crear = (Button) findViewById(R.id.btnCrearUsuarioLogin);
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CreateUserActivity.class));
            }
        });

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                if (!email.isEmpty()) {
                    resetPasword();
                } else {

                    Toast.makeText(LoginActivity.this, "ntroducir correo", Toast.LENGTH_LONG).show();

                }
            }
        });


        editTextEmail = (EditText) findViewById(R.id.emailLogin);
        editTextPassword = (EditText) findViewById(R.id.contraseñaLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        auth = FirebaseAuth.getInstance();
        //boton çlogin ,
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()) {
                    login();
                    saveOnPreferences(email, password);

                } else {
                    Toast.makeText(LoginActivity.this, "rellene ls campos", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void login() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Los datos no pertenecen a ningun usuario", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void resetPasword() {
        auth = FirebaseAuth.getInstance();
        auth.setLanguageCode("es");
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Se ha enviado el correo a" + email, Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(LoginActivity.this, "No existe usuario con ese" + email, Toast.LENGTH_SHORT);

                }
            }
        });
    }

    //metodo para recordar usuario
  /*  @Override
    protected void onStart() {
        super.onStart();

        //si el usuario ya a iniciado sesion , lo envia a la panatalla de inicio
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            Toast.makeText(LoginActivity.this, "ya tiene usuario", Toast.LENGTH_LONG).show();
            finish();
        }
    }*/
    private void setCredentialsIfExist() {
        editTextEmail = (EditText) findViewById(R.id.emailLogin);
        editTextPassword = (EditText) findViewById(R.id.contraseñaLogin);
        email = Util.getUserMailPrefs(prefs);
        password = Util.getUserPassPrefs(prefs);
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            editTextEmail.setText(email);
            editTextPassword.setText(password);
            switchRemember.setChecked(true);
        }
    }

    private void saveOnPreferences(String email, String password) {
        if (switchRemember.isChecked()) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", email);
            editor.putString("pass", password);
            editor.apply();
        }
    }
}

