package com.example.planyourmurder.ui.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.planyourmurder.R;
import com.example.planyourmurder.ui.model.SocketInstance;
import com.example.planyourmurder.ui.model.Game;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;

import java.util.ArrayList;
import java.util.logging.Level;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private Button button_confirm;
    private EditText editText;
    private TextView textView3;
    private Game mGame;
    private Socket mSocket;
    private int result;
    /*{
        try{
            mSocket = IO.socket("http://localhost:1337/");
        } catch (URISyntaxException e){}
    }*/
    /*private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            System.out.println("Call");
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    System.out.println("ReceptionOK");

                    try {
                        result = parseInt(data.getString("isCorrectID"));
                        System.out.println("OK Received");
;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGame = new Game();
        SocketInstance instance = (SocketInstance) getApplication();
        mSocket = instance.getSocketInstance();
        //mSocket.connect();
        //System.out.println("CONNECT :"+mSocket.connect());
        System.out.println("CONNECT :"+mSocket.connected());
        if (mSocket.connected()){
            Log.d(TAG, "onCreate: ");
            Toast.makeText(MainActivity.this, "Socket Connected!!",Toast.LENGTH_SHORT).show();
        }
        java.util.logging.Logger.getLogger(Socket.class.getName()).setLevel(Level.FINEST);
        //java.util.logging.Logger.getLogger(io.socket.engineio.client.Socket.class.getName()).setLevel(Level.FINEST);
        java.util.logging.Logger.getLogger(Manager.class.getName()).setLevel(Level.FINEST);

        this.button_confirm= findViewById(R.id.activity_main_button_confirm);
        this.editText=findViewById(R.id.activity_main_editText);
        this.textView3=findViewById(R.id.textView3);
        textView3.setText("Socket connected : "+mSocket.connected());

        button_confirm.setEnabled(false);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                button_confirm.setEnabled(s.toString().length() == 3);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int gameNumber = parseInt(editText.getText().toString());
                //System.out.println(gameNumber);
                attemptSend();
                mGame.setGameNumber(gameNumber);

                //if (result == 1) {
                    Intent nameIntent = new Intent(MainActivity.this, NameActivity.class);
                    startActivity(nameIntent);
                //}
            }
        });

    }
    private void attemptSend(){
        mSocket.on("testId", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Ack ack = (Ack) args[args.length-1];
                ack.call();
            }
        });
        String message = editText.getText().toString().trim();
        if(TextUtils.isEmpty(message)) {
            return;
        }
        System.out.println("OK "+message);
        mSocket.emit("testId", message, new Ack(){
            @Override
            public void call(Object... args) {
                Log.d(TAG, "call: OK");
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        //mSocket.off("testId", onNewMessage);
    }
}
