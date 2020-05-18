package com.example.proyectoincremental.ui.cerrarSesion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class cerrarSesionViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public cerrarSesionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

