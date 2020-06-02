package com.example.proyectoincremental.ui.cerrarSesion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.proyectoincremental.Activity.CreateUserActivity;
import com.example.proyectoincremental.Activity.LoginActivity;
import com.example.proyectoincremental.R;
import com.example.proyectoincremental.Utils.Util;
import com.example.proyectoincremental.ui.gestionar.GruposViewModel;
import com.example.proyectoincremental.Adaptadores.PagerController;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class cerrarSesionFragment extends Fragment {
    private FirebaseAuth auth;

    Button button;
    private SharedPreferences prefs;

    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem uno, dos, tres;
    PagerController pagerController;
    private GruposViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cerrar_sesion, container, false);
        auth = FirebaseAuth.getInstance();
        prefs = this.getActivity().getSharedPreferences("Preferences", getContext().MODE_PRIVATE);


        button = view.findViewById(R.id.btncerarcesion);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Util.removeSharedPreferences(prefs);

                logOut();

            }
        });
        return view;
    }

    private void logOut() {
        auth.signOut();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
