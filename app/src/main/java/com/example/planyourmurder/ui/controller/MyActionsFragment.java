
package com.example.planyourmurder.ui.controller;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;

public class MyActionsFragment extends Fragment {

    public static final String TAG = "MyActionsFragment";

    private MyActionsViewModel missionsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        missionsViewModel =
                ViewModelProviders.of(this).get(MyActionsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myactions, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        missionsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        SwipeMenuListView listView = root.findViewById(R.id.listView);
        ArrayList<String> mission_list = new ArrayList<>();
        mission_list.add("Tuer le méchant");
        mission_list.add("Tuer le méchant");
        mission_list.add("Tuer le méchant");
        mission_list.add("Tuer le méchant");
        mission_list.add("Tuer le méchant");
        mission_list.add("Tuer le méchant");
        mission_list.add("Tuer le méchant");
        mission_list.add("Tuer le méchant");
        mission_list.add("Tuer le méchant");
        mission_list.add("Tuer le méchant");
        mission_list.add("Tuer le méchant");
        mission_list.add("Tuer le méchant");
        mission_list.add("Tuer le méchant");
        mission_list.add("Tuer le méchant");
        mission_list.add("Tuer le méchant");
        mission_list.add("Tuer le méchant");
        mission_list.add("Tuer le méchant");
        mission_list.add("Tuer le méchant");

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mission_list);
        listView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(170);
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_check_black_24dp);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        Log.d(TAG, "onMenuItemClick : clicked item "+index);
                        break;
                    case 1:
                        // delete
                        Log.d(TAG, "onMenuItemClick : clicked item "+index);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        return root;
    }
}