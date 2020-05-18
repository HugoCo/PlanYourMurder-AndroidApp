package com.example.planyourmurder.ui.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.planyourmurder.R;
import com.example.planyourmurder.ui.model.MyCharacterViewModel;

public class MyCharacterFragment extends Fragment {

    private MyCharacterViewModel myCharacterViewModel;

    private TextView text_username;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myCharacterViewModel =
                ViewModelProviders.of(this).get(MyCharacterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mycharacter, container, false);
        final TextView textView = root.findViewById(R.id.textView2);
        myCharacterViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        HomePageActivity activity = (HomePageActivity) getActivity();
        return root;
    }
}