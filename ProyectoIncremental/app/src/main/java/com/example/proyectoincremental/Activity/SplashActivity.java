package com.example.proyectoincremental.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoincremental.Activity.CreateUserActivity;
import com.google.firebase.auth.FirebaseAuth;


public class SplashActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        Intent intentLogin = new Intent(this, LoginActivity.class);

        startActivity(intentLogin);

        finish();
    }


}
