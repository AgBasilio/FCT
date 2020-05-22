package com.example.proyectoincremental.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Usuario;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    ;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private ArrayList<String> list;
    //--------------------------------------------------------------------------------

    private TextView nombreUsuarioMenu;
    private Button btnAtrasMenu;
    private SearchView searchView;
    private FirebaseDatabase dataBase;
    private SharedPreferences prefs;


    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBase = FirebaseDatabase.getInstance();
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View heaView = navigationView.getHeaderView(0);
        imageView = (ImageView) heaView.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PerfilActivity.class));
            }
        });


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.activyty, R.id.actyvyty)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onResume() {
        auth = FirebaseAuth.getInstance();

        super.onResume();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            //Referenciamos al nodo Users
            reference = dataBase.getReference("Usuarios/" + currentUser.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String nombreUsuario = " ";
                    String apellido1 = " ";
                    String apellido2 = " ";

                    String imgPerfilBar = "";
                    nombreUsuarioMenu = (TextView) findViewById(R.id.NombreUsuario);
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    if(usuario != null) {
                        nombreUsuario = usuario != null ? usuario.getNombre() : "";
                        imgPerfilBar = usuario != null ? usuario.getImagen() : "";
                        apellido1 = usuario != null ? usuario.getApellido1() : "";
                        apellido2 = usuario != null ? usuario.getApellido2() : "";
                        nombreUsuarioMenu.setText(nombreUsuario + " " + apellido1 + " " + apellido2);
                        if (!imgPerfilBar.isEmpty()) {
                            Picasso.get().load(imgPerfilBar).into(imageView);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
