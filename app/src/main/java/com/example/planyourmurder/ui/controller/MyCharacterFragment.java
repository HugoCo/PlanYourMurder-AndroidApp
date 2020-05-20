package com.example.planyourmurder.ui.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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
import com.example.planyourmurder.ui.model.Socket;
import com.example.planyourmurder.ui.model.SocketHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class MyCharacterFragment extends Fragment {

    private MyCharacterViewModel myCharacterViewModel;

    private TextView text_username;
    private Socket socket;

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
        this.text_username=root.findViewById(R.id.textView2);
        socket = SocketHandler.getSocket();
        try {
            JSONObject obj = new JSONObject();
            obj.put("status","ok");
            socket.send("getMyPlayer",obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Socket.OnEventResponseListener socketPairListener = new Socket.OnEventResponseListener() {
            @Override
            public void onMessage(String event, String status, String data) {
                try {
                    System.out.println(data);
                    JSONObject MyPlayerPageJson = new  JSONObject(data);
                    JSONObject characterRole = (JSONObject) MyPlayerPageJson.get("characterRole");

                    if (status.equals("ok"))
                    {
                        if(characterRole.getString("name")!="null"){
                            text_username.setText(characterRole.getString("name"));
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        socket.onEventResponse("getMyPlayer", socketPairListener);
        return root;
    }
}