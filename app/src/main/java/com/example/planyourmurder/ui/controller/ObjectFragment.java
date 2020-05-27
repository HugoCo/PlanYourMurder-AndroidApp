package com.example.planyourmurder.ui.controller;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.planyourmurder.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ObjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ObjectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static Object objectName;
    private TextView objectNametv;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ObjectFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_object, container, false);
        this.objectNametv = root.findViewById(R.id.object_name);
        System.out.println("test");
        this.objectNametv.setText("test");
        return inflater.inflate(R.layout.fragment_object, container, false);
    }
    public static ObjectFragment newInstance(String object) {
        ObjectFragment fragment = new ObjectFragment();
        objectName = object;

        // do some initial setup if needed, for example Listener etc

        return fragment;
    }
}
