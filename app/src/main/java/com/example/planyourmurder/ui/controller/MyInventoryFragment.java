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
import com.example.planyourmurder.ui.model.EventsViewModel;
import com.example.planyourmurder.ui.model.MyInventoryViewModel;

public class MyInventoryFragment extends Fragment {
    private MyInventoryViewModel myInventoryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myInventoryViewModel =
                ViewModelProviders.of(this).get(MyInventoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_inventory, container, false);
        final TextView textView = root.findViewById(R.id.text_my_inventory);
        myInventoryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
