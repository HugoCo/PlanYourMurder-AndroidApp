package com.example.planyourmurder.ui.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyInventoryViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public MyInventoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is inventory fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
