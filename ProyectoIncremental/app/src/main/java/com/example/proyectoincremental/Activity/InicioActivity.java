package com.example.proyectoincremental.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoincremental.R;
import com.google.firebase.auth.FirebaseAuth;

public class InicioActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        auth = FirebaseAuth.getInstance();


        button = findViewById(R.id.btncerarcesion);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();

            }
        });
    }
    private void logOut () {
        auth.signOut();


        Intent intent = new Intent(this, Main2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
