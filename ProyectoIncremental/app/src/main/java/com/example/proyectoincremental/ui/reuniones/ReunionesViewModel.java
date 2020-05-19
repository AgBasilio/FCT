package com.example.proyectoincremental.ui.reuniones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReunionesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReunionesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}