package com.example.proyectoincremental.ui.reuniones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class reunionesViewModee extends ViewModel {
    private MutableLiveData<String> mText;

    public reunionesViewModee() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}