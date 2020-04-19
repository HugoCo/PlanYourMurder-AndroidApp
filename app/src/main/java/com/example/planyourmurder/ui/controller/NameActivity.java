package com.example.planyourmurder.ui.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.planyourmurder.R;
import com.example.planyourmurder.ui.model.Game;
import com.example.planyourmurder.ui.model.GameCharacter;
import com.example.planyourmurder.ui.model.GameCharacterAdaptater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NameActivity extends AppCompatActivity {

    private Button button_confirm;
    private EditText editText;
    public static final int HOME_PAGE_ACTIVITY_REQUEST_CODE = 42;
    private String roles;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        this.button_confirm= findViewById(R.id.activity_name_button_confirm);
        this.editText=findViewById(R.id.activity_name_editText);
        this.listView = findViewById(R.id.listView);
        Intent intent = getIntent();
        if (intent.hasExtra("roles")){ // vérifie qu'une valeur est associée à la clé “roles”
            roles = intent.getStringExtra("roles"); // on récupère la valeur associée à la clé
        }

        try {
            JSONObject rolesObj = new JSONObject(roles);
            JSONArray dataArray = rolesObj.getJSONArray("roles");
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
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                button_confirm.setEnabled(s.toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                Intent homePageIntent = new Intent(NameActivity.this, HomePageActivity.class);
                homePageIntent.putExtra("name", name);
                startActivity(homePageIntent);

            }
        });
    }
    AdapterView.OnItemClickListener listview_listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
            View titleView = view.findViewById(R.id.namechar);
            String title = (String) titleView.getTag();
            Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
        }
    };
}

