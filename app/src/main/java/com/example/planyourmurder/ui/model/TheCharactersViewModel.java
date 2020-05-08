package com.example.planyourmurder.ui.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TheCharactersViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public TheCharactersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the characters fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
