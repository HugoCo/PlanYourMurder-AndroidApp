package com.example.planyourmurder.ui.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planyourmurder.R;
import com.example.planyourmurder.ui.model.Socket;
import com.example.planyourmurder.ui.model.SocketHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ChooseActionActivity extends AppCompatActivity {
    private Socket socket;
    private ListView listView;
    private TextView messagetv;
    private String actionName;
    private JSONArray actionData;
    private JSONArray possibilities;
    private JSONArray returnTab = new JSONArray();
    private int numberOfPagesToDisplay;
    private int pageNumero;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_action);
        this.messagetv = findViewById(R.id.message_choose_action);
        Intent intent = getIntent();
        if (intent.hasExtra("actionChosen")){
            actionName = intent.getStringExtra("actionChosen");
        }

        socket = SocketHandler.getSocket();
        try {
            JSONObject obj = new JSONObject();
            obj.put("actionName",actionName);
            socket.send("makeAction",obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView = findViewById(R.id.choose_action_list_view);
        ArrayList<String> list = new ArrayList<>();
        list.add("Nothing to add");
        ArrayAdapter adapter = new ArrayAdapter(ChooseActionActivity.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    System.out.println(possibilities.get(position));
                    result = ""+possibilities.get(position);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("result = "+ result);
                returnTab.put(result);
                if(numberOfPagesToDisplay>0) {
                    try {

                        ArrayList<String> list = new ArrayList<>();
                        numberOfPagesToDisplay -= 1;
                        JSONObject choice = (JSONObject) actionData.get(0);
                        messagetv.setText(choice.getString("message"));
                        possibilities = (JSONArray) choice.get("possibilities");
                        for (int i = 0; i < possibilities.length(); i++) {
                            list.add("" + possibilities.get(i));
                        }
                        ArrayAdapter adapter = new ArrayAdapter(ChooseActionActivity.this, android.R.layout.simple_list_item_1, list);
                        listView.setAdapter(adapter);
                        pageNumero += 1;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    JSONObject obj = new JSONObject();
                    System.out.println(returnTab);
                    try {
                        obj.put("choices", returnTab);
                        obj.put("actionName",actionName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socket.send("actionResult", obj.toString());
                }
            }
        });


        Socket.OnEventResponseListener socketPairListener = new Socket.OnEventResponseListener() {
            @Override

            public void onMessage(String event, String status, String data) {
                try {
                    if (status.equals("ok"))
                    {
                        JSONObject ChooseActionPageJson = new  JSONObject(data);
                        actionData = (JSONArray) ChooseActionPageJson.get("choices");
                        ArrayList<String> list = new ArrayList<>();
                        numberOfPagesToDisplay = actionData.length();
                        if (numberOfPagesToDisplay !=0) {
                            JSONObject choice = (JSONObject) actionData.get(0);
                            messagetv.setText(choice.getString("message"));
                            possibilities = (JSONArray) choice.get("possibilities");
                            for (int i=0; i<possibilities.length();i++) {
                                list.add(""+possibilities.get(i));
                            }
                            ArrayAdapter adapter = new ArrayAdapter(ChooseActionActivity.this, android.R.layout.simple_list_item_1, list);
                            listView.setAdapter(adapter);
                            numberOfPagesToDisplay -= 1;
                            pageNumero = 1;
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        socket.onEventResponse("makeAction", socketPairListener);

        Socket.OnEventResponseListener socketPairListenerAR = new Socket.OnEventResponseListener() {
            @Override
            public void onMessage(String event, String status, String data) {
                if(status.equals("ok")) {
                    Toast.makeText(ChooseActionActivity.this, "Action effectuée", Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(ChooseActionActivity.this, "Problème ! L'action n'a pas été effectuée", Toast.LENGTH_LONG).show();
                    Intent homePageIntent = new Intent(ChooseActionActivity.this, HomePageActivity.class);
                    startActivity(homePageIntent);
                }

            }
        };
        socket.onEventResponse("actionResult", socketPairListenerAR);

        Socket.OnEventResponseListener socketPairListenerNotif = new Socket.OnEventResponseListener() {
            @Override
            public void onMessage(String event, String status, String data) {
                try {
                    System.out.println(data);
                    JSONObject notificationJson = new  JSONObject(data);

                    if (status.equals("ok"))
                    {
                        DialogInterface.OnClickListener listener;
                        new AlertDialog.Builder(ChooseActionActivity.this)
                                .setTitle(notificationJson.getString("type"))
                                .setMessage(notificationJson.getString("message"))
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent homePageIntent = new Intent(ChooseActionActivity.this, HomePageActivity.class);
                                        startActivity(homePageIntent);
                                    }
                                })
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
