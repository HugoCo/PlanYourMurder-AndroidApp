
package com.example.planyourmurder.ui.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.planyourmurder.R;
import com.example.planyourmurder.ui.model.MyActionsViewModel;
import com.example.planyourmurder.ui.model.Socket;
import com.example.planyourmurder.ui.model.SocketHandler;

import org.json.*;

import java.util.ArrayList;
import java.util.Iterator;

public class MyActionsFragment extends Fragment {

    public static final String TAG = "MyActionsFragment";

    private MyActionsViewModel missionsViewModel;
    private Socket socket;
    private ListView listView;
    private JSONArray listActionsDetails;
    private ArrayList<String> mission_list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        missionsViewModel =
                ViewModelProviders.of(this).get(MyActionsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myactions, container, false);


        socket = SocketHandler.getSocket();
        try {
            JSONObject obj = new JSONObject();
            obj.put("status","ok");
            socket.send("getMyActions",obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView = root.findViewById(R.id.myactions_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChooseActionActivity.class);
                intent.putExtra("actionChosen", mission_list.get(position));
                startActivity(intent);
            }
        });


        Socket.OnEventResponseListener socketPairListener = new Socket.OnEventResponseListener() {
            @Override

            public void onMessage(String event, String status, String data) {
                try {
                    JSONObject homePageJson = new  JSONObject(data);
                    JSONObject characterActions = (JSONObject) homePageJson.get("characterActions");
                    mission_list = new ArrayList<>();
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mission_list);
                    listView.setAdapter(adapter);

                    listActionsDetails = new JSONArray();

                    if (status.equals("ok"))
                    {
                        Iterator < ? > keys = characterActions.keys();

                        while (keys.hasNext()) {
                            String key = (String) keys.next();
                            if(characterActions.getJSONObject(key).getString("possible").equals("true")){
                                mission_list.add(key);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        socket.onEventResponse("getMyActions", socketPairListener);




        return root;
    }
}