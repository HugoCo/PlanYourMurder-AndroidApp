package com.example.planyourmurder.ui.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.planyourmurder.R;
import com.example.planyourmurder.ui.model.Game;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private Button button_confirm;
    private EditText editText;
    private TextView textView3;
    private Game mGame;
    private OkHttpClient client;
    private int isCorrectId = 0;
    private int gameNumber = 0;
    private boolean ready = false;

    private final class EchoWebSocketListener extends WebSocketListener {
        public static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override

        public void onOpen(WebSocket webSocket, Response response){
            System.out.println(response.toString());
            JSONObject obj = new JSONObject();
            try {
                obj.put("type", "testId");
                obj.put("id", gameNumber);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            webSocket.send(obj.toString());
            System.out.println("Sent to server : "+obj.toString());
            //webSocket.close(NORMAL_CLOSURE_STATUS, "Bye");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output("Receiving : " + text );
            try {
                JSONObject connectJson = new JSONObject(text);
                isCorrectId = (int) connectJson.get("result");
                continueToName();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + "/" + reason);
        }

        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error : "+ t.getMessage());
        }
    }

    private void continueToName() {
        if(isCorrectId == 1) {
            Intent nameIntent = new Intent(MainActivity.this, NameActivity.class);
            startActivity(nameIntent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGame = new Game();


        this.button_confirm= findViewById(R.id.activity_main_button_confirm);
        this.editText=findViewById(R.id.activity_main_editText);
        this.textView3=findViewById(R.id.textView3);

        client = new OkHttpClient();

        button_confirm.setEnabled(false);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                button_confirm.setEnabled(s.toString().length() == 6);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameNumber = parseInt(editText.getText().toString());
                mGame.setGameNumber(gameNumber);
                start();

            }
        });

    }

    private void start() {
        Request request = new Request.Builder().url("ws://rpplanner-api.herokuapp.com/").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        //client.dispatcher().executorService().shutdown();


    }

    private void output(final String txt){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView3.setText(textView3.getText().toString() + "\n\n" + txt);
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
