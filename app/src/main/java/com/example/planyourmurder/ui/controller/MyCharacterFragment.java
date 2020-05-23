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
import com.example.planyourmurder.ui.model.ListCharactersViewModel;
import com.example.planyourmurder.ui.model.Socket;
import com.example.planyourmurder.ui.model.SocketHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class MyCharacterFragment extends Fragment {

    private ListCharactersViewModel myCharacterViewModel;

    private TextView role_name;
    private TextView role_summary;
    private TextView role_description;


    private Socket socket;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myCharacterViewModel =
                ViewModelProviders.of(this).get(ListCharactersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mycharacter, container, false);
        final TextView role_name = root.findViewById(R.id.role_name);
        final TextView role_summary = root.findViewById(R.id.role_summary);
        final TextView role_description = root.findViewById(R.id.role_description);

        myCharacterViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                role_name.setText(s);


            }
        });
        HomePageActivity activity = (HomePageActivity) getActivity();
        this.role_name=root.findViewById(R.id.role_name);
        this.role_summary=root.findViewById(R.id.role_summary);
        this.role_description=root.findViewById(R.id.role_description);

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
                            role_name.setText(characterRole.getString("name"));
                        }

                        if(characterRole.getString("summary")!="null"){
                            role_summary.setText(characterRole.getString("summary"));
                        }

                        if(characterRole.getString("description")!="null"){
                            role_description.setText(characterRole.getString("description"));
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