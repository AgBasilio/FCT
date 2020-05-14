package com.example.proyectoincremental.ui.otro2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class otro2ViewModee extends ViewModel {
    private MutableLiveData<String> mText;

    public otro2ViewModee() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}