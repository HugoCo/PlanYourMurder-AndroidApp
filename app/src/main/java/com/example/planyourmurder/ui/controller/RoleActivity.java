package com.example.planyourmurder.ui.controller;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.example.planyourmurder.R;
import com.example.planyourmurder.ui.model.GameCharacter;
import com.example.planyourmurder.ui.model.GameCharacterAdaptater;
import com.example.planyourmurder.ui.model.Socket;
import com.example.planyourmurder.ui.model.SocketHandler;
import com.example.planyourmurder.ui.model.TokenHandler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class RoleActivity extends AppCompatActivity {

    private Button button_confirm;
    private EditText editText;
    private Socket socket;
    private String roles;
    private ListView listView;
    private String roleSelected;
    public static final int HOME_PAGE_ACTIVITY_REQUEST_CODE = 42;
    private String token;
    private String name;
    private int gameId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
        this.button_confirm= findViewById(R.id.activity_role_button_confirm);
        socket = SocketHandler.getSocket();
        Intent intent = getIntent();
        if (intent.hasExtra("roles")){ // vérifie qu'une valeur est associée à la clé “roles”
            roles = intent.getStringExtra("roles"); // on récupère la valeur associée à la clé
        }

        try {
            System.out.println(roles);
            JSONArray dataArray = new JSONArray(roles);
            LinkedList<GameCharacter> chars = new LinkedList<GameCharacter>();
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject data = (JSONObject) dataArray.get(i);
                String name = (String) data.get("name");
                String image = (String) data.get("image");
                chars.add(new GameCharacter(name,image));
                System.out.println(chars.get(i).getName());

            }
            GameCharacterAdaptater adapter = new GameCharacterAdaptater(getApplicationContext(), R.layout.activity_item, chars);
            ListView list_char = (ListView) findViewById(R.id.list_char);
            list_char.setAdapter(adapter);
            list_char.setOnItemClickListener(listview_listener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        button_confirm.setEnabled(false);

        Socket.OnEventResponseListener socketPairListener = new Socket.OnEventResponseListener() {
            @Override
            public void onMessage(String event, String status, String data) {
                try {
                    JSONObject dataObj = new JSONObject(data);
                    if(status.equals("ok")) {
                        Intent homePageIntent = new Intent(RoleActivity.this, HomePageActivity.class);
                        startActivity(homePageIntent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        socket.onEventResponse("setRole", socketPairListener);

        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("roleName", roleSelected);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.send("setRole", obj.toString());
            }
        });
    }


    AdapterView.OnItemClickListener listview_listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
            View titleView = view.findViewById(R.id.namechar);
            roleSelected = (String) titleView.getTag();
            Toast.makeText(getApplicationContext(), roleSelected, Toast.LENGTH_SHORT).show();
            button_confirm.setEnabled(true);
        }
    };
}
