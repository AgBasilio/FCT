package com.example.proyectoincremental.ui.otro1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class otro1ViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public otro1ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

