package com.example.proyectoincremental.ui.usuarios;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UsuariosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UsuariosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}