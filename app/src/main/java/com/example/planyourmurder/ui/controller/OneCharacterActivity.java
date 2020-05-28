package com.example.planyourmurder.ui.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.planyourmurder.R;
import com.example.planyourmurder.ui.model.Socket;
import com.example.planyourmurder.ui.model.SocketHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONObject;

public class OneCharacterActivity extends AppCompatActivity {
    private TextView OneCharacterName;
    private ImageView OneCharacterPhoto;
    private TextView OneCharacterDescription;
    private TextView OneCharacterThoughts;
    private String OneCharacterNamestr;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_character);

        this.OneCharacterName = findViewById(R.id.one_character_name_text_view);
        this.OneCharacterPhoto = findViewById(R.id.one_character_photo);
        this.OneCharacterDescription = findViewById(R.id.description_one_character);
        this.OneCharacterThoughts = findViewById(R.id.one_character_thoughts);

        Intent intent = getIntent();
        if (intent.hasExtra("OneCharacterName")){
            OneCharacterNamestr = intent.getStringExtra("OneCharacterName");
            OneCharacterName.setText(OneCharacterNamestr);
        }

        socket = SocketHandler.getSocket();
        try {
            JSONObject obj = new JSONObject();
            obj.put("playerRoleName",OneCharacterNamestr);
            socket.send("getPlayerData",obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Socket.OnEventResponseListener socketPairListener = new Socket.OnEventResponseListener() {
            @Override
            public void onMessage(String event, String status, String data) {
                try {
                    System.out.println(data);
                    JSONObject OneCharacterPageJson = new  JSONObject(data);

                    if (status.equals("ok"))
                    {
                        /*if(OneCharacterPageJson.getString("OneCharacterPhoto")!="null") {
                            byte[] decodedString = Base64.decode(OneCharacterPageJson.getString("OneCharacterPhoto"), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            OneCharacterPhoto.setImageBitmap(decodedByte);
                        }*/

                        if(OneCharacterPageJson.getString("characterRole")!="null"){
                            OneCharacterDescription.setText(OneCharacterPageJson.getString("characterRole"));
                        }
                        if(OneCharacterPageJson.getString("characterThoughts")!="null"){
                            OneCharacterThoughts.setText(OneCharacterPageJson.getString("characterThoughts"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        socket.onEventResponse("getPlayerData", socketPairListener);

        Socket.OnEventResponseListener socketPairListenerNotif = new Socket.OnEventResponseListener() {
            @Override
            public void onMessage(String event, String status, String data) {
                try {
                    System.out.println(data);
                    JSONObject notificationJson = new  JSONObject(data);

                    if (status.equals("ok"))
                    {
                        new AlertDialog.Builder(OneCharacterActivity.this)
                                .setTitle(notificationJson.getString("type"))
                                .setMessage(notificationJson.getString("message"))
                                .setPositiveButton("ok", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        socket.onEventResponse("notification", socketPairListenerNotif);
    }
}
