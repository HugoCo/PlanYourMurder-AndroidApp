package com.example.planyourmurder.ui.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.planyourmurder.R;
import com.example.planyourmurder.ui.model.HomeViewModel;

public class HomeFragment extends Fragment {

    private TextView text_username;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        HomePageActivity activity = (HomePageActivity) getActivity();
        String username=activity.getName();
        this.text_username=root.findViewById(R.id.text_mycharacter);
        text_username.setText(username);

        return root;
    }
}
