package com.example.proyectoincremental.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoincremental.Activity.CreateUserActivity;
import com.example.proyectoincremental.R;
import com.google.firebase.auth.FirebaseAuth;


public class SplashActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentLogin = new Intent(SplashActivity.this, LoginActivity.class);

                startActivity(intentLogin);

                finish();
            }
        },2000);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);


    }


}
