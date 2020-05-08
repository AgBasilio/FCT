package com.example.proyectoincremental;

import android.app.Application;
import android.os.SystemClock;

public class MyFirebaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SystemClock.sleep(3000);
    }
}
