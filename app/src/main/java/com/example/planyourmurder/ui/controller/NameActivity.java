package com.example.planyourmurder.ui.controller;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.planyourmurder.R;
import com.example.planyourmurder.ui.model.Game;
import com.example.planyourmurder.ui.model.Socket;
import com.example.planyourmurder.ui.model.SocketHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class NameActivity<socket> extends AppCompatActivity {


    private Integer GameID ;
    Intent intent = getIntent();
    private Integer Num_game = 318534; // A recupérer directement

    private Button button_confirm;
    private EditText edit_name;
    private EditText edit_password;
    public static final int HOME_PAGE_ACTIVITY_REQUEST_CODE = 42;
    private Socket socket = SocketHandler.getSocket();
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        this.button_confirm= findViewById(R.id.activity_name_button_confirm);
        this.edit_name=findViewById(R.id.activity_name_editText);
        this.edit_password=findViewById(R.id.activity_password_editText);


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
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edit_name.getText().toString();
                String password = edit_password.getText().toString();

                // envoie de la socket
                JSONObject obj = new JSONObject();
                try {
                    obj.put("username", name);
                    obj.put("password", password);
                    obj.put("gameId",Num_game);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.send("connectGame", obj.toString());

                // réponse base de donnée 2 possibilités : soit vers home page soit vers select personnage

                Socket.OnEventResponseListener socketPairListener = new Socket.OnEventResponseListener() {
                    @Override
                    public void onMessage(String event, String data) {
                        try {
                            JSONObject data_obj = new JSONObject(data);
                            if ("status")=="ok"){
                                token = data_obj.get("token").toString();
                                System.out.println("J'ai eu le Token ! ");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                socket.onEventResponse("connectGame", socketPairListener);

                Intent homePageIntent = new Intent(NameActivity.this, HomePageActivity.class);
                homePageIntent.putExtra("name", name);
                startActivity(homePageIntent);

            }
        });
    }

}

