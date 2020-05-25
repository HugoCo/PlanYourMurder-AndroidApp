package com.example.planyourmurder.ui.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.planyourmurder.R;
import com.example.planyourmurder.ui.model.Game;
import com.example.planyourmurder.ui.model.Player;
import com.example.planyourmurder.ui.model.Socket;
import com.example.planyourmurder.ui.model.SocketHandler;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.json.JSONArray;
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
    private JSONObject connectJson;
    public static final String WEBSOCKET_BASE_URL = "wss://rpplanner-api.herokuapp.com/";

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
                connectJson = new JSONObject(text);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGame = new Game();



        this.button_confirm= findViewById(R.id.activity_main_button_confirm);
        this.editText=findViewById(R.id.activity_main_editText);


        final Socket socket = Socket.Builder.with(WEBSOCKET_BASE_URL).build().connect();
        SocketHandler.setSocket(socket);

        Socket.OnEventResponseListener socketPairListener = new Socket.OnEventResponseListener() {
            @Override
            public void onMessage(String event, String status, String data) {
                try {
                    if (status.equals("ok")) {
                        System.out.println("go to name");
                        continueToName();
                    }
                    else{
                        String message =" Code de partie Incorrect";
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        socket.onEventResponse("testId", socketPairListener);

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
                Player.setGameId(gameNumber);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("id", gameNumber);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.send("testId", obj.toString());

            }
        });

    }
    private void continueToName() throws JSONException {
        Intent nameIntent = new Intent(MainActivity.this, NameActivity.class);
        nameIntent.putExtra("gameId", ""+gameNumber);
        startActivity(nameIntent);
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