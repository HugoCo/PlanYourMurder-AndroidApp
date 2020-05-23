package com.example.planyourmurder.ui.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListCharactersViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ListCharactersViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}