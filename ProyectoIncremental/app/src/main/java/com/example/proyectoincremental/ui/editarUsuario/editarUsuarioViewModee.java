package com.example.proyectoincremental.ui.editarUsuario;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class editarUsuarioViewModee extends ViewModel {
    private MutableLiveData<String> mText;

    public editarUsuarioViewModee() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}