package com.example.planyourmurder.ui.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.planyourmurder.R;
import com.example.planyourmurder.ui.model.HomeViewModel;
import com.example.planyourmurder.ui.model.Socket;
import com.example.planyourmurder.ui.model.SocketHandler;
import com.example.planyourmurder.ui.model.TokenHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private Socket socket;

    private TextView text_username;

    private TextView summary_role;
    private TextView scenario_title;
    private TextView hints;
    private TextView summary_scenario;
    private ImageView home_photo;
    private TextView properties;
    private String charact_properties;


    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        HomePageActivity activity = (HomePageActivity) getActivity();

        this.text_username=root.findViewById(R.id.text_mycharacter);
        this.summary_role=root.findViewById(R.id.summary_role);
        this.scenario_title=root.findViewById(R.id.scenario_title);
        this.summary_scenario=root.findViewById(R.id.scenario_summary);
        this.hints=root.findViewById(R.id.the_hints);
        this.home_photo=root.findViewById(R.id.home_photo);
        this.properties=root.findViewById(R.id.properties);



        socket = SocketHandler.getSocket();
        try {
            JSONObject obj = new JSONObject();
            obj.put("status","ok");
            socket.send("getHomePage",obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Socket.OnEventResponseListener socketPairListener = new Socket.OnEventResponseListener() {
            @Override
            public void onMessage(String event, String status, String data) {
                try {
                    JSONObject homePageJson = new  JSONObject(data);

                    System.out.println(status);
                    if (status.equals("ok"))
                    {
                        if(homePageJson.getString("characterName")!="null"){
                            text_username.setText(homePageJson.getString("characterName"));
                        }

                        if(homePageJson.getString("characterPhoto")!="null") {
                            byte[] decodedString = Base64.decode(homePageJson.getString("characterPhoto"), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            home_photo.setImageBitmap(decodedByte);
                        }

                        if(homePageJson.getString("characterSummaryRole")!="null"){
                            summary_role.setText(homePageJson.getString("characterSummaryRole"));
                        }

                        if(homePageJson.getString("scenarioTitle")!="null"){
                            scenario_title.setText(homePageJson.getString("scenarioTitle"));
                        }

                        if(homePageJson.getString("scenarioSummary")!="null"){
                            summary_scenario.setText(homePageJson.getString("scenarioSummary"));
                        }

                        if(homePageJson.getString("characterHints")!="[]"){

                            System.out.println("Les indices: " + homePageJson.getString("characterHints"));

                            JSONArray indices = homePageJson.getJSONArray("characterHints");
                            String affichage="";
                            for(int i=0; i<indices.length(); i++){

                                JSONObject indice = indices.getJSONObject(i);

                                affichage += (String) "--------------  " + indice.getString("name") + "  --------------\n" + indice.getString("description")+ "\n\n" ;
                            }

                            System.out.println("Indice: "+ affichage);
                            hints.setText(affichage);

                        }


                        /* TODO test état du joueur
                        if(homePageJson.getString("characterProperties")!="null"){
                            String Texte = "";

                            if(homePageJson.getBoolean("alive")) {
                                Texte = Texte + "Vous êtes Vivant !\n";
                            } else Texte = Texte + "Vous êtes Mort !\n";

                            if(homePageJson.getBoolean("protected")) {
                                Texte = Texte + "Vous êtes protégé\n" ;
                            }else Texte = Texte + "Vous êtes sans protection";

                            properties.setText(Texte);
                        }*/


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
