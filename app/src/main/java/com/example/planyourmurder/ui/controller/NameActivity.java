package com.example.planyourmurder.ui.controller;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.planyourmurder.R;

import com.example.planyourmurder.ui.model.Hashage;
import com.example.planyourmurder.ui.model.Player;
import com.example.planyourmurder.ui.model.Socket;
import com.example.planyourmurder.ui.model.SocketHandler;
import com.example.planyourmurder.ui.model.TokenHandler;


import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Integer.parseInt;


public class NameActivity<socket> extends AppCompatActivity {


    private Button button_confirm;
    private EditText edit_name;
    private EditText edit_password;
    public static final int HOME_PAGE_ACTIVITY_REQUEST_CODE = 42;
    private String token;
    private Socket socket;
    private String name;
    private String password;
    private String password_hasher = null;
    private String roles;
    private int gameId;
    private Hashage hash = new Hashage();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        this.button_confirm= findViewById(R.id.activity_name_button_confirm);
        this.edit_name=findViewById(R.id.activity_name_editText);
        this.edit_password=findViewById(R.id.activity_password_editText);

        socket = SocketHandler.getSocket();
        Intent intent = getIntent();
        gameId = parseInt(intent.getStringExtra("gameId"));

        button_confirm.setEnabled(false);

        edit_name.addTextChangedListener(new TextWatcher() {
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

        Socket.OnEventResponseListener socketPairListener = new Socket.OnEventResponseListener() {
            @Override
            public void onMessage(String event, String status, String data) {
                try {
                    JSONObject loginJson = new  JSONObject(data);

                    System.out.println(status);
                    if (status.equals("ok"))
                    {
                        token = loginJson.getString("token");
                        TokenHandler.setToken(token);
                        roles = loginJson.getString("roles");
                        if (roles != "null") {
                            Intent roleIntent = new Intent(NameActivity.this, RoleActivity.class);
                            roleIntent.putExtra("roles", roles);
                            startActivity(roleIntent);
                        }
                        else {
                            Intent homePageIntent = new Intent(NameActivity.this, HomePageActivity.class);
                            startActivity(homePageIntent);
                        }
                    } else {
                        String message ="Nom d'utilisateur déjà utilisé et mauvais mot de passe";
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        socket.onEventResponse("connectGame", socketPairListener);

        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = edit_name.getText().toString();
                password = edit_password.getText().toString();

                try {
                    password_hasher = hash.getHash(password);
                } catch (Exception e) {
                    System.out.println("J'ai pas pu recup le hash");
                    e.printStackTrace();
                }

                System.out.println("Mot de Passe Hasher: " + password_hasher);

                Player.setName(name);

                JSONObject obj = new JSONObject();
                try {
                    obj.put("gameId", ""+gameId);
                    obj.put("username", name);
                    obj.put("password", password_hasher);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.send("connectGame", obj.toString());
            }
        });
    }


}