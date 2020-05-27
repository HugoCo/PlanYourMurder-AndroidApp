package com.example.planyourmurder.ui.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
                String [] result = new String[1];
                try {
                    System.out.println(possibilities.get(position));
                    result[0] = ""+possibilities.get(position);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("result = "+ Arrays.toString(result));
                returnTab.put(Arrays.toString(result));
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
                    Intent homePageIntent = new Intent(ChooseActionActivity.this, HomePageActivity.class);
                    startActivity(homePageIntent);
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
                        numberOfPagesToDisplay = actionData.length()-1;
                        JSONObject choice = (JSONObject) actionData.get(0);
                        messagetv.setText(choice.getString("message"));
                        possibilities = (JSONArray) choice.get("possibilities");
                        for (int i=0; i<possibilities.length();i++) {
                            list.add(""+possibilities.get(i));
                        }
                        ArrayAdapter adapter = new ArrayAdapter(ChooseActionActivity.this, android.R.layout.simple_list_item_1, list);
                        listView.setAdapter(adapter);
                        pageNumero = 1;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        socket.onEventResponse("makeAction", socketPairListener);

    }

}
