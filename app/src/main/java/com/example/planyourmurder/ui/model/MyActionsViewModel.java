package com.example.planyourmurder.ui.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyActionsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyActionsViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}