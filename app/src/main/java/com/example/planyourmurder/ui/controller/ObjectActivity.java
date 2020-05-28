package com.example.planyourmurder.ui.controller;

import androidx.appcompat.app.AppCompatActivity;

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

public class ObjectActivity extends AppCompatActivity {
    private TextView objectName;
    private ImageView objectPhoto;
    private TextView objectDescription;
    private String objectNamestr;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object);

        this.objectName = findViewById(R.id.object_name_text_view);
        this.objectPhoto = findViewById(R.id.object_photo);
        this.objectDescription = findViewById(R.id.description_object_text_view);

        Intent intent = getIntent();
        if (intent.hasExtra("objectName")){
            objectNamestr = intent.getStringExtra("objectName");
            objectName.setText(objectNamestr);
        }

        socket = SocketHandler.getSocket();
        try {
            JSONObject obj = new JSONObject();
            obj.put("objectName",objectNamestr);
            socket.send("getObjectPage",obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Socket.OnEventResponseListener socketPairListener = new Socket.OnEventResponseListener() {
            @Override
            public void onMessage(String event, String status, String data) {
                try {
                    System.out.println(data);
                    JSONObject objectPageJson = new  JSONObject(data);

                    if (status.equals("ok"))
                    {
                        if(objectPageJson.getString("objectPhoto")!="null") {
                            byte[] decodedString = Base64.decode(objectPageJson.getString("objectPhoto"), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            objectPhoto.setImageBitmap(decodedByte);
                        }

                        if(objectPageJson.getString("objectDescription")!="null"){
                            objectDescription.setText(objectPageJson.getString("objectDescription"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        socket.onEventResponse("getObjectPage", socketPairListener);
    }
}
