package com.example.planyourmurder.ui.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.planyourmurder.ui.model.Socket;
import com.example.planyourmurder.ui.model.SocketHandler;
import com.example.planyourmurder.ui.model.TokenHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private Socket socket;

    private TextView text_username;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        HomePageActivity activity = (HomePageActivity) getActivity();

        this.text_username=root.findViewById(R.id.text_mycharacter);
        text_username.setText(R.string.perso);
        socket = SocketHandler.getSocket();
        try {
            JSONObject obj = new JSONObject();
            obj.put("status","ok");
            socket.send("getHomePage",obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("OK");

        Socket.OnEventResponseListener socketPairListener = new Socket.OnEventResponseListener() {
            @Override
            public void onMessage(String event, String status, String data) {
                try {
                    JSONObject homePageJson = new  JSONObject(data);

                    System.out.println(status);
                    if (status.equals("ok"))
                    {
                        text_username.setText(homePageJson.getString("characterName"));
                        //String characterPhoto = homePageJson.getString("characterPhoto");
                        //String characterSummaryRole = homePageJson.getString("characterSummaryRole");
                        //String scenarioTitle = homePageJson.getString("scenarioTitle");
                        //String scenarioSummary = homePageJson.getString("scenarioSummary");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        socket.onEventResponse("getHomePage", socketPairListener);

        return root;
    }
}
