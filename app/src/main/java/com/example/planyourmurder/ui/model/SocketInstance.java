package com.example.planyourmurder.ui.model;

import android.app.Application;



import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;


public class SocketInstance extends Application {
    private Socket iSocket;
    private static final String URL = "https://rpplanner-api.herokuapp.com/";
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            //IO.Options opts = new IO.Options();
            //opts.query = "auth_token=" + authToken;
            iSocket = IO.socket(URL);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    public Socket getSocketInstance(){
        return iSocket;
    }
}