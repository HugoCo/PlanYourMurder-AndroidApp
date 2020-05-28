package com.example.planyourmurder.ui.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.planyourmurder.R;
import com.example.planyourmurder.ui.model.ListCharactersViewModel;
import com.example.planyourmurder.ui.model.MyInventoryViewModel;
import com.example.planyourmurder.ui.model.Socket;
import com.example.planyourmurder.ui.model.SocketHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ListCharactersFragment extends Fragment {

    private ListCharactersViewModel ListCharactersViewModel;
    private Socket socket;
    private ListView listView;
    private JSONArray characters;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ListCharactersViewModel =
                ViewModelProviders.of(this).get(ListCharactersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_listcharacters, container, false);
        socket = SocketHandler.getSocket();
        try {
            JSONObject obj = new JSONObject();
            obj.put("status","ok");
            socket.send("getPlayersPage",obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView = root.findViewById(R.id.characters_list_view);
        final ArrayList<String> perso_list = new ArrayList<>();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent onecharacterIntent = new Intent(getActivity(), OneCharacterActivity.class);
                try {
                    onecharacterIntent.putExtra("OneCharacterName", ""+characters.get(position));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(onecharacterIntent);
            }
        });

        Socket.OnEventResponseListener socketPairListener = new Socket.OnEventResponseListener() {
            @Override
            public void onMessage(String event, String status, String data) {
                try {
                    JSONObject MyInventoryPageJson = new  JSONObject(data);
                    characters = new JSONArray(MyInventoryPageJson.getString("charactersName"));

                    if (status.equals("ok") && characters.length() != 0)
                    {
                        for (int i = 0; i<characters.length();i++) {
                            perso_list.add(characters.get(i)+"");
                        }
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, perso_list);
                        listView.setAdapter(adapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        socket.onEventResponse("getPlayersPage", socketPairListener);



        return root;
    }
}
