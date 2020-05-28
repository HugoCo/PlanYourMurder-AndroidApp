package com.example.planyourmurder.ui.controller;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.planyourmurder.R;
import com.example.planyourmurder.ui.model.EventsViewModel;
import com.example.planyourmurder.ui.model.GameCharacter;
import com.example.planyourmurder.ui.model.GameCharacterAdaptater;
import com.example.planyourmurder.ui.model.Socket;
import com.example.planyourmurder.ui.model.SocketHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

public class EventsFragment extends Fragment {
    private EventsViewModel EventsViewModel;
    private Socket socket;
    private ListView listView;
    private JSONArray events;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        EventsViewModel = ViewModelProviders.of(this).get(EventsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_events, container, false);
        socket = SocketHandler.getSocket();

        listView = root.findViewById(R.id.event_list_view);
        final ArrayList<String> event_list = new ArrayList<>();

        Socket.OnEventResponseListener socketPairListener = new Socket.OnEventResponseListener() {
            @Override
            public void onMessage(String event, String status, String data) {
                try {
                    JSONObject MyInventoryPageJson = new  JSONObject(data);
                    events = new JSONArray(MyInventoryPageJson.getString("events"));

                    if (status.equals("ok") && events.length() != 0)
                    {
                        for (int i = 0; i<events.length();i++) {
                            event_list.add(events.get(i)+"");
                        }

                        // TODO Surement revoir la façon dont s'affiche les évènements

                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, event_list);
                        listView.setAdapter(adapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        socket.onEventResponse("getEventsPage", socketPairListener);


        return root;
    }
}


