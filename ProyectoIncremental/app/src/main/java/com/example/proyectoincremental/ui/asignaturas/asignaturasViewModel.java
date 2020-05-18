package com.example.proyectoincremental.ui.asignaturas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class asignaturasViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public asignaturasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}