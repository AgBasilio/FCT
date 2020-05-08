package com.example.proyectoincremental;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class Util {


    public static String getUserMailPrefs(SharedPreferences preferences) {
        return preferences.getString("email", "");
    }

    public static String getUserPassPrefs(SharedPreferences preferences) {
        return preferences.getString("pass", "");
    }

    public static void removeSharedPreferences(SharedPreferences preferences) {
        SharedPreferences.Editor editor =  preferences.edit();
        editor.remove("email");
        editor.remove("email");
        editor.apply();
    }


}