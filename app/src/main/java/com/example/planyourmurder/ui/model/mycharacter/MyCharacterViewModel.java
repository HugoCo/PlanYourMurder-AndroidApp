package com.example.planyourmurder.ui.model.mycharacter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyCharacterViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyCharacterViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}