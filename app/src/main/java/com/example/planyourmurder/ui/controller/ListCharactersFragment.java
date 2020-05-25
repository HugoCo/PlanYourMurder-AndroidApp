package com.example.planyourmurder.ui.controller;

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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ListCharactersFragment extends Fragment {

    private ListCharactersViewModel ListCharactersViewModel;
    private Socket socket;
    private ListView listView;

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
        listView = root.findViewById(R.id.inventory_list_view);
        ArrayList<String> perso_list = new ArrayList<>();
        perso_list.add("Perso 1");
        perso_list.add("Perso 2");                      // TODO Récup liste player
        perso_list.add("Perso 3");
        perso_list.add("Perso 4");
        perso_list.add("Perso 5");
        perso_list.add("Perso 6");

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, perso_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((HomePageActivity)getActivity()).replaceFragment();
            }
        });

        Socket.OnEventResponseListener socketPairListener = new Socket.OnEventResponseListener() {
            @Override
            public void onMessage(String event, String status, String data) {
                try {
                    JSONObject ListCharacterJson = new  JSONObject(data);

                    if (status.equals("ok"))                // TODO RECUP LISTE PERSO
                    {


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
