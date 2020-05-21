package com.example.planyourmurder.ui.controller;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.planyourmurder.R;
import com.example.planyourmurder.ui.model.EventsViewModel;
import com.example.planyourmurder.ui.model.MyInventoryViewModel;
import com.example.planyourmurder.ui.model.Socket;
import com.example.planyourmurder.ui.model.SocketHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.planyourmurder.ui.controller.MyActionsFragment.TAG;

public class MyInventoryFragment extends Fragment {
    private MyInventoryViewModel myInventoryViewModel;
    private Socket socket;
    private ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myInventoryViewModel =
                ViewModelProviders.of(this).get(MyInventoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_inventory, container, false);
        socket = SocketHandler.getSocket();
        try {
            JSONObject obj = new JSONObject();
            obj.put("status","ok");
            socket.send("getMyInventoryPage",obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView = root.findViewById(R.id.inventory_list_view);
        ArrayList<String> mission_list = new ArrayList<>();
        mission_list.add("salut");
        mission_list.add("salut");
        mission_list.add("salut");
        mission_list.add("salut");
        mission_list.add("salut");
        mission_list.add("salut");
        mission_list.add("salut");
        mission_list.add("salut");
        mission_list.add("salut");

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mission_list);
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
                    JSONObject MyInventoryPageJson = new  JSONObject(data);

                    if (status.equals("ok"))
                    {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        socket.onEventResponse("getMyInventoryPage", socketPairListener);



        return root;
    }
}
